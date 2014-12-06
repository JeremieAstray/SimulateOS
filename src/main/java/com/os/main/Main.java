package com.os.main;


import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Jeremie on 2014/10/13.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Parent root = FXMLLoader.load(new URL("file:D:\\wuyu3\\simulateOS\\SimulateOS\\src\\main\\resources\\fxml\\main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setResizable(true);
        primaryStage.setTitle("SimulateOS!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
