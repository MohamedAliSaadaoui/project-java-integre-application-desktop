package com.example.rewear.controller;

import com.example.rewear.model.CartItem;
import com.example.rewear.service.CartService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.util.Callback;

public class CartController {
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> productNameColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, Double> totalColumn;
    @FXML private TableColumn<CartItem, Void> actionsColumn;
    @FXML private Label totalLabel;
    @FXML private Button checkoutButton;
    @FXML private Button continueShoppingButton;

    private CartService cartService;

    @FXML
    public void initialize() {
        cartService = CartService.getInstance();
        setupTableColumns();
        loadCartItems();
        updateTotal();

        // Listen for changes in cart items
        cartService.getCartItems().addListener((javafx.collections.ListChangeListener.Change<? extends CartItem> c) -> {
            updateTotal();
        });
    }

    private void setupTableColumns() {
        productNameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProduct().getObjetAVendre()));
        
        priceColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getProduct().getPrixDeVente()).asObject());
        
        quantityColumn.setCellValueFactory(cellData -> 
            cellData.getValue().quantityProperty().asObject());
        
        totalColumn.setCellValueFactory(cellData -> 
            cellData.getValue().totalPriceProperty().asObject());

        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button deleteButton = new Button("Remove");
            private final Spinner<Integer> quantitySpinner = new Spinner<>(1, 99, 1);

            {
                deleteButton.setOnAction(event -> {
                    CartItem item = getTableRow().getItem();
                    if (item != null) {
                        cartService.removeFromCart(item);
                    }
                });

                quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    CartItem item = getTableRow().getItem();
                    if (item != null) {
                        cartService.updateQuantity(item, newValue);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CartItem cartItem = getTableRow().getItem();
                    if (cartItem != null) {
                        quantitySpinner.getValueFactory().setValue(cartItem.getQuantity());
                    }
                    setGraphic(new javafx.scene.layout.HBox(10, quantitySpinner, deleteButton));
                }
            }
        });
    }

    private void loadCartItems() {
        cartTable.setItems(cartService.getCartItems());
    }

    private void updateTotal() {
        totalLabel.setText(String.format("Total: $%.2f", cartService.getTotal()));
    }

    @FXML
    private void handleCheckout() {
        if (cartService.getCartItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cart is empty", "Please add items to your cart before checking out.");
            return;
        }
        // TODO: Implement checkout logic
        showAlert(Alert.AlertType.INFORMATION, "Checkout", "Proceeding to checkout...");
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
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading product list view");
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