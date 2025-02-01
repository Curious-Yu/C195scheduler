package controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class appointment {
    public Button addAppointmentButton;
    public Button modifyAppointmentButton;
    public Button deleteAppointmentButton;
    public RadioButton allTimeRadio;
    public ToggleGroup appointmentToggle;
    public RadioButton currentMonthRadio;
    public RadioButton currentWeekRadio;
    public TableView appointmentTable;
    public TableColumn appointmentIDColumn;
    public TableColumn titleColumn;
    public TableColumn descriptionColumn;
    public TableColumn locationColumn;
    public TableColumn typeColumn;
    public TableColumn startsAtColumn;
    public TableColumn endsAtColumn;
    public TableColumn customerIdColumn;
    public TableColumn userIdColumn;
    public TableColumn contactIdColumn;
    public Button customersButton;
    public Button reportsButton;
    public Button exitButton;

    public void addAppointmentActionButton(ActionEvent actionEvent) {
    }

    public void modifyAppointmentActionButton(ActionEvent actionEvent) {
    }

    public void deleteAppointmentActionButton(ActionEvent actionEvent) {
    }

    public void OnAllTimeRadio(ActionEvent actionEvent) {
    }

    public void OnCurrentMonthRadio(ActionEvent actionEvent) {
    }

    public void OnCurrentWeekRadio(ActionEvent actionEvent) {
    }

    public void customersActionButton(ActionEvent actionEvent) {
    }

    public void reportsActionButton(ActionEvent actionEvent) {
    }

    public void exitActionButton(ActionEvent actionEvent) {
        // Show confirmation dialog before exiting
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Exit Confirmation");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Get the current stage and close it
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
