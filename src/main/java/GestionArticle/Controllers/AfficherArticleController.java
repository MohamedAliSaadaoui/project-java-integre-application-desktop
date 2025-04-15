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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AfficherArticleController {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private TextField RechercherActualiteAdmin;

    @FXML
    private Button buttonreturnA;

    @FXML
    private GridPane gridAdmin;

    @FXML
    private ScrollPane scrollAdmin;

    @FXML
    private Button sortActualiteAdmin;

    @FXML
    private Button stat;

    private final ServiceArticle serviceArticle = new ServiceArticle();

    @FXML
    void initialize() {
        afficherArticleList();
    }

    private void afficherArticleList() {
        try {
            Set<Article> articleSet = serviceArticle.getAll();
            List<Article> articleList = new ArrayList<>(articleSet);
            afficherArticleList(articleList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des articles.");
            e.printStackTrace();
        }
    }

    private void afficherArticleList(List<Article> articles) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        int row = 0;
        for (Article article : articles) {
            // Image
            ImageView imageView = new ImageView();
            if (article.getImage() != null && !article.getImage().isEmpty()) {
                try {
                    Image image = new Image("file:" + article.getImage(), 150, 150, true, true);
                    imageView.setImage(image);
                } catch (Exception e) {
                    System.out.println("Erreur chargement image : " + e.getMessage());
                }
            }
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            // Titre
            Text title = new Text(article.getTitle());
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            // Bouton Détails
            Button detailButton = new Button("Détails");
            detailButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
            detailButton.setOnAction(e -> afficherDetails(article));

            // Add to Grid
            grid.add(imageView, 0, row);
            grid.add(title, 1, row);
            grid.add(detailButton, 2, row);
            row++;
        }

        scrollAdmin.setContent(grid);
    }

    private void afficherDetails(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsArticle.fxml"));
            Parent root = loader.load();

            // Get the controller and set the article details
            detailsArticleController controller = loader.getController();
            controller.setDetails(article);

            // Create and show the new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'Article");
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture des détails de l'article.");
            e.printStackTrace();
        }
    }


    public void refreshArticles() {
        try {
            // Fetch all articles from the service
            Set<Article> articleSet = serviceArticle.getAll();
            List<Article> articleList = new ArrayList<>(articleSet);

            // Call the method to update the displayed articles
            afficherArticleList(articleList); // This will refresh the GridPane with the new list
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'actualisation des articles.");
            e.printStackTrace();
        }
    }



    @FXML
    void buttonreturnA(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterArticle.fxml"));
            Stage stage = (Stage) scrollAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d’ouvrir le formulaire d’ajout.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    public void adminadd(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterArticleAdmin.fxml"));
            Stage stage = (Stage) scrollAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger les statistiques.");
            e.printStackTrace();
        }
    }
}
