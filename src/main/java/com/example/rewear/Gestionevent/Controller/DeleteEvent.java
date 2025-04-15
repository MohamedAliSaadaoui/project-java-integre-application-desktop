package com.example.rewear.Gestionevent.Controller;
import com.example.rewear.DBUtil;
import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.Gestionevent.Service.EventDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteEvent {
    private EventDAO eventDAO;

    @FXML
    private Button deleteButton; // Bouton pour supprimer l'événement

    @FXML
    private Button cancelButton; // Bouton pour annuler l'opération

    @FXML
    private Label eventLabel;   // Label pour afficher les détails de l'événement à supprimer

    private Event eventToDelete; // Événement à supprimer

    @FXML
    public void initialize() {
        try {
            // Initialisation de la connexion à la base de données et du DAO
            Connection connection = DBUtil.getConnection();
            eventDAO = new EventDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
        }
    }

    /**
     * Cette méthode est appelée lorsqu'on clique sur le bouton de suppression
     */
    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            if (eventToDelete != null) {
                // Supprimer l'événement
                eventDAO.deleteEvent(eventToDelete.getId());
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'événement a été supprimé avec succès.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Aucun événement à supprimer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de suppression", "Impossible de supprimer l'événement.");
        }
    }

    /**
     * Cette méthode est appelée lorsqu'on clique sur le bouton d'annulation
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow(); // Ferme la fenêtre sans supprimer l'événement
    }

    /**
     * Cette méthode ferme la fenêtre actuelle
     */
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow(); // Récupère la scène du bouton d'annulation
        stage.close(); // Ferme la fenêtre
    }

    /**
     * Méthode utilitaire pour afficher une alerte à l'utilisateur
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Cette méthode est appelée pour définir l'événement à supprimer
     */
    public void setEventToDelete(Event event) {
        this.eventToDelete = event;
        if (event != null) {
            // Mettre à jour l'affichage avec les détails de l'événement
            eventLabel.setText("Êtes-vous sûr de vouloir supprimer l'événement : " + event.getTitre() + " ?");
        }
    }
}