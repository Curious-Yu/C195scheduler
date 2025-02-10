package controller;

import helper.AppointmentData;
import helper.CountryData;
import helper.CustomerData;
import helper.FirstLevelDivisionData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Countries;
import model.Customers;
import model.FirstLevelDivisions;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import javafx.scene.control.TextField;
import java.time.*;
import java.util.Locale;
import java.util.TimeZone;

public class mainpage {

    @FXML
    public Button addAppointmentButton;
    @FXML
    public Button modifyAppointmentButton;
    @FXML
    public Button deleteAppointmentButton;
    @FXML
    public RadioButton allTimeRadio;
    @FXML
    public ToggleGroup appointmentToggle;
    @FXML
    public RadioButton currentMonthRadio;
    @FXML
    public RadioButton currentWeekRadio;
    @FXML
    public TableView<Appointments> appointmentTable;
    @FXML
    public TableColumn<Appointments, Integer> appointmentIDColumn;
    @FXML
    public TableColumn<Appointments, String> titleColumn;
    @FXML
    public TableColumn<Appointments, String> descriptionColumn;
    @FXML
    public TableColumn<Appointments, String> locationColumn;
    @FXML
    public TableColumn<Appointments, String> typeColumn;
    @FXML
    public TableColumn<Appointments, String> startsAtColumn;
    @FXML
    public TableColumn<Appointments, String> endsAtColumn;
    @FXML
    public TableColumn<Appointments, Integer> customerIdColumn;
    @FXML
    public TableColumn<Appointments, Integer> userIdColumn;
    @FXML
    public TableColumn<Appointments, Integer> contactIdColumn;
    @FXML
    public Button reportsButton;
    @FXML
    public Button exitButton;
    @FXML
    public TableView<Customers> customerTable; // Specify the correct type for the TableView
    @FXML
    public TableColumn<Customers, Integer> customerIDColumn;
    @FXML
    public TableColumn<Customers, String> nameColumn;
    @FXML
    public TableColumn<Customers, String> addressColumn;
    @FXML
    public TableColumn<Customers, String> postalCodeColumn;
    @FXML
    public TableColumn<Customers, String> divisionIDColumn;
    @FXML
    public TableColumn<Customers, String> phoneColumn;
    @FXML
    public TableColumn<Customers, String> stateColumn;
    @FXML
    public TableColumn<Customers, String> countryColumn;
    public Button addCustomerButton;
    public Button modifyCustomerButton;
    public Button deleteCustomerButton;
    @FXML
    public TextField currentTimeZone;


    @FXML
    public void initialize() {
        // Initialize Appointment TableView columns
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startsAtColumn.setCellValueFactory(new PropertyValueFactory<>("localStart"));
        endsAtColumn.setCellValueFactory(new PropertyValueFactory<>("localEnd"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        displayCurrentTimeZone();

        // Load all appointments on startup
        try {
            ObservableList<Appointments> appointments = AppointmentData.selectAllAppointments();
            appointmentTable.setItems(appointments);
            checkUpcomingAppointments(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments.");
        }

        // Initialize Customer TableView columns with the correct PropertyValueFactory
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        divisionIDColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Set the stateColumn property value factory
        stateColumn.setCellValueFactory(cellData -> {
            try {
                // Ensure that the value is of type Customer
                Customers customer = cellData.getValue();

                // Get the divisionId from the customer data
                int divisionId = customer.getDivisionId();

                // Retrieve the corresponding division (state) using the divisionId
                FirstLevelDivisions division = FirstLevelDivisionData.getFirstLevelDivisionById(divisionId);

                // Return the division (state) name, if found, otherwise return "Unknown"
                return new SimpleStringProperty(division != null ? division.getDivision() : "Unknown");
            } catch (SQLException e) {
                // Print stack trace and more detailed error information
                e.printStackTrace();
                return new SimpleStringProperty("SQL Error: " + e.getMessage());
            } catch (ClassCastException e) {
                // Handle case where cellData is not a Customer object
                e.printStackTrace();
                return new SimpleStringProperty("Invalid data type");
            }
        });

        // Set the countryColumn property value factory
        countryColumn.setCellValueFactory(cellData -> {
            try {
                // Ensure that the value is of type Customer
                Customers customer = cellData.getValue();
                int divisionId = customer.getDivisionId(); // Get the divisionId
                FirstLevelDivisions division = FirstLevelDivisionData.getFirstLevelDivisionById(divisionId); // Get the division

                if (division != null) {
                    int countryId = division.getCountryId(); // Get the associated countryId
                    Countries country = CountryData.getCountryById(countryId); // Retrieve the country using countryId
                    return new SimpleStringProperty(country != null ? country.getCountry() : "Unknown");
                } else {
                    return new SimpleStringProperty("Unknown");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("SQL Error: " + e.getMessage());
            }
        });

        // Load customer data into customerTable
        try {
            ObservableList<Customers> customers = CustomerData.getAllCustomers();
            customerTable.setItems(customers);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load customer data.");
        }

        // Load customer data into customerTable
        try {
            ObservableList<Customers> customers = CustomerData.getAllCustomers();
            customerTable.setItems(customers);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load customer data.");
        }
        /**
        startsAtColumn.setCellValueFactory(cellData -> {
            LocalDateTime start = cellData.getValue().getLocalStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(start != null ? start.format(formatter) : "");
        });

        endsAtColumn.setCellValueFactory(cellData -> {
            LocalDateTime end = cellData.getValue().getLocalEnd();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(end != null ? end.format(formatter) : "");
        }); **/
        // Initialize the table columns
        startsAtColumn.setCellValueFactory(cellData -> {
            // Convert LocalDateTime to String with formatting
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(cellData.getValue().getLocalStart().format(formatter));
        });

        endsAtColumn.setCellValueFactory(cellData -> {
            // Convert LocalDateTime to String with formatting
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(cellData.getValue().getLocalEnd().format(formatter));
        });
        loadAppointments();
    }
    private void loadAppointments() {
        try {
            // Fetch all appointments (already adjusted for local time in AppointmentData.java)
            ObservableList<Appointments> appointments = AppointmentData.selectAllAppointments();

            // Set the data to the table view
            appointmentTable.setItems(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void checkUpcomingAppointments(ObservableList<Appointments> appointments) {
        LocalDateTime now = LocalDateTime.now();
        for (Appointments appointment : appointments) {
            LocalDateTime appointmentStartTime = appointment.getLocalStart();
            long minutesUntilAppointment = ChronoUnit.MINUTES.between(now, appointmentStartTime);

            if (minutesUntilAppointment > 0 && minutesUntilAppointment <= 15) {
                // Alert for an upcoming mainpage within 15 minutes
                String message = String.format("You have an upcoming mainpage!\n" +
                                "Appointment ID: %d\nDate: %s\nTime: %s",
                        appointment.getAppointmentId(),
                        appointmentStartTime.toLocalDate().toString(),
                        appointmentStartTime.toLocalTime().toString());
                showAlert("Upcoming Appointment", message);
                return; // Show alert only for the first upcoming mainpage
            }
        }

        // If no appointments within 15 minutes, show a custom message
        showAlert("No Upcoming Appointments", "There are no appointments within 15 minutes of your login.");
    }

    // Action Methods
    public void addAppointmentActionButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addAppointment.fxml"));
            if (loader.getLocation() == null) {
                showAlert("Error", "addAppointment.fxml not found. Please check the file path.");
                return;
            }
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("Successfully opened addAppointment.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open the Add Appointment window.");
        }
    }

    public void modifyAppointmentActionButton(ActionEvent actionEvent) {
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert("No Selection", "Please select an appointment to modify.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyAppointment.fxml"));
            if (loader.getLocation() == null) {
                showAlert("Error", "modifyAppointment.fxml not found. Please check the file path.");
                return;
            }
            Parent root = loader.load();
            modifyAppointment controller = loader.getController();
            controller.setAppointmentData(selectedAppointment);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("Successfully opened modifyAppointment.fxml with selected mainpage data.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open the Modify Appointment window.");
        }
    }

    public void deleteAppointmentActionButton(ActionEvent actionEvent) {
        try {
            Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                showAlert("No Selection", "Please select an appointment to delete.");
                return;
            }
            int appointmentId = selectedAppointment.getAppointmentId();
            String appointmentType = selectedAppointment.getType();
            String appointmentDate = selectedAppointment.getLocalStart().toLocalDate().toString();
            String appointmentTime = selectedAppointment.getLocalStart().toLocalTime().toString();

            String confirmationMessage = String.format(
                    "Are you sure you want to delete Appointment ID: %d?\nDate: %s\nTime: %s\nType: %s",
                    appointmentId, appointmentDate, appointmentTime, appointmentType
            );
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText(confirmationMessage);
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentData.deleteAppointment(appointmentId);
                appointmentTable.setItems(AppointmentData.selectAllAppointments());
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Deletion Successful");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText(
                        String.format("Appointment ID: %d on %s at %s (Type: %s) has been deleted.",
                                appointmentId, appointmentDate, appointmentTime, appointmentType)
                );
                infoAlert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the mainpage.");
        }
    }

    public void OnAllTimeRadio(ActionEvent actionEvent) {
        try {
            ObservableList<Appointments> appointments = AppointmentData.selectAllAppointments();
            updateAppointmentTable(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments.");
        }
    }

    public void OnCurrentMonthRadio(ActionEvent actionEvent) {
        try {
            ObservableList<Appointments> appointments = AppointmentData.selectAppointmentsForCurrentMonth();
            updateAppointmentTable(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments for the current month.");
        }
    }

    public void OnCurrentWeekRadio(ActionEvent actionEvent) {
        try {
            ObservableList<Appointments> appointments = AppointmentData.selectAppointmentsForCurrentWeek();
            updateAppointmentTable(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments for the current week.");
        }
    }


    public void reportsActionButton(ActionEvent actionEvent) {
        // Code for handling reports action
    }

    public void exitActionButton(ActionEvent actionEvent) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Exit Confirmation");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    private void updateAppointmentTable(ObservableList<Appointments> appointments) {
        appointmentTable.setItems(appointments);
        appointmentIDColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        startsAtColumn.setCellValueFactory(cellData -> cellData.getValue().localStartProperty().asString());
        endsAtColumn.setCellValueFactory(cellData -> cellData.getValue().localEndProperty().asString());
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        contactIdColumn.setCellValueFactory(cellData -> cellData.getValue().contactIdProperty().asObject());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Navigates to the Add Customer view.
     * @param actionEvent The event triggered by clicking the Add Customer button.
     */
    public void addCustomerActionButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addCustomer.fxml"));
            if (loader.getLocation() == null) {
                showAlert("Error", "addCustomer.fxml not found. Please check the file path.");
                return;
            }
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("Successfully opened addCustomer.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open the Add Customer window.");
        }
    }

    /**
     * Navigates to the Modify Customer view and preloads customer data.
     * @param actionEvent The event triggered by clicking the Modify Customer button.
     */
    public void modifyCustomerActionButton(ActionEvent actionEvent) {
        Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            showAlert("No Selection", "Please select a customer to modify.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyCustomer.fxml"));
            if (loader.getLocation() == null) {
                showAlert("Error", "modifyCustomer.fxml not found. Please check the file path.");
                return;
            }

            Parent root = loader.load();

            // Get the controller for the modify customer form
            modifyCustomer controller = loader.getController();
            controller.setCustomerData(selectedCustomer);

            // Load the new scene
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Successfully opened modifyCustomer.fxml with selected customer data.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open the Modify Customer window.");
        }
    }

    public void deleteCustomerActionButton(ActionEvent actionEvent) {
    }

    private void displayCurrentTimeZone() {
        // Get the system's time zone
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime localDateTime = ZonedDateTime.now(zoneId);
        ZonedDateTime utcDateTime = localDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        // Extract city name (last part of the time zone ID)
        String timeZoneId = zoneId.getId(); // e.g., "America/New_York"
        String[] parts = timeZoneId.split("/");
        String city = (parts.length > 1) ? parts[1].replace("_", " ") : timeZoneId; // Handle missing city case

        // Get the country from the time zone
        String country = getCountryFromTimeZone(zoneId);

        // Format time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String localTime = localDateTime.format(formatter);
        String utcTime = utcDateTime.format(formatter);

        // Set the text field with city, country, and time details
        String displayText = String.format("%s, %s | Local: %s | UTC: %s", city, country, localTime, utcTime);
        currentTimeZone.setText(displayText);
    }

    private String getCountryFromTimeZone(ZoneId zoneId) {
        // Get the country based on the time zone
        String timeZoneId = zoneId.getId();
        String countryCode = TimeZone.getTimeZone(timeZoneId).getID();
        Locale locale = new Locale("", countryCode);
        return locale.getDisplayCountry().isEmpty() ? "Unknown" : locale.getDisplayCountry(); // Ensure a valid country
    }
}