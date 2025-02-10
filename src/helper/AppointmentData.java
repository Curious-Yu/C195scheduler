package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

            // Convert UTC to local time
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime();

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

    // Method to delete an mainpage by Appointment_ID
    public static void deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?";

        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, appointmentId);
        statement.executeUpdate();
    }

    // Method to insert a new mainpage into the database
    public static void insertAppointment(String title, String description, String location, String type,
                                         LocalDateTime startDateTime, LocalDateTime endDateTime,
                                         Integer customerId, Integer userId, Integer contactId) throws SQLException {
        if (JDBC.connection == null) {
            JDBC.openConnection();
        }

        // Convert local time to UTC before storing
        startDateTime = startDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
        endDateTime = endDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = JDBC.connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setString(4, type);
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(startDateTime));
            statement.setTimestamp(6, java.sql.Timestamp.valueOf(endDateTime));
            statement.setInt(7, customerId);
            statement.setInt(8, userId);
            statement.setInt(9, contactId);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Successfully added appointment.");
            } else {
                System.out.println("Failed to add appointment.");
            }
        }
    }

    // Method to update an existing mainpage
    public static void updateAppointment(int appointmentId, String title, String description, String location, String type,
                                         LocalDateTime startDateTime, LocalDateTime endDateTime,
                                         Integer customerId, Integer userId, Integer contactId) throws SQLException {
        if (JDBC.connection == null) {
            JDBC.openConnection();
        }

        String sql = "UPDATE client_schedule.appointments " +
                "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setString(4, type);
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(startDateTime));
            statement.setTimestamp(6, java.sql.Timestamp.valueOf(endDateTime));
            statement.setInt(7, customerId);
            statement.setInt(8, userId);
            statement.setInt(9, contactId);
            statement.setInt(10, appointmentId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Successfully updated mainpage ID: " + appointmentId);
            } else {
                System.out.println("Failed to update mainpage ID: " + appointmentId);
            }
        }
    }

    public static boolean checkForOverlappingAppointments(LocalDateTime startDateTime, LocalDateTime endDateTime, int customerId) {
        boolean hasOverlap = false;

        if (JDBC.connection == null) {
            JDBC.openConnection();
        }

        // SQL query to check for overlapping appointments
        String sql = "SELECT * FROM client_schedule.appointments " +
                "WHERE Customer_ID = ? AND (" +
                "(Start BETWEEN ? AND ?) OR " +
                "(End BETWEEN ? AND ?) OR " +
                "(? BETWEEN Start AND End) OR " +
                "(? BETWEEN Start AND End))";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(sql)) {
            // Set parameters: customer ID, start and end times
            statement.setInt(1, customerId);
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(startDateTime));
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(endDateTime));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(startDateTime));
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(endDateTime));
            statement.setTimestamp(6, java.sql.Timestamp.valueOf(startDateTime));
            statement.setTimestamp(7, java.sql.Timestamp.valueOf(endDateTime));

            ResultSet result = statement.executeQuery();

            // If the result set has any rows, it means there is an overlapping mainpage
            if (result.next()) {
                hasOverlap = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasOverlap;
    }
}