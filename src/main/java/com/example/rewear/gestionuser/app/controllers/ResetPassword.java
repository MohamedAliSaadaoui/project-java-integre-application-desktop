package com.example.rewear.gestionuser.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ResetPassword {

    @FXML
    private VBox resetPasswordPane;

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button sendResetLinkButton;

    @FXML
    private Button backToLoginButton;

    // Référence au contrôleur principal pour la navigation
    private Main mainController;

    /**
     * Initialise le contrôleur après que le FXML a été chargé
     */
    @FXML
    public void initialize() {
        // Initialisation si nécessaire
        if (messageLabel != null) {
            messageLabel.setVisible(false);
        }
    }

    /**
     * Définit le contrôleur principal pour gérer la navigation
     * @param main le contrôleur principal
     */
    public void setMainController(Main main) {
        this.mainController = main;
    }

    /**
     * Gère l'envoi du lien de réinitialisation
     * @param event L'événement du bouton
     */
    @FXML
    private void handleSendResetLink(ActionEvent event) {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showMessage("Veuillez saisir votre adresse e-mail.", true);
            return;
        }

        if (!isValidEmail(email)) {
            showMessage("Adresse e-mail invalide.", true);
            return;
        }

        // Simuler l'envoi du lien de réinitialisation
        // Dans une application réelle, vous feriez appel à votre service
        // pour générer un token et envoyer un email

        // Afficher un message de succès
        showMessage("Un lien de réinitialisation a été envoyé à " + email, false);

        // Réinitialiser le champ email
        emailField.clear();
    }

    /**
     * Retourne à l'écran de connexion
     * @param event L'événement du bouton
     */
    @FXML
    private void handleBackToLogin(ActionEvent event) {
        if (mainController != null) {
            mainController.showLoginScreen();
        } else {
            System.err.println("Erreur: Le contrôleur principal n'est pas défini.");
        }
    }

    /**
     * Vérifie si l'email est valide avec une validation basique
     * @param email L'email à valider
     * @return true si l'email est valide, sinon false
     */
    private boolean isValidEmail(String email) {
        // Validation simple d'email avec regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Affiche un message à l'utilisateur
     * @param message Le message à afficher
     * @param isError true si c'est un message d'erreur, false sinon
     */
    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
            messageLabel.setVisible(true);
        }
    }

    /**
     * Réinitialise les champs et messages
     */
    public void resetForm() {
        if (emailField != null) {
            emailField.clear();
        }
        if (messageLabel != null) {
            messageLabel.setVisible(false);
        }
    }
}