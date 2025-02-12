package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides methods to access country data from the database.
 */
public abstract class CountryData {

    /**
     * Retrieves all countries from the database.
     *
     * @return an ObservableList of Countries objects
     */
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> countryList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM countries";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            // Iterate through each row in the ResultSet to create a Countries object.
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

    /**
     * Retrieves a country from the database by its ID.
     *
     * @param countryId the unique identifier of the country to retrieve
     * @return a Countries object if found; otherwise, null
     */
    public static Countries getCountryById(int countryId) {
        Countries country = null;
        try {
            String sql = "SELECT * FROM countries WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            // If a matching row is found, create and return a Countries object.
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