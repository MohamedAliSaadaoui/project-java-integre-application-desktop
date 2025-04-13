package com.example.rewear;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.rewear.util.DBUtil;

import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {
    private static Connection connection;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            connection = DBUtil.getConnection();
            System.out.println("Database connection established successfully");
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            System.exit(1);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/rewear/views/product-list-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Rewear - Products");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        if (connection != null) {
            DBUtil.closeConnection(connection);
        }
    }
}