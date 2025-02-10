package controller;

import helper.CustomerData;
import helper.CountryData;
import helper.FirstLevelDivisionData;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.util.StringConverter;
import model.Countries;
import model.FirstLevelDivisions;

public class addCustomer implements Initializable {
    public Button addCustomerCancel;
    public Button addCustomerSave;
    public TextField customerID;
    public TextField customerFirstName;
    public TextField customerLastName;
    public TextField customerAddress;
    public TextField customerPostalCode;
    public TextField customerPhoneNumber;
    public ComboBox<Countries> customerCountry;
    public ComboBox<String> customerDivision;

    /**
     * Navigates to the main page.
     */
    private void navigateToMainPage(ActionEvent actionEvent) {
        try {
            // Load the main page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"));
            Parent root = loader.load();
            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            // Set the scene and show the main page
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to return to the main page.");
        }
    }

    // Change Alert Type for success
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Change to INFORMATION
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void customerCountryDropdown(ActionEvent actionEvent) {
        // Get the selected country from the combo box
        Countries selectedCountry = customerCountry.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            try {
                // Get the divisions for the selected country (you need to implement a method that gets divisions by country)
                List<String> divisionsList = FirstLevelDivisionData.getDivisionsByCountry(selectedCountry.getCountryId());
                // Clear the existing items in the division combo box
                customerDivision.getItems().clear();
                // Add the divisions to the division combo box
                customerDivision.getItems().addAll(divisionsList);
            } catch (Exception e) { // Handle a general exception, since SQLException might not be thrown here
                e.printStackTrace();
                showAlert("Error", "Unable to load divisions for the selected country.");
            }
        }
    }

    public void customerDivisionDropdown(ActionEvent actionEvent) {
        // Get the selected division from the combo box
        String selectedDivision = customerDivision.getSelectionModel().getSelectedItem();
        if (selectedDivision != null) {
            // You can now perform any logic based on the selected division
            System.out.println("Selected Division: " + selectedDivision);
            // For example, you could store it in a variable or use it in the customer form
            customerDivision.setPromptText("Selected Division: " + selectedDivision);
        }
    }

    public void addCustomerSaveButton(ActionEvent actionEvent) {
        // Get all customer data from the form fields
        String firstName = customerFirstName.getText();
        String lastName = customerLastName.getText();
        String address = customerAddress.getText();
        String postalCode = customerPostalCode.getText();
        String phoneNumber = customerPhoneNumber.getText();
        String country = customerCountry.getSelectionModel().getSelectedItem().getCountryName();
        String division = customerDivision.getSelectionModel().getSelectedItem();

        // Ensure all fields are filled out
        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phoneNumber.isEmpty() || country.isEmpty() || division == null) {
            showAlert("Error", "Please fill out all fields.");
            return;
        }

        // Combine first name and last name for Customer_Name
        String customerName = firstName + " " + lastName;
        // Retrieve the Division_ID based on the selected division
        int divisionId = 0;
        try {
            divisionId = FirstLevelDivisionData.getDivisionIdByName(division);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Division not found.");
            return;
        }

        // Save customer data (you can implement saving logic here, such as calling CustomerData.saveCustomer())
        try {
            // Use CustomerData to save the customer data to the database
            CustomerData.saveCustomer(customerName, address, postalCode, phoneNumber, divisionId);
            showAlert("Success", "Customer saved successfully!");
            navigateToMainPage(actionEvent);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to save customer data.");
        }
    }

    public void addCustomerCancelButton(ActionEvent actionEvent) {
        // Confirm the cancellation and navigate back to the main page
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                navigateToMainPage(actionEvent);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize country combo box (Populate it with all countries)
        try {
            List<Countries> countriesList = CountryData.getAllCountries();
            customerCountry.getItems().addAll(countriesList);
            customerCountry.setConverter(new StringConverter<Countries>() {
                @Override
                public String toString(Countries country) {
                    return country != null ? country.getCountryName() : "";
                }

                @Override
                public Countries fromString(String string) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load countries.");
        }
    }
}