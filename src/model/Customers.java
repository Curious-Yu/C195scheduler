package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customers {
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;

    //------ Parameterized Constructor ------
    public Customers(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    //------ ResultSet Constructor ------
    public Customers(ResultSet result) throws SQLException {
        this.customerId = result.getInt("Customer_ID");
        this.customerName = result.getString("Customer_Name");
        this.address = result.getString("Address");
        this.postalCode = result.getString("Postal_Code");
        this.phone = result.getString("Phone");
        this.divisionId = result.getInt("Division_ID");
    }

    //------ Getters and Setters ------
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}