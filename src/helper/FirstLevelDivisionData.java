package helper;

import model.FirstLevelDivisions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a Data Access Object (DAO) for managing first-level division information in a database.
 * It provides static methods to retrieve first-level division details from the database.
 */
public class FirstLevelDivisionData {

    /**
     * Retrieves a list of all first-level divisions from the first_level_division table in the database.
     *
     * @return A List of FirstLevelDivisions objects.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<FirstLevelDivisions> getAllFirstLevelDivisions() throws SQLException {
        List<FirstLevelDivisions> divisionsList = new ArrayList<>();
        String sql = "SELECT * FROM first_level_divisions";  // Simple query to fetch all first-level divisions
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            // Create a new FirstLevelDivisions object using the result set
            FirstLevelDivisions division = new FirstLevelDivisions(
                    result.getInt("Division_ID"),
                    result.getString("Division"),
                    result.getInt("Country_ID")
            );
            divisionsList.add(division);
        }
        return divisionsList;
    }

    /**
     * Retrieves a specific first-level division by its ID.
     *
     * @param divisionId The ID of the division.
     * @return A FirstLevelDivisions object representing the division with the given ID.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static FirstLevelDivisions getFirstLevelDivisionById(int divisionId) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, divisionId);  // Set the divisionId parameter
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            // Return a FirstLevelDivisions object with the data from the result set
            return new FirstLevelDivisions(
                    result.getInt("Division_ID"),
                    result.getString("Division"),
                    result.getInt("Country_ID")
            );
        }
        return null;  // If no division was found, return null
    }

    /**
     * Retrieves a list of first-level divisions by country ID.
     *
     * @param countryId The ID of the country.
     * @return A List of FirstLevelDivisions objects belonging to the given country.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<FirstLevelDivisions> getDivisionsByCountryId(int countryId) throws SQLException {
        List<FirstLevelDivisions> divisionsList = new ArrayList<>();
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, countryId);  // Set the countryId parameter
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            // Create and add a new FirstLevelDivisions object for each row in the result set
            FirstLevelDivisions division = new FirstLevelDivisions(
                    result.getInt("Division_ID"),
                    result.getString("Division"),
                    result.getInt("Country_ID")
            );
            divisionsList.add(division);
        }
        return divisionsList;
    }
}