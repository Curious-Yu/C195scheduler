package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public abstract class AppointmentData {

    /**
     * Retrieves all appointments from the database.
     *
     * @return an ObservableList of Appointments objects.
     */
    public static ObservableList<Appointments> getAllAppointments() {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            // Do not pass a Calendar here:
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");

                // Retrieve timestamps without a Calendar parameter.
                Timestamp startTimestamp = rs.getTimestamp("Start");
                Timestamp endTimestamp = rs.getTimestamp("End");

                // Convert SQL Timestamps to LocalDateTime.
                LocalDateTime startDateTime = startTimestamp.toLocalDateTime();
                LocalDateTime endDateTime = endTimestamp.toLocalDateTime();

                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                Appointments appointment = new Appointments(
                        appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId
                );
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;
    }

    /**
     * Inserts a new appointment into the database.
     *
     * @param appointment the Appointments object containing the data to be stored.
     */
    public static void addAppointment(Appointments appointment) {
        try {
            String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, appointment.getAppointmentId());
            ps.setString(2, appointment.getTitle());
            ps.setString(3, appointment.getDescription());
            ps.setString(4, appointment.getLocation());
            ps.setString(5, appointment.getType());
            // Since the start and end times are stored in UTC, we assume the values provided in the Appointments
            // object are in UTC. They are inserted as SQL Timestamp values.
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getStartDateTime()));
            ps.setTimestamp(7, Timestamp.valueOf(appointment.getEndDateTime()));
            ps.setInt(8, appointment.getCustomerId());
            ps.setInt(9, appointment.getUserId());
            ps.setInt(10, appointment.getContactId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing appointment in the database.
     *
     * @param updatedAppointment the appointment with updated values.
     */
    public static void updateAppointment(Appointments updatedAppointment) {
        try {
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                    "WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, updatedAppointment.getTitle());
            ps.setString(2, updatedAppointment.getDescription());
            ps.setString(3, updatedAppointment.getLocation());
            ps.setString(4, updatedAppointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(updatedAppointment.getStartDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(updatedAppointment.getEndDateTime()));
            ps.setInt(7, updatedAppointment.getCustomerId());
            ps.setInt(8, updatedAppointment.getUserId());
            ps.setInt(9, updatedAppointment.getContactId());
            ps.setInt(10, updatedAppointment.getAppointmentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an appointment from the database.
     *
     * @param appointmentId the ID of the appointment to delete.
     */
    public static void deleteAppointment(int appointmentId) {
        try {
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
