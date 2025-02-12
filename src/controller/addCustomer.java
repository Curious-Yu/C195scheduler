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

public class addCustomer implements Initializable {

    //------ FXML Fields ------
    @FXML private TextField customerID;           // Auto-generated
    @FXML private TextField customerFirstName;
    @FXML private TextField customerLastName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPostalCode;
    @FXML private TextField customerPhoneNumber;
    @FXML private ComboBox<Countries> customerCountry;
    @FXML private ComboBox<FirstLevelDivisions> customerDivision;
    @FXML private Button addCustomerSave;
    @FXML private Button addCustomerCancel;

    //------ ObservableList for Countries ------
    private ObservableList<Countries> countriesList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //------ Set Customer ID as Auto-Gen ------
        customerID.setText("Auto-Gen");

        //------ Load Countries ------
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

        //------ Clear Divisions ComboBox ------
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

    //------ Save Customer ------
    @FXML
    private void addCustomerSaveButton(ActionEvent event) {
        // Validate fields
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

        // Combine first and last names
        String customerName = customerFirstName.getText().trim() + " " + customerLastName.getText().trim();
        String address = customerAddress.getText().trim();
        String postalCode = customerPostalCode.getText().trim();
        String phone = customerPhoneNumber.getText().trim();
        int divisionId = customerDivision.getValue().getDivisionId();

        // Create new customer and save
        Customers newCustomer = new Customers(0, customerName, address, postalCode, phone, divisionId);
        CustomerData.addCustomer(newCustomer);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Customer Added");
        successAlert.setHeaderText(null);
        successAlert.setContentText("The customer was successfully added.");
        successAlert.showAndWait();

        // Return to main page
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

    //------ Cancel and return to main page ------
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

    //------ Load Divisions when a Country is selected ------
    @FXML
    private void customerCountryDropdown(ActionEvent event) {
        Countries selectedCountry = customerCountry.getValue();
        if (selectedCountry != null) {
            try {
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
        // Additional logic can be added here if needed.
    }
}