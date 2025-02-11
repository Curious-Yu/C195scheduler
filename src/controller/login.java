package controller;

import helper.JDBC;
import helper.UsersData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class login implements Initializable {

    @FXML
    public TextField UserNameBox;
    @FXML
    public PasswordField PasswordBox;
    @FXML
    public Label UserNameLabel;
    @FXML
    public Label PasswordLabel;
    @FXML
    public Label LoginMessageLabel;
    @FXML
    public Label LoginLabel;
    @FXML
    public Button LoginSubmitButton;
    @FXML
    public Button LoginCancelButton;
    @FXML
    public Label LocationLabel;
    @FXML
    public TextField LocationBox;

    // ResourceBundle for language support
    private ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Use the provided resource bundle, or load it manually if not provided.
        if (resourceBundle == null) {
            this.rb = ResourceBundle.getBundle("login");
        } else {
            this.rb = resourceBundle;
        }

        // Set texts from the resource bundle.
        LoginLabel.setText(rb.getString("LoginLabel"));
        LoginMessageLabel.setText(rb.getString("LoginMessageLabel"));
        UserNameLabel.setText(rb.getString("UserNameLabel"));
        PasswordLabel.setText(rb.getString("PasswordLabel"));
        LocationLabel.setText(rb.getString("LocationLabel"));
        UserNameBox.setPromptText(rb.getString("UserNameBox.prompt"));
        PasswordBox.setPromptText(rb.getString("PasswordBox.prompt"));
        LocationBox.setPromptText(rb.getString("LocationBox.prompt"));
        LoginSubmitButton.setText(rb.getString("LoginSubmitButton.text"));
        LoginCancelButton.setText(rb.getString("LoginCancelButton.text"));

        // Set the LocationBox to show the user's city, region, and current local time.
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(dtf);
        // Assume zone ID is in the format "Region/City" (e.g., "America/New_York")
        String zoneIdStr = zone.getId();
        String[] zoneParts = zoneIdStr.split("/");
        String region = zoneParts.length > 0 ? zoneParts[0] : "";
        String city = zoneParts.length > 1 ? zoneParts[1].replace('_', ' ') : "";
        LocationBox.setText(city + ", " + region + " - " + formattedTime);
    }

    /**
     * Event handler for the Submit button.
     * Validates the username and password fields and verifies the credentials.
     * If valid, loads the main page; otherwise, displays an error alert.
     */
    @FXML
    private void LoginSubmitButtonAction(ActionEvent event) {
        String username = UserNameBox.getText().trim();
        String password = PasswordBox.getText().trim();

        // Validate that both fields are filled.
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("Alert"));
            alert.setHeaderText(rb.getString("EmptyFieldsMessage"));
            alert.setContentText(rb.getString("EmptyFieldsMessage"));
            alert.showAndWait();
            return;
        }

        // Validate credentials against the users database.
        Users user = UsersData.validateUser(username, password);
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("Alert"));
            alert.setHeaderText(rb.getString("AlertMessage"));
            alert.setContentText(rb.getString("AlertMessage"));
            alert.showAndWait();
            return;
        }

        // If credentials are valid, update the login message.
        LoginMessageLabel.setText(String.format(rb.getString("SuccessMessage"), user.getUserName()));

        // Load mainpage.fxml using the same resource bundle.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"), rb);
            Parent root = loader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("Alert"));
            alert.setHeaderText(rb.getString("AlertMessage"));
            alert.setContentText("Unable to load the main page. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Event handler for the Cancel button.
     * Confirms exit, closes the database connection, and exits the application.
     */
    @FXML
    private void LoginCancelButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("Alert"));
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit the application?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            JDBC.closeConnection();  // Close the database connection
            System.exit(0);          // Exit the application
        }
    }
}