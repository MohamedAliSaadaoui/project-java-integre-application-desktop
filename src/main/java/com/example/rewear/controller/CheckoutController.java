package com.example.rewear.controller;

import com.example.rewear.service.CheckoutService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class CheckoutController {
    @FXML private TabPane checkoutSteps;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    
    @FXML private RadioButton creditCardRadio;
    @FXML private RadioButton paypalRadio;
    @FXML private RadioButton cashOnDeliveryRadio;
    @FXML private VBox creditCardForm;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    
    @FXML private VBox orderItemsContainer;
    @FXML private Label subtotalLabel;
    @FXML private Label shippingLabel;
    @FXML private Label totalLabel;

    private CheckoutService checkoutService;
    private ToggleGroup paymentMethodGroup;

    @FXML
    public void initialize() {
        checkoutService = CheckoutService.getInstance();
        checkoutService.initializeCheckout();
        
        setupPaymentMethodGroup();
        bindFormFields();
        updateOrderSummary();
    }

    private void setupPaymentMethodGroup() {
        paymentMethodGroup = new ToggleGroup();
        creditCardRadio.setToggleGroup(paymentMethodGroup);
        paypalRadio.setToggleGroup(paymentMethodGroup);
        cashOnDeliveryRadio.setToggleGroup(paymentMethodGroup);

        creditCardRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            creditCardForm.setVisible(newVal);
            creditCardForm.setManaged(newVal);
        });

        paymentMethodGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                RadioButton selected = (RadioButton) newVal;
                checkoutService.setPaymentMethod(selected.getText());
            }
        });
    }

    private void bindFormFields() {
        nameField.textProperty().bindBidirectional(checkoutService.shippingNameProperty());
        emailField.textProperty().bindBidirectional(checkoutService.shippingEmailProperty());
        phoneField.textProperty().bindBidirectional(checkoutService.shippingPhoneProperty());
        addressField.textProperty().bindBidirectional(checkoutService.shippingAddressProperty());
        cityField.textProperty().bindBidirectional(checkoutService.shippingCityProperty());
        postalCodeField.textProperty().bindBidirectional(checkoutService.shippingPostalCodeProperty());
        
        subtotalLabel.textProperty().bind(checkoutService.subtotalProperty().asString("$%.2f"));
        shippingLabel.textProperty().bind(checkoutService.shippingProperty().asString("$%.2f"));
        totalLabel.textProperty().bind(checkoutService.totalProperty().asString("$%.2f"));
    }

    private void updateOrderSummary() {
        orderItemsContainer.getChildren().clear();
        // Add order items from cart service
        // This will be implemented later
    }

    @FXML
    private void handleBack() {
        navigateToView("/com/example/rewear/views/cart-view.fxml");
    }

    @FXML
    private void handleNextToPayment() {
        if (!checkoutService.validateShippingInfo()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Information", 
                     "Please fill in all shipping information fields correctly.");
            return;
        }
        
        checkoutSteps.getTabs().get(1).setDisable(false);
        checkoutSteps.getSelectionModel().select(1);
    }

    @FXML
    private void handleNextToReview() {
        if (!checkoutService.validatePaymentInfo()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Information", 
                     "Please select a payment method.");
            return;
        }
        
        checkoutSteps.getTabs().get(2).setDisable(false);
        checkoutSteps.getSelectionModel().select(2);
    }

    @FXML
    private void handlePlaceOrder() {
        try {
            checkoutService.processOrder();
            showAlert(Alert.AlertType.INFORMATION, "Success", 
                     "Your order has been placed successfully!");
            navigateToView("/com/example/rewear/views/product-list-view.fxml");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                     "Failed to process your order: " + e.getMessage());
        }
    }

    private void navigateToView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) checkoutSteps.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                     "Could not navigate to the requested page.");
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