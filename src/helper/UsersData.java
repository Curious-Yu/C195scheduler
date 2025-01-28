package helper;

import model.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public abstract class UsersData {
    /**
     * Validates a user's login credentials against the users table in the database.
     * Messages indicating the success or failure of the login attempt are printed to the console.
     *
     * @param userName The username provided for login.
     * @param password The password provided for login.
     * @return A Users object representing the authenticated user if login is successful.
     */
    public static Users validateUser(String userName, String password) {
        try {
            String sql = "SELECT * FROM client_schedule.users WHERE User_Name = ? AND Password = ?";
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                // Create a Users object with data from the database
                int userId = result.getInt("User_ID");
                String retrievedUserName = result.getString("User_Name");
                String retrievedPassword = result.getString("Password");
                Users authenticatedUser = new Users(userId, retrievedUserName, retrievedPassword);

                System.out.println("Successful Login!");
                return authenticatedUser;
            } else {
                System.out.println("Failed Login!");
                return null;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a list of all user IDs from the users table in the database.
     *
     * @return A List of unique user IDs from the database.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Integer> getUserIds() throws SQLException {
        List<Integer> listOfUserIds = new ArrayList<>();
        String sql = "SELECT User_ID FROM client_schedule.users";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            listOfUserIds.add(result.getInt("User_ID"));
        }
        return listOfUserIds;
    }
}