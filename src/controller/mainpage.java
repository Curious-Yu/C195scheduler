package controller;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;
import helper.AppointmentData;
import helper.JDBC;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class mainpage {

    // TableView for appointments and customers
    @FXML private TableView<Appointments> appointmentTable;
    @FXML private TableView<Customers> customerTable;

    // Table Columns for Appointments
    @FXML private TableColumn<Appointments, String> appointmentIDColumn;
    @FXML private TableColumn<Appointments, String> titleColumn;
    @FXML private TableColumn<Appointments, String> descriptionColumn;
    @FXML private TableColumn<Appointments, String> locationColumn;
    @FXML private TableColumn<Appointments, String> typeColumn;
    @FXML private TableColumn<Appointments, String> startsAtColumn;
    @FXML private TableColumn<Appointments, String> endsAtColumn;
    @FXML private TableColumn<Appointments, String> customerIdColumn;
    @FXML private TableColumn<Appointments, String> userIdColumn;
    @FXML private TableColumn<Appointments, String> contactIdColumn;

    // Table Columns for Customers
    @FXML private TableColumn<Customers, String> customerIDColumn;
    @FXML private TableColumn<Customers, String> nameColumn;
    @FXML private TableColumn<Customers, String> addressColumn;
    @FXML private TableColumn<Customers, String> stateColumn;
    @FXML private TableColumn<Customers, String> countryColumn;
    @FXML private TableColumn<Customers, String> postalCodeColumn;
    @FXML private TableColumn<Customers, String> divisionIDColumn;
    @FXML private TableColumn<Customers, String> phoneColumn;

    // Buttons
    @FXML private Button addAppointmentButton;
    @FXML private Button modifyAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button addCustomerButton;
    @FXML private Button modifyCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button exitButton;
    @FXML private Button reportsButton;

    // Radio Buttons
    @FXML private RadioButton allTimeRadio;
    @FXML private RadioButton currentMonthRadio;
    @FXML private RadioButton currentWeekRadio;
    @FXML private ToggleGroup appointmentToggle;

    // TextField for Time Zone (displaying "UTC")
    @FXML private TextField currentTimeZone;

    // ------------------ Event Handlers ------------------

    @FXML
    private void addAppointmentActionButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addAppointment.fxml"));
            Parent root = loader.load();
            // Use the current stage; replace the scene's root.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifyAppointmentActionButton(ActionEvent event) {
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to modify.");
            alert.showAndWait();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyAppointment.fxml"));
                Parent root = loader.load();
                // Pass the selected appointment to the modify controller.
                modifyAppointment modController = loader.getController();
                modController.initData(selectedAppointment);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteAppointmentActionButton(ActionEvent event) {
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }
        // Format the UTC start and end times.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String start = selectedAppointment.getStartDateTime().format(formatter);
        String end = selectedAppointment.getEndDateTime().format(formatter);
        String confirmationMessage = "Appointment ID: " + selectedAppointment.getAppointmentId() +
                "\nType: " + selectedAppointment.getType() +
                "\nStart: " + start +
                "\nEnd: " + end;
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Appointment");
        confirmAlert.setContentText("Are you sure you want to delete the following appointment?\n\n" + confirmationMessage);
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AppointmentData.deleteAppointment(selectedAppointment.getAppointmentId());
            ObservableList<Appointments> updatedAppointments = AppointmentData.getAllAppointments();
            appointmentTable.setItems(updatedAppointments);
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Appointment Deleted");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("The appointment has been deleted successfully.");
            infoAlert.showAndWait();
        }
    }

    @FXML
    private void addCustomerActionButton(ActionEvent event) {
        // Logic for adding a customer.
    }

    @FXML
    private void modifyCustomerActionButton(ActionEvent event) {
        // Logic for modifying a customer.
    }

    @FXML
    private void deleteCustomerActionButton(ActionEvent event) {
        // Logic for deleting a customer.
    }

    @FXML
    private void exitActionButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit the application?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                JDBC.closeConnection();
                System.exit(0);
            }
        });
    }

    @FXML
    private void reportsActionButton(ActionEvent event) {
        // Logic for generating reports.
    }

    @FXML
    private void OnAllTimeRadio(ActionEvent event) {
        ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
        appointmentTable.setItems(allAppointments);
    }

    @FXML
    private void OnCurrentMonthRadio(ActionEvent event) {
        ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
        LocalDate today = LocalDate.now();
        FilteredList<Appointments> filteredAppointments = new FilteredList<>(allAppointments, appointment -> {
            LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
            return (appointmentDate.getMonthValue() == today.getMonthValue()) &&
                    (appointmentDate.getYear() == today.getYear());
        });
        appointmentTable.setItems(filteredAppointments);
    }

    @FXML
    private void OnCurrentWeekRadio(ActionEvent event) {
        ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue(); // Monday=1, Sunday=7
        LocalDate startOfWeek = today.minusDays(dayOfWeek - 1);
        LocalDate endOfWeek = today.plusDays(7 - dayOfWeek);
        FilteredList<Appointments> filteredAppointments = new FilteredList<>(allAppointments, appointment -> {
            LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
            return (!appointmentDate.isBefore(startOfWeek)) && (!appointmentDate.isAfter(endOfWeek));
        });
        appointmentTable.setItems(filteredAppointments);
    }

    // Initialize method to set up table data, time zone, etc.
    public void initialize() {
        // Setup appointment table columns.
        appointmentIDColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty().asString());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // Display the UTC times exactly as stored in the database.
        startsAtColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStartDateTime().format(formatter)));
        endsAtColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEndDateTime().format(formatter)));
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asString());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asString());
        contactIdColumn.setCellValueFactory(cellData -> cellData.getValue().contactIdProperty().asString());
        // Update the currentTimeZone TextField to display the user's city, region, and current local time.
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(dtf);
        String zoneIdStr = zone.getId();
        String[] zoneParts = zoneIdStr.split("/");
        String region = (zoneParts.length > 0) ? zoneParts[0] : "";
        String city = (zoneParts.length > 1) ? zoneParts[1].replace('_', ' ') : "";
        currentTimeZone.setText(city + ", " + region + " - " + formattedTime);

        // Set the All Time radio button as default and load all appointments.
        allTimeRadio.setSelected(true);
        OnAllTimeRadio(null);
    }
}