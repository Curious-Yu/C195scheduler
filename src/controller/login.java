package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class login implements Initializable {

    public TextField UserNameBox;
    public PasswordField PasswordBox;
    public Label UserNameLabel;
    public Label PasswordLabel;
    public Label LoginMessageLabel;
    public Label LoginLabel;
    public Button LoginSubmitButton;
    public Button LoginCancelButton;
    public Label LocationLabel;
    public TextField LocationBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized!");
    }

    @FXML
    public void LoginCancelButtonAction(ActionEvent actionEvent) {
        System.out.println("Cancel button clicked!");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void LoginSubmitButtonAction(ActionEvent actionEvent) {
        System.out.println("Submit button clicked!");
    }
}
