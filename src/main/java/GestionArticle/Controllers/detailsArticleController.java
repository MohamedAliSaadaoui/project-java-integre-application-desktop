package GestionArticle.Controllers;

import GestionArticle.Entites.Article;
import GestionArticle.Service.ServiceArticle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class detailsArticleController {

    @FXML
    private Text TFDetailstitle;

    @FXML
    private Text tfcontenudetails;

    @FXML
    private Text tfdatedeatils;

    @FXML
    private Button btnCommentaire; // Add the button here

    private Article article; // To store the current article
    private ServiceArticle serviceArticle = new ServiceArticle();

    public void setDetails(Article article) {
        this.article = article;

        TFDetailstitle.setText(article.getTitle());
        tfcontenudetails.setText(article.getContent());
        tfdatedeatils.setText(article.getDate().toString()); // Assuming date is of type Date
    }

    @FXML
    void modifaction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierArticle.fxml"));
            Parent root = loader.load();

            ModifierArticleController controller = loader.getController();
            controller.setData(article);
            controller.setServiceArticle(serviceArticle);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Article");
            stage.show();

            Stage currentStage = (Stage) TFDetailstitle.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Erreur lors de l'ouverture de la page de modification.");
            e.printStackTrace();
        }
    }

    @FXML
    void deleteaction(ActionEvent event) {
        boolean confirmed = true; // Add an actual confirmation box if needed

        if (confirmed && article != null) {
            try {
                serviceArticle.supprimer(article.getId());
                showAlert("Article supprimé avec succès");

                // Notify the AfficherArticle controller to refresh the list
                reloadArticlesList();

                Stage stage = (Stage) TFDetailstitle.getScene().getWindow();
                stage.close();
            } catch (Exception e) {

            }
        }
    }

    private void reloadArticlesList() {
        try {
            // Load the AfficherArticle view again and refresh the list of articles
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherArticle.fxml"));
            Parent root = loader.load();

            // Get the controller of the AfficherArticle view
            AfficherArticleController controller = loader.getController();

            // Call a method to refresh the articles list
            controller.refreshArticles();

            // Show the refreshed list
            Stage stage = (Stage) TFDetailstitle.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur lors de l'actualisation de la liste des articles.");
            e.printStackTrace();
        }
    }


    @FXML
    void backaction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherArticle.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Articles");
            stage.show();

            Stage currentStage = (Stage) TFDetailstitle.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Erreur lors du retour à la liste.");
            e.printStackTrace();
        }
    }

    @FXML
    void commentaireAction(ActionEvent event) {

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle("Information");
        alert.showAndWait();
    }

    public void btnCommentaire(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCommentaire.fxml"));
            Stage stage = (Stage) TFDetailstitle.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void backactionadmin(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherArticleAdmin.fxml"));
            Stage stage = (Stage) TFDetailstitle.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void commentadmin(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCommentaireAdmin.fxml"));
            Stage stage = (Stage) TFDetailstitle.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
