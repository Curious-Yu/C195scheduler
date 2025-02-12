package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //------ Start Application ------
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        stage.setTitle("C195 Scheduler");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    //------ Main Method ------
    public static void main(String[] args) {
        JDBC.openConnection();
        System.out.println("Hello C195 Scheduler!");
        launch(args);
        JDBC.closeConnection();
    }
}
