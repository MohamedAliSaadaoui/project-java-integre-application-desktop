package com.example.rewear;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger la vue ListEvent.fxml au lieu de AjoutEvent.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/ListEvent.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Rewear - Liste des événements");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur de chargement du FXML: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur imprévue: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}