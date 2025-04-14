package com.example.rewear.Gestionevent.Controller;
import com.example.rewear.Gestionevent.Entity.Participe;
import com.example.rewear.Gestionevent.Service.ParticipeDAO;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.time.LocalDate;

public class UpdateParticipe {
    @FXML private Label labelNomEvent;
    @FXML private Label labelLieu;
    @FXML private Label labelPeriode;
    @FXML private Label labelPlacesRestantes;
    @FXML private DatePicker datePickerParticipation;
    @FXML private Label labelMessage;

    private Participe participationActuelle;
    private ParticipeDAO participationDAO;
    private long eventId;
    private long userId;

    public void initialiserDonnees(Participe participation, String nomEvent, String lieu, String periode, int placesRestantes) {
        this.participationActuelle = participation;
        this.eventId = participation.getId_event_id();
        this.userId = participation.getUser_id();

        participationDAO = new ParticipeDAO();

        // Initialisation des champs
        labelNomEvent.setText(nomEvent);
        labelLieu.setText(lieu);
        labelPeriode.setText(periode);
        labelPlacesRestantes.setText(String.valueOf(placesRestantes));

        // Définir la date actuelle de participation
        datePickerParticipation.setValue(participation.getDateParticipation());
    }

    @FXML
    private void confirmerModification() {
        labelMessage.setText("");
        LocalDate nouvelleDateParticipation = datePickerParticipation.getValue();

        if (nouvelleDateParticipation == null) {
            labelMessage.setText("Veuillez sélectionner une date");
            return;
        }

        // Mettre à jour l'objet participation
        participationActuelle.setDateParticipation(nouvelleDateParticipation);

        // Appeler la méthode de modification
        boolean modificationReussie = participationDAO.modifier(participationActuelle);

        if (modificationReussie) {
            // Fermer la fenêtre
            ((Stage) datePickerParticipation.getScene().getWindow()).close();
        } else {
            labelMessage.setText("Erreur lors de la modification de la participation");
        }
    }

    @FXML
    private void annuler() {
        // Fermer la fenêtre
        ((Stage) datePickerParticipation.getScene().getWindow()).close();
    }
}
