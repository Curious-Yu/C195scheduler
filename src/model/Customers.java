package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a customer with an ID, name, address, postal code, phone number, and associated division ID.
 */
public class Customers {
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;

    /**
     * Constructs a Customers object with the specified values.
     *
     * @param customerId   the unique ID of the customer
     * @param customerName the customer's name
     * @param address      the customer's address
     * @param postalCode   the customer's postal code
     * @param phone        the customer's phone number
     * @param divisionId   the division ID associated with the customer
     */
    public Customers(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /**
     * Constructs a Customers object from a SQL ResultSet.
     *
     * @param result the ResultSet containing customer data
     * @throws SQLException if an SQL error occurs while retrieving data
     */
    public Customers(ResultSet result) throws SQLException {
        this.customerId = result.getInt("Customer_ID");
        this.customerName = result.getString("Customer_Name");
        this.address = result.getString("Address");
        this.postalCode = result.getString("Postal_Code");
        this.phone = result.getString("Phone");
        this.divisionId = result.getInt("Division_ID");
    }

    /**
     * Returns the customer's ID.
     *
     * @return the customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer's ID.
     *
     * @param customerId the new customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns the customer's name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer's name.
     *
     * @param customerName the new customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Returns the customer's address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the customer's address.
     *
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the customer's postal code.
     *
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the customer's postal code.
     *
     * @param postalCode the new postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Returns the customer's phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the customer's phone number.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the division ID associated with the customer.
     *
     * @return the division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division ID associated with the customer.
     *
     * @param divisionId the new division ID
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}