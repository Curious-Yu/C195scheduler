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
import javafx.scene.Scene;
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

public class report implements Initializable {

    // ***************** Contact Report *****************
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

    // ********* Customer Appointment Count by Type and Month Report *********
    @FXML private TableView<CustomerApptReport> customerTypeMonthReportTable;
    @FXML private TableColumn<CustomerApptReport, String> reportCustomerApptMonth;
    @FXML private TableColumn<CustomerApptReport, String> reportCustomerApptType;
    @FXML private TableColumn<CustomerApptReport, Long> reportCustomerApptCount;

    // ********* Appointment Location Report *********
    @FXML private TableView<CustomerLocationReport> apptLocationReportTable;
    @FXML private TableColumn<CustomerLocationReport, String> reportApptCountry;
    @FXML private TableColumn<CustomerLocationReport, String> reportApptStateProvince;
    @FXML private TableColumn<CustomerLocationReport, Long> reportApptCount;

    @FXML private Button reportBack;

    // Inner class for Customer Appointment Count by Type and Month Report.
    public static class CustomerApptReport {
        private final String appointmentMonth;
        private final String appointmentType;
        private final long appointmentCount;

        public CustomerApptReport(String appointmentMonth, String appointmentType, long appointmentCount) {
            this.appointmentMonth = appointmentMonth;
            this.appointmentType = appointmentType;
            this.appointmentCount = appointmentCount;
        }

        public String getAppointmentMonth() {
            return appointmentMonth;
        }

        public String getAppointmentType() {
            return appointmentType;
        }

        public long getAppointmentCount() {
            return appointmentCount;
        }
    }

    // Inner class for Appointment Location Report.
    public static class CustomerLocationReport {
        private final String country;
        private final String state;
        private final long count;

        public CustomerLocationReport(String country, String state, long count) {
            this.country = country;
            this.state = state;
            this.count = count;
        }

        public String getCountry() {
            return country;
        }

        public String getState() {
            return state;
        }

        public long getCount() {
            return count;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ------------------ Contact Report Setup ------------------
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        reportApptID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAppointmentId())));
        reportApptTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        reportApptType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        reportApptDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        reportApptAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        reportApptStart.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime().format(formatter)));
        reportApptEnd.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDateTime().format(formatter)));
        reportApptCustomerID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCustomerId())));
        contactReportTable.setItems(AppointmentData.getAllAppointments());

        reportContact.setItems(ContactData.getAllContacts());
        reportContact.setConverter(new StringConverter<Contacts>() {
            @Override
            public String toString(Contacts contact) {
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
                setText(empty || contact == null ? "" : contact.getContactName() + " (ID: " + contact.getContactId() + ")");
            }
        });

        // ------------------ Customer Appointment Count by Type and Month Report Setup ------------------
        reportCustomerApptMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        reportCustomerApptType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        reportCustomerApptCount.setCellValueFactory(new PropertyValueFactory<>("appointmentCount"));

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        Map<String, Map<String, Long>> groupedData = AppointmentData.getAllAppointments().stream()
                .collect(Collectors.groupingBy(
                        appt -> appt.getStartDateTime().format(monthFormatter),
                        Collectors.groupingBy(
                                Appointments::getType,
                                Collectors.counting()
                        )
                ));

        DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
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
            for (Map.Entry<String, Long> entry : typeMap.entrySet()) {
                apptReportData.add(new CustomerApptReport(month, entry.getKey(), entry.getValue()));
            }
        }
        customerTypeMonthReportTable.setItems(apptReportData);

        // ------------------ Appointment Location Report Setup ------------------
        reportApptCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        reportApptStateProvince.setCellValueFactory(new PropertyValueFactory<>("state"));
        reportApptCount.setCellValueFactory(new PropertyValueFactory<>("count"));

        ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
        ObservableList<model.Customers> allCustomers = CustomerData.getAllCustomers();
        ObservableList<CustomerLocationReport> locationReports = FXCollections.observableArrayList();

        Map<String, Long> locationGrouping = allAppointments.stream().collect(Collectors.groupingBy(
                appt -> {
                    int custId = appt.getCustomerId();
                    model.Customers cust = allCustomers.stream()
                            .filter(c -> c.getCustomerId() == custId)
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

        locationReports.sort((r1, r2) -> {
            int cmp = r1.getCountry().compareTo(r2.getCountry());
            return (cmp == 0) ? r1.getState().compareTo(r2.getState()) : cmp;
        });
        apptLocationReportTable.setItems(locationReports);

        // ------------------ Back Button and Other Initialization ------------------
        // (Other initialization code such as setting the current time zone, loading additional data, etc.)
    }

    @FXML
    private void reportContactDropdown(ActionEvent event) {
        Contacts selectedContact = reportContact.getValue();
        if (selectedContact != null) {
            ObservableList<Appointments> allAppointments = AppointmentData.getAllAppointments();
            ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();
            for (Appointments appt : allAppointments) {
                if (appt.getContactId() == selectedContact.getContactId()) {
                    filteredAppointments.add(appt);
                }
            }
            contactReportTable.setItems(filteredAppointments);
        }
    }

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