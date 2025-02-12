package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class UsersData {

    //------ Retrieve All Users ------
    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> usersList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                Users user = new Users(userId, userName, password);
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    //------ Validate User Credentials ------
    public static Users validateUser(String username, String password) {
        Users user = null;
        try {
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String pw = rs.getString("Password");
                user = new Users(userId, userName, pw);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}