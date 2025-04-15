package com.example.rewear.gestionuser.app.controllers;

import com.example.rewear.utils.PasswordUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

public class SignUp {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL chargé avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur: Driver MySQL non trouvé");
            e.printStackTrace();
        }
    }

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField phoneField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField addressField;
    @FXML
    private Label photoPathLabel;
    @FXML
    private Button uploadButton;

    private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/rewear_db";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private String photoPath;

    @FXML
    private void handleSignUp(ActionEvent event) {
        if (!testDatabaseConnection()) {
            showAlert("Erreur", "Impossible de se connecter à la base de données.", Alert.AlertType.ERROR);
            return;
        }

        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String birthDate = (birthDatePicker.getValue() != null) ? birthDatePicker.getValue().toString() : "";

        // Validation des champs
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.", Alert.AlertType.ERROR);
            return;
        }

        // Vérifier la longueur minimale du mot de passe
        if (password.length() < 6) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères.", Alert.AlertType.ERROR);
            return;
        }

        if (email.isEmpty() || phone.isEmpty() || address.isEmpty() || birthDate.isEmpty()) {
            showAlert("Erreur", "Tous les champs obligatoires doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Hasher le mot de passe avant de l'enregistrer
            String hashedPassword = PasswordUtils.hashPassword(password);

            String sql = "INSERT INTO user (username, password, email, num_tel, date_naiss, adresse, photo) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); // Utiliser le mot de passe haché
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, birthDate);
            stmt.setString(6, address);
            stmt.setString(7, photoPath);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Nombre de lignes affectées: " + rowsAffected);

            if (rowsAffected > 0) {
                showAlert("Succès", "Inscription réussie !", Alert.AlertType.INFORMATION);
                redirectToLogin(event);
            } else {
                showAlert("Erreur", "Aucune ligne n'a été insérée.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
            showAlert("Erreur", "Erreur lors de l'enregistrement : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleFileUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            photoPath = selectedFile.getAbsolutePath();
            photoPathLabel.setText("Fichier sélectionné: " + selectedFile.getName());
        } else {
            photoPathLabel.setText("Aucune photo sélectionnée");
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de connexion.", Alert.AlertType.ERROR);
        }
    }

    private boolean testDatabaseConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connexion à la base de données réussie!");
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
            return false;
        }
    }


    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        try {
            // Méthode 1: Utiliser getResourceAsStream (plus fiable)
            InputStream stream = getClass().getResourceAsStream("/images/logo rewear.jpg");
            if (stream != null) {
                Image image = new Image(stream);
                logoImageView.setImage(image);
            } else {
                System.err.println("Ressource d'image introuvable");
            }

        /* Méthode alternative si la précédente ne fonctionne pas
        String imagePath = getClass().getResource("/images/logo.png").toExternalForm();
        Image image = new Image(imagePath);
        logoImageView.setImage(image);
        */
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            // Vous pouvez créer une image par défaut si nécessaire
        }
    }
}
