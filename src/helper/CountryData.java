package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CountryData {

    //------ Get All Countries ------
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> countryList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM countries";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                countryList.add(new Countries(countryId, countryName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryList;
    }

    //------ Get Country by ID ------
    public static Countries getCountryById(int countryId) {
        Countries country = null;
        try {
            String sql = "SELECT * FROM countries WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                country = new Countries(id, countryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return country;
    }
}