package controller;

import helper.ContactData;
import helper.CustomerData;
import helper.UsersData; // Add this import to access UsersData
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
import model.Users; // Add this import for the Users model

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
    private ComboBox<String> apptCustomerID;  // Change this to ComboBox<String> for displaying names with IDs
    @FXML
    private ComboBox<String> apptUserID;    // ComboBox for user ID
    @FXML
    private ComboBox<String> apptContactID;  // Change this to ComboBox<String> for displaying contact names with IDs
    @FXML
    private Button addApptSave;
    @FXML
    private Button addApptCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTimeChoiceBoxes();
        populateContactComboBox();  // Populate the Contact ID ComboBox
        populateCustomerComboBox(); // Populate the Customer ID ComboBox with customer names
        populateUserComboBox();     // Populate the User ID ComboBox with user names
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
     * Populates the apptContactID ComboBox with the contact names and IDs.
     */
    private void populateContactComboBox() {
        try {
            // Retrieve the list of all contacts
            List<Contacts> contactsList = ContactData.getAllContacts();
            // Prepare the ComboBox with contact names (and IDs if needed)
            for (Contacts contact : contactsList) {
                // Format the string to show both name and ID
                apptContactID.getItems().add(contact.getContactName() + " (ID: " + contact.getContactId() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to retrieve contact data.");
        }
    }

    /**
     * Populates the apptCustomerID ComboBox with the customer names and IDs.
     */
    private void populateCustomerComboBox() {
        try {
            // Retrieve the list of all customers
            List<Customers> customersList = CustomerData.getAllCustomers();
            // Prepare the ComboBox with customer names (and IDs if needed)
            for (Customers customer : customersList) {
                // Format the string to show both name and ID
                apptCustomerID.getItems().add(customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to retrieve customer data.");
        }
    }

    /**
     * Populates the apptUserID ComboBox with the user names and IDs.
     */
    private void populateUserComboBox() {
        try {
            // Retrieve the list of all users
            List<Users> usersList = UsersData.getAllUsers();  // Ensure this method exists in your UsersData helper class
            // Prepare the ComboBox with user names (and IDs if needed)
            for (Users user : usersList) {
                // Add the formatted string to the ComboBox to show both user name and ID
                apptUserID.getItems().add(user.getUserName() + " (ID: " + user.getUserId() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to retrieve user data.");
        }
    }

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

            // Extract customer ID from the selected combo box entry (parse it from the string)
            String selectedCustomer = apptCustomerID.getValue();
            int customerId = Integer.parseInt(selectedCustomer.split(" \\(ID: ")[1].replace(")", ""));

            // Extract user ID from the selected combo box entry (parse it from the string)
            String selectedUser = apptUserID.getValue();
            int userId = Integer.parseInt(selectedUser.split(" \\(ID: ")[1].replace(")", ""));

            // Extract contact ID from the selected combo box entry (parse it from the string)
            String selectedContact = apptContactID.getValue();
            int contactId = Integer.parseInt(selectedContact.split(" \\(ID: ")[1].replace(")", ""));

            // Validate input fields
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() ||
                    startDate == null || startTime == null || endDate == null || endTime == null ||
                    customerId == 0 || userId == 0 || contactId == 0) {
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
     * Handles the cancel action by closing the add appointment window and going back to the appointment page.
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