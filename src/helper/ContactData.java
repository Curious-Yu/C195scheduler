package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ContactData {

    //------ Retrieve All Contacts ------
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contactList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                contactList.add(new Contacts(contactId, contactName, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }
}