package com.example.rewear.Gestionevent.Controller;
import com.example.rewear.DBUtil;
import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.Gestionevent.Service.EventDAO;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ListEvent implements Initializable {
    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, Integer> idColumn;

    @FXML
    private TableColumn<Event, String> titreColumn;

    @FXML
    private TableColumn<Event, LocalDate> dateDebutColumn;

    @FXML
    private TableColumn<Event, LocalDate> dateFinColumn;

    @FXML
    private TableColumn<Event, String> lieuColumn;

    @FXML
    private TableColumn<Event, String> statutColumn;

    @FXML
    private TableColumn<Event, String> categorieColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();
    private EventDAO eventDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialiser la connexion à la base de données et le DAO
            Connection connection = DBUtil.getConnection();
            eventDAO = new EventDAO(connection);

            // Configurer les colonnes de la TableView
            // Utilisation des noms de propriétés en snake_case pour correspondre à la base de données
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
            lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
            statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
            categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));

            // Charger les événements
            loadEvents();

            System.out.println("Initialisation terminée");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion",
                    "Impossible de se connecter à la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge tous les événements depuis la base de données
     */
    private void loadEvents() {
        try {
            System.out.println("Début du chargement des événements");
            List<Event> events = eventDAO.getAllEvents();
            System.out.println("Événements récupérés: " + events.size());

            if (events.isEmpty()) {
                System.out.println("Aucun événement trouvé");
            } else {
                System.out.println("Premier événement: " + events.get(0));
            }

            eventList.clear();
            eventList.addAll(events);
            eventTable.setItems(eventList);
            System.out.println("Événements chargés dans la TableView");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement",
                    "Impossible de charger les événements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gère le bouton Actualiser
     */
    @FXML
    void handleRefresh(ActionEvent event) {
        loadEvents();
    }

    /**
     * Gère le bouton Ajouter
     */
    @FXML
    void handleAdd(ActionEvent event) {
        try {
            // Charger l'interface d'ajout d'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/AjoutEvent.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Ajouter un événement");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addButton.getScene().getWindow());

            // Afficher la fenêtre et attendre qu'elle soit fermée
            stage.showAndWait();

            // Recharger les événements après la fermeture de la fenêtre
            loadEvents();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ouvrir la fenêtre d'ajout: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Gérer l'action du bouton Modifier
    @FXML
    private void handleEdit(ActionEvent event) {
        // Récupérer l'événement sélectionné dans la TableView
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        // Vérifier si un événement a été sélectionné
        if (selectedEvent != null) {
            try {
                // Charger le formulaire de modification
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/UpdateEvent.fxml"));
                Parent root = loader.load();

                // Créer une nouvelle scène pour la modification de l'événement
                Stage stage = new Stage();
                stage.setTitle("Modifier un événement");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(editButton.getScene().getWindow());

                // Passer l'événement sélectionné au contrôleur de la fenêtre ModifierEvent
                 UpdateEvent controller = loader.getController();
                controller.setEventDAO(eventDAO);
                controller.setEventToEdit(selectedEvent);  // Passer l'événement sélectionné pour modification

                // Afficher la fenêtre de modification et attendre qu'elle soit fermée
                stage.showAndWait();

                // Recharger les événements après la modification
                loadEvents();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Si aucun événement n'est sélectionné, afficher une alerte
            showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
        }
    }

    /**
     * Affiche une alerte
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}