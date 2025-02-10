package controller;

import helper.AppointmentData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
            ObservableList<Appointments> appointments = AppointmentData.selectAllAppointments();
            appointmentTable.setItems(appointments);
            checkUpcomingAppointments(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load appointments.");
        }

        startsAtColumn.setCellValueFactory(cellData -> {
            LocalDateTime start = cellData.getValue().getLocalStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(start != null ? start.format(formatter) : "");
        });

        endsAtColumn.setCellValueFactory(cellData -> {
            LocalDateTime end = cellData.getValue().getLocalEnd();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(end != null ? end.format(formatter) : "");
        });

    }

    private void checkUpcomingAppointments(ObservableList<Appointments> appointments) {
        LocalDateTime now = LocalDateTime.now();
        for (Appointments appointment : appointments) {
            LocalDateTime appointmentStartTime = appointment.getLocalStart();
            long minutesUntilAppointment = ChronoUnit.MINUTES.between(now, appointmentStartTime);

            if (minutesUntilAppointment > 0 && minutesUntilAppointment <= 15) {
                // Alert for an upcoming appointment within 15 minutes
                String message = String.format("You have an upcoming appointment!\n" +
                                "Appointment ID: %d\nDate: %s\nTime: %s",
                        appointment.getAppointmentId(),
                        appointmentStartTime.toLocalDate().toString(),
                        appointmentStartTime.toLocalTime().toString());
                showAlert("Upcoming Appointment", message);
                return; // Show alert only for the first upcoming appointment
            }
        }

        // If no appointments within 15 minutes, show a custom message
        showAlert("No Upcoming Appointments", "There are no appointments within 15 minutes of your login.");
    }

    // Action Methods
    public void addAppointmentActionButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addAppointment.fxml"));
            if (loader.getLocation() == null) {
                showAlert("Error", "addAppointment.fxml not found. Please check the file path.");
                return;
            }
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("Successfully opened addAppointment.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open the Add Appointment window.");
        }
    }

    public void modifyAppointmentActionButton(ActionEvent actionEvent) {
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert("No Selection", "Please select an appointment to modify.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyAppointment.fxml"));
            if (loader.getLocation() == null) {
                showAlert("Error", "modifyAppointment.fxml not found. Please check the file path.");
                return;
            }
            Parent root = loader.load();
            modifyAppointment controller = loader.getController();
            controller.setAppointmentData(selectedAppointment);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("Successfully opened modifyAppointment.fxml with selected appointment data.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open the Modify Appointment window.");
        }
    }

    public void deleteAppointmentActionButton(ActionEvent actionEvent) {
        try {
            Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                showAlert("No Selection", "Please select an appointment to delete.");
                return;
            }
            int appointmentId = selectedAppointment.getAppointmentId();
            String appointmentType = selectedAppointment.getType();
            String appointmentDate = selectedAppointment.getLocalStart().toLocalDate().toString();
            String appointmentTime = selectedAppointment.getLocalStart().toLocalTime().toString();

            String confirmationMessage = String.format(
                    "Are you sure you want to delete Appointment ID: %d?\nDate: %s\nTime: %s\nType: %s",
                    appointmentId, appointmentDate, appointmentTime, appointmentType
            );
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText(confirmationMessage);
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentData.deleteAppointment(appointmentId);
                appointmentTable.setItems(AppointmentData.selectAllAppointments());
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Deletion Successful");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText(
                        String.format("Appointment ID: %d on %s at %s (Type: %s) has been deleted.",
                                appointmentId, appointmentDate, appointmentTime, appointmentType)
                );
                infoAlert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the appointment.");
        }
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
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Exit Confirmation");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    private void updateAppointmentTable(ObservableList<Appointments> appointments) {
        appointmentTable.setItems(appointments);
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}