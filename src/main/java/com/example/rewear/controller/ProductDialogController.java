package com.example.rewear.controller;

import com.example.rewear.dao.ProductDAO;
import com.example.rewear.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductDialogController {
    @FXML
    private TextField objetAVendreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField prixDeVenteField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField tailleField;
    @FXML
    private TextField etatField;
    @FXML
    private TextField photoField;

    private Product product;
    private ProductDAO productDAO;

    public ProductDialogController() {
        productDAO = new ProductDAO();
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            objetAVendreField.setText(product.getObjetAVendre());
            descriptionField.setText(product.getDescription());
            prixDeVenteField.setText(String.valueOf(product.getPrixDeVente()));
            genreField.setText(product.getGenre());
            tailleField.setText(product.getTaille());
            etatField.setText(product.getEtat());
            photoField.setText(product.getPhoto());
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (product == null) {
                product = new Product();
            }
            
            product.setObjetAVendre(objetAVendreField.getText());
            product.setDescription(descriptionField.getText());
            product.setPrixDeVente(Double.parseDouble(prixDeVenteField.getText()));
            product.setGenre(genreField.getText());
            product.setTaille(tailleField.getText());
            product.setEtat(etatField.getText());
            product.setPhoto(photoField.getText());

            if (product.getId() == 0) {
                productDAO.addProduct(product);
            } else {
                productDAO.updateProduct(product);
            }

            closeDialog();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid price");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save product");
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) objetAVendreField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 