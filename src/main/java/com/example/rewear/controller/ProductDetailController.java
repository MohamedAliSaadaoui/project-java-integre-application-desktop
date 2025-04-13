package com.example.rewear.controller;

import com.example.rewear.model.Product;
import com.example.rewear.service.CartService;
import com.example.rewear.service.WishlistService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProductDetailController {
    @FXML private Button backButton;
    @FXML private ImageView productImage;
    @FXML private Label productName;
    @FXML private Label priceLabel;
    @FXML private Label stockStatus;
    @FXML private Text descriptionText;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Button addToCartButton;
    @FXML private Button addToWishlistButton;
    
    // New product detail labels
    @FXML private Label genreLabel;
    @FXML private Label tailleLabel;
    @FXML private Label etatLabel;
    @FXML private Label couleurLabel;

    private Product product;

    @FXML
    public void initialize() {
        // Initialize the quantity spinner
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1);
        quantitySpinner.setValueFactory(valueFactory);
    }

    public void setProduct(Product product) {
        this.product = product;
        
        // Update UI with product details
        productName.setText(product.getObjetAVendre());
        descriptionText.setText(product.getDescription());
        priceLabel.setText(String.format("$%.2f", product.getPrixDeVente()));
        
        // Set stock status based on availability
        stockStatus.setText("Available In Stock");
        stockStatus.getStyleClass().add("in-stock");
        
        // Set product details
        genreLabel.setText(product.getGenre());
        tailleLabel.setText(product.getTaille());
        etatLabel.setText(product.getEtat());
        couleurLabel.setText(product.getCouleur());
        
        // Load product image if available
        if (product.getPhoto() != null && !product.getPhoto().isEmpty()) {
            try {
                Image image = new Image(product.getPhoto());
                productImage.setImage(image);
            } catch (Exception e) {
                // Set default image if loading fails
                try {
                    Image defaultImage = new Image(getClass().getResourceAsStream("/com/example/rewear/images/default-product.png"));
                    productImage.setImage(defaultImage);
                } catch (Exception ex) {
                    System.err.println("Failed to load default product image: " + ex.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAddToCart() {
        int quantity = quantitySpinner.getValue();
        CartService.getInstance().addToCart(product, quantity);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added to Cart");
        alert.setHeaderText(null);
        alert.setContentText(String.format("%d x %s added to cart", quantity, product.getObjetAVendre()));
        alert.showAndWait();
    }

    @FXML
    private void handleAddToWishlist() {
        WishlistService.getInstance().addToWishlist(product);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added to Wishlist");
        alert.setHeaderText(null);
        alert.setContentText(product.getObjetAVendre() + " has been added to your wishlist");
        alert.showAndWait();
    }
} 