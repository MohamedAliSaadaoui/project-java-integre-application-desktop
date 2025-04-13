package com.example.rewear.controller;

import com.example.rewear.dao.ProductDAO;
import com.example.rewear.model.Product;
import com.example.rewear.controller.ProductDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.example.rewear.util.SVGLoader;
import java.io.IOException;
import javafx.stage.Stage;

public class ProductController extends BaseController {
    private final ProductDAO productDAO = new ProductDAO();
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML private GridPane productsGrid;
    @FXML private TextField searchField;
    @FXML private Text welcomeText;
    @FXML private ImageView userAvatar;
    @FXML private Button cartButton;
    @FXML private Button wishlistButton;
    @FXML private Button ordersButton;
    @FXML private Button logoutButton;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML
    public void initialize() {
        setupWelcomeMessage();
        loadProducts();
        setupEventHandlers();
        setupIcons();
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
        Label titleLabel = new Label(product.getObjetAVendre());
        titleLabel.getStyleClass().add("product-title");
        titleLabel.setWrapText(true);

        // Description
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
        addButton.setOnAction(event -> showProductDialog(null));
        editButton.setOnAction(event -> {
            Product selectedProduct = productsGrid.getChildren().stream()
                .filter(node -> node instanceof VBox)
                .map(node -> (VBox) node)
                .filter(vbox -> vbox.getUserData() instanceof Product)
                .map(vbox -> (Product) vbox.getUserData())
                .findFirst()
                .orElse(null);

            if (selectedProduct != null) {
                showProductDialog(selectedProduct);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a product to edit");
                alert.showAndWait();
            }
        });
        deleteButton.setOnAction(event -> {
            Product selectedProduct = productsGrid.getChildren().stream()
                .filter(node -> node instanceof VBox)
                .map(node -> (VBox) node)
                .filter(vbox -> vbox.getUserData() instanceof Product)
                .map(vbox -> (Product) vbox.getUserData())
                .findFirst()
                .orElse(null);

            if (selectedProduct != null) {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
                confirmDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            productDAO.deleteProduct(selectedProduct.getId());
                            loadProducts();
                        } catch (Exception e) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error deleting product: " + e.getMessage());
                            errorAlert.showAndWait();
                        }
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a product to delete");
                alert.showAndWait();
            }
        });
        cartButton.setOnAction(event -> navigateToCart());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterProducts(newValue));
        
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

    private void setupIcons() {
        // Load SVG icons for buttons
        cartButton.setGraphic(SVGLoader.loadSvg("/com/example/rewear/images/cart-icon.svg", 20, 20));
        wishlistButton.setGraphic(SVGLoader.loadSvg("/com/example/rewear/images/heart-icon.svg", 20, 20));
        ordersButton.setGraphic(SVGLoader.loadSvg("/com/example/rewear/images/order-icon.svg", 20, 20));
        logoutButton.setGraphic(SVGLoader.loadSvg("/com/example/rewear/images/logout-icon.svg", 20, 20));
        
        // Create and set up search button if not already in FXML
        Button searchButton = new Button();
        searchButton.setGraphic(SVGLoader.loadSvg("/com/example/rewear/images/search-icon.svg", 20, 20));
        searchButton.getStyleClass().add("search-button");
        searchButton.setOnAction(e -> handleSearch());
        
        // Add search button to the search field's parent HBox
        HBox searchContainer = (HBox) searchField.getParent();
        if (!searchContainer.getChildren().contains(searchButton)) {
            searchContainer.getChildren().add(searchButton);
        }
    }

    private void handleSearch() {
        filterProducts(searchField.getText());
    }

    private void navigateToCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/views/cart-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cartButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading cart view");
            alert.showAndWait();
        }
    }

    private void showProductDialog(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/views/product-dialog.fxml"));
            Parent root = loader.load();
            ProductDialogController controller = loader.getController();
            controller.setProduct(product);
            
            Stage stage = new Stage();
            stage.setTitle(product == null ? "Add Product" : "Edit Product");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            loadProducts(); // Refresh the table after dialog closes
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load product dialog");
        }
    }
} 