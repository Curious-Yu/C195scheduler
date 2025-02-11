package controller;

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
import helper.CountryData;
import helper.CustomerData;
import helper.FirstLevelDivisionData;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class modifyCustomer implements Initializable {

    @FXML public TextField customerID;
    @FXML public TextField customerFirstName;
    @FXML public TextField customerLastName;
    @FXML public TextField customerAddress;
    @FXML public TextField customerPostalCode;
    @FXML public TextField customerPhoneNumber;
    @FXML public ComboBox<Countries> customerCountry;
    @FXML public ComboBox<FirstLevelDivisions> customerDivision;
    @FXML public Button modifyCustomerSave;
    @FXML public Button modifyCustomerCancel;

    private Customers selectedCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the Country ComboBox.
        ObservableList<Countries> countriesList = FXCollections.observableArrayList();
        try {
            countriesList.addAll(CountryData.getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        // Initialize the Division ComboBox; it will be populated when a country is selected.
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
     * Called by the main page controller to pass the selected customer's data.
     */
    public void setCustomerData(Customers selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        // Populate fields.
        customerID.setText(String.valueOf(selectedCustomer.getCustomerId()));

        // Assume the full name is stored in a single field; split into first and last names.
        String fullName = selectedCustomer.getCustomerName();
        String[] nameParts = fullName.split(" ", 2);
        customerFirstName.setText(nameParts.length > 0 ? nameParts[0] : "");
        customerLastName.setText(nameParts.length > 1 ? nameParts[1] : "");

        customerAddress.setText(selectedCustomer.getAddress());
        customerPostalCode.setText(selectedCustomer.getPostalCode());
        customerPhoneNumber.setText(selectedCustomer.getPhone());

        try {
            // Get the division associated with this customer.
            FirstLevelDivisions division = FirstLevelDivisionData.getFirstLevelDivisionById(selectedCustomer.getDivisionId());
            if (division != null) {
                // Load countries.
                ObservableList<Countries> countriesList = FXCollections.observableArrayList(CountryData.getAllCountries());
                customerCountry.setItems(countriesList);
                // Get the country for the division.
                Countries country = CountryData.getCountryById(division.getCountryId());
                customerCountry.setValue(country);
                // Load divisions for the selected country.
                ObservableList<FirstLevelDivisions> divisionsList = FXCollections.observableArrayList(FirstLevelDivisionData.getDivisionsByCountryId(country.getCountryId()));
                customerDivision.setItems(divisionsList);
                // Set the selected division.
                customerDivision.setValue(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event handler for the Save button.
     * Validates input, updates the customer information, and returns to the main page.
     */
    public void modifyCustomerSaveButton(ActionEvent actionEvent) {
        // Validate required fields.
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

        // Combine first and last names.
        String fullName = customerFirstName.getText().trim() + " " + customerLastName.getText().trim();
        String address = customerAddress.getText().trim();
        String postalCode = customerPostalCode.getText().trim();
        String phone = customerPhoneNumber.getText().trim();
        int divisionId = customerDivision.getValue().getDivisionId();

        // Create an updated Customers object.
        Customers updatedCustomer = new Customers(selectedCustomer.getCustomerId(), fullName, address, postalCode, phone, divisionId);

        // Update the customer in the database.
        CustomerData.updateCustomer(updatedCustomer);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Customer Updated");
        successAlert.setHeaderText(null);
        successAlert.setContentText("The customer was successfully updated.");
        successAlert.showAndWait();

        // Return to main page.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"));
            Parent mainPageRoot = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
     * Event handler for the Cancel button.
     * Returns to the main page without saving changes.
     */
    public void modifyCustomerCancelButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"));
            Parent mainPageRoot = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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

    public void customerCountryDropdown(ActionEvent actionEvent) {
    }

    public void customerDivisionDropdown(ActionEvent actionEvent) {
    }
}