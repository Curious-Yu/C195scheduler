package controller;

import helper.AppointmentData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class modifyAppointment implements Initializable {

    @FXML private TextField apptID;
    @FXML private TextField apptTitle;
    @FXML private TextField apptDescription;
    @FXML private TextField apptLocation;
    @FXML private TextField apptType;
    @FXML private DatePicker apptStartDate;
    @FXML private ChoiceBox<LocalTime> apptStartTime;
    @FXML private DatePicker apptEndDate;
    @FXML private ChoiceBox<LocalTime> apptEndTime;
    @FXML private ComboBox<Integer> apptCustomerID;
    @FXML private ComboBox<Integer> apptUserID;
    @FXML private ComboBox<Integer> apptContactID;
    @FXML private Button modifyApptSave;
    @FXML private Button modifyApptCancel;

    private Appointments selectedAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTimeChoiceBoxes();
    }

    /**
     * Populates the time selection ChoiceBoxes with 5-minute increments.
     */
    private void populateTimeChoiceBoxes() {
        LocalTime startTime = LocalTime.of(0, 0); // Midnight
        LocalTime endTime = LocalTime.of(23, 55); // Last available time

        // Generate time slots in 5-minute increments
        List<LocalTime> timeSlots = IntStream.iterate(0, i -> i + 5)
                .limit(((24 * 60) / 5)) // 24 hours * 60 minutes / 5-minute intervals
                .mapToObj(startTime::plusMinutes)
                .collect(Collectors.toList());

        // Populate the ChoiceBoxes
        apptStartTime.getItems().addAll(timeSlots);
        apptEndTime.getItems().addAll(timeSlots);
    }

    /**
     * Receives the selected appointment data and populates the fields.
     */
    public void setAppointmentData(Appointments appointment) {
        this.selectedAppointment = appointment;

        apptID.setText(String.valueOf(appointment.getAppointmentId()));
        apptTitle.setText(appointment.getTitle());
        apptDescription.setText(appointment.getDescription());
        apptLocation.setText(appointment.getLocation());
        apptType.setText(appointment.getType());
        apptStartDate.setValue(appointment.getLocalStart().toLocalDate());
        apptStartTime.setValue(appointment.getLocalStart().toLocalTime());
        apptEndDate.setValue(appointment.getLocalEnd().toLocalDate());
        apptEndTime.setValue(appointment.getLocalEnd().toLocalTime());
        apptCustomerID.setValue(appointment.getCustomerId());
        apptUserID.setValue(appointment.getUserId());
        apptContactID.setValue(appointment.getContactId());
    }

    /**
     * Handles saving the modified appointment.
     */
    @FXML
    public void ModifyApptSaveAction(ActionEvent actionEvent) {
        try {
            // Retrieve user input
            String title = apptTitle.getText();
            String description = apptDescription.getText();
            String location = apptLocation.getText();
            String type = apptType.getText();
            LocalDate startDate = apptStartDate.getValue();
            LocalTime startTime = apptStartTime.getValue();
            LocalDate endDate = apptEndDate.getValue();
            LocalTime endTime = apptEndTime.getValue();
            Integer customerId = apptCustomerID.getValue();
            Integer userId = apptUserID.getValue();
            Integer contactId = apptContactID.getValue();
            int appointmentId = Integer.parseInt(apptID.getText());

            // Validate input fields
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() ||
                    startDate == null || startTime == null || endDate == null || endTime == null ||
                    customerId == null || userId == null || contactId == null) {
                showAlert("Validation Error", "All fields must be filled.");
                return;
            }

            // Convert LocalDate and LocalTime to LocalDateTime
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

            // Ensure end time is after start time
            if (endDateTime.isBefore(startDateTime)) {
                showAlert("Validation Error", "End time must be after start time.");
                return;
            }

            // Update the appointment in the database
            AppointmentData.updateAppointment(appointmentId, title, description, location, type, startDateTime, endDateTime, customerId, userId, contactId);

            // Show success message
            showAlert("Success", "Appointment successfully modified!");

            // Return to appointment screen
            ReturnToAppointmentScreen(actionEvent);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while updating the appointment.");
        }
    }

    /**
     * Handles the cancel action by returning to the appointment page.
     */
    @FXML
    public void ModifyApptCancelAction(ActionEvent actionEvent) {
        ReturnToAppointmentScreen(actionEvent);
    }

    /**
     * Returns the user to the appointment screen.
     */
    private void ReturnToAppointmentScreen(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to return to the Appointments.");
        }
    }

    /**
     * Displays an alert with the given title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}