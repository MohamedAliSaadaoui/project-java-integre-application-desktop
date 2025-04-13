package com.example.rewear.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class CartController {
    @FXML private TableView<?> cartTable;
    @FXML private TableColumn<?, ?> productNameColumn;
    @FXML private TableColumn<?, ?> priceColumn;
    @FXML private TableColumn<?, ?> quantityColumn;
    @FXML private TableColumn<?, ?> totalColumn;
    @FXML private TableColumn<?, ?> actionsColumn;
    @FXML private Label totalLabel;
    @FXML private Button checkoutButton;
    @FXML private Button continueShoppingButton;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCartItems();
    }

    private void setupTableColumns() {
        // Configure table columns
        productNameColumn.setCellValueFactory(cellData -> null);
        priceColumn.setCellValueFactory(cellData -> null);
        quantityColumn.setCellValueFactory(cellData -> null);
        totalColumn.setCellValueFactory(cellData -> null);
        actionsColumn.setCellValueFactory(cellData -> null);
    }

    private void loadCartItems() {
        // TODO: Load cart items from your cart service
        updateTotal();
    }

    private void updateTotal() {
        // TODO: Calculate and update the total
        totalLabel.setText("Total: $0.00");
    }

    @FXML
    private void handleCheckout() {
        // TODO: Implement checkout logic
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Checkout functionality will be implemented here");
        alert.showAndWait();
    }

    @FXML
    private void handleContinueShopping() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rewear/views/product-list-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) continueShoppingButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading product list view");
            alert.showAndWait();
        }
    }
} 