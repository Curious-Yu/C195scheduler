package controller;

import helper.CountryData;
import helper.CustomerData;
import helper.FirstLevelDivisionData;
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
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Countries;
import model.Customers;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class addCustomer implements Initializable {

    // FXML fields mapped from the addCustomer.fxml file.
    @FXML private TextField customerID;           // Disabled; auto-generated.
    @FXML private TextField customerFirstName;
    @FXML private TextField customerLastName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPostalCode;
    @FXML private TextField customerPhoneNumber;
    @FXML private ComboBox<Countries> customerCountry;
    @FXML private ComboBox<FirstLevelDivisions> customerDivision;
    @FXML private Button addCustomerSave;
    @FXML private Button addCustomerCancel;

    // ObservableLists to store loaded countries and divisions.
    private ObservableList<Countries> countriesList;
    // No need to keep a global divisions list; we'll load divisions per country.

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the customerID field as auto-generated.
        customerID.setText("Auto-Gen");

        // Load countries into the customerCountry ComboBox.
        countriesList = CountryData.getAllCountries();
        customerCountry.setItems(countriesList);
        customerCountry.setConverter(new StringConverter<Countries>() {
            @Override
            public String toString(Countries country) {
                return (country == null) ? "" : country.getCountry();
            }
            @Override
            public Countries fromString(String string) {
                return null;
            }
        });
        customerCountry.setCellFactory(cb -> new ListCell<Countries>() {
            @Override
            protected void updateItem(Countries country, boolean empty) {
                super.updateItem(country, empty);
                setText(empty || country == null ? "" : country.getCountry());
            }
        });

        // Initially, clear the divisions ComboBox. It will be populated when a country is selected.
        customerDivision.setItems(FXCollections.observableArrayList());
        customerDivision.setConverter(new StringConverter<FirstLevelDivisions>() {
            @Override
            public String toString(FirstLevelDivisions division) {
                return (division == null) ? "" : division.getDivision();
            }
            @Override
            public FirstLevelDivisions fromString(String string) {
                return null;
            }
        });
        customerDivision.setCellFactory(cb -> new ListCell<FirstLevelDivisions>() {
            @Override
            protected void updateItem(FirstLevelDivisions division, boolean empty) {
                super.updateItem(division, empty);
                setText(empty || division == null ? "" : division.getDivision());
            }
        });
    }

    /**
     * Event handler for the Save button.
     * Validates input, creates a new Customers object, saves it via CustomerData,
     * and then returns to the main page in the same window.
     */
    @FXML
    private void addCustomerSaveButton(ActionEvent event) {
        // Validate that all required fields are filled.
        if (customerFirstName.getText().isEmpty() ||
                customerLastName.getText().isEmpty() ||
                customerAddress.getText().isEmpty() ||
                customerPostalCode.getText().isEmpty() ||
                customerPhoneNumber.getText().isEmpty() ||
                customerCountry.getValue() == null ||
                customerDivision.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Data");
            alert.setHeaderText("All fields must be completed");
            alert.setContentText("Please fill in all fields before saving the customer.");
            alert.showAndWait();
            return;
        }

        // Create the customer name by combining first and last names.
        String customerName = customerFirstName.getText().trim() + " " + customerLastName.getText().trim();
        String address = customerAddress.getText().trim();
        String postalCode = customerPostalCode.getText().trim();
        String phone = customerPhoneNumber.getText().trim();
        int divisionId = customerDivision.getValue().getDivisionId();

        // Create a new Customers object.
        // public Customers(int customerId, String customerName, String address, String postalCode, String phone, int divisionId)
        Customers newCustomer = new Customers(0, customerName, address, postalCode, phone, divisionId);

        // Save the customer to the database.
        CustomerData.addCustomer(newCustomer);

        // Show success message.
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Customer Added");
        successAlert.setHeaderText(null);
        successAlert.setContentText("The customer was successfully added.");
        successAlert.showAndWait();

        // Return to the main page in the same window.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"));
            Parent mainPageRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(mainPageRoot);
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error Returning to Main Page");
            errorAlert.setHeaderText("An error occurred");
            errorAlert.setContentText("Unable to load the main page. Please try again.");
            errorAlert.showAndWait();
        }
    }

    /**
     * Event handler for the Cancel button.
     * Returns to the main page in the same window without saving.
     */
    @FXML
    private void addCustomerCancelButton(ActionEvent event) {
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

    /**
     * Event handler for when a country is selected from the customerCountry ComboBox.
     * This method populates the customerDivision ComboBox with divisions that belong to the selected country.
     */
    @FXML
    private void customerCountryDropdown(ActionEvent event) {
        Countries selectedCountry = customerCountry.getValue();
        if (selectedCountry != null) {
            try {
                // Retrieve divisions for the selected country.
                ObservableList<FirstLevelDivisions> divisionsForCountry = FXCollections.observableArrayList();
                divisionsForCountry.addAll(FirstLevelDivisionData.getDivisionsByCountryId(selectedCountry.getCountryId()));
                customerDivision.setItems(divisionsForCountry);
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Loading Divisions");
                alert.setHeaderText("An error occurred");
                alert.setContentText("Unable to load divisions for the selected country.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void customerDivisionDropdown(ActionEvent event) {
        // Already embedded in the Country dropdown menu. Additional logic can be added here if needed when a division is selected.
    }
}