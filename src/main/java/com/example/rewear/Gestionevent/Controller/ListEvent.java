package com.example.rewear.Gestionevent.Controller;
import com.example.rewear.DBUtil;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.net.URL;

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
    private TableColumn<Event, Void> actionsColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button btnMesParticipations;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();
    private EventDAO eventDAO;
    private ParticipeDAO participeDAO;
    private Long currentUserId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialiser la connexion à la base de données et les DAOs
            Connection connection = DBUtil.getConnection();
            eventDAO = new EventDAO(connection);
            participeDAO = new ParticipeDAO(connection);

            // Pour les besoins de démonstration, définir un ID utilisateur par défaut
            currentUserId = 5L;

            // Configurer les colonnes de la TableView
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
            lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
            statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
            categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));

            // Configurer la colonne d'actions avec un bouton "Participer"
            setupActionsColumn();

            // Charger les événements
            loadEvents();

            // Configuration du bouton "Mes Participations"
            if (btnMesParticipations != null) {
                btnMesParticipations.setOnAction(event -> afficherMesParticipations());
            }

            System.out.println("Initialisation terminée");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion",
                    "Impossible de se connecter à la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configure la colonne d'actions avec un bouton "Participer"
     */
    private void setupActionsColumn() {
        // Vérifier si la colonne d'actions existe
        if (actionsColumn == null) {
            // Créer la colonne d'actions si elle n'existe pas
            actionsColumn = new TableColumn<>("Actions");
            eventTable.getColumns().add(actionsColumn);
        }

        actionsColumn.setCellFactory(new Callback<TableColumn<Event, Void>, TableCell<Event, Void>>() {
            @Override
            public TableCell<Event, Void> call(final TableColumn<Event, Void> param) {
                return new TableCell<Event, Void>() {
                    private final Button participerButton = new Button("Participer");

                    {
                        participerButton.setOnAction(event -> {
                            Event eventData = getTableView().getItems().get(getIndex());
                            ouvrirFormulaireParticipation(eventData);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Récupérer l'événement de cette ligne
                            Event event = getTableView().getItems().get(getIndex());

                            // Vérifier s'il reste des places disponibles
                            try {
                                int placesRestantes = participeDAO.obtenirPlacesRestantes((long) event.getId());

                                // Vérifier si l'utilisateur participe déjà à cet événement
                                boolean dejaInscrit = participeDAO.utilisateurParticipeDejaAEvenement(
                                        (long) event.getId(), currentUserId);

                                // Configurer le bouton en fonction de l'état
                                if (dejaInscrit) {
                                    participerButton.setText("Inscrit");
                                    participerButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white;");
                                    participerButton.setDisable(true);
                                } else if (placesRestantes <= 0) {
                                    participerButton.setText("Complet");
                                    participerButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                                    participerButton.setDisable(true);
                                } else {
                                    participerButton.setText("Participer");
                                    participerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                                    participerButton.setDisable(false);
                                }

                                setGraphic(participerButton);
                            } catch (Exception  e) {
                                e.printStackTrace();
                                setGraphic(participerButton);
                            }
                        }
                    }
                };
            }
        });
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
     * Ouvre le formulaire de participation pour un événement
     */
    private void ouvrirFormulaireParticipation(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/AjoutParticipe.fxml"));
            Parent root = loader.load();

            // Configurer le contrôleur
            AjoutParticipe controller = loader.getController();
            controller.setEvent(event);
            controller.setUserId(currentUserId);
            controller.initialiser();

            // Créer et afficher la fenêtre modale
            Stage stage = new Stage();
            stage.setTitle("Participer à l'événement: " + event.getTitre());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(eventTable.getScene().getWindow());
            stage.showAndWait();

            // Recharger la liste après participation
            loadEvents();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ouvrir le formulaire de participation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche les participations de l'utilisateur actuel
     */
    private void afficherMesParticipations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/MesParticipations.fxml"));
            Parent root = loader.load();

            // Configurer le contrôleur
            // MesParticipationsController controller = loader.getController();
            // controller.setUserId(currentUserId);
            // controller.setParticipeDAO(participeDAO);
            // controller.setEventDAO(eventDAO);
            // controller.loadParticipations();

            // Créer et afficher la fenêtre modale
            Stage stage = new Stage();
            stage.setTitle("Mes Participations");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(eventTable.getScene().getWindow());
            stage.showAndWait();

            // Recharger la liste après annulation éventuelle
            loadEvents();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ouvrir la liste des participations: " + e.getMessage());
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

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/DeleteEvent.fxml"));
            Parent root = loader.load();

            // Obtenez le contrôleur de la fenêtre de suppression
            DeleteEvent deleteEventController = loader.getController();

            // Passez l'événement à supprimer
            Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                deleteEventController.setEventToDelete(selectedEvent);

                // Affichez la fenêtre de suppression
                Stage stage = new Stage();
                stage.setTitle("Supprimer un événement");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(deleteButton.getScene().getWindow());
                stage.showAndWait();

                // Rechargez les événements après la suppression
                loadEvents();
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de suppression.");
        }
    }
    @FXML
    private void handleAddParticipant(ActionEvent event) {
        // Récupérer l'événement sélectionné
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Vous pouvez appeler votre méthode existante
            ouvrirFormulaireParticipation(selectedEvent);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné",
                    "Veuillez sélectionner un événement avant de participer.");
        }
    }
    /**
     * Gère l'action du bouton "Voir mes participations"
     */
    @FXML
    private void handleViewParticipations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/ListParticipe .fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et lui passer l'ID de l'utilisateur connecté
            ListParticipe controller = loader.getController();
            controller.setUserId(this.currentUserId); // Corrigé: Utilisation de currentUserId au lieu de userId

            Stage stage = new Stage();
            stage.setTitle("Mes Participations");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Rend la fenêtre modale
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ouvrir la liste des participations: " + e.getMessage());
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