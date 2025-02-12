package controller;

import helper.CustomerData;
import helper.CountryData;
import helper.AppointmentData;
import helper.JDBC;
import helper.FirstLevelDivisionData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import model.Appointments;
import model.Countries;
import model.Customers;
import model.FirstLevelDivisions;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class mainpage {

    //------ TableViews ------
    @FXML private TableView<Appointments> appointmentTable;
    @FXML private TableView<Customers> customerTable;

    //------ Appointment Columns ------
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

    //------ Customer Columns ------
    @FXML private TableColumn<Customers, String> customerIDColumn;
    @FXML private TableColumn<Customers, String> nameColumn;
    @FXML private TableColumn<Customers, String> addressColumn;
    @FXML private TableColumn<Customers, String> stateColumn;
    @FXML private TableColumn<Customers, String> countryColumn;
    @FXML private TableColumn<Customers, String> postalCodeColumn;
    @FXML private TableColumn<Customers, String> divisionIDColumn;
    @FXML private TableColumn<Customers, String> phoneColumn;

    //------ Buttons ------
    @FXML private Button addAppointmentButton;
    @FXML private Button modifyAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button addCustomerButton;
    @FXML private Button modifyCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button exitButton;
    @FXML private Button reportsButton;

    //------ Radio Buttons & ToggleGroup ------
    @FXML private RadioButton allTimeRadio;
    @FXML private RadioButton currentMonthRadio;
    @FXML private RadioButton currentWeekRadio;
    @FXML private ToggleGroup appointmentToggle;

    //------ Time Zone Display ------
    @FXML private TextField currentTimeZone;

    //------ Event Handlers ------

    // Open addAppointment view
    @FXML
    private void addAppointmentActionButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addAppointment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Open modifyAppointment view with selected appointment data
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
                modifyAppointment modController = loader.getController();
                modController.initData(selectedAppointment);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Delete selected appointment
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String start = selectedAppointment.getStartDateTime().format(formatter);
        String end = selectedAppointment.getEndDateTime().format(formatter);
        String confirmationMessage = "Appointment ID: " + selectedAppointment.getAppointmentId() +
                "\nType: " + selectedAppointment.getType() +
                "\nStart (UTC): " + start +
                "\nEnd (UTC): " + end;
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

    // Open addCustomer view
    @FXML
    private void addCustomerActionButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addCustomer.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading Add Customer View");
            alert.setHeaderText("An error occurred");
            alert.setContentText("Unable to load the add customer view. Please try again.");
            alert.showAndWait();
        }
    }

    // Open modifyCustomer view with selected customer data
    @FXML
    private void modifyCustomerActionButton(ActionEvent event) {
        Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Customer Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to modify.");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyCustomer.fxml"));
            Parent root = loader.load();
            modifyCustomer modController = loader.getController();
            modController.setCustomerData(selectedCustomer);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading Modify Customer View");
            alert.setHeaderText("An error occurred");
            alert.setContentText("Unable to load the modify customer view. Please try again.");
            alert.showAndWait();
        }
    }

    // Delete customer and associated appointments
    @FXML
    private void deleteCustomerActionButton(ActionEvent event) {
        Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Customer Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        }
        ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
        StringBuilder appointmentsDetails = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Appointments appt : allAppointments) {
            if (appt.getCustomerId() == selectedCustomer.getCustomerId()) {
                appointmentsDetails.append("Appointment ID: ").append(appt.getAppointmentId())
                        .append(", Type: ").append(appt.getType())
                        .append(", Start: ").append(appt.getStartDateTime().format(formatter))
                        .append(", End: ").append(appt.getEndDateTime().format(formatter))
                        .append("\n");
            }
        }
        String customerDetails = "Customer ID: " + selectedCustomer.getCustomerId() + "\n" +
                "Customer Name: " + selectedCustomer.getCustomerName() + "\n" +
                "Address: " + selectedCustomer.getAddress() + "\n" +
                "Postal Code: " + selectedCustomer.getPostalCode() + "\n" +
                "Phone: " + selectedCustomer.getPhone();
        String confirmationMessage;
        if (appointmentsDetails.length() > 0) {
            confirmationMessage = "Deleting this customer will also delete the following appointments:\n\n" +
                    appointmentsDetails.toString() +
                    "\nCustomer Details:\n" + customerDetails +
                    "\n\nDo you want to proceed?";
        } else {
            confirmationMessage = "Customer Details:\n" + customerDetails +
                    "\n\nThere are no appointments for this customer.\nDo you want to proceed with deletion?";
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete Customer");
        confirmAlert.setHeaderText("Delete Customer and Associated Appointments");
        confirmAlert.setContentText(confirmationMessage);
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (Appointments appt : allAppointments) {
                if (appt.getCustomerId() == selectedCustomer.getCustomerId()) {
                    AppointmentData.deleteAppointment(appt.getAppointmentId());
                }
            }
            CustomerData.deleteCustomer(selectedCustomer.getCustomerId());
            ObservableList<Customers> updatedCustomers = CustomerData.getAllCustomers();
            customerTable.setItems(updatedCustomers);
            ObservableList<Appointments> updatedAppointments = AppointmentData.getAllAppointments();
            appointmentTable.setItems(updatedAppointments);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Customer Deleted");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The customer and all associated appointments have been deleted successfully.");
            successAlert.showAndWait();
        }
    }

    // Exit application and close DB connection
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

    // Open reports view
    @FXML
    private void reportsActionButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading Reports");
            alert.setHeaderText("An error occurred");
            alert.setContentText("Unable to load the reports view. Please try again.");
            alert.showAndWait();
        }
    }

    // Filter all appointments
    @FXML
    private void OnAllTimeRadio(ActionEvent event) {
        ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
        appointmentTable.setItems(allAppointments);
    }

    // Filter appointments by current month
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

    // Filter appointments by current week
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

    //------ Initialize tables and time zone ------
    public void initialize() {
        // Appointment table columns
        appointmentIDColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty().asString());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        startsAtColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime().format(formatter)));
        endsAtColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDateTime().format(formatter)));
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asString());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asString());
        contactIdColumn.setCellValueFactory(cellData -> cellData.getValue().contactIdProperty().asString());

        // Customer table columns
        customerIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCustomerId())));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        postalCodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        divisionIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDivisionId())));

        // State column: lookup division name using division ID.
        ObservableList<FirstLevelDivisions> divisions = FXCollections.observableArrayList();
        try {
            divisions.addAll(FirstLevelDivisionData.getAllFirstLevelDivisions());
        } catch (Exception e) {
            e.printStackTrace();
        }
        stateColumn.setCellValueFactory(cellData -> {
            int divisionId = cellData.getValue().getDivisionId();
            String stateName = divisions.stream()
                    .filter(div -> div.getDivisionId() == divisionId)
                    .map(FirstLevelDivisions::getDivision)
                    .findFirst()
                    .orElse("N/A");
            return new SimpleStringProperty(stateName);
        });

        // Country column: lookup country using division ID.
        ObservableList<Countries> countries = FXCollections.observableArrayList();
        try {
            countries.addAll(CountryData.getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
        countryColumn.setCellValueFactory(cellData -> {
            int divisionId = cellData.getValue().getDivisionId();
            String countryName = divisions.stream()
                    .filter(div -> div.getDivisionId() == divisionId)
                    .map(div -> {
                        int countryId = div.getCountryId();
                        return countries.stream()
                                .filter(c -> c.getCountryId() == countryId)
                                .map(Countries::getCountry)
                                .findFirst()
                                .orElse("N/A");
                    })
                    .findFirst()
                    .orElse("N/A");
            return new SimpleStringProperty(countryName);
        });

        // Time zone display
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(dtf);
        String zoneIdStr = zone.getId();
        String[] zoneParts = zoneIdStr.split("/");
        String region = (zoneParts.length > 0) ? zoneParts[0] : "";
        String city = (zoneParts.length > 1) ? zoneParts[1].replace('_', ' ') : "";
        currentTimeZone.setText(city + ", " + region + " - " + formattedTime);

        // Default radio button and load appointments
        allTimeRadio.setSelected(true);
        OnAllTimeRadio(null);

        // Load customer data
        ObservableList<Customers> customersList = CustomerData.getAllCustomers();
        customerTable.setItems(customersList);

        // Check upcoming appointments
        checkUpcomingAppointments();
    }

    //------ Check upcoming appointments within 15 minutes ------
    private void checkUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdUtc = now.plusMinutes(15);
        ObservableList<Appointments> appointments = AppointmentData.getAllAppointments();
        StringBuilder alertMsg = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Appointments appt : appointments) {
            LocalDateTime apptStart = appt.getStartDateTime();
            if ((apptStart.isEqual(now) || apptStart.isAfter(now)) && apptStart.isBefore(thresholdUtc)) {
                alertMsg.append("Appointment ID: ").append(appt.getAppointmentId())
                        .append(", Date & Time (UTC): ").append(apptStart.format(formatter))
                        .append("\n");
            }
        }
        if (alertMsg.length() > 0) {
            Alert upcomingAlert = new Alert(Alert.AlertType.INFORMATION);
            upcomingAlert.setTitle("Upcoming Appointment(s)");
            upcomingAlert.setHeaderText("You have an appointment within the next 15 minutes:");
            upcomingAlert.setContentText(alertMsg.toString());
            upcomingAlert.showAndWait();
        } else {
            Alert noUpcomingAlert = new Alert(Alert.AlertType.INFORMATION);
            noUpcomingAlert.setTitle("No Upcoming Appointments");
            noUpcomingAlert.setHeaderText(null);
            noUpcomingAlert.setContentText("You do not have any appointments scheduled within the next 15 minutes.");
            noUpcomingAlert.showAndWait();
        }
    }
}