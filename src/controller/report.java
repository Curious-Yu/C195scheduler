package controller;

import helper.AppointmentData;
import helper.ContactData;
import helper.CountryData;
import helper.CustomerData;
import helper.FirstLevelDivisionData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appointments;
import model.Contacts;
import model.Countries;
import model.FirstLevelDivisions;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Provides functionality for generating various reports such as:
 * - A contact report listing appointment details.
 * - A customer appointment count report grouped by appointment type and month.
 * - An appointment location report grouped by country and state/province.
 */
public class report implements Initializable {

    //------ Contact Report UI Elements ------
    @FXML private TableView<Appointments> contactReportTable;
    @FXML private TableColumn<Appointments, String> reportApptID;
    @FXML private TableColumn<Appointments, String> reportApptTitle;
    @FXML private TableColumn<Appointments, String> reportApptType;
    @FXML private TableColumn<Appointments, String> reportApptDescription;
    @FXML private TableColumn<Appointments, String> reportApptAddress;
    @FXML private TableColumn<Appointments, String> reportApptStart;
    @FXML private TableColumn<Appointments, String> reportApptEnd;
    @FXML private TableColumn<Appointments, String> reportApptCustomerID;
    @FXML private ComboBox<Contacts> reportContact;

    //------ Appointment Count by Type and Month Report UI Elements ------
    @FXML private TableView<CustomerApptReport> customerTypeMonthReportTable;
    @FXML private TableColumn<CustomerApptReport, String> reportCustomerApptMonth;
    @FXML private TableColumn<CustomerApptReport, String> reportCustomerApptType;
    @FXML private TableColumn<CustomerApptReport, Long> reportCustomerApptCount;

    //------ Appointment Location Report UI Elements ------
    @FXML private TableView<CustomerLocationReport> apptLocationReportTable;
    @FXML private TableColumn<CustomerLocationReport, String> reportApptCountry;
    @FXML private TableColumn<CustomerLocationReport, String> reportApptStateProvince;
    @FXML private TableColumn<CustomerLocationReport, Long> reportApptCount;
    @FXML private Button reportBack;

    /**
     * Inner class representing the appointment count report for a given month and type.
     */
    public static class CustomerApptReport {
        private final String appointmentMonth;
        private final String appointmentType;
        private final long appointmentCount;

        /**
         * Constructs a CustomerApptReport.
         *
         * @param appointmentMonth the formatted appointment month (e.g., "February 2020")
         * @param appointmentType  the appointment type
         * @param appointmentCount the count of appointments for the month and type
         */
        public CustomerApptReport(String appointmentMonth, String appointmentType, long appointmentCount) {
            this.appointmentMonth = appointmentMonth;
            this.appointmentType = appointmentType;
            this.appointmentCount = appointmentCount;
        }

        /**
         * @return the appointment month
         */
        public String getAppointmentMonth() {
            return appointmentMonth;
        }

        /**
         * @return the appointment type
         */
        public String getAppointmentType() {
            return appointmentType;
        }

        /**
         * @return the appointment count
         */
        public long getAppointmentCount() {
            return appointmentCount;
        }
    }

    /**
     * Inner class representing the appointment location report.
     */
    public static class CustomerLocationReport {
        private final String country;
        private final String state;
        private final long count;

        /**
         * Constructs a CustomerLocationReport.
         *
         * @param country the country name
         * @param state   the state/province name
         * @param count   the count of appointments for that location
         */
        public CustomerLocationReport(String country, String state, long count) {
            this.country = country;
            this.state = state;
            this.count = count;
        }

        /**
         * @return the country name
         */
        public String getCountry() {
            return country;
        }

        /**
         * @return the state/province name
         */
        public String getState() {
            return state;
        }

        /**
         * @return the appointment count
         */
        public long getCount() {
            return count;
        }
    }

    /**
     * Initializes the report view by setting up tables, columns, and data.
     *
     * <p>
     * The following lambda expressions are used:
     * <ul>
     *   <li>Each cell value factory for the appointment columns converts a property (e.g., appointment ID)
     *       to a StringProperty using a lambda expression.</li>
     *   <li>The lambda in reportContact.setConverter converts a Contacts object to its string representation.</li>
     *   <li>The lambda in reportContact.setCellFactory creates a custom ListCell for displaying Contacts.</li>
     *   <li>Lambda expressions in the stream operations group appointments by month and type, sort months, and group by location.</li>
     * </ul>
     * </p>
     *
     * @param url the URL used to resolve relative paths for the root object, or null if not known
     * @param resourceBundle the ResourceBundle for localization, or null if not provided
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //------ Contact Report Setup ------
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        reportApptID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAppointmentId()))); // Lambda: Converts appointment ID to StringProperty.
        reportApptTitle.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitle())); // Lambda: Converts title to StringProperty.
        reportApptType.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType())); // Lambda: Converts type to StringProperty.
        reportApptDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription())); // Lambda: Converts description to StringProperty.
        reportApptAddress.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLocation())); // Lambda: Converts location to StringProperty.
        reportApptStart.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStartDateTime().format(formatter))); // Lambda: Formats startDateTime to StringProperty.
        reportApptEnd.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEndDateTime().format(formatter))); // Lambda: Formats endDateTime to StringProperty.
        reportApptCustomerID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCustomerId()))); // Lambda: Converts customer ID to StringProperty.
        contactReportTable.setItems(AppointmentData.getAllAppointments());

        reportContact.setItems(ContactData.getAllContacts());
        reportContact.setConverter(new StringConverter<Contacts>() {
            @Override
            public String toString(Contacts contact) {
                // Lambda in setConverter: Converts a Contacts object to its string representation.
                return (contact == null) ? "" : contact.getContactName() + " (ID: " + contact.getContactId() + ")";
            }

            @Override
            public Contacts fromString(String string) {
                return null;
            }
        });
        reportContact.setCellFactory(cb -> new ListCell<Contacts>() {
            @Override
            protected void updateItem(Contacts contact, boolean empty) {
                super.updateItem(contact, empty);
                // Lambda in setCellFactory: Sets the cell text for a Contacts object.
                setText(empty || contact == null ? "" : contact.getContactName() + " (ID: " + contact.getContactId() + ")");
            }
        });

        //------ Appointment Count by Type and Month Setup ------
        reportCustomerApptMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        reportCustomerApptType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        reportCustomerApptCount.setCellValueFactory(new PropertyValueFactory<>("appointmentCount"));

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        // Lambda: Group appointments by formatted month and type, counting the number in each group.
        Map<String, Map<String, Long>> groupedData = AppointmentData.getAllAppointments().stream()
                .collect(Collectors.groupingBy(
                        appt -> appt.getStartDateTime().format(monthFormatter),
                        Collectors.groupingBy(Appointments::getType, Collectors.counting())
                ));

        DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        // Lambda: Sort the month keys from groupedData chronologically.
        List<String> sortedMonths = groupedData.keySet().stream()
                .sorted((m1, m2) -> {
                    try {
                        LocalDate d1 = LocalDate.parse("01 " + m1, parseFormatter);
                        LocalDate d2 = LocalDate.parse("01 " + m2, parseFormatter);
                        return d1.compareTo(d2);
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                        return m1.compareTo(m2);
                    }
                })
                .collect(Collectors.toList());

        ObservableList<CustomerApptReport> apptReportData = FXCollections.observableArrayList();
        for (String month : sortedMonths) {
            Map<String, Long> typeMap = groupedData.get(month);
            // Loop through each appointment type and add a CustomerApptReport.
            for (Map.Entry<String, Long> entry : typeMap.entrySet()) {
                apptReportData.add(new CustomerApptReport(month, entry.getKey(), entry.getValue()));
            }
        }
        customerTypeMonthReportTable.setItems(apptReportData);

        //------ Appointment Location Report Setup ------
        reportApptCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        reportApptStateProvince.setCellValueFactory(new PropertyValueFactory<>("state"));
        reportApptCount.setCellValueFactory(new PropertyValueFactory<>("count"));

        ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
        ObservableList<model.Customers> allCustomers = CustomerData.getAllCustomers();
        ObservableList<CustomerLocationReport> locationReports = FXCollections.observableArrayList();

        // Lambda: Group appointments by customer location (country and state) using customer division.
        Map<String, Long> locationGrouping = allAppointments.stream().collect(Collectors.groupingBy(
                appt -> {
                    int custId = appt.getCustomerId();
                    model.Customers cust = allCustomers.stream()
                            .filter(c -> c.getCustomerId() == custId) // Lambda: Filter to match customer ID.
                            .findFirst().orElse(null);
                    if (cust != null) {
                        int divisionId = cust.getDivisionId();
                        try {
                            FirstLevelDivisions division = FirstLevelDivisionData.getFirstLevelDivisionById(divisionId);
                            if (division != null) {
                                int countryId = division.getCountryId();
                                Countries country = CountryData.getCountryById(countryId);
                                String countryName = (country != null) ? country.getCountry() : "N/A";
                                String stateName = division.getDivision();
                                return countryName + " - " + stateName;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return "N/A - N/A";
                },
                Collectors.counting()
        ));
        locationGrouping.forEach((key, count) -> {
            String[] parts = key.split(" - ");
            String country = parts.length > 0 ? parts[0] : "N/A";
            String state = parts.length > 1 ? parts[1] : "N/A";
            locationReports.add(new CustomerLocationReport(country, state, count));
        });
        // Lambda: Sort the location reports by country and state.
        locationReports.sort((r1, r2) -> {
            int cmp = r1.getCountry().compareTo(r2.getCountry());
            return (cmp == 0) ? r1.getState().compareTo(r2.getState()) : cmp;
        });
        apptLocationReportTable.setItems(locationReports);

        //------ Back Button Setup ------
        // (Additional initialization as needed)
    }

    /**
     * Handles the contact dropdown selection and filters the contact report table.
     *
     * @param event the ActionEvent triggered by selecting a contact
     */
    @FXML
    private void reportContactDropdown(ActionEvent event) {
        Contacts selectedContact = reportContact.getValue();
        if (selectedContact != null) {
            ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
            ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();
            // Lambda: Iterate through appointments and add those matching the selected contact.
            for (Appointments appt : allAppointments) {
                if (appt.getContactId() == selectedContact.getContactId()) {
                    filteredAppointments.add(appt);
                }
            }
            contactReportTable.setItems(filteredAppointments);
        }
    }

    /**
     * Handles the back button action to return to the main page.
     *
     * @param event the ActionEvent triggered by the back button
     */
    @FXML
    private void reportBackButton(ActionEvent event) {
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