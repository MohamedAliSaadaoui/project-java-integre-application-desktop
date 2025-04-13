package com.example.rewear.gestionuser.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Login {

        @FXML
        private TextField emailField;

        @FXML
        private PasswordField passwordField;

        @FXML
        private Button signUpButton;

        // Méthode pour gérer la connexion
        @FXML
        private void handleLogin() {
                String email = emailField.getText();
                String password = passwordField.getText();

                // Vérification des champs (email et mot de passe non vides)
                if (email.isEmpty() || password.isEmpty()) {
                        System.out.println("Erreur : Veuillez remplir tous les champs.");
                        return;  // Si l'un des champs est vide, on arrête l'exécution
                }

                // Vérifier que l'email contient "@" (simplification de validation)
                if (!email.contains("@")) {
                        System.out.println("Erreur : L'email n'est pas valide.");
                        return;  // Si l'email est invalide, on arrête l'exécution
                }

                // Logique de connexion ici, vérifier avec une base de données si nécessaire
                System.out.println("Connexion avec : " + email);

                // Si la connexion est réussie, rediriger l'utilisateur
                // Redirection vers la page principale (par exemple)
                // Cette partie dépend de ta logique métier
                // Exemple : goToMainPage();
        }

        // Méthode pour rediriger l'utilisateur vers la page d'inscription
        @FXML
        private void handleSignUp(ActionEvent event) throws IOException {
                // Charger la scène d'inscription
                Parent signUpRoot = FXMLLoader.load(getClass().getResource("/interfaces/signup.fxml"));
                Scene signUpScene = new Scene(signUpRoot);

                // Récupérer la scène actuelle
                Stage currentStage = (Stage) signUpButton.getScene().getWindow();

                // Appliquer la nouvelle scène d'inscription
                currentStage.setScene(signUpScene);
                currentStage.show();
        }
}
