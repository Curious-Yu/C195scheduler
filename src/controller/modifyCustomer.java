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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Countries;
import model.Customers;
import model.FirstLevelDivisions;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for modifying a customer's information.
 * Provides functionality to populate the modify form with selected customer data,
 * save updates, and cancel the modification.
 */
public class modifyCustomer implements Initializable {

    //------ FXML Fields ------
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

    /**
     * Initializes the modify customer form by setting up the country and division ComboBoxes.
     * <p>
     * Lambda expressions used:
     * <ul>
     *   <li>In customerCountry.setConverter: converts a Countries object to its String representation.</li>
     *   <li>In customerCountry.setCellFactory: creates a custom ListCell for Countries.</li>
     *   <li>In customerDivision.setConverter: converts a FirstLevelDivisions object to its String representation.</li>
     *   <li>In customerDivision.setCellFactory: creates a custom ListCell for FirstLevelDivisions.</li>
     * </ul>
     * </p>
     *
     * @param url the URL used to resolve relative paths for the root object, or null if not known
     * @param resourceBundle the ResourceBundle for localization, or null if not provided
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //------ Initialize Country ComboBox ------
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
                // Lambda: Display country name
                setText(empty || country == null ? "" : country.getCountry());
            }
        });

        //------ Initialize Division ComboBox ------
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
                // Lambda: Display division name
                setText(empty || division == null ? "" : division.getDivision());
            }
        });
    }

    /**
     * Populates the modify form with the selected customer's data.
     *
     * @param selectedCustomer the customer selected from the main page
     */
    public void setCustomerData(Customers selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        customerID.setText(String.valueOf(selectedCustomer.getCustomerId()));
        // Split full name into first and last names.
        String fullName = selectedCustomer.getCustomerName();
        String[] nameParts = fullName.split(" ", 2);
        customerFirstName.setText(nameParts.length > 0 ? nameParts[0] : "");
        customerLastName.setText(nameParts.length > 1 ? nameParts[1] : "");
        customerAddress.setText(selectedCustomer.getAddress());
        customerPostalCode.setText(selectedCustomer.getPostalCode());
        customerPhoneNumber.setText(selectedCustomer.getPhone());
        try {
            FirstLevelDivisions division = FirstLevelDivisionData.getFirstLevelDivisionById(selectedCustomer.getDivisionId());
            if (division != null) {
                ObservableList<Countries> countriesList = FXCollections.observableArrayList(CountryData.getAllCountries());
                customerCountry.setItems(countriesList);
                Countries country = CountryData.getCountryById(division.getCountryId());
                customerCountry.setValue(country);
                ObservableList<FirstLevelDivisions> divisionsList = FXCollections.observableArrayList(
                        FirstLevelDivisionData.getDivisionsByCountryId(country.getCountryId()));
                customerDivision.setItems(divisionsList);
                customerDivision.setValue(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the updated customer data to the database.
     * Validates that all required fields are filled before updating.
     *
     * @param actionEvent the ActionEvent triggered by clicking the Save button
     */
    public void modifyCustomerSaveButton(ActionEvent actionEvent) {
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
        String fullName = customerFirstName.getText().trim() + " " + customerLastName.getText().trim();
        String address = customerAddress.getText().trim();
        String postalCode = customerPostalCode.getText().trim();
        String phone = customerPhoneNumber.getText().trim();
        int divisionId = customerDivision.getValue().getDivisionId();
        Customers updatedCustomer = new Customers(selectedCustomer.getCustomerId(), fullName, address, postalCode, phone, divisionId);
        CustomerData.updateCustomer(updatedCustomer);
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Customer Updated");
        successAlert.setHeaderText(null);
        successAlert.setContentText("The customer was successfully updated.");
        successAlert.showAndWait();
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
     * Cancels the modification and returns to the main page.
     *
     * @param actionEvent the ActionEvent triggered by clicking the Cancel button
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

    /**
     * Handles the country dropdown event.
     * <p>
     * When a new country is selected, clears the division selection and re-populates the division ComboBox
     * with divisions that belong to the selected country. If no country is selected, the division ComboBox is cleared.
     * </p>
     *
     * @param actionEvent the ActionEvent triggered by selecting a country
     */
    public void customerCountryDropdown(ActionEvent actionEvent) {
        Countries selectedCountry = customerCountry.getValue();
        if (selectedCountry != null) {
            try {
                ObservableList<FirstLevelDivisions> divisionsForCountry = FXCollections.observableArrayList();
                divisionsForCountry.addAll(FirstLevelDivisionData.getDivisionsByCountryId(selectedCountry.getCountryId()));
                customerDivision.setItems(divisionsForCountry);
                // Clear any previously selected division.
                customerDivision.setValue(null);
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Loading Divisions");
                alert.setHeaderText("An error occurred");
                alert.setContentText("Unable to load divisions for the selected country.");
                alert.showAndWait();
            }
        } else {
            // Clear divisions if no country is selected.
            customerDivision.setItems(FXCollections.observableArrayList());
            customerDivision.setValue(null);
        }
    }

    /**
     * Handles the division dropdown event.
     * <p>
     * Additional logic can be added here if needed when a division is selected.
     * </p>
     *
     * @param actionEvent the ActionEvent triggered by selecting a division
     */
    public void customerDivisionDropdown(ActionEvent actionEvent) {
        // No additional logic implemented.
    }
}