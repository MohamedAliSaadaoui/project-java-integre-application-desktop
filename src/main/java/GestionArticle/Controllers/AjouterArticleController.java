package GestionArticle.Controllers;

import GestionArticle.Entites.Article;
import GestionArticle.Service.ServiceArticle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class AjouterArticleController implements Initializable {

    @FXML
    private TextField TFtitre;

    @FXML
    private TextField TFContenu;

    @FXML
    private Button AjouterArticleAction;

    @FXML
    private Button goafficher;

    @FXML
    private Button uploadbutton;

    @FXML
    private ImageView imgView_reclamation;

    @FXML
    private Label warningtitle;

    @FXML
    private Label goodtitle;

    @FXML
    private Label warningDescription;

    @FXML
    private Label gooddescription;

    private String imagePath = "";
    private final ServiceArticle serviceArticle = new ServiceArticle();
    java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TFContenu.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 12) {
                warningDescription.setVisible(true);
                gooddescription.setVisible(false);
            } else {
                warningDescription.setVisible(false);
                gooddescription.setVisible(true);
            }
        });

        TFtitre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 4) {
                warningtitle.setVisible(true);
                goodtitle.setVisible(false);
            } else {
                warningtitle.setVisible(false);
                goodtitle.setVisible(true);
            }
        });
    }

    @FXML
    public void AjouterArticleAction(ActionEvent event) {
        try {
            if (TFtitre.getText().length() < 4 || TFContenu.getText().length() < 12) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Vérifiez les champs : le titre doit avoir au moins 4 caractères et la description 12 !");
                alert.show();
                return;
            }

            Article article = new Article( TFtitre.getText(), sqlDate, TFContenu.getText(), imagePath);
            serviceArticle.ajouter(article);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Article ajouté avec succès !");
            alert.show();

            Parent root = FXMLLoader.load(getClass().getResource("/AfficherArticle.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur : " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void uploadimg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(uploadbutton.getScene().getWindow());

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:///" + imagePath);
            imgView_reclamation.setImage(image);
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goafficherAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherArticle.fxml"));
            Stage stage = (Stage) TFtitre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d’ouvrir le formulaire d’ajout.");
            e.printStackTrace();
        }
    }

    public void goafficheradmin(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherArticleAdmin.fxml"));
            Stage stage = (Stage) TFtitre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d’ouvrir le formulaire d’ajout.");
            e.printStackTrace();
        }
    }
}
