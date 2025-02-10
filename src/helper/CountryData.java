package helper;

import model.Countries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a Data Access Object (DAO) for managing country information in a database.
 * It provides static methods to retrieve country details from the database.
 */
public class CountryData {

    /**
     * Retrieves a list of all countries from the countries table in the database.
     *
     * @return A List of Countries objects.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Countries> getAllCountries() throws SQLException {
        List<Countries> countriesList = new ArrayList<>();
        String sql = "SELECT * FROM countries";  // Simple query to fetch all countries
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            // Create a new Countries object using the result set
            Countries country = new Countries(result.getInt("Country_ID"), result.getString("Country"));
            countriesList.add(country);
        }
        return countriesList;
    }

    /**
     * Retrieves a specific country by its ID.
     *
     * @param countryId The ID of the country.
     * @return A Countries object representing the country with the given ID.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static Countries getCountryById(int countryId) throws SQLException {
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, countryId);  // Set the countryId parameter
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            // Return a country object with the data from the result set
            return new Countries(result.getInt("Country_ID"), result.getString("Country"));
        }
        return null;  // If no country was found, return null
    }
}