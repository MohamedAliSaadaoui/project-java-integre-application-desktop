package com.example.rewear.gestionuser.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class Profile {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label addressLabel;

    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button logoutButton;

    private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/rewear_db";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private int userId; // L'ID de l'utilisateur connecté

    public void initialize() {
        try {
            // Charger le pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Pilote JDBC chargé avec succès");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur lors du chargement du pilote JDBC: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger le pilote JDBC.", Alert.AlertType.ERROR);
        }

        // L'initialisation de userId se fait via la méthode setUserId appelée depuis Login
        System.out.println("Initialisation du contrôleur Profile");
    }

    // Nouvelle méthode pour définir l'ID utilisateur depuis le login
    public void setUserId(int userId) {
        System.out.println("ID utilisateur défini: " + userId);
        this.userId = userId;
        loadUserProfile(); // Charger les infos du profil avec le nouvel ID
    }

    // Charger les informations de l'utilisateur
    private void loadUserProfile() {
        System.out.println("Chargement du profil pour l'utilisateur ID: " + userId);

        // Pour des tests rapides quand la base de données n'est pas disponible
        if (userId == 1) {
            try {
                // Tenter d'abord de charger depuis la base de données
                loadFromDatabase();
            } catch (Exception e) {
                System.out.println("Erreur de base de données, utilisation des valeurs de test: " + e.getMessage());
                // Valeurs de test si la BD échoue
                usernameLabel.setText("Utilisateur Test");
                emailLabel.setText("user@example.com");
                phoneLabel.setText("123-456-7890");
                birthDateLabel.setText("01/01/2000");
                addressLabel.setText("123 Rue du Test, Ville Test");
            }
            return;
        }

        loadFromDatabase();
    }

    // Méthode séparée pour charger depuis la base de données
    private void loadFromDatabase() {
        String query = "SELECT username, email, num_tel, date_naiss, adresse FROM user WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connexion à la base de données établie");

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            System.out.println("Exécution de la requête pour ID: " + userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameLabel.setText(rs.getString("username"));
                emailLabel.setText(rs.getString("email"));
                phoneLabel.setText(rs.getString("num_tel"));
                birthDateLabel.setText(rs.getString("date_naiss"));
                addressLabel.setText(rs.getString("adresse"));
                System.out.println("Informations utilisateur chargées avec succès");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID: " + userId);
                showAlert("Erreur", "Utilisateur non trouvé.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors du chargement du profil: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des informations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Modifier les informations (fonctionnalité à ajouter plus tard)
    @FXML
    private void handleEdit() {
        try {
            System.out.println("Redirection vers le formulaire de modification du profil...");

            // Charger le formulaire d'édition
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/edit_profile.fxml"));
            Parent editRoot = loader.load();

            // Récupérer le contrôleur d'édition et passer l'ID utilisateur
            EditProfile editController = loader.getController();
            editController.setUserId(userId);

            // Créer une nouvelle scène pour l'édition
            Scene editScene = new Scene(editRoot);

            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) editButton.getScene().getWindow();

            // Appliquer la nouvelle scène
            currentStage.setScene(editScene);
            currentStage.setTitle("Modifier le profil");
            currentStage.show();
            System.out.println("Redirection vers la page d'édition réussie");
        } catch (IOException e) {
            System.out.println("Erreur lors de la redirection vers l'édition: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'édition: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    // Supprimer l'utilisateur
    @FXML
    private void handleDelete() {
        // Confirmation avant suppression
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText("Suppression de compte");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteUser();
            }
        });
    }

    // Méthode pour supprimer l'utilisateur de la base de données
    private void deleteUser() {
        String query = "DELETE FROM user WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            System.out.println("Tentative de suppression de l'utilisateur ID: " + userId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Utilisateur supprimé avec succès");
                showAlert("Succès", "Utilisateur supprimé avec succès.", Alert.AlertType.INFORMATION);
                logout(); // Déconnexion après la suppression du compte
            } else {
                System.out.println("Échec de la suppression, aucune ligne affectée");
                showAlert("Erreur", "Échec de la suppression de l'utilisateur.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la suppression: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la suppression de l'utilisateur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Déconnexion de l'utilisateur
    @FXML
    private void handleLogout() {
        logout();
    }

    // Logique pour se déconnecter
    private void logout() {
        System.out.println("Déconnexion de l'utilisateur");
        try {
            // Charger la scène de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/login.fxml"));
            Parent loginRoot = loader.load();

            // Créer une nouvelle scène pour le login
            Scene loginScene = new Scene(loginRoot);

            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();

            // Appliquer la scène de login
            currentStage.setScene(loginScene);
            currentStage.setTitle("Connexion");
            currentStage.show();
            System.out.println("Redirection vers la page de login réussie");
        } catch (IOException e) {
            System.out.println("Erreur lors de la redirection vers le login: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la déconnexion.", Alert.AlertType.ERROR);

            // En cas d'erreur grave, on ferme simplement la fenêtre
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();
        }
    }

    // Afficher les alertes
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}