package com.example.rewear.gestionuser.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SignUp {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleSignUp() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation des champs
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
            return;
        }

        // Validation de l'email
        if (!email.contains("@")) {
            showAlert("Erreur", "L'email n'est pas valide.", AlertType.ERROR);
            return;
        }

        // Validation du mot de passe
        if (!password.equals(confirmPassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.", AlertType.ERROR);
            return;
        }

        // Enregistrer l'utilisateur (logique à ajouter, par exemple dans une base de données)
        System.out.println("Utilisateur inscrit : " + email);

        // Rediriger vers la page de connexion après une inscription réussie
        redirectToLogin();
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToLogin() {
        // Logic to redirect the user to the login page
        System.out.println("Redirection vers la page de connexion...");
    }
}
