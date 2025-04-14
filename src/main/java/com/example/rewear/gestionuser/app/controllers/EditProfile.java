package com.example.rewear.gestionuser.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class EditProfile {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField addressField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/rewear_db";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private int userId;

    public void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Pilote JDBC chargé avec succès dans l'éditeur de profil");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur lors du chargement du pilote JDBC: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger le pilote JDBC.", Alert.AlertType.ERROR);
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadUserData();
    }

    private void loadUserData() {
        String query = "SELECT username, email, num_tel, date_naiss, adresse FROM user WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("num_tel"));

                // Conversion de la date String en LocalDate pour le DatePicker
                String birthDateStr = rs.getString("date_naiss");
                if (birthDateStr != null && !birthDateStr.isEmpty()) {
                    try {
                        // Supposons que la date est au format yyyy-MM-dd dans la base de données
                        LocalDate birthDate = LocalDate.parse(birthDateStr);
                        birthDatePicker.setValue(birthDate);
                    } catch (Exception e) {
                        System.out.println("Erreur de format de date: " + e.getMessage());
                    }
                }

                addressField.setText(rs.getString("adresse"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des données: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des informations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSave() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        LocalDate birthDate = birthDatePicker.getValue();
        String address = addressField.getText();

        // Validation des champs
        if (username.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Le nom d'utilisateur et l'email sont obligatoires.", Alert.AlertType.ERROR);
            return;
        }

        // Vérifier que l'email contient "@"
        if (!email.contains("@")) {
            showAlert("Erreur", "L'email n'est pas valide.", Alert.AlertType.ERROR);
            return;
        }

        // Mise à jour dans la base de données
        String query = "UPDATE user SET username = ?, email = ?, num_tel = ?, date_naiss = ?, adresse = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, birthDate != null ? birthDate.toString() : null);
            stmt.setString(5, address);
            stmt.setInt(6, userId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Succès", "Profil mis à jour avec succès.", Alert.AlertType.INFORMATION);
                redirectToProfile();
            } else {
                showAlert("Erreur", "Échec de la mise à jour du profil.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour du profil: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        redirectToProfile();
    }

    private void redirectToProfile() {
        try {
            // Charger la page de profil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/profile.fxml"));
            Parent profileRoot = loader.load();

            // Récupérer le contrôleur et définir l'ID utilisateur
            Profile profileController = loader.getController();
            profileController.setUserId(userId);

            // Créer une nouvelle scène pour le profil
            Scene profileScene = new Scene(profileRoot);

            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();

            // Appliquer la scène de profil
            currentStage.setScene(profileScene);
            currentStage.setTitle("Profil Utilisateur");
            currentStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors de la redirection vers le profil: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour au profil.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}