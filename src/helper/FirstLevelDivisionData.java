package helper;

import model.FirstLevelDivisions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FirstLevelDivisionData {

    //------ Get All Divisions ------
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

    //------ Get Division by ID ------
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

    //------ Get Divisions by Country ID ------
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

    //------ Get Division Names by Country ID ------
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

    //------ Get Division ID by Name ------
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