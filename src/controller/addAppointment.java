package controller;

import helper.AppointmentData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class addAppointment {

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
    @FXML private Button addApptSave;
    @FXML private Button addApptCancel;

    /**
     * Handles the save action when adding a new appointment.
     */
    @FXML
    public void AddApptSaveAction(ActionEvent actionEvent) {
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

            // Insert the new appointment into the database
            AppointmentData.insertAppointment(title, description, location, type, startDateTime, endDateTime, customerId, userId, contactId);

            // Show success message
            showAlert("Success", "Appointment successfully added!");

            // Close the window
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while saving the appointment.");
        }
    }

    /**
     * Handles the cancel action by closing the add appointment window and go back to the appointment page
     */
    @FXML
    public void AddApptCancelAction(ActionEvent actionEvent) {
        try {
            // Load the appointment.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointment.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Create a new scene and set it
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to return to Appointments.");
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