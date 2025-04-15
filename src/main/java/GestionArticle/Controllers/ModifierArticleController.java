package GestionArticle.Controllers;

import GestionArticle.Entites.Article;
import GestionArticle.Service.ServiceArticle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierArticleController implements Initializable {

    @FXML
    private TextField TFtitre;

    @FXML
    private TextField TFContenuModif;

    @FXML
    private Button modifierbutton;

    @FXML
    private Button uploadbutton;

    @FXML
    private Button uploadbuttonmodif;

    @FXML
    private Label warningtitle;

    @FXML
    private Label goodtitle;

    @FXML
    private Label warningDescription;

    @FXML
    private Label gooddescription;

    @FXML
    private ImageView imgView_reclamationmodifffffff;

    private ServiceArticle serviceArticle;
    private Article article;
    private String imagePath;

    @FXML
    void modifierbutton(ActionEvent event) {
        if (article != null && serviceArticle != null) {
            String nouveauTitre = TFtitre.getText();
            String nouveauContenu = TFContenuModif.getText();

            // Validate title and content
            if (nouveauTitre.length() >= 4 && nouveauContenu.length() >= 12) {
                article.setTitle(nouveauTitre);
                article.setContent(nouveauContenu);

                // Update image if selected
                if (imagePath != null) {
                    article.setImage(imagePath);
                } else {
                    // If no image is selected, retain the old image (if any)
                    String currentImage = article.getImage();
                    if (currentImage != null && !currentImage.isEmpty()) {
                        article.setImage(currentImage);
                    }
                }

                try {
                    serviceArticle.modifier(article);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setContentText("Article modifié avec succès !");
                    successAlert.setTitle("Modification réussie");
                    successAlert.show();
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Erreur lors de la modification de l'article : " + e.getMessage());
                    errorAlert.setTitle("Erreur de modification");
                    errorAlert.show();
                }
            } else {
                // Show validation message for title and content
                if (nouveauTitre.length() < 4) {
                    warningtitle.setVisible(true);
                    TFtitre.requestFocus();
                } else {
                    warningtitle.setVisible(false);
                }
                if (nouveauContenu.length() < 12) {
                    warningDescription.setVisible(true);
                    TFContenuModif.requestFocus();
                } else {
                    warningDescription.setVisible(false);
                }
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de modifier l'article, aucune donnée n'est sélectionnée.");
            errorAlert.setTitle("Erreur de modification");
            errorAlert.show();
        }
    }

    @FXML
    void uploadimg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        Stage stage = (Stage) uploadbutton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
            imgView_reclamationmodifffffff.setImage(image);
        }
    }

    @FXML
    void uploadimgmodif(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        Stage stage = (Stage) uploadbuttonmodif.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
            imgView_reclamationmodifffffff.setImage(image);
        }
    }

    public void setData(Article article) {
        this.article = article;
        if (article != null) {
            TFtitre.setText(article.getTitle());
            TFContenuModif.setText(article.getContent());
            String imageUrl = article.getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    File file = new File(imageUrl);
                    String fileUrl = file.toURI().toURL().toString();
                    Image image = new Image(fileUrl);
                    imgView_reclamationmodifffffff.setImage(image);
                } catch (MalformedURLException e) {
                    System.err.println("Malformed URL: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void setServiceArticle(ServiceArticle serviceArticle) {
        this.serviceArticle = serviceArticle;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Any initialization code goes here
    }

    public void goafficherAction(ActionEvent actionEvent) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherArticle.fxml")); // Adjust the path if necessary

            // Set the new scene
            Stage stage = (Stage) TFtitre.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Erreur lors du chargement de la vue.");
            alert.setTitle("Erreur");
            alert.show();
        }
    }
}
