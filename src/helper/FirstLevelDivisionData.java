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
            return new FirstLevelDivisions(
                    result.getInt("Division_ID"),
                    result.getString("Division"),
                    result.getInt("Country_ID")
            );
        }
        return null;
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
     * Retrieves a list of division names (as Strings) by country ID.
     *
     * @param countryId The ID of the country.
     * @return A List of Strings representing division names belonging to the given country.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<String> getDivisionsByCountry(int countryId) {
        List<String> divisionsList = new ArrayList<>();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
        try {
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setInt(1, countryId);  // Set the countryId parameter
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                divisionsList.add(result.getString("Division"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionsList;
    }

    /**
     * Retrieves the Division_ID by the division name.
     *
     * @param division The name of the division.
     * @return The Division_ID of the division, or -1 if no such division exists.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int getDivisionIdByName(String division) {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        try {
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, division);  // Set the division name parameter
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("Division_ID");  // Return the Division_ID if found
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print the stack trace for debugging
        }
        return -1;  // Return -1 if the division name is not found
    }
}