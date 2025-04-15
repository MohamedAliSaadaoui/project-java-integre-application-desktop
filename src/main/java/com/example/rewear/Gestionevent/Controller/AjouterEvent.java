package com.example.rewear.Gestionevent.Controller;

import com.example.rewear.DBUtil;
import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.Gestionevent.Entity.SessionManager;
import com.example.rewear.Gestionevent.Service.EventDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AjouterEvent implements Initializable {
    @FXML
    private TextField titre_ajout;

    @FXML
    private TextField dated_ajout;

    @FXML
    private TextField datef_ajout;

    @FXML
    private TextField lieu_ajout;

    @FXML
    private TextField statut_ajout;

    @FXML
    private TextField categorie_ajout;

    @FXML
    private Button creer;

    @FXML
    private Button retour;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Simuler qu'un utilisateur est connecté
        SessionManager.getInstance().login(5);
    }

    @FXML
    public void handleCreate(ActionEvent event) {
        try {
            // Vérifier que tous les champs sont remplis
            if (titre_ajout.getText().isEmpty() || dated_ajout.getText().isEmpty() ||
                    datef_ajout.getText().isEmpty() || lieu_ajout.getText().isEmpty() ||
                    statut_ajout.getText().isEmpty() || categorie_ajout.getText().isEmpty()) {

                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis");
                return;
            }

            // Formater et valider les dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateDebut, dateFin;

            try {
                dateDebut = LocalDate.parse(dated_ajout.getText(), formatter);
                dateFin = LocalDate.parse(datef_ajout.getText(), formatter);
            } catch (DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de format",
                        "Les dates doivent être au format YYYY-MM-DD");
                return;
            }

            // Vérifier que la date de fin est après la date de début
            if (dateFin.isBefore(dateDebut)) {
                showAlert(Alert.AlertType.ERROR, "Erreur de date",
                        "La date de fin doit être après la date de début");
                return;
            }

            // Créer l'objet Event
            Event newEvent = new Event(
                    titre_ajout.getText(),
                    dateDebut,
                    dateFin,
                    lieu_ajout.getText(),
                    statut_ajout.getText(),
                    categorie_ajout.getText(),
                    5 // ID créateur par défaut
            );

            // Sauvegarder dans la base de données
            try (Connection connection = DBUtil.getConnection()) {
                EventDAO eventDAO = new EventDAO(connection);
                eventDAO.addEvent(newEvent);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès !");
                clearFields();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur SQL",
                        "Erreur lors de l'ajout : " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        titre_ajout.clear();
        dated_ajout.clear();
        datef_ajout.clear();
        lieu_ajout.clear();
        statut_ajout.clear();
        categorie_ajout.clear();
    }

    /**
     * Gère l'action du bouton Retour
     * Ferme simplement la fenêtre actuelle pour revenir à l'écran précédent
     */
    @FXML
    private void handleRetour() {
        // Obtenir la fenêtre actuelle à partir du bouton retour
        Stage stage = (Stage) retour.getScene().getWindow();

        // Fermer cette fenêtre
        stage.close();
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}