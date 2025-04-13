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
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import com.example.rewear.util.SVGLoader;
import java.io.IOException;
import javafx.stage.Stage;
import com.example.rewear.service.CartService;
import com.example.rewear.service.WishlistService;
import javafx.event.ActionEvent;

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
    @FXML private Button allCategoryButton;
    @FXML private Button womenCategoryButton;
    @FXML private Button menCategoryButton;
    @FXML private Button kidsCategoryButton;
    @FXML private Button accessoriesCategoryButton;
    @FXML private Button shoesCategoryButton;
    @FXML private Button bagsCategoryButton;
    @FXML private Button jewelryCategoryButton;
    @FXML private Button sportswearCategoryButton;
    @FXML private Button searchButton;

    private String currentCategory = "All";

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
        card.setUserData(product);

        // Product Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("product-image");
        
        if (product.getPhoto() != null && !product.getPhoto().isEmpty()) {
            try {
                Image image = new Image(product.getPhoto());
                imageView.setImage(image);
            } catch (Exception e) {
                // Set default image if loading fails
                // imageView.setImage(new Image("/com/example/rewear/images/default-product.png"));
            }
        }

        // Product Title
        Label titleLabel = new Label(product.getObjetAVendre());
        titleLabel.getStyleClass().add("product-title");
        titleLabel.setWrapText(true);

        // Description
        Label descLabel = new Label(product.getDescription());
        descLabel.getStyleClass().add("product-brand");
        descLabel.setWrapText(true);

        // Price
        Label priceLabel = new Label(String.format("$ %.2f", product.getPrixDeVente()));
        priceLabel.getStyleClass().add("product-price");

        // Buttons Container
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Add to Cart Button
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.getStyleClass().add("add-to-cart-button");
        addToCartBtn.setOnAction(e -> {
            e.consume(); // Prevent event from bubbling up to card click
            handleAddToCart(product);
        });

        // Add to Wishlist Button
        Button wishlistBtn = new Button();
        wishlistBtn.getStyleClass().add("wishlist-button");
        Region heartIcon = new Region();
        heartIcon.getStyleClass().add("heart-icon");
        wishlistBtn.setGraphic(heartIcon);
        wishlistBtn.setOnAction(e -> {
            e.consume(); // Prevent event from bubbling up to card click
            handleAddToWishlist(product);
        });

        buttonsBox.getChildren().addAll(addToCartBtn, wishlistBtn);

        card.getChildren().addAll(imageView, titleLabel, descLabel, priceLabel, buttonsBox);
        
        // Show product details when clicking on the card (except buttons)
        card.setOnMouseClicked(e -> showProductDetails(product));
        
        return card;
    }

    private void handleAddToCart(Product product) {
        CartService.getInstance().addToCart(product, 1);
        showAlert(Alert.AlertType.INFORMATION, "Added to Cart", 
                 product.getObjetAVendre() + " has been added to your cart");
    }

    private void handleAddToWishlist(Product product) {
        WishlistService.getInstance().addToWishlist(product);
        showAlert(Alert.AlertType.INFORMATION, "Added to Wishlist", 
                 product.getObjetAVendre() + " has been added to your wishlist");
    }

    private void showProductDetails(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/views/product-detail-view.fxml"));
            Parent root = loader.load();
            
            ProductDetailController controller = loader.getController();
            controller.setProduct(product);
            
            Stage stage = new Stage();
            stage.setTitle(product.getObjetAVendre());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load product details");
        }
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
        searchButton.setOnAction(event -> filterProducts(searchField.getText()));
        
        wishlistButton.setOnAction(e -> showWishlist());
        ordersButton.setOnAction(e -> showOrders());
        logoutButton.setOnAction(e -> handleLogout());
    }

    private void filterProducts(String searchText) {
        ObservableList<Product> filtered = products;
        
        // Apply category filter
        if (!currentCategory.equals("All")) {
            filtered = filtered.filtered(product ->
                product.getGenre().equalsIgnoreCase(currentCategory)
            );
        }
        
        // Apply search text filter if present
        if (searchText != null && !searchText.isEmpty()) {
            filtered = filtered.filtered(product ->
                product.getObjetAVendre().toLowerCase().contains(searchText.toLowerCase()) ||
                product.getDescription().toLowerCase().contains(searchText.toLowerCase())
            );
        }
        
        // Update the display with filtered products
        productsGrid.getChildren().clear();
        int column = 0;
        int row = 0;
        
        for (Product product : filtered) {
            VBox productCard = createProductCard(product);
            productsGrid.add(productCard, column, row);
            
            column++;
            if (column == 4) { // 4 products per row
                column = 0;
                row++;
            }
        }
    }

    private void showCart() {
        showAlert(Alert.AlertType.INFORMATION, "Cart", "Opening cart...");
    }

    private void showWishlist() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/views/wishlist-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) wishlistButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load wishlist view");
        }
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

    @FXML
    private void handleCategoryFilter(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        currentCategory = clickedButton.getText();
        
        // Reset all buttons to default style
        allCategoryButton.getStyleClass().remove("active");
        womenCategoryButton.getStyleClass().remove("active");
        menCategoryButton.getStyleClass().remove("active");
        kidsCategoryButton.getStyleClass().remove("active");
        accessoriesCategoryButton.getStyleClass().remove("active");
        shoesCategoryButton.getStyleClass().remove("active");
        bagsCategoryButton.getStyleClass().remove("active");
        jewelryCategoryButton.getStyleClass().remove("active");
        sportswearCategoryButton.getStyleClass().remove("active");
        
        // Add active style to clicked button
        clickedButton.getStyleClass().add("active");
        
        // Filter products based on category
        filterProducts(searchField.getText());
    }
} 