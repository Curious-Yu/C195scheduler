package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.ResourceBundle;

public class addAppointment implements Initializable {

    // FXML fields mapped from the addAppointment.fxml file
    @FXML private TextField apptID;           // Disabled; auto-generated.
    @FXML private TextField apptTitle;
    @FXML private TextField apptDescription;
    @FXML private TextField apptLocation;
    @FXML private TextField apptType;
    @FXML private DatePicker apptStartDate;
    @FXML private ChoiceBox<String> apptStartTime;
    @FXML private DatePicker apptEndDate;
    @FXML private ChoiceBox<String> apptEndTime;
    // Use ComboBox with model objects for meaningful display.
    @FXML private ComboBox<Contacts> apptContactID;
    @FXML private ComboBox<Customers> apptCustomerID;
    @FXML private ComboBox<Users> apptUserID;
    @FXML private Button addApptSave;
    @FXML private Button addApptCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the start and end time ChoiceBoxes with times in 5-minute increments (00:00 to 23:55)
        apptStartTime.getItems().clear();
        apptEndTime.getItems().clear();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 5) {
                String timeStr = String.format("%02d:%02d", hour, minute);
                apptStartTime.getItems().add(timeStr);
                apptEndTime.getItems().add(timeStr);
            }
        }

        // Populate the contacts ComboBox from the database.
        apptContactID.setItems(ContactData.getAllContacts());
        apptContactID.setConverter(new StringConverter<Contacts>() {
            @Override
            public String toString(Contacts contact) {
                if (contact == null) {
                    return "";
                }
                return contact.getContactName() + " (ID: " + contact.getContactId() + ")";
            }
            @Override
            public Contacts fromString(String string) {
                return null; // Not needed.
            }
        });
        apptContactID.setCellFactory((comboBox) -> {
            return new ListCell<Contacts>() {
                @Override
                protected void updateItem(Contacts contact, boolean empty) {
                    super.updateItem(contact, empty);
                    if (empty || contact == null) {
                        setText("");
                    } else {
                        setText(contact.getContactName() + " (ID: " + contact.getContactId() + ")");
                    }
                }
            };
        });

        // Populate the customer ComboBox with data from the database.
        apptCustomerID.setItems(CustomerData.getAllCustomers());
        apptCustomerID.setConverter(new StringConverter<Customers>() {
            @Override
            public String toString(Customers customer) {
                if (customer == null) {
                    return "";
                }
                return customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")";
            }
            @Override
            public Customers fromString(String string) {
                return null; // Not needed.
            }
        });
        apptCustomerID.setCellFactory((comboBox) -> {
            return new ListCell<Customers>() {
                @Override
                protected void updateItem(Customers customer, boolean empty) {
                    super.updateItem(customer, empty);
                    if (empty || customer == null) {
                        setText("");
                    } else {
                        setText(customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")");
                    }
                }
            };
        });

        // Populate the user ComboBox with data from the database.
        apptUserID.setItems(UsersData.getAllUsers());
        apptUserID.setConverter(new StringConverter<Users>() {
            @Override
            public String toString(Users user) {
                if (user == null) {
                    return "";
                }
                return user.getUserName() + " (ID: " + user.getUserId() + ")";
            }
            @Override
            public Users fromString(String string) {
                return null; // Not needed.
            }
        });
        apptUserID.setCellFactory((comboBox) -> {
            return new ListCell<Users>() {
                @Override
                protected void updateItem(Users user, boolean empty) {
                    super.updateItem(user, empty);
                    if (empty || user == null) {
                        setText("");
                    } else {
                        setText(user.getUserName() + " (ID: " + user.getUserId() + ")");
                    }
                }
            };
        });

        // Set the Appointment ID field; since it's auto-generated, display "Auto-Gen".
        apptID.setText("Auto-Gen");
    }

    /**
     * Event handler for the Save button.
     * Validates input, creates a new Appointment object, saves it via AppointmentData,
     * and then returns to the main page by replacing the current scene's root.
     */
    @FXML
    private void AddApptSaveAction(ActionEvent event) {
        // Validate that all required fields are completed.
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
            // Combine DatePicker and ChoiceBox values to create LocalDateTime for the Start and End fields.
            LocalDate startDate = apptStartDate.getValue();
            LocalDate endDate = apptEndDate.getValue();
            LocalTime startTime = LocalTime.parse(apptStartTime.getValue());
            LocalTime endTime = LocalTime.parse(apptEndTime.getValue());
            LocalDateTime startDateTime = startDate.atTime(startTime);
            LocalDateTime endDateTime = endDate.atTime(endTime);

            // --- BUSINESS HOURS VALIDATION ---
            // Business hours: 8:00 AM to 10:00 PM Eastern Time (ET)
            ZoneId easternZone = ZoneId.of("America/New_York");
            // Convert the entered local times to Eastern Time.
            ZonedDateTime startEastern = startDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(easternZone);
            ZonedDateTime endEastern = endDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(easternZone);

            LocalTime businessStart = LocalTime.of(8, 0);  // 8:00 AM ET
            LocalTime businessEnd = LocalTime.of(22, 0);   // 10:00 PM ET

            // Check that the start time is not before business start and end time is not after business end.
            if (startEastern.toLocalTime().isBefore(businessStart) || endEastern.toLocalTime().isAfter(businessEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Outside Business Hours");
                alert.setHeaderText("Appointment time is outside business hours");
                alert.setContentText("Appointments must be scheduled between 8:00 AM and 10:00 PM Eastern Time.");
                alert.showAndWait();
                return;
            }

            // --- OVERLAPPING APPOINTMENT VALIDATION ---
            // Check if the new appointment overlaps with any existing appointment for the same customer.
            int customerID = apptCustomerID.getValue().getCustomerId();
            ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
            for (Appointments existingAppt : allAppointments) {
                // Only check appointments for the same customer.
                if (existingAppt.getCustomerId() == customerID) {
                    // Overlap condition: newStart < existingEnd AND newEnd > existingStart.
                    if (startDateTime.isBefore(existingAppt.getEndDateTime()) &&
                            endDateTime.isAfter(existingAppt.getStartDateTime())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Overlapping Appointment");
                        alert.setHeaderText("Appointment Overlap Detected");
                        alert.setContentText("The new appointment overlaps with an existing appointment for the selected customer.");
                        alert.showAndWait();
                        return;
                    }
                }
            }

            // Retrieve text values for Title, Description, Location, and Type.
            String title = apptTitle.getText();
            String description = apptDescription.getText();
            String location = apptLocation.getText();
            String type = apptType.getText();
            int contactID = apptContactID.getValue().getContactId();
            int userID = apptUserID.getValue().getUserId();

            // Create a new Appointment object.
            Appointments newAppointment = new Appointments(
                    0,             // Appointment ID (0 if auto-generated)
                    title,         // Title
                    description,   // Description
                    location,      // Location
                    type,          // Type
                    startDateTime, // Start (as entered)
                    endDateTime,   // End (as entered)
                    customerID,    // Customer_ID
                    userID,        // User_ID
                    contactID      // Contact_ID
            );

            // Save the appointment to the database.
            AppointmentData.addAppointment(newAppointment);

            // Notify the user that the appointment was successfully added.
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Appointment Added");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The appointment was successfully added.");
            successAlert.showAndWait();

            // Return to the main page in the same window.
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
     * Event handler for the Cancel button.
     * Returns to the main page (refreshing the appointment table) in the same window.
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