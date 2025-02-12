package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for the C195 Scheduler application.
 * Opens the database connection, launches the JavaFX application, and closes the connection upon exit.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by loading the login view.
     *
     * @param stage the primary stage for this application
     * @throws Exception if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        stage.setTitle("C195 Scheduler");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    /**
     * The main method which launches the application.
     * Opens the database connection, launches the JavaFX application, and then closes the connection.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        System.out.println("Hello C195 Scheduler!");
        launch(args);
        JDBC.closeConnection();
    }
}
