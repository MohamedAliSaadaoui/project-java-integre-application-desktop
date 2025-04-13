package com.example.rewear.controller;

import com.example.rewear.model.Product;
import com.example.rewear.service.CartService;
import com.example.rewear.service.WishlistService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;

public class WishlistController {
    @FXML private Button backButton;
    @FXML private TableView<Product> wishlistTable;
    @FXML private TableColumn<Product, ImageView> imageColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> sizeColumn;
    @FXML private TableColumn<Product, String> conditionColumn;
    @FXML private TableColumn<Product, Void> actionsColumn;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadWishlistItems();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/views/product-list-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to product list");
        }
    }

    private void setupTableColumns() {
        // Image column
        imageColumn.setCellValueFactory(param -> {
            ImageView imageView = new ImageView();
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageView.setPreserveRatio(true);
            
            String photoUrl = param.getValue().getPhoto();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                try {
                    Image image = new Image(photoUrl, true);
                    imageView.setImage(image);
                } catch (Exception e) {
                    // Load default image if the product image fails to load
                    try {
                        Image defaultImage = new Image(getClass().getResourceAsStream("/com/example/rewear/images/default-product.png"));
                        imageView.setImage(defaultImage);
                    } catch (Exception ex) {
                        System.err.println("Failed to load default image: " + ex.getMessage());
                    }
                }
            }
            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });

        // Other columns
        nameColumn.setCellValueFactory(param -> param.getValue().objetAVendreProperty());
        priceColumn.setCellValueFactory(param -> param.getValue().prixDeVenteProperty().asObject());
        sizeColumn.setCellValueFactory(param -> param.getValue().tailleProperty());
        conditionColumn.setCellValueFactory(param -> param.getValue().etatProperty());

        // Actions column
        actionsColumn.setCellFactory(createActionsColumnCellFactory());
    }

    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> createActionsColumnCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button addToCartButton = new Button("Add to Cart");
                    private final Button removeButton = new Button("Remove");
                    private final HBox container = new HBox(5, addToCartButton, removeButton);

                    {
                        container.setAlignment(Pos.CENTER);
                        removeButton.getStyleClass().add("remove");
                        
                        addToCartButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            CartService.getInstance().addToCart(product, 1);
                            showAlert("Added to Cart", product.getObjetAVendre() + " has been added to your cart");
                        });

                        removeButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            WishlistService.getInstance().removeFromWishlist(product);
                            showAlert("Removed from Wishlist", product.getObjetAVendre() + " has been removed from your wishlist");
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : container);
                    }
                };
            }
        };
    }

    private void loadWishlistItems() {
        wishlistTable.setItems(WishlistService.getInstance().getWishlistItems());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 