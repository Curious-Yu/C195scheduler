package controller;

import helper.ContactData;
import helper.CustomerData;
import helper.UsersData;
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
import model.Customers;
import model.Contacts;
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

public class addAppointment implements Initializable {
    @FXML
    private TextField apptTitle;
    @FXML
    private TextField apptDescription;
    @FXML
    private TextField apptLocation;
    @FXML
    private TextField apptType;
    @FXML
    private DatePicker apptStartDate;
    @FXML
    private ChoiceBox<LocalTime> apptStartTime;
    @FXML
    private DatePicker apptEndDate;
    @FXML
    private ChoiceBox<LocalTime> apptEndTime;
    @FXML
    private ComboBox<String> apptCustomerID;
    @FXML
    private ComboBox<String> apptUserID;
    @FXML
    private ComboBox<String> apptContactID;
    @FXML
    private Button addApptSave;
    @FXML
    private Button addApptCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTimeChoiceBoxes();
        populateContactComboBox();
        populateCustomerComboBox();
        populateUserComboBox();
    }

    private void populateTimeChoiceBoxes() {
        LocalTime startTime = LocalTime.of(0, 0);  // Start at 12:00 AM (midnight)
        LocalTime endTime = LocalTime.of(23, 55);  // End at 11:55 PM
        List<LocalTime> timeSlots = IntStream.iterate(0, i -> i + 5)  // Step by 5 minutes
                .limit(((24 * 60) / 5))  // Generate time slots for a full 24-hour period
                .mapToObj(startTime::plusMinutes)
                .collect(Collectors.toList());
        apptStartTime.getItems().addAll(timeSlots);
        apptEndTime.getItems().addAll(timeSlots);
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
    @FXML
    public void AddApptSaveAction(ActionEvent actionEvent) {
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

            // Validation for empty fields
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() ||
                    startDate == null || startTime == null || endDate == null || endTime == null ||
                    selectedCustomer == null || selectedUser == null || selectedContact == null) {
                showAlert("Validation Error", "All fields must be filled.");
                return;
            }

            // Parse IDs
            int customerId = Integer.parseInt(selectedCustomer.split(" \\(ID: ")[1].replace(")", ""));
            int userId = Integer.parseInt(selectedUser.split(" \\(ID: ")[1].replace(")", ""));
            int contactId = Integer.parseInt(selectedContact.split(" \\(ID: ")[1].replace(")", ""));

            // Check for 0 or invalid IDs
            if (customerId == 0 || userId == 0 || contactId == 0) {
                showAlert("Validation Error", "Invalid customer, user, or contact ID.");
                return;
            }

            // Convert start and end times to Eastern Time Zone for comparison
            ZonedDateTime startET = ZonedDateTime.of(LocalDateTime.of(startDate, startTime), ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime endET = ZonedDateTime.of(LocalDateTime.of(endDate, endTime), ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("America/New_York"));

            // Check if appointment is within business hours (8:00 AM to 10:00 PM Eastern Time)
            LocalTime businessStart = LocalTime.of(8, 0);  // 8:00 AM Eastern Time
            LocalTime businessEnd = LocalTime.of(22, 0);  // 10:00 PM Eastern Time
            if (startET.toLocalTime().isBefore(businessStart) || endET.toLocalTime().isAfter(businessEnd)) {
                showAlert("Validation Error", "Appointments must be scheduled between 8 AM and 10 PM Eastern Time.");
                return;
            }

            // Check if the appointment is on a weekend (Saturday or Sunday)
            if (startDate.getDayOfWeek() == DayOfWeek.SATURDAY || startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                showAlert("Validation Error", "Appointments cannot be scheduled on weekends.");
                return;
            }

            // Check for overlapping appointments
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
            if (AppointmentData.checkForOverlappingAppointments(startDateTime, endDateTime, customerId)) {
                showAlert("Validation Error", "Customer already has an overlapping appointment.");
                return;
            }

            // Insert the appointment into the database
            AppointmentData.insertAppointment(title, description, location, type, startDateTime, endDateTime, customerId, userId, contactId);
            showAlert("Success", "Appointment successfully added!");
            navigateToAppointmentView(actionEvent);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while saving the appointment.");
        } catch (NumberFormatException e) {
            // Catch parsing errors if any of the IDs cannot be parsed
            showAlert("Validation Error", "Invalid input in one or more fields.");
        }
    }

    @FXML
    public void AddApptCancelAction(ActionEvent actionEvent) {
        navigateToAppointmentView(actionEvent);
    }

    private void navigateToAppointmentView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to return to Appointments.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
