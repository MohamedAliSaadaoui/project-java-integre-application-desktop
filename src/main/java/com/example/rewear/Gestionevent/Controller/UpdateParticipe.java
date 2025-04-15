
package com.example.rewear.Gestionevent.Controller;

import com.example.rewear.Gestionevent.Entity.Participe;
import com.example.rewear.Gestionevent.Service.ParticipeDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class UpdateParticipe {
    @FXML
    private Label lblTitre;

    @FXML
    private Label lblLieu;

    @FXML
    private Label lblPeriode;

    @FXML
    private Label lblPlacesDisponibles;

    @FXML
    private DatePicker dateParticipation;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    private Participe participation;
    private ParticipeDAO participeDAO;

    public UpdateParticipe() {
        participeDAO = new ParticipeDAO();
    }

    /**
     * Initialise les données du formulaire avec les informations de l'événement et de la participation
     */
    public void initialiserDonnees(Participe participation, String titre, String lieu, String periode, int placesRestantes) {
        this.participation = participation;

        lblTitre.setText(titre);
        lblLieu.setText(lieu);
        lblPeriode.setText(periode);
        lblPlacesDisponibles.setText(String.valueOf(placesRestantes));

        // Initialiser le DatePicker avec la date de participation actuelle
        dateParticipation.setValue(participation.getDateParticipation());
    }

    /**
     * Gère l'enregistrement des modifications
     */
    @FXML
    private void handleEnregistrer(ActionEvent event) {
        try {
            // Récupérer la nouvelle date de participation
            LocalDate nouvelleDate = dateParticipation.getValue();

            if (nouvelleDate == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date de participation.");
                return;
            }

            // Mettre à jour la date de participation
            participation.setDateParticipation(nouvelleDate);

            // Appeler le DAO pour mettre à jour la participation
            boolean success = participeDAO.modifierParticipation(participation);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La participation a été modifiée avec succès.");
                fermerFenetre();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier la participation. Veuillez réessayer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue: " + e.getMessage());
        }
    }

    /**
     * Gère l'annulation et ferme la fenêtre
     */
    @FXML
    private void handleAnnuler(ActionEvent event) {
        fermerFenetre();
    }

    /**
     * Ferme la fenêtre courante
     */
    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    /**
     * Affiche une alerte
     */
    private void showAlert(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}