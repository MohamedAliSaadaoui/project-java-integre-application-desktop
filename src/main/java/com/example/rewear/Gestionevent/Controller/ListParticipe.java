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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListParticipe implements Initializable {
    @FXML
    private FlowPane participationsCardsContainer;

    @FXML
    private Button btnRetour;

    @FXML
    private Button refreshButton;

    @FXML
    private ImageView logoImageView;

    private Long userId;
    private ParticipeDAO participeDAO;
    private EventDAO eventDAO;
    private ObservableList<Participe> participationsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des DAO
        participeDAO = new ParticipeDAO();
        eventDAO = new EventDAO();

        // Chargement du logo
        try {
            Image logo = new Image(getClass().getResourceAsStream("/com/example/rewear/images/logo.png"));
            logoImageView.setImage(logo);
        } catch (Exception e) {
            System.err.println("Impossible de charger le logo: " + e.getMessage());
        }
    }

    /**
     * Initialise le contrôleur avec l'ID de l'utilisateur
     */
    public void setUserId(Long userId) {
        this.userId = userId;
        chargerParticipations();
    }

    /**
     * Charge les participations de l'utilisateur et les affiche sous forme de cartes
     */
    public void chargerParticipations() {
        if (userId == null) return;

        try {
            List<Participe> participations = participeDAO.obtenirParUserId(userId);
            participationsList.clear();
            participationsList.addAll(participations);

            // Vider le conteneur avant d'ajouter les nouvelles cartes
            participationsCardsContainer.getChildren().clear();

            // Créer une carte pour chaque participation
            for (Participe participation : participationsList) {
                // Récupérer l'événement associé
                Event event = eventDAO.getEventById(participation.getId_event_id());

                if (event != null) {
                    // Créer une carte
                    VBox card = createParticipationCard(participation, event);
                    participationsCardsContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger les participations: " + e.getMessage());
        }
    }

    /**
     * Crée une carte pour une participation avec un titre mis en évidence
     */
    private VBox createParticipationCard(Participe participation, Event event) {
        // Création de la carte (VBox)
        VBox card = new VBox();
        card.getStyleClass().add("participation-card");
        card.setStyle("-fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        card.setPrefWidth(250);
        card.setPrefHeight(220); // Légèrement plus haut pour accommoder le titre
        card.setSpacing(8);

        // Création d'un conteneur pour le titre avec fond coloré
        VBox titleContainer = new VBox();
        titleContainer.setStyle("-fx-background-color: #6a11cb; -fx-background-radius: 15 15 0 0; -fx-padding: 10;");
        titleContainer.setPrefWidth(250);
        titleContainer.setAlignment(Pos.CENTER);

        // Création du label pour le titre
        Label nomEventLabel = new Label(event.getTitre());
        nomEventLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-wrap-text: true;");
        nomEventLabel.setAlignment(Pos.CENTER);
        nomEventLabel.setWrapText(true);
        titleContainer.getChildren().add(nomEventLabel);

        // Conteneur pour le contenu de la carte
        VBox contentContainer = new VBox();
        contentContainer.setPadding(new Insets(15, 15, 15, 15));
        contentContainer.setSpacing(8);

        // Création des labels pour les informations
        Label lieuLabel = new Label("Lieu: " + event.getLieu());
        lieuLabel.setStyle("-fx-font-size: 12px;");

        String periode = event.getDateDebut().toString() + " au " + event.getDateFin().toString();
        Label periodeLabel = new Label("Période: " + periode);
        periodeLabel.setStyle("-fx-font-size: 12px;");

        Label dateInscriptionLabel = new Label("Inscrit le: " + participation.getDateParticipation());
        dateInscriptionLabel.setStyle("-fx-font-size: 12px;");

        // Création des boutons
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        VBox.setVgrow(buttonBox, Priority.ALWAYS);

        Button modifierBtn = new Button("Modifier");
        modifierBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 20;");
        modifierBtn.setPrefWidth(100);
        modifierBtn.setOnAction(event1 -> ouvrirFormulaireModification(participation, event));

        Button annulerBtn = new Button("Annuler");
        annulerBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20;");
        annulerBtn.setPrefWidth(100);
        annulerBtn.setOnAction(event1 -> confirmerAnnulation(participation));

        buttonBox.getChildren().addAll(modifierBtn, annulerBtn);

        // Assemblage du contenu de la carte
        contentContainer.getChildren().addAll(lieuLabel, periodeLabel, dateInscriptionLabel, buttonBox);

        // Assemblage de la carte complète
        card.getChildren().addAll(titleContainer, contentContainer);

        return card;
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
     * Gère le bouton d'actualisation
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        chargerParticipations();
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