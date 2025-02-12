package controller;

import helper.AppointmentData;
import helper.ContactData;
import helper.CustomerData;
import helper.UsersData;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appointments;
import model.Contacts;
import model.Customers;
import model.Users;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.ResourceBundle;

/**
 * Controller for modifying an appointment.
 * Provides methods to populate the form with existing appointment data, validate and save modifications,
 * and return to the main page.
 */
public class modifyAppointment implements Initializable {

    //------ FXML Fields ------
    @FXML private TextField apptID;
    @FXML private TextField apptTitle;
    @FXML private TextField apptDescription;
    @FXML private TextField apptLocation;
    @FXML private TextField apptType;
    @FXML private DatePicker apptStartDate;
    @FXML private ChoiceBox<LocalTime> apptStartTime;
    @FXML private DatePicker apptEndDate;
    @FXML private ChoiceBox<LocalTime> apptEndTime;
    @FXML private ComboBox<Customers> apptCustomerID;
    @FXML private ComboBox<Users> apptUserID;
    @FXML private ComboBox<Contacts> apptContactID;
    @FXML private Button modifyApptSave;
    @FXML private Button modifyApptCancel;
    private Appointments selectedAppointment;

    /**
     * Initializes the modify appointment form.
     * <p>
     * Sets up the time ChoiceBoxes and the Contacts, Customers, and Users ComboBoxes.
     *
     * Lambda expressions used:
     * <ul>
     *   <li>In populateTimeChoiceBox: loops adding LocalTime objects to the ChoiceBox.</li>
     *   <li>In setupComboBoxForContacts: converter lambda converts a Contacts object to String,
     *       and cell factory lambda creates a ListCell to display contact details.</li>
     *   <li>In setupComboBoxForCustomers: converter lambda converts a Customers object to String,
     *       and cell factory lambda creates a ListCell to display customer details.</li>
     *   <li>In setupComboBoxForUsers: converter lambda converts a Users object to String,
     *       and cell factory lambda creates a ListCell to display user details.</li>
     * </ul>
     * </p>
     *
     * @param url the URL used to resolve relative paths for the root object, or null if not known
     * @param resourceBundle the ResourceBundle for localization, or null if not provided
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //------ Populate time choice boxes ------
        populateTimeChoiceBox(apptStartTime);
        populateTimeChoiceBox(apptEndTime);
        //------ Set up Contacts ComboBox ------
        apptContactID.setItems(ContactData.getAllContacts());
        setupComboBoxForContacts(apptContactID);
        //------ Set up Customers ComboBox ------
        apptCustomerID.setItems(CustomerData.getAllCustomers());
        setupComboBoxForCustomers(apptCustomerID);
        //------ Set up Users ComboBox ------
        apptUserID.setItems(UsersData.getAllUsers());
        setupComboBoxForUsers(apptUserID);
    }

    /**
     * Populates the given ChoiceBox with LocalTime values in 5-minute increments.
     *
     * @param choiceBox the ChoiceBox to populate
     */
    private void populateTimeChoiceBox(ChoiceBox<LocalTime> choiceBox) {
        choiceBox.getItems().clear();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 5) {
                choiceBox.getItems().add(LocalTime.of(hour, minute));
            }
        }
        choiceBox.setConverter(new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime time) {
                return (time == null) ? "" : String.format("%02d:%02d", time.getHour(), time.getMinute());
            }
            @Override
            public LocalTime fromString(String string) {
                return (string == null || string.isEmpty()) ? null : LocalTime.parse(string);
            }
        });
    }

    /**
     * Sets up the Contacts ComboBox with a converter and custom cell factory.
     *
     * Lambda expressions used:
     * <ul>
     *   <li>Converter lambda: converts a Contacts object to its string representation.</li>
     *   <li>Cell factory lambda: creates a ListCell to display contact name and ID.</li>
     * </ul>
     *
     * @param comboBox the ComboBox to set up
     */
    private void setupComboBoxForContacts(ComboBox<Contacts> comboBox) {
        comboBox.setConverter(new StringConverter<Contacts>() {
            @Override
            public String toString(Contacts contact) {
                return (contact == null) ? "" : contact.getContactName() + " (ID: " + contact.getContactId() + ")";
            }
            @Override
            public Contacts fromString(String string) {
                return null;
            }
        });
        comboBox.setCellFactory(cb -> new ListCell<Contacts>() {
            @Override
            protected void updateItem(Contacts contact, boolean empty) {
                super.updateItem(contact, empty);
                setText(empty || contact == null ? "" : contact.getContactName() + " (ID: " + contact.getContactId() + ")");
            }
        });
    }

    /**
     * Sets up the Customers ComboBox with a converter and custom cell factory.
     *
     * Lambda expressions used:
     * <ul>
     *   <li>Converter lambda: converts a Customers object to its string representation.</li>
     *   <li>Cell factory lambda: creates a ListCell to display customer name and ID.</li>
     * </ul>
     *
     * @param comboBox the ComboBox to set up
     */
    private void setupComboBoxForCustomers(ComboBox<Customers> comboBox) {
        comboBox.setConverter(new StringConverter<Customers>() {
            @Override
            public String toString(Customers customer) {
                return (customer == null) ? "" : customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")";
            }
            @Override
            public Customers fromString(String string) {
                return null;
            }
        });
        comboBox.setCellFactory(cb -> new ListCell<Customers>() {
            @Override
            protected void updateItem(Customers customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty || customer == null ? "" : customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")");
            }
        });
    }

    /**
     * Sets up the Users ComboBox with a converter and custom cell factory.
     *
     * Lambda expressions used:
     * <ul>
     *   <li>Converter lambda: converts a Users object to its string representation.</li>
     *   <li>Cell factory lambda: creates a ListCell to display user name and ID.</li>
     * </ul>
     *
     * @param comboBox the ComboBox to set up
     */
    private void setupComboBoxForUsers(ComboBox<Users> comboBox) {
        comboBox.setConverter(new StringConverter<Users>() {
            @Override
            public String toString(Users user) {
                return (user == null) ? "" : user.getUserName() + " (ID: " + user.getUserId() + ")";
            }
            @Override
            public Users fromString(String string) {
                return null;
            }
        });
        comboBox.setCellFactory(cb -> new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? "" : user.getUserName() + " (ID: " + user.getUserId() + ")");
            }
        });
    }

    /**
     * Populates the modify appointment form with data from the given appointment.
     *
     * @param appointment the appointment whose data is to be loaded into the form
     */
    public void initData(Appointments appointment) {
        this.selectedAppointment = appointment;
        apptID.setText(String.valueOf(appointment.getAppointmentId()));
        apptTitle.setText(appointment.getTitle());
        apptDescription.setText(appointment.getDescription());
        apptLocation.setText(appointment.getLocation());
        apptType.setText(appointment.getType());
        // Use raw UTC times as stored.
        LocalDate startDate = appointment.getStartDateTime().toLocalDate();
        LocalTime startTime = appointment.getStartDateTime().toLocalTime();
        LocalDate endDate = appointment.getEndDateTime().toLocalDate();
        LocalTime endTime = appointment.getEndDateTime().toLocalTime();
        apptStartDate.setValue(startDate);
        apptStartTime.setValue(startTime);
        apptEndDate.setValue(endDate);
        apptEndTime.setValue(endTime);
        // Set ComboBox selections based on IDs.
        for (Contacts contact : apptContactID.getItems()) {
            if (contact.getContactId() == appointment.getContactId()) {
                apptContactID.setValue(contact);
                break;
            }
        }
        for (Customers customer : apptCustomerID.getItems()) {
            if (customer.getCustomerId() == appointment.getCustomerId()) {
                apptCustomerID.setValue(customer);
                break;
            }
        }
        for (Users user : apptUserID.getItems()) {
            if (user.getUserId() == appointment.getUserId()) {
                apptUserID.setValue(user);
                break;
            }
        }
    }

    /**
     * Saves the modified appointment.
     * <p>
     * Validates input fields, ensures end time is after start time,
     * checks business hours (8 AM - 10 PM ET) and overlapping appointments,
     * then updates the appointment in the database and returns to the main page.
     * </p>
     *
     * @param event the ActionEvent triggered by clicking the Save button
     */
    @FXML
    private void ModifyApptSaveAction(ActionEvent event) {
        // Validate fields.
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
            // Combine date/time values.
            LocalDate startDate = apptStartDate.getValue();
            LocalTime startTime = apptStartTime.getValue();
            LocalDate endDate = apptEndDate.getValue();
            LocalTime endTime = apptEndTime.getValue();
            LocalDateTime startDateTime = startDate.atTime(startTime);
            LocalDateTime endDateTime = endDate.atTime(endTime);
            // Validate that end time is after start time.
            if (!endDateTime.isAfter(startDateTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Appointment Time");
                alert.setHeaderText("End time must be after start time");
                alert.setContentText("Please enter an end date/time that is later than the start date/time.");
                alert.showAndWait();
                return;
            }
            // Business hours validation (8 AM - 10 PM ET).
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
            // Overlapping appointment validation.
            int customerID = apptCustomerID.getValue().getCustomerId();
            ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
            for (Appointments existingAppt : allAppointments) {
                if (existingAppt.getCustomerId() == customerID &&
                        existingAppt.getAppointmentId() != selectedAppointment.getAppointmentId()) {
                    if (startDateTime.isBefore(existingAppt.getEndDateTime()) &&
                            endDateTime.isAfter(existingAppt.getStartDateTime())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Overlapping Appointment");
                        alert.setHeaderText("Appointment overlap detected");
                        alert.setContentText("The updated appointment overlaps with an existing appointment for the selected customer.");
                        alert.showAndWait();
                        return;
                    }
                }
            }
            // Retrieve field values.
            String title = apptTitle.getText();
            String description = apptDescription.getText();
            String location = apptLocation.getText();
            String type = apptType.getText();
            int contactID = apptContactID.getValue().getContactId();
            int userID = apptUserID.getValue().getUserId();
            // Create and update appointment.
            Appointments updatedAppointment = new Appointments(
                    selectedAppointment.getAppointmentId(),
                    title, description, location, type,
                    startDateTime, endDateTime, customerID, userID, contactID);
            AppointmentData.updateAppointment(updatedAppointment);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Appointment Updated");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The appointment was successfully updated.");
            successAlert.showAndWait();
            // Return to main page.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"));
            Parent mainPageRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(mainPageRoot);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Updating Appointment");
            alert.setHeaderText("An error occurred");
            alert.setContentText("Unable to update the appointment. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Cancels the modification and returns to the main page.
     *
     * @param event the ActionEvent triggered by clicking the Cancel button
     */
    @FXML
    private void ModifyApptCancelAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"));
            Parent mainPageRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(mainPageRoot);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Returning to Main Page");
            alert.setHeaderText("An error occurred");
            alert.setContentText("Unable to load the main page. Please try again.");
            alert.showAndWait();
        }
    }
}