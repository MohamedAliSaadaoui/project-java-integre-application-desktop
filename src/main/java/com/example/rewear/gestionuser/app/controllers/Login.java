package com.example.rewear.gestionuser.app.controllers;

import com.example.rewear.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

        @FXML
        private TextField emailField;

        @FXML
        private PasswordField passwordField;

        @FXML
        private Button signUpButton;

        @FXML
        private Button loginButton;

        @FXML
        private Hyperlink forgotPasswordLink;

        @FXML
        private ImageView logoImageView;

        private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/rewear_db";
        private final String DB_USER = "root";
        private final String DB_PASSWORD = "";

        @FXML
        public void initialize() {
                try {
                        // Chargement de l'image du logo
                        InputStream stream = getClass().getResourceAsStream("/images/logo rewear.jpg");
                        if (stream != null) {
                                Image image = new Image(stream);
                                logoImageView.setImage(image);
                                System.out.println("Logo chargé avec succès");
                        } else {
                                System.err.println("Ressource d'image introuvable");

                                // Alternative - essayez un autre chemin si le premier échoue
                                stream = getClass().getClassLoader().getResourceAsStream("images/logo rewear.jpg");
                                if (stream != null) {
                                        Image image = new Image(stream);
                                        logoImageView.setImage(image);
                                        System.out.println("Logo chargé avec succès via ClassLoader");
                                } else {
                                        System.err.println("Image introuvable via le ClassLoader également");
                                }
                        }
                } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
                        e.printStackTrace();
                }
        }

        // Méthode pour gérer la connexion
        @FXML
        private void handleLogin() {
                String email = emailField.getText();
                String password = passwordField.getText();

                // Vérification des champs (email et mot de passe non vides)
                if (email.isEmpty() || password.isEmpty()) {
                        showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
                        return;  // Si l'un des champs est vide, on arrête l'exécution
                }

                // Vérifier que l'email contient "@" (simplification de validation)
                if (!email.contains("@")) {
                        showAlert("Erreur", "L'email n'est pas valide.", Alert.AlertType.ERROR);
                        return;  // Si l'email est invalide, on arrête l'exécution
                }

                // Récupérer l'ID de l'utilisateur s'il existe
                int userId = authenticateUser(email, password);
                if (userId > 0) {
                        redirectToProfile(userId);
                } else {
                        showAlert("Erreur", "Email ou mot de passe incorrect.", Alert.AlertType.ERROR);
                }
        }

        // Méthode pour authentifier l'utilisateur et récupérer son ID
        private int authenticateUser(String email, String password) {
                try {
                        // Charger le pilote JDBC
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                                // Récupérer l'utilisateur avec son mot de passe haché
                                String query = "SELECT id, password FROM user WHERE email = ?";
                                PreparedStatement stmt = conn.prepareStatement(query);
                                stmt.setString(1, email);

                                ResultSet rs = stmt.executeQuery();
                                if (rs.next()) {
                                        int userId = rs.getInt("id");
                                        String storedPassword = rs.getString("password");

                                        // Vérifier si le mot de passe est correct
                                        // Si les mots de passe sont stockés en clair (temporairement)
                                        if (storedPassword.equals(password)) {
                                                return userId;
                                        }

                                        // Si les mots de passe sont déjà hachés
                                        if (PasswordUtils.comparePassword(password, storedPassword)) {
                                                return userId;
                                        }
                                }
                        }
                } catch (ClassNotFoundException e) {
                        System.out.println("Pilote JDBC introuvable: " + e.getMessage());
                        e.printStackTrace();
                } catch (SQLException e) {
                        System.out.println("Erreur SQL: " + e.getMessage());
                        e.printStackTrace();
                }

                // Pour test quand la base de données n'est pas configurée
                if (email.equals("user@example.com") && password.equals("password123")) {
                        return 1; // ID de test
                }

                return -1; // Authentification échouée
        }

        // Méthode pour rediriger l'utilisateur vers la page du profil
        private void redirectToProfile(int userId) {
                try {
                        System.out.println("Tentative de redirection vers le profil avec l'utilisateur ID: " + userId);

                        // Charger la scène de profil
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/profile.fxml"));
                        Parent profileRoot = loader.load();
                        System.out.println("FXML de profil chargé avec succès");

                        // Récupérer le contrôleur de profil et définir l'ID de l'utilisateur
                        Profile profileController = loader.getController();
                        profileController.setUserId(userId);
                        System.out.println("ID utilisateur défini dans le contrôleur de profil");

                        // Créer une nouvelle scène avec la page de profil
                        Scene profileScene = new Scene(profileRoot);

                        // Récupérer la scène actuelle
                        Stage currentStage = (Stage) loginButton.getScene().getWindow();

                        // Appliquer la nouvelle scène (redirection vers le profil)
                        currentStage.setScene(profileScene);
                        currentStage.setTitle("Profil Utilisateur");
                        currentStage.show();
                        System.out.println("Redirection vers le profil réussie");
                } catch (IOException e) {
                        System.out.println("Erreur lors de la redirection: " + e.getMessage());
                        e.printStackTrace();
                        showAlert("Erreur", "Erreur de redirection vers la page de profil: " + e.getMessage(), Alert.AlertType.ERROR);
                }
        }

        // Méthode pour afficher une alerte
        private void showAlert(String title, String message, Alert.AlertType alertType) {
                Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }

        // Méthode pour rediriger l'utilisateur vers la page d'inscription
        @FXML
        private void handleSignUp(ActionEvent event) throws IOException {
                try {
                        // Charger la scène d'inscription
                        Parent signUpRoot = FXMLLoader.load(getClass().getResource("/interfaces/signup.fxml"));
                        Scene signUpScene = new Scene(signUpRoot);

                        // Récupérer la scène actuelle
                        Stage currentStage = (Stage) signUpButton.getScene().getWindow();

                        // Appliquer la nouvelle scène d'inscription
                        currentStage.setScene(signUpScene);
                        currentStage.setTitle("Inscription");
                        currentStage.show();
                } catch (IOException e) {
                        System.out.println("Erreur lors de la redirection vers l'inscription: " + e.getMessage());
                        e.printStackTrace();
                        showAlert("Erreur", "Erreur de redirection vers la page d'inscription: " + e.getMessage(), Alert.AlertType.ERROR);
                }
        }

        // Méthode pour gérer le lien "mot de passe oublié"
        @FXML
        private void handleForgotPassword(ActionEvent event) {
                // Implémentez la logique pour réinitialiser le mot de passe
                // Par exemple, rediriger vers un écran de réinitialisation ou envoyer un email
                showAlert("Information", "La fonctionnalité de récupération de mot de passe n'est pas encore implémentée.", Alert.AlertType.INFORMATION);
        }
}