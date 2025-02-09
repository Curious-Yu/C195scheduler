package helper;

import model.Customers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a Data Access Object (DAO) for managing customer information in a database.
 * It provides static methods to retrieve customer details from the database.
 */
public class CustomerData {

    /**
     * Retrieves a list of all customer IDs from the customers table in the database.
     *
     * @return A List of integers where each integer represents a unique customer ID.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Integer> getCustomerIds() throws SQLException {
        List<Integer> customerIds = new ArrayList<>();
        String sql = "SELECT Customer_ID FROM customers";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            customerIds.add(result.getInt("Customer_ID"));
        }
        return customerIds;
    }

    /**
     * Retrieves a list of all customers with their ID, name, address, postal code, phone, and division ID.
     *
     * @return A List of Customers objects.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Customers> getAllCustomers() throws SQLException {
        List<Customers> customersList = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            int customerId = result.getInt("Customer_ID");
            String customerName = result.getString("Customer_Name");
            String address = result.getString("Address");
            String postalCode = result.getString("Postal_Code");
            String phone = result.getString("Phone");
            int divisionId = result.getInt("Division_ID");

            // Create a new Customers object and add it to the list
            Customers customer = new Customers(customerId, customerName, address, postalCode, phone, divisionId);
            customersList.add(customer);
        }
        return customersList;
    }

    /**
     * Retrieves a customer's name by ID.
     *
     * @param customerId The ID of the customer.
     * @return The customer's name if found, otherwise null.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static String getCustomerNameById(int customerId) throws SQLException {
        String sql = "SELECT Customer_Name FROM customers WHERE Customer_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, customerId);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getString("Customer_Name");
        }
        return null;
    }
}