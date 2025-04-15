package com.example.rewear.gestionuser.app.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Main {

    @FXML
    private StackPane mainContainer;

    private Pane loginPane;
    private Pane resetPasswordPane;

    private Login loginController;
    private ResetPassword resetPasswordController;

    /**
     * Initialise le contrôleur principal et charge les vues
     */
    @FXML
    public void initialize() {
        try {
            // Charger la vue de connexion
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/interfaces/login.fxml"));
            loginPane = loginLoader.load();
            loginController = loginLoader.getController();
            loginController.setMainController(this);

            // Charger la vue de réinitialisation de mot de passe
            FXMLLoader resetLoader = new FXMLLoader(getClass().getResource("/interfaces/resetpassword.fxml"));
            resetPasswordPane = resetLoader.load();
            resetPasswordController = resetLoader.getController();
            resetPasswordController.setMainController(this);

            // Ajouter les deux vues au conteneur principal
            mainContainer.getChildren().addAll(loginPane, resetPasswordPane);

            // Afficher la vue de connexion par défaut
            showLoginScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche l'écran de connexion
     */
    public void showLoginScreen() {
        loginPane.setVisible(true);
        resetPasswordPane.setVisible(false);
    }

    /**
     * Affiche l'écran de réinitialisation de mot de passe
     */
    public void showResetPasswordScreen() {
        if (resetPasswordController != null) {
            resetPasswordController.resetForm(); // Réinitialiser le formulaire
        }
        loginPane.setVisible(false);
        resetPasswordPane.setVisible(true);
    }
}