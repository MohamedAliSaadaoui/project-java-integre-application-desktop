package com.example.rewear.Gestionevent.Controller;
import com.example.rewear.DBUtil;
import com.example.rewear.Gestionevent.Service.ParticipeDAO;
import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.Gestionevent.Entity.Participe;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AjoutParticipe {
    @FXML
    private Label labelTitre;

    @FXML
    private Label labelNomEvent;

    @FXML
    private Label labelDescription;

    @FXML
    private Label labelLieu;

    @FXML
    private Label labelPeriode;

    @FXML
    private Label labelPlacesRestantes;

    @FXML
    private DatePicker datePickerParticipation;

    @FXML
    private Label labelMessage;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnConfirmer;

    private Event event;
    private Long userId;
    private ParticipeDAO participeDAO;

    // Format de date pour l'affichage
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AjoutParticipe() {
        try {
            Connection connection = DBUtil.getConnection();
            participeDAO = new ParticipeDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void initialiser() {
        if (event == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun événement sélectionné.");
            return;
        }

        // Mise à jour du titre
        labelTitre.setText("Participer à l'événement : " + event.getTitre());

        // Remplissage des informations de l'événement
        labelNomEvent.setText(event.getTitre());
        // S'assurer que la description est définie dans l'objet Event
        labelLieu.setText(event.getLieu());
        labelPeriode.setText(
                event.getDateDebut().format(dateFormatter) + " au " +
                        event.getDateFin().format(dateFormatter)
        );

        try {
            // Mise à jour du nombre de places restantes
            int placesRestantes = participeDAO.obtenirPlacesRestantes((long) event.getId());
            labelPlacesRestantes.setText(String.valueOf(placesRestantes));

            // Configuration du date picker
            datePickerParticipation.setValue(LocalDate.now());

            // Limiter la sélection de date à la période de l'événement
            datePickerParticipation.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (empty || date == null) {
                        return;
                    }

                    // Désactiver les dates en dehors de la période de l'événement
                    if (date.isBefore(event.getDateDebut()) || date.isAfter(event.getDateFin())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;"); // Fond rose pour les dates désactivées
                    }
                }
            });

            // Définir la date par défaut à la date de début de l'événement
            datePickerParticipation.setValue(event.getDateDebut());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void confirmerParticipation() {
        if (event == null || userId == null) {
            labelMessage.setText("Données manquantes. Impossible de créer la participation.");
            return;
        }

        LocalDate dateParticipation = datePickerParticipation.getValue();
        if (dateParticipation == null) {
            labelMessage.setText("Veuillez sélectionner une date de participation.");
            return;
        }

        // Vérifier si la date est valide
        if (dateParticipation.isBefore(event.getDateDebut()) || dateParticipation.isAfter(event.getDateFin())) {
            labelMessage.setText("La date sélectionnée n'est pas dans la période de l'événement.");
            return;
        }

        try {
            // Créer l'objet participation
            Participe participation = new Participe();
            participation.setId_event_id((long) event.getId());
            participation.setUser_id(userId);
            participation.setDateParticipation(dateParticipation);

            // Ajouter la participation via le DAO
            boolean success = participeDAO.ajouter(participation);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre participation a bien été enregistrée.");
                // Fermer la fenêtre
                fermerFenetre();
            } else {
                labelMessage.setText("Erreur lors de l'enregistrement de la participation.");
            }
        } catch (Exception e) {
            labelMessage.setText("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
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