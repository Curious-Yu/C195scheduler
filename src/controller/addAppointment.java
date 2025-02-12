package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appointments;
import model.Contacts;
import model.Customers;
import model.Users;
import helper.AppointmentData;
import helper.ContactData;
import helper.CustomerData;
import helper.UsersData;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for adding a new appointment.
 * <p>
 * This controller handles populating the add appointment form, validating user input,
 * creating and saving a new appointment, and returning to the main page.
 * </p>
 */
public class addAppointment implements Initializable {

    //------ FXML Fields ------
    @FXML private TextField apptID; // Auto-generated
    @FXML private TextField apptTitle;
    @FXML private TextField apptDescription;
    @FXML private TextField apptLocation;
    @FXML private TextField apptType;
    @FXML private DatePicker apptStartDate;
    @FXML private ChoiceBox<String> apptStartTime;
    @FXML private DatePicker apptEndDate;
    @FXML private ChoiceBox<String> apptEndTime;
    @FXML private ComboBox<Contacts> apptContactID;
    @FXML private ComboBox<Customers> apptCustomerID;
    @FXML private ComboBox<Users> apptUserID;
    @FXML private Button addApptSave;
    @FXML private Button addApptCancel;

    /**
     * Initializes the add appointment form.
     * <p>
     * Populates the start and end time ChoiceBoxes with 5-minute increments, and sets up the
     * Contacts, Customers, and Users ComboBoxes with appropriate converters and cell factories.
     * <br>
     * Lambda expressions used:
     * <ul>
     *   <li>In the converter for apptContactID: converts a Contacts object to a String.</li>
     *   <li>In the cell factory for apptContactID: creates a custom ListCell to display contact details.</li>
     *   <li>Similarly for apptCustomerID and apptUserID converters and cell factories.</li>
     * </ul>
     * </p>
     *
     * @param url the URL used to resolve relative paths for the root object, or null if not known
     * @param resourceBundle the ResourceBundle for localization, or null if not provided
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //------ Populate Time ChoiceBoxes ------
        apptStartTime.getItems().clear();
        apptEndTime.getItems().clear();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 5) {
                String timeStr = String.format("%02d:%02d", hour, minute);
                apptStartTime.getItems().add(timeStr);
                apptEndTime.getItems().add(timeStr);
            }
        }
        //------ Set up Contacts ComboBox ------
        apptContactID.setItems(ContactData.getAllContacts());
        apptContactID.setConverter(new StringConverter<Contacts>() {
            @Override
            public String toString(Contacts contact) {
                return (contact == null) ? "" : contact.getContactName() + " (ID: " + contact.getContactId() + ")";
            }
            @Override
            public Contacts fromString(String string) {
                return null; // Not needed.
            }
        });
        apptContactID.setCellFactory(comboBox -> new ListCell<Contacts>() {
            @Override
            protected void updateItem(Contacts contact, boolean empty) {
                super.updateItem(contact, empty);
                setText(empty || contact == null ? "" : contact.getContactName() + " (ID: " + contact.getContactId() + ")");
            }
        });
        //------ Set up Customers ComboBox ------
        apptCustomerID.setItems(CustomerData.getAllCustomers());
        apptCustomerID.setConverter(new StringConverter<Customers>() {
            @Override
            public String toString(Customers customer) {
                return (customer == null) ? "" : customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")";
            }
            @Override
            public Customers fromString(String string) {
                return null; // Not needed.
            }
        });
        apptCustomerID.setCellFactory(comboBox -> new ListCell<Customers>() {
            @Override
            protected void updateItem(Customers customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty || customer == null ? "" : customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")");
            }
        });
        //------ Set up Users ComboBox ------
        apptUserID.setItems(UsersData.getAllUsers());
        apptUserID.setConverter(new StringConverter<Users>() {
            @Override
            public String toString(Users user) {
                return (user == null) ? "" : user.getUserName() + " (ID: " + user.getUserId() + ")";
            }
            @Override
            public Users fromString(String string) {
                return null; // Not needed.
            }
        });
        apptUserID.setCellFactory(comboBox -> new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? "" : user.getUserName() + " (ID: " + user.getUserId() + ")");
            }
        });
        //------ Set Appointment ID ------
        apptID.setText("Auto-Gen");
    }

    /**
     * Handles the Save button action to add a new appointment.
     * <p>
     * Validates that all required fields are completed, ensures the appointment end time is after the start time,
     * validates business hours (8:00 AM to 10:00 PM Eastern Time), checks for overlapping appointments,
     * creates a new appointment, saves it to the database, and returns to the main page.
     * </p>
     *
     * @param event the ActionEvent triggered by clicking the Save button
     */
    @FXML
    private void AddApptSaveAction(ActionEvent event) {
        // Validate required fields
        if (apptTitle.getText().isEmpty() ||
                apptDescription.getText().isEmpty() ||
                apptLocation.getText().isEmpty() ||
                apptType.getText().isEmpty() ||
                apptStartDate.getValue() == null ||
                apptStartTime.getValue() == null ||
                apptEndDate.getValue() == null ||
                apptEndTime.getValue() == null ||
                apptContactID.getValue() == null ||
                apptCustomerID.getValue() == null ||
                apptUserID.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Data");
            alert.setHeaderText("All fields must be completed");
            alert.setContentText("Please complete all fields before saving the appointment.");
            alert.showAndWait();
            return;
        }
        try {
            // Combine date and time values
            LocalDate startDate = apptStartDate.getValue();
            LocalDate endDate = apptEndDate.getValue();
            LocalTime startTime = LocalTime.parse(apptStartTime.getValue());
            LocalTime endTime = LocalTime.parse(apptEndTime.getValue());
            LocalDateTime startDateTime = startDate.atTime(startTime);
            LocalDateTime endDateTime = endDate.atTime(endTime);
            // Validate end time is after start time
            if (!endDateTime.isAfter(startDateTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Appointment Time");
                alert.setHeaderText("End time must be after start time");
                alert.setContentText("Please enter an end date/time that is later than the start date/time.");
                alert.showAndWait();
                return;
            }
            // Business hours validation (8:00 AM - 10:00 PM ET)
            ZoneId easternZone = ZoneId.of("America/New_York");
            ZonedDateTime startEastern = startDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(easternZone);
            ZonedDateTime endEastern = endDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(easternZone);
            LocalTime businessStart = LocalTime.of(8, 0);
            LocalTime businessEnd = LocalTime.of(22, 0);
            if (startEastern.toLocalTime().isBefore(businessStart) || endEastern.toLocalTime().isAfter(businessEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Outside Business Hours");
                alert.setHeaderText("Appointment time is outside business hours");
                alert.setContentText("Appointments must be scheduled between 8:00 AM and 10:00 PM Eastern Time.");
                alert.showAndWait();
                return;
            }
            // Overlapping appointment validation
            int customerID = apptCustomerID.getValue().getCustomerId();
            ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
            for (Appointments existingAppt : allAppointments) {
                if (existingAppt.getCustomerId() == customerID) {
                    if (startDateTime.isBefore(existingAppt.getEndDateTime()) &&
                            endDateTime.isAfter(existingAppt.getStartDateTime())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Overlapping Appointment");
                        alert.setHeaderText("Appointment overlap detected");
                        alert.setContentText("The new appointment overlaps with an existing appointment for the selected customer.");
                        alert.showAndWait();
                        return;
                    }
                }
            }
            // Retrieve field values
            String title = apptTitle.getText();
            String description = apptDescription.getText();
            String location = apptLocation.getText();
            String type = apptType.getText();
            int contactID = apptContactID.getValue().getContactId();
            int userID = apptUserID.getValue().getUserId();
            // Create and save appointment
            Appointments newAppointment = new Appointments(
                    0, title, description, location, type,
                    startDateTime, endDateTime, customerID, userID, contactID);
            AppointmentData.addAppointment(newAppointment);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Appointment Added");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The appointment was successfully added.");
            successAlert.showAndWait();
            // Return to main page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"));
            Parent mainPageRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error Saving Appointment");
            errorAlert.setHeaderText("An error occurred");
            errorAlert.setContentText("Unable to save the appointment. Please try again.");
            errorAlert.showAndWait();
        }
    }

    /**
     * Cancels adding an appointment and returns to the main page.
     *
     * @param event the ActionEvent triggered by clicking the Cancel button
     */
    @FXML
    private void AddApptCancelAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"));
            Parent mainPageRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Returning to Main Page");
            alert.setHeaderText("An error occurred");
            alert.setContentText("Unable to load the main page. Please try again.");
            alert.showAndWait();
        }
    }
}