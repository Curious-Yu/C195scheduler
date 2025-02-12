package controller;

import helper.JDBC;
import helper.UsersData;
import helper.LoginActivityLogger;
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

/**
 * Controller for handling user login functionality.
 * <p>
 * This class initializes the login view, validates user credentials, logs login attempts,
 * and navigates to the main page upon successful login.
 * </p>
 */
public class login implements Initializable {

    //------ FXML Fields ------
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

    //------ Resource Bundle for Language Support ------
    private ResourceBundle rb;

    /**
     * Initializes the login view.
     * <p>
     * Loads the resource bundle for localization, sets UI texts and prompts, and displays the local time zone.
     * </p>
     *
     * @param url the URL used to resolve relative paths for the root object, or null if not known
     * @param resourceBundle the ResourceBundle for localization, or null if not provided
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load Resource Bundle
        rb = (resourceBundle == null) ? ResourceBundle.getBundle("login") : resourceBundle;
        // Set UI texts from the resource bundle.
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

        // Set LocationBox with local time and zone.
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(dtf);
        String[] zoneParts = zone.getId().split("/");
        String region = (zoneParts.length > 0) ? zoneParts[0] : "";
        String city = (zoneParts.length > 1) ? zoneParts[1].replace('_', ' ') : "";
        LocationBox.setText(city + ", " + region + " - " + formattedTime);
    }

    /**
     * Handles the login submit button action.
     * <p>
     * Validates the input fields, checks user credentials, logs the login attempt,
     * and navigates to the main page upon successful login.
     * </p>
     *
     * @param event the ActionEvent triggered by clicking the submit button
     */
    @FXML
    private void LoginSubmitButtonAction(ActionEvent event) {
        String username = UserNameBox.getText().trim();
        String password = PasswordBox.getText().trim();
        // Validate fields.
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("Alert"));
            alert.setHeaderText(rb.getString("EmptyFieldsMessage"));
            alert.setContentText(rb.getString("EmptyFieldsMessage"));
            alert.showAndWait();
            LoginActivityLogger.logAttempt(username, false); // Log failure
            return;
        }
        // Validate credentials.
        Users user = UsersData.validateUser(username, password);
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("Alert"));
            alert.setHeaderText(rb.getString("AlertMessage"));
            alert.setContentText(rb.getString("AlertMessage"));
            alert.showAndWait();
            LoginActivityLogger.logAttempt(username, false); // Log failure
            return;
        }
        // Successful login.
        LoginActivityLogger.logAttempt(username, true); // Log success
        LoginMessageLabel.setText(String.format(rb.getString("SuccessMessage"), user.getUserName()));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainpage.fxml"), rb);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
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
     * Handles the login cancel button action.
     * <p>
     * Prompts the user for confirmation and, if confirmed, closes the database connection and exits the application.
     * </p>
     *
     * @param event the ActionEvent triggered by clicking the cancel button
     */
    @FXML
    private void LoginCancelButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("Alert"));
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit the application?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            JDBC.closeConnection();
            System.exit(0);
        }
    }
}