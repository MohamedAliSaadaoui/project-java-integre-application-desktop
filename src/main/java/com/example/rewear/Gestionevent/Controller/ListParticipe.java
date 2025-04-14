package com.example.rewear.Gestionevent.Controller;

import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.Gestionevent.Entity.Participe;
import com.example.rewear.Gestionevent.Service.EventDAO;
import com.example.rewear.Gestionevent.Service.ParticipeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListParticipe implements Initializable {
    @FXML
    private TableView<Participe> participationsTable;

    @FXML
    private TableColumn<Participe, Long> idColumn;

    @FXML
    private TableColumn<Participe, Long> eventIdColumn;

    @FXML
    private TableColumn<Participe, LocalDate> dateParticipationColumn;

    @FXML
    private TableColumn<Participe, String> nomEventColumn;

    @FXML
    private TableColumn<Participe, String> lieuColumn;

    @FXML
    private TableColumn<Participe, String> periodeColumn;

    @FXML
    private TableColumn<Participe, Void> actionsColumn;

    @FXML
    private Button btnRetour;

    private Long userId;
    private ParticipeDAO participeDAO;
    private EventDAO eventDAO;
    private ObservableList<Participe> participationsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation de base
        participeDAO = new ParticipeDAO();
        eventDAO = new EventDAO();

        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("id_event_id"));
        dateParticipationColumn.setCellValueFactory(new PropertyValueFactory<>("dateParticipation"));

        // Configuration de la colonne nom d'événement (nécessite une conversion)
        nomEventColumn.setCellValueFactory(cellData -> {
            Long eventId = cellData.getValue().getId_event_id();
            Event event = eventDAO.getEventById(eventId);
            return event != null ? new javafx.beans.property.SimpleStringProperty(event.getTitre())
                    : new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Configuration de la colonne lieu
        lieuColumn.setCellValueFactory(cellData -> {
            Long eventId = cellData.getValue().getId_event_id();
            Event event = eventDAO.getEventById(eventId);
            return event != null ? new javafx.beans.property.SimpleStringProperty(event.getLieu())
                    : new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Configuration de la colonne période
        periodeColumn.setCellValueFactory(cellData -> {
            Long eventId = cellData.getValue().getId_event_id();
            Event event = eventDAO.getEventById(eventId);
            if (event != null) {
                String periode = event.getDateDebut().toString() + " au " + event.getDateFin().toString();
                return new javafx.beans.property.SimpleStringProperty(periode);
            } else {
                return new javafx.beans.property.SimpleStringProperty("N/A");
            }
        });

        // Configuration de la colonne d'actions
        setupActionsColumn();
    }

    /**
     * Configure la colonne d'actions avec les boutons Modifier et Annuler
     */
    private void setupActionsColumn() {
        actionsColumn.setCellFactory(new Callback<TableColumn<Participe, Void>, TableCell<Participe, Void>>() {
            @Override
            public TableCell<Participe, Void> call(final TableColumn<Participe, Void> param) {
                return new TableCell<Participe, Void>() {
                    private final Button modifierBtn = new Button("Modifier");
                    private final Button annulerBtn = new Button("Annuler");
                    private final HBox pane = new HBox(5, modifierBtn, annulerBtn);

                    {
                        // Style des boutons
                        modifierBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        annulerBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                        // Action du bouton Modifier
                        modifierBtn.setOnAction(event -> {
                            Participe participe = getTableView().getItems().get(getIndex());
                            Event evenement = eventDAO.getEventById(participe.getId_event_id());
                            if (evenement != null) {
                                ouvrirFormulaireModification(participe, evenement);
                            } else {
                                showAlert(Alert.AlertType.ERROR, "Erreur",
                                        "Impossible de trouver l'événement associé à cette participation.");
                            }
                        });

                        // Action du bouton Annuler
                        annulerBtn.setOnAction(event -> {
                            Participe participe = getTableView().getItems().get(getIndex());
                            confirmerAnnulation(participe);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        });
    }

    /**
     * Initialise le contrôleur avec l'ID de l'utilisateur
     */
    public void setUserId(Long userId) {
        this.userId = userId;
        chargerParticipations();
    }

    /**
     * Charge les participations de l'utilisateur
     */
    public void chargerParticipations() {
        if (userId == null) return;

        try {
            List<Participe> participations = participeDAO.obtenirParUserId(userId);
            participationsList.clear();
            participationsList.addAll(participations);
            participationsTable.setItems(participationsList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger les participations: " + e.getMessage());
        }
    }

    /**
     * Ouvre le formulaire de modification d'une participation
     */
    public void ouvrirFormulaireModification(Participe participation, Event evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/UpdadteParticipe.fxml"));
            Parent root = loader.load();

            UpdateParticipe controller = loader.getController();

            // Calculer la période à afficher
            String periode = evenement.getDateDebut().toString() + " au " + evenement.getDateFin().toString();

            // Obtenir le nombre de places restantes
            int placesRestantes = participeDAO.obtenirPlacesRestantes(Long.valueOf(evenement.getId()));

            // Initialiser les données du formulaire
            controller.initialiserDonnees(
                    participation,
                    evenement.getTitre(),
                    evenement.getLieu(),
                    periode,
                    placesRestantes
            );

            Stage stage = new Stage();
            stage.setTitle("Modifier la participation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Rafraîchir la liste des participations après modification
            chargerParticipations();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de l'ouverture du formulaire de modification: " + e.getMessage());
        }
    }

    /**
     * Demande confirmation avant d'annuler une participation
     */
    private void confirmerAnnulation(Participe participation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Annuler la participation");
        alert.setContentText("Êtes-vous sûr de vouloir annuler votre participation à cet événement ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = participeDAO.supprimerParticipation(
                    participation.getId_event_id(), participation.getUser_id());

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès",
                        "Votre participation a été annulée avec succès.");
                chargerParticipations();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Impossible d'annuler votre participation. Veuillez réessayer.");
            }
        }
    }

    /**
     * Gère le bouton de retour
     */
    @FXML
    private void handleRetour(ActionEvent event) {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
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