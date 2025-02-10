package controller;

import helper.AppointmentData;
import helper.ContactData;
import helper.CustomerData;
import helper.UsersData;
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
import model.Contacts;
import model.Customers;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    @FXML private ComboBox<String> apptCustomerID;
    @FXML private ComboBox<String> apptUserID;
    @FXML private ComboBox<String> apptContactID;
    @FXML private Button modifyApptSave;
    @FXML private Button modifyApptCancel;

    private Appointments selectedAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTimeChoiceBoxes();
        populateCustomerComboBox();
        populateUserComboBox();
        populateContactComboBox();
    }

    private void populateTimeChoiceBoxes() {
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 55);

        List<LocalTime> timeSlots = IntStream.iterate(0, i -> i + 5)
                .limit(((24 * 60) / 5))
                .mapToObj(startTime::plusMinutes)
                .collect(Collectors.toList());

        apptStartTime.getItems().addAll(timeSlots);
        apptEndTime.getItems().addAll(timeSlots);
    }

    private void populateCustomerComboBox() {
        try {
            List<Customers> customersList = CustomerData.getAllCustomers();
            for (Customers customer : customersList) {
                apptCustomerID.getItems().add(customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to retrieve customer data.");
        }
    }

    private void populateUserComboBox() {
        try {
            List<Users> usersList = UsersData.getAllUsers();
            for (Users user : usersList) {
                apptUserID.getItems().add(user.getUserName() + " (ID: " + user.getUserId() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to retrieve user data.");
        }
    }

    private void populateContactComboBox() {
        try {
            List<Contacts> contactsList = ContactData.getAllContacts();
            for (Contacts contact : contactsList) {
                apptContactID.getItems().add(contact.getContactName() + " (ID: " + contact.getContactId() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to retrieve contact data.");
        }
    }

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

        apptCustomerID.setValue(getComboBoxFormat(appointment.getCustomerId(), "Customer"));
        apptUserID.setValue(getComboBoxFormat(appointment.getUserId(), "User"));
        apptContactID.setValue(getComboBoxFormat(appointment.getContactId(), "Contact"));
    }

    private String getComboBoxFormat(int id, String type) {
        return type + " (ID: " + id + ")";
    }

    @FXML
    public void ModifyApptSaveAction(ActionEvent actionEvent) {
        try {
            String title = apptTitle.getText();
            String description = apptDescription.getText();
            String location = apptLocation.getText();
            String type = apptType.getText();
            LocalDate startDate = apptStartDate.getValue();
            LocalTime startTime = apptStartTime.getValue();
            LocalDate endDate = apptEndDate.getValue();
            LocalTime endTime = apptEndTime.getValue();
            String selectedCustomer = apptCustomerID.getValue();
            String selectedUser = apptUserID.getValue();
            String selectedContact = apptContactID.getValue();
            int appointmentId = Integer.parseInt(apptID.getText());

            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() ||
                    startDate == null || startTime == null || endDate == null || endTime == null ||
                    selectedCustomer == null || selectedUser == null || selectedContact == null) {
                showAlert("Validation Error", "All fields must be filled.");
                return;
            }

            int customerId = Integer.parseInt(selectedCustomer.split(" \\(ID: ")[1].replace(")", ""));
            int userId = Integer.parseInt(selectedUser.split(" \\(ID: ")[1].replace(")", ""));
            int contactId = Integer.parseInt(selectedContact.split(" \\(ID: ")[1].replace(")", ""));

            ZonedDateTime startET = ZonedDateTime.of(LocalDateTime.of(startDate, startTime), ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime endET = ZonedDateTime.of(LocalDateTime.of(endDate, endTime), ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("America/New_York"));

            if (startET.toLocalTime().isBefore(LocalTime.of(8, 0)) || endET.toLocalTime().isAfter(LocalTime.of(22, 0))) {
                showAlert("Validation Error", "Appointments must be between 8 AM and 10 PM Eastern Time.");
                return;
            }

            if (startDate.getDayOfWeek() == DayOfWeek.SATURDAY || startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                showAlert("Validation Error", "Appointments cannot be scheduled on weekends.");
                return;
            }

            if (AppointmentData.checkForOverlappingAppointments(startET.toLocalDateTime(), endET.toLocalDateTime(), customerId)) {
                showAlert("Validation Error", "Customer already has an overlapping mainpage.");
                return;
            }

            AppointmentData.updateAppointment(appointmentId, title, description, location, type,
                    startET.toLocalDateTime(), endET.toLocalDateTime(), customerId, userId, contactId);

            showAlert("Success", "Appointment successfully modified!");
            navigateToAppointmentView(actionEvent);

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the mainpage.");
        }
    }

    @FXML
    public void ModifyApptCancelAction(ActionEvent actionEvent) {
        navigateToAppointmentView(actionEvent);
    }

    private void navigateToAppointmentView(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/mainpage.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Unable to return to the Appointments.");
        }
    }

    private void showAlert(String title, String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }
}