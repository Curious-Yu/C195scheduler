package controller;

import helper.AppointmentData;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class appointment {

    @FXML
    public Button addAppointmentButton;
    @FXML
    public Button modifyAppointmentButton;
    @FXML
    public Button deleteAppointmentButton;
    @FXML
    public RadioButton allTimeRadio;
    @FXML
    public ToggleGroup appointmentToggle;
    @FXML
    public RadioButton currentMonthRadio;
    @FXML
    public RadioButton currentWeekRadio;
    @FXML
    public TableView<Appointments> appointmentTable;
    @FXML
    public TableColumn<Appointments, Integer> appointmentIDColumn;
    @FXML
    public TableColumn<Appointments, String> titleColumn;
    @FXML
    public TableColumn<Appointments, String> descriptionColumn;
    @FXML
    public TableColumn<Appointments, String> locationColumn;
    @FXML
    public TableColumn<Appointments, String> typeColumn;
    @FXML
    public TableColumn<Appointments, String> startsAtColumn;
    @FXML
    public TableColumn<Appointments, String> endsAtColumn;
    @FXML
    public TableColumn<Appointments, Integer> customerIdColumn;
    @FXML
    public TableColumn<Appointments, Integer> userIdColumn;
    @FXML
    public TableColumn<Appointments, Integer> contactIdColumn;
    @FXML
    public Button customersButton;
    @FXML
    public Button reportsButton;
    @FXML
    public Button exitButton;

    @FXML
    public void initialize() {
        // Initialize TableView columns
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startsAtColumn.setCellValueFactory(new PropertyValueFactory<>("localStart"));
        endsAtColumn.setCellValueFactory(new PropertyValueFactory<>("localEnd"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        // Load all appointments on startup
        try {
            appointmentTable.setItems(AppointmentData.selectAllAppointments());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments.");
        }
    }

    public void addAppointmentActionButton(ActionEvent actionEvent) {
        // Code for adding a new appointment
    }

    public void modifyAppointmentActionButton(ActionEvent actionEvent) {
        // Code for modifying an existing appointment
    }

    public void deleteAppointmentActionButton(ActionEvent actionEvent) {
        // Code for deleting an appointment
    }

    public void OnAllTimeRadio(ActionEvent actionEvent) {
        try {
            ObservableList<Appointments> appointments = AppointmentData.selectAllAppointments();
            updateAppointmentTable(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments.");
        }
    }

    public void OnCurrentMonthRadio(ActionEvent actionEvent) {
        try {
            ObservableList<Appointments> appointments = AppointmentData.selectAppointmentsForCurrentMonth();
            updateAppointmentTable(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments for the current month.");
        }
    }

    public void OnCurrentWeekRadio(ActionEvent actionEvent) {
        try {
            ObservableList<Appointments> appointments = AppointmentData.selectAppointmentsForCurrentWeek();
            updateAppointmentTable(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments for the current week.");
        }
    }

    public void customersActionButton(ActionEvent actionEvent) {
        // Code for handling customers action
    }

    public void reportsActionButton(ActionEvent actionEvent) {
        // Code for handling reports action
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

    private void updateAppointmentTable(ObservableList<Appointments> appointments) {
        appointmentTable.setItems(appointments);
        // Set the cell value factories for the table columns
        appointmentIDColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        startsAtColumn.setCellValueFactory(cellData -> cellData.getValue().localStartProperty().asString());
        endsAtColumn.setCellValueFactory(cellData -> cellData.getValue().localEndProperty().asString());
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        contactIdColumn.setCellValueFactory(cellData -> cellData.getValue().contactIdProperty().asObject());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
