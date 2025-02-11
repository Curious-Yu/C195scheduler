package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ContactData {

    /**
     * Retrieves all contacts from the database.
     *
     * @return an ObservableList of Contacts objects.
     */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contactList = FXCollections.observableArrayList();

        try {
            // SQL query to fetch all contacts.
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Iterate through the result set and create Contacts objects.
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                Contacts contact = new Contacts(contactId, contactName, email);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contactList;
    }
}