package gui;
import entities.User;
import services.UserService;

import entities.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.UserService;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserManagementFX extends Application{
    private UserService userService;
    private ObservableList<User> userList;
    private TableView<User> userTable;

    // Champs du formulaire
    private TextField txtId, txtUsername, txtPassword, txtEmail, txtNumTel, txtDateNaiss, txtAdresse, txtRecherche;
    private Button btnAjouter, btnModifier, btnSupprimer, btnRechercher, btnReset;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void start(Stage primaryStage) {
        userService = new UserService();

        // Création de la scène principale
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Création du formulaire
        GridPane formPane = createFormPane();
        root.setTop(formPane);

        // Création du tableau
        createTable();
        root.setCenter(userTable);

        // Boutons d'action
        HBox buttonBox = createButtonBox();
        root.setBottom(buttonBox);

        // Chargement initial des données
        loadUsers();

        // Création de la scène
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Gestion des Utilisateurs");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createFormPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Ligne 0
        grid.add(new Label("ID:"), 0, 0);
        txtId = new TextField();
        txtId.setEditable(false);
        grid.add(txtId, 1, 0);

        grid.add(new Label("Nom d'utilisateur:"), 2, 0);
        txtUsername = new TextField();
        grid.add(txtUsername, 3, 0);

        // Ligne 1
        grid.add(new Label("Mot de passe:"), 0, 1);
        txtPassword = new TextField();
        grid.add(txtPassword, 1, 1);

        grid.add(new Label("Email:"), 2, 1);
        txtEmail = new TextField();
        grid.add(txtEmail, 3, 1);

        // Ligne 2
        grid.add(new Label("Téléphone:"), 0, 2);
        txtNumTel = new TextField();
        grid.add(txtNumTel, 1, 2);

        grid.add(new Label("Date (YYYY-MM-DD):"), 2, 2);
        txtDateNaiss = new TextField();
        grid.add(txtDateNaiss, 3, 2);

        // Ligne 3
        grid.add(new Label("Adresse:"), 0, 3);
        txtAdresse = new TextField();
        grid.add(txtAdresse, 1, 3);

        grid.add(new Label("Recherche:"), 2, 3);
        txtRecherche = new TextField();
        grid.add(txtRecherche, 3, 3);

        return grid;
    }

    private void createTable() {
        userTable = new TableView<>();
        userList = FXCollections.observableArrayList();
        userTable.setItems(userList);

        // Définition des colonnes
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Nom d'utilisateur");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> telCol = new TableColumn<>("Téléphone");
        telCol.setCellValueFactory(new PropertyValueFactory<>("num_tel"));

        TableColumn<User, String> adresseCol = new TableColumn<>("Adresse");
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        userTable.getColumns().addAll(idCol, usernameCol, emailCol, telCol, adresseCol);

        // Gestion de la sélection
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectUser(newSelection);
            }
        });
    }

    private HBox createButtonBox() {
        HBox box = new HBox(10);
        box.setPadding(new Insets(10));

        btnAjouter = new Button("Ajouter");
        btnModifier = new Button("Modifier");
        btnSupprimer = new Button("Supprimer");
        btnRechercher = new Button("Rechercher");
        btnReset = new Button("Réinitialiser");

        // Configuration des événements
        btnAjouter.setOnAction(e -> addUser());
        btnModifier.setOnAction(e -> updateUser());
        btnSupprimer.setOnAction(e -> deleteUser());
        btnRechercher.setOnAction(e -> searchUsers());
        btnReset.setOnAction(e -> {
            resetForm();
            loadUsers();
        });

        box.getChildren().addAll(btnAjouter, btnModifier, btnSupprimer, btnRechercher, btnReset);

        return box;
    }

    private void loadUsers() {
        try {
            userList.clear();
            userList.addAll(userService.recuperer());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des utilisateurs", e.getMessage());
        }
    }

    private void selectUser(User user) {
        txtId.setText(String.valueOf(user.getId()));
        txtUsername.setText(user.getUsername());
        txtPassword.setText(user.getPassword());
        txtEmail.setText(user.getEmail());
        txtNumTel.setText(user.getNum_tel());

        if (user.getDate_naiss() != null) {
            txtDateNaiss.setText(dateFormat.format(user.getDate_naiss()));
        } else {
            txtDateNaiss.setText("");
        }

        txtAdresse.setText(user.getAdresse());
    }

    private void addUser() {
        // Vérifier si les champs obligatoires sont remplis
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants",
                    "Veuillez remplir tous les champs obligatoires",
                    "Les champs Nom d'utilisateur, Mot de passe et Email sont obligatoires.");
            return;
        }

        try {
            // Créer un nouvel utilisateur
            User user = new User();
            user.setUsername(txtUsername.getText());
            user.setPassword(txtPassword.getText());
            user.setEmail(txtEmail.getText());
            user.setNum_tel(txtNumTel.getText());

            // Traitement de la date
            if (!txtDateNaiss.getText().isEmpty()) {
                try {
                    Date date = dateFormat.parse(txtDateNaiss.getText());
                    user.setDate_naiss(date);
                } catch (ParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de format",
                            "Format de date invalide",
                            "Veuillez utiliser le format YYYY-MM-DD pour la date.");
                    return;
                }
            }

            user.setAdresse(txtAdresse.getText());

            // Ajouter l'utilisateur via le service
            userService.ajouter(user);

            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Utilisateur ajouté",
                    "L'utilisateur a été ajouté avec succès.");
            resetForm();
            loadUsers();
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de l'ajout de l'utilisateur",
                    ex.getMessage());
        }
    }

    private void updateUser() {
        // Vérifier si un utilisateur est sélectionné
        if (txtId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection",
                    "Aucun utilisateur sélectionné",
                    "Veuillez sélectionner un utilisateur à modifier.");
            return;
        }

        // Vérifier si les champs obligatoires sont remplis
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants",
                    "Veuillez remplir tous les champs obligatoires",
                    "Les champs Nom d'utilisateur, Mot de passe et Email sont obligatoires.");
            return;
        }

        try {
            // Créer un utilisateur avec les modifications
            User user = new User();
            user.setId(Integer.parseInt(txtId.getText()));
            user.setUsername(txtUsername.getText());
            user.setPassword(txtPassword.getText());
            user.setEmail(txtEmail.getText());
            user.setNum_tel(txtNumTel.getText());

            // Traitement de la date
            if (!txtDateNaiss.getText().isEmpty()) {
                try {
                    Date date = dateFormat.parse(txtDateNaiss.getText());
                    user.setDate_naiss(date);
                } catch (ParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de format",
                            "Format de date invalide",
                            "Veuillez utiliser le format YYYY-MM-DD pour la date.");
                    return;
                }
            }

            user.setAdresse(txtAdresse.getText());

            // Mettre à jour l'utilisateur via le service
            userService.modifier(user);

            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Utilisateur modifié",
                    "L'utilisateur a été modifié avec succès.");
            resetForm();
            loadUsers();
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la modification de l'utilisateur",
                    ex.getMessage());
        }
    }

    private void deleteUser() {
        // Vérifier si un utilisateur est sélectionné
        if (txtId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection",
                    "Aucun utilisateur sélectionné",
                    "Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }

        // Demander confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
        confirmation.setContentText("Cette action est irréversible.");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                // Créer un utilisateur avec l'ID à supprimer
                User user = new User();
                user.setId(Integer.parseInt(txtId.getText()));

                // Supprimer l'utilisateur via le service
                userService.supprimer(user);

                showAlert(Alert.AlertType.INFORMATION, "Succès",
                        "Utilisateur supprimé",
                        "L'utilisateur a été supprimé avec succès.");
                resetForm();
                loadUsers();
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Erreur lors de la suppression de l'utilisateur",
                        ex.getMessage());
            }
        }
    }

    private void searchUsers() {
        String keyword = txtRecherche.getText().trim();
        if (keyword.isEmpty()) {
            loadUsers();
            return;
        }

        try {
            userList.clear();
            userList.addAll(userService.searchUsers(keyword));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la recherche",
                    e.getMessage());
        }
    }

    private void resetForm() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        txtNumTel.setText("");
        txtDateNaiss.setText("");
        txtAdresse.setText("");
        txtRecherche.setText("");
        userTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
