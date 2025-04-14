package com.example.rewear.gestionuser.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML de la scène de login
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/interfaces/login.fxml"));

        // Créer la scène avec le root de la page de login
        Scene loginScene = new Scene(loginRoot);

        // Définir la scène principale et le titre
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login - Rewear");

        // Afficher la fenêtre principale
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
