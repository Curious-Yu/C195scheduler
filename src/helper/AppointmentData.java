package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public abstract class AppointmentData {
    // method for fetching all appointments
    public static ObservableList<Appointments> selectAllAppointments() throws SQLException {
        ObservableList<Appointments> appointmentObservableList = FXCollections.observableArrayList();

        if (JDBC.connection == null) {
            JDBC.openConnection();
        }

        String sql = "SELECT * FROM client_schedule.appointments";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int appId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contId = result.getInt("Contact_ID");

            appointmentObservableList.add(new Appointments(appId, title, description, location, type, begin, end, custId, userId, contId));
        }

        return appointmentObservableList;
    }

    // method to fetch appointments for the current month
    public static ObservableList<Appointments> selectAppointmentsForCurrentMonth() throws SQLException {
        ObservableList<Appointments> appointmentObservableList = FXCollections.observableArrayList();

        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        String sql = "SELECT * FROM client_schedule.appointments " +
                "WHERE YEAR(Start) = ? AND MONTH(Start) = ?";

        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, currentYear);
        statement.setInt(2, currentMonth);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int appId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contId = result.getInt("Contact_ID");

            appointmentObservableList.add(new Appointments(appId, title, description, location, type, begin, end, custId, userId, contId));
        }

        return appointmentObservableList;
    }

    // method to fetch appointments for the current week
    public static ObservableList<Appointments> selectAppointmentsForCurrentWeek() throws SQLException {
        ObservableList<Appointments> appointmentObservableList = FXCollections.observableArrayList();

        // Get the current date and calculate the start and end of the current week (Monday-Sunday)
        LocalDateTime now = LocalDateTime.now();

        // Find the start of the current week (Monday)
        LocalDateTime startOfWeek = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();

        // Find the end of the current week (Sunday, 23:59:59)
        LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        // SQL query to fetch appointments within the current week
        String sql = "SELECT * FROM client_schedule.appointments " +
                "WHERE Start BETWEEN ? AND ?";

        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setTimestamp(1, java.sql.Timestamp.valueOf(startOfWeek));
        statement.setTimestamp(2, java.sql.Timestamp.valueOf(endOfWeek));
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int appId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contId = result.getInt("Contact_ID");

            appointmentObservableList.add(new Appointments(appId, title, description, location, type, begin, end, custId, userId, contId));
        }

        return appointmentObservableList;
    }

    // Method to delete an appointment by Appointment_ID
    public static void deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?";

        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, appointmentId);
        statement.executeUpdate();
    }
}
