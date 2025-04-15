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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ListEvent implements Initializable {
    @FXML
    private FlowPane eventCardsContainer;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button refreshButton;

    @FXML
    private Button addButton;

    @FXML
    private Button viewParticipationsButton;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();
    private EventDAO eventDAO;
    private ParticipeDAO participeDAO;
    private Long currentUserId;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialiser la connexion à la base de données et les DAOs
            Connection connection = DBUtil.getConnection();
            eventDAO = new EventDAO(connection);
            participeDAO = new ParticipeDAO(connection);

            // Pour les besoins de démonstration, définir un ID utilisateur par défaut
            currentUserId = 5L;

            // Charger les événements
            loadEvents();

            // Tentative de chargement du logo
            try {
                Image logoImage = new Image(getClass().getResourceAsStream("/com/example/rewear/logo.png"));
                if (logoImageView != null) {
                    logoImageView.setImage(logoImage);
                }
            } catch (Exception e) {
                System.err.println("Impossible de charger le logo: " + e.getMessage());
            }

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

            // Effacer le conteneur de cartes
            eventCardsContainer.getChildren().clear();

            // Créer et ajouter une carte pour chaque événement
            for (Event event : eventList) {
                VBox eventCard = createEventCard(event);
                eventCardsContainer.getChildren().add(eventCard);
            }

            System.out.println("Événements chargés dans les cartes");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement",
                    "Impossible de charger les événements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Crée une carte pour afficher un événement
     */
    private VBox createEventCard(Event event) {
        // Création de la carte principale
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        card.setPrefWidth(280);
        card.setPrefHeight(280);
        card.setSpacing(8);
        card.setPadding(new Insets(15));

        // Titre de l'événement
        Label titleLabel = new Label(event.getTitre());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #808000;");
        titleLabel.setWrapText(true);

        // Informations de l'événement
        Label dateLabel = new Label("Date: " + formatDate(event.getDateDebut()) + " - " + formatDate(event.getDateFin()));
        dateLabel.setStyle("-fx-font-size: 12px;");

        Label lieuLabel = new Label("Lieu: " + event.getLieu());
        lieuLabel.setStyle("-fx-font-size: 12px;");

        Label categorieLabel = new Label("Catégorie: " + event.getCategorie());
        categorieLabel.setStyle("-fx-font-size: 12px;");

        Label statutLabel = new Label("Statut: " + event.getStatut());
        statutLabel.setStyle("-fx-font-size: 12px;");

        // Spacer pour pousser les boutons vers le bas
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Informations sur les places disponibles
        Label placesLabel = new Label("");
        try {
            int placesRestantes = participeDAO.obtenirPlacesRestantes((long) event.getId());
            placesLabel.setText("Places disponibles: " + placesRestantes);
            placesLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des places: " + e.getMessage());
        }

        // Boutons d'action
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        // Bouton Participer
        Button participerButton = new Button("Participer");
        participerButton.setPrefWidth(110);
        participerButton.setStyle("-fx-background-color: #6a11cb; -fx-text-fill: white; -fx-background-radius: 20;");

        // Vérifier si l'utilisateur participe déjà et s'il reste des places
        try {
            int placesRestantes = participeDAO.obtenirPlacesRestantes((long) event.getId());
            boolean dejaInscrit = participeDAO.utilisateurParticipeDejaAEvenement(
                    (long) event.getId(), currentUserId);

            if (dejaInscrit) {
                participerButton.setText("Inscrit");
                participerButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white; -fx-background-radius: 20;");
                participerButton.setDisable(true);
            } else if (placesRestantes <= 0) {
                participerButton.setText("Complet");
                participerButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 20;");
                participerButton.setDisable(true);
            } else {
                participerButton.setText("Participer");
                participerButton.setStyle("-fx-background-color: #6a11cb; -fx-text-fill: white; -fx-background-radius: 20;");
                participerButton.setDisable(false);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification des inscriptions: " + e.getMessage());
        }

        participerButton.setOnAction(e -> ouvrirFormulaireParticipation(event));

        // Bouton Modifier
        Button detailsButton = new Button("Modifier");
        detailsButton.setPrefWidth(80);
        detailsButton.setStyle("-fx-background-color: #2575fc; -fx-text-fill: white; -fx-background-radius: 20;");
        detailsButton.setOnAction(e -> handleEventDetails(event));

        // Bouton Supprimer
        Button deleteButton = new Button("Supprimer");
        deleteButton.setPrefWidth(80);
        deleteButton.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white; -fx-background-radius: 20;");
        deleteButton.setOnAction(e -> handleDeleteEvent(event));

        buttonBox.getChildren().addAll(participerButton, detailsButton, deleteButton);

        // Ajouter tous les éléments à la carte
        card.getChildren().addAll(
                titleLabel,
                dateLabel,
                lieuLabel,
                categorieLabel,
                statutLabel,
                placesLabel,
                spacer,
                buttonBox
        );

        return card;
    }

    /**
     * Formatage des dates
     */
    private String formatDate(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        return date.format(dateFormatter);
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
            stage.initOwner(eventCardsContainer.getScene().getWindow());
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
     * Affiche les détails d'un événement et permet de le modifier
     */
    private void handleEventDetails(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/UpdateEvent.fxml"));
            Parent root = loader.load();

            UpdateEvent controller = loader.getController();
            controller.setEventDAO(eventDAO);
            controller.setEventToEdit(event);

            Stage stage = new Stage();
            stage.setTitle("Détails de l'événement: " + event.getTitre());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(eventCardsContainer.getScene().getWindow());
            stage.showAndWait();

            // Recharger les événements après modification
            loadEvents();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ouvrir les détails de l'événement: " + e.getMessage());
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

    /**
     * Gère la suppression d'un événement
     */
    private void handleDeleteEvent(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/Gestionevent/DeleteEvent.fxml"));
            Parent root = loader.load();

            // Obtenez le contrôleur de la fenêtre de suppression
            DeleteEvent deleteEventController = loader.getController();

            // Passez l'événement à supprimer
            if (event != null) {
                deleteEventController.setEventToDelete(event);

                // Affichez la fenêtre de suppression
                Stage stage = new Stage();
                stage.setTitle("Supprimer un événement");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(eventCardsContainer.getScene().getWindow());
                stage.showAndWait();

                // Rechargez les événements après la suppression
                loadEvents();
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Impossible de supprimer cet événement.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de suppression.");
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
            controller.setUserId(this.currentUserId);

            Stage stage = new Stage();
            stage.setTitle("Mes Participations");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
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