package com.example.rewear.Gestionevent.Controller;
import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.Gestionevent.Service.EventDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.LocalDate;
public class UpdateEvent {
    @FXML private TextField titreField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private TextField lieuField;
    @FXML private TextField statutField;
    @FXML private TextField categorieField;
    @FXML private Button saveButton;

    private EventDAO eventDAO;
    private Event eventToEdit;

    public void setEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public void setEventToEdit(Event event) {
        this.eventToEdit = event;

        // Pré-remplir les champs
        titreField.setText(event.getTitre());
        dateDebutPicker.setValue(event.getDateDebut());
        dateFinPicker.setValue(event.getDateFin());
        lieuField.setText(event.getLieu());
        statutField.setText(event.getStatut());
        categorieField.setText(event.getCategorie());
    }

    @FXML
    private void handleSave() {
        try {
            eventToEdit.setTitre(titreField.getText());
            eventToEdit.setDateDebut(dateDebutPicker.getValue());
            eventToEdit.setDateFin(dateFinPicker.getValue());
            eventToEdit.setLieu(lieuField.getText());
            eventToEdit.setStatut(statutField.getText());
            eventToEdit.setCategorie(categorieField.getText());

            eventDAO.updateEvent(eventToEdit);

            closeWindow();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour : " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
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
