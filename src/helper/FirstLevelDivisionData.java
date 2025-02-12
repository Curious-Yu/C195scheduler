package helper;

import model.FirstLevelDivisions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides data access methods for first-level division information.
 */
public class FirstLevelDivisionData {

    /**
     * Retrieves all first-level divisions from the database.
     *
     * @return a List of FirstLevelDivisions objects
     * @throws SQLException if an SQL error occurs
     */
    public static List<FirstLevelDivisions> getAllFirstLevelDivisions() throws SQLException {
        List<FirstLevelDivisions> divisionsList = new ArrayList<>();
        String sql = "SELECT * FROM first_level_divisions";
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
     * Retrieves a first-level division by its ID.
     *
     * @param divisionId the division ID to search for
     * @return a FirstLevelDivisions object if found; otherwise, null
     * @throws SQLException if an SQL error occurs
     */
    public static FirstLevelDivisions getFirstLevelDivisionById(int divisionId) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, divisionId);
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
     * Retrieves all first-level divisions for a given country ID.
     *
     * @param countryId the country ID to filter by
     * @return a List of FirstLevelDivisions objects for the specified country
     * @throws SQLException if an SQL error occurs
     */
    public static List<FirstLevelDivisions> getDivisionsByCountryId(int countryId) throws SQLException {
        List<FirstLevelDivisions> divisionsList = new ArrayList<>();
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, countryId);
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
     * Retrieves a list of division names for a given country ID.
     *
     * @param countryId the country ID to filter by
     * @return a List of Strings representing division names for the specified country
     */
    public static List<String> getDivisionsByCountry(int countryId) {
        List<String> divisionsList = new ArrayList<>();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
        try {
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setInt(1, countryId);
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
     * Retrieves the division ID corresponding to the specified division name.
     *
     * @param division the name of the division
     * @return the Division_ID if found; otherwise, -1
     */
    public static int getDivisionIdByName(String division) {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        try {
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, division);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("Division_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}