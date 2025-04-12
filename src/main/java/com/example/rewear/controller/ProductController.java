package com.example.rewear.controller;

import com.example.rewear.dao.ProductDAO;
import com.example.rewear.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProductController extends BaseController {
    private final ProductDAO productDAO = new ProductDAO();
    private ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML private GridPane productsGrid;
    @FXML private TextField searchField;
    @FXML private Text welcomeText;
    @FXML private ImageView userAvatar;
    @FXML private Button cartButton;
    @FXML private Button wishlistButton;
    @FXML private Button ordersButton;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        setupWelcomeMessage();
        loadProducts();
        setupEventHandlers();
    }

    private void setupWelcomeMessage() {
        welcomeText.setText("Welcome, Omar");
    }

    private void loadProducts() {
        products.setAll(productDAO.getAllProducts());
        displayProducts();
    }

    private void displayProducts() {
        productsGrid.getChildren().clear();
        int column = 0;
        int row = 0;
        
        for (Product product : products) {
            VBox productCard = createProductCard(product);
            productsGrid.add(productCard, column, row);
            
            column++;
            if (column == 4) { // 4 products per row
                column = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.getStyleClass().add("product-card");

        // Product Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("product-image");

        // Product Title
        Label titleLabel = new Label(product.getObjetAVendre()); // Using existing method
        titleLabel.getStyleClass().add("product-title");
        titleLabel.setWrapText(true);

        // Description (instead of Brand since we don't have brand in the model)
        Label descLabel = new Label(product.getDescription());
        descLabel.getStyleClass().add("product-brand");

        // Price
        Label priceLabel = new Label(String.format("$ %.2f", product.getPrixDeVente()));
        priceLabel.getStyleClass().add("product-price");

        // Rating (using a default value since we don't have rating in the model)
        HBox ratingBox = createRatingStars(5.0);
        ratingBox.getStyleClass().add("rating");

        card.getChildren().addAll(imageView, titleLabel, descLabel, priceLabel, ratingBox);
        
        card.setOnMouseClicked(e -> showProductDetails(product));
        
        return card;
    }

    private HBox createRatingStars(double rating) {
        HBox starsBox = new HBox(2);
        int fullStars = (int) rating;
        boolean hasHalfStar = rating % 1 >= 0.5;

        for (int i = 0; i < fullStars; i++) {
            Label star = new Label("★");
            starsBox.getChildren().add(star);
        }

        if (hasHalfStar) {
            Label halfStar = new Label("½");
            starsBox.getChildren().add(halfStar);
        }

        while (starsBox.getChildren().size() < 5) {
            Label emptyStar = new Label("☆");
            starsBox.getChildren().add(emptyStar);
        }

        return starsBox;
    }

    private void setupEventHandlers() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterProducts(newVal));
        
        cartButton.setOnAction(e -> showCart());
        wishlistButton.setOnAction(e -> showWishlist());
        ordersButton.setOnAction(e -> showOrders());
        logoutButton.setOnAction(e -> handleLogout());
    }

    private void filterProducts(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            displayProducts();
        } else {
            // Filter products locally since we don't have searchProducts in DAO
            ObservableList<Product> filtered = products.filtered(product ->
                product.getObjetAVendre().toLowerCase().contains(searchText.toLowerCase()) ||
                product.getDescription().toLowerCase().contains(searchText.toLowerCase())
            );
            products.setAll(filtered);
            displayProducts();
        }
    }

    private void showProductDetails(Product product) {
        showAlert(Alert.AlertType.INFORMATION, "Product Details", 
                 "Showing details for: " + product.getObjetAVendre());
    }

    private void showCart() {
        showAlert(Alert.AlertType.INFORMATION, "Cart", "Opening cart...");
    }

    private void showWishlist() {
        showAlert(Alert.AlertType.INFORMATION, "Wishlist", "Opening wishlist...");
    }

    private void showOrders() {
        showAlert(Alert.AlertType.INFORMATION, "Orders", "Opening orders...");
    }

    private void handleLogout() {
        if (showConfirmation("Are you sure you want to logout?")) {
            // TODO: Implement logout logic
            showAlert(Alert.AlertType.INFORMATION, "Success", "Logged out successfully");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 