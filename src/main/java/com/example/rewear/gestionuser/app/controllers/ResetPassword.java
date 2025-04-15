package com.example.rewear.gestionuser.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ResetPassword {
    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleResetPassword() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            messageLabel.setText("Veuillez entrer votre email.");
        } else if (!email.contains("@")) {
            messageLabel.setText("Adresse email invalide.");
        } else {
            // Simulation d'envoi de l'email
            messageLabel.setText("Un lien a été envoyé à " + email + ".");
            messageLabel.setStyle("-fx-text-fill: green;");
        }
    }
}
