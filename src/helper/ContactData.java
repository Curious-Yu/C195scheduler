package helper;

import model.Contacts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a Data Access Object (DAO) for managing contact information in a database.
 * It provides static methods to retrieve contact details from the database.
 */
public class ContactData {

    /**
     * Retrieves a list of all contact IDs from the contacts table in the database.
     *
     * @return A List of integers where each integer represents a unique contact ID.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Integer> getContactIds() throws SQLException {
        List<Integer> contactIds = new ArrayList<>();
        String sql = "SELECT Contact_ID FROM contacts";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            contactIds.add(result.getInt("Contact_ID"));
        }
        return contactIds;
    }

    /**
     * Retrieves a list of all contacts with their ID, name, and email.
     *
     * @return A List of Contacts objects.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Contacts> getAllContacts() throws SQLException {
        List<Contacts> contactsList = new ArrayList<>();
        String sql = "SELECT * FROM contacts";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int id = result.getInt("Contact_ID");
            String name = result.getString("Contact_Name");
            String email = result.getString("Email");

            // Create a new Contacts object and add it to the list
            Contacts contact = new Contacts(id, name, email);
            contactsList.add(contact);
        }
        return contactsList;
    }

    /**
     * Retrieves a contact's name by ID.
     *
     * @param contactId The ID of the contact.
     * @return The contact's name if found, otherwise null.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static String getContactNameById(int contactId) throws SQLException {
        String sql = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, contactId);
        ResultSet result = statement.executeQuery();

        if (result.next()) {
            return result.getString("Contact_Name");
        }
        return null;
    }
}