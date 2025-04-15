package com.example.rewear.gestionuser.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML principal qui contient le conteneur pour les autres vues
        Parent root = FXMLLoader.load(getClass().getResource("/interfaces/main.fxml"));

        // Créer la scène avec le conteneur principal
        Scene scene = new Scene(root, 400, 600);

        // Configurer la fenêtre principale

        primaryStage.setTitle("Login - Rewear");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(600);

        // Afficher la fenêtre principale
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}