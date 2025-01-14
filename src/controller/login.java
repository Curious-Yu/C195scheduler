package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class login implements Initializable {
    @FXML public TextField UserNameBox;
    @FXML public PasswordField PasswordBox;
    @FXML public Label UserNameLabel;
    @FXML public Label PasswordLabel;
    @FXML public Label LoginMessageLabel;
    @FXML public Label LoginLabel;
    @FXML public Button LoginSubmitButton;
    @FXML public Button LoginCancelButton;
    @FXML public Label LocationLabel;
    @FXML public TextField LocationBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get the user's default locale
        Locale locale = Locale.getDefault();

        // Load the appropriate ResourceBundle based on the locale (english or french)
        ResourceBundle bundle = ResourceBundle.getBundle("login", locale);

        // Set text for labels and buttons from the resource bundle
        LoginLabel.setText(bundle.getString("LoginLabel"));
        LoginMessageLabel.setText(bundle.getString("LoginMessageLabel"));
        UserNameLabel.setText(bundle.getString("UserNameLabel"));
        PasswordLabel.setText(bundle.getString("PasswordLabel"));
        LocationLabel.setText(bundle.getString("LocationLabel"));

        // Set prompt texts for text fields
        UserNameBox.setPromptText(bundle.getString("UserNameBox.prompt"));
        PasswordBox.setPromptText(bundle.getString("PasswordBox.prompt"));

        // Dynamically set location prompt text with city, country, local time, and UTCX
        String city = getCityFromTimeZone();
        String country = locale.getDisplayCountry(locale);
        String localTime = getLocalTime();
        String utcOffset = getUTCOffset();

        // Combine city, country, local time, and UTCX
        String location = String.format("%s, %s, %s, %s", city, country, localTime, utcOffset);
        LocationBox.setPromptText(location);

        // Set button texts
        LoginSubmitButton.setText(bundle.getString("LoginSubmitButton.text"));
        LoginCancelButton.setText(bundle.getString("LoginCancelButton.text"));
    }

    private String getCityFromTimeZone() {
        // Get the time zone ID (e.g., America/New_York)
        String timeZoneId = TimeZone.getDefault().getID();

        // Extract the city name from the time zone ID (e.g., America/New_York -> New York)
        String city = timeZoneId.substring(timeZoneId.lastIndexOf("/") + 1).replace("_", " ");
        return city;
    }

    private String getLocalTime() {
        // Get the current ZonedDateTime in the system's default time zone
        ZonedDateTime now = ZonedDateTime.now();

        // Format the local time
        return now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private String getUTCOffset() {
        // Get the current ZonedDateTime in the system's default time zone
        ZonedDateTime now = ZonedDateTime.now();

        // Extract the UTC offset from the current time zone
        int offsetInSeconds = now.getOffset().getTotalSeconds();

        // Calculate the UTC hour offset (in hours)
        int offsetHours = offsetInSeconds / 3600;

        // Format as "UTC+X" or "UTC-X" (simplified to hour offset)
        return String.format("UTC%+d", offsetHours);
    }

    @FXML
    public void LoginCancelButtonAction(ActionEvent actionEvent) {
        System.out.println("Cancel button clicked!");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void LoginSubmitButtonAction(ActionEvent actionEvent) {
        // Check if username or password is empty
        String userName = UserNameBox.getText();
        String password = PasswordBox.getText();

        // If either the username or password is empty, show an alert
        if (userName.isEmpty() || password.isEmpty()) {
            showAlert();
        } else {
            // Proceed with login logic
            System.out.println("Submit button clicked!");
            // Add your login logic here
        }
    }

    private void showAlert() {
        // Get the user's default locale
        Locale locale = Locale.getDefault();

        // Load the appropriate ResourceBundle based on the locale (english or french)
        ResourceBundle bundle = ResourceBundle.getBundle("login", locale);

        // Get alert message from properties
        String alertTitle = bundle.getString("Alert");
        String alertMessage = bundle.getString("AlertMessage");

        // Create an alert with the message from the properties file
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(alertTitle);  // Title from properties
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);  // Message from properties
        alert.showAndWait();
    }
}