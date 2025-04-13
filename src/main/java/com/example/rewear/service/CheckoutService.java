package com.example.rewear.service;

import com.example.rewear.model.CartItem;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class CheckoutService {
    private static CheckoutService instance;
    private final CartService cartService;
    private final SimpleStringProperty shippingName = new SimpleStringProperty();
    private final SimpleStringProperty shippingEmail = new SimpleStringProperty();
    private final SimpleStringProperty shippingPhone = new SimpleStringProperty();
    private final SimpleStringProperty shippingAddress = new SimpleStringProperty();
    private final SimpleStringProperty shippingCity = new SimpleStringProperty();
    private final SimpleStringProperty shippingPostalCode = new SimpleStringProperty();
    private final SimpleStringProperty paymentMethod = new SimpleStringProperty();
    private final SimpleDoubleProperty subtotal = new SimpleDoubleProperty();
    private final SimpleDoubleProperty shipping = new SimpleDoubleProperty(5.00);
    private final SimpleDoubleProperty total = new SimpleDoubleProperty();

    private CheckoutService() {
        cartService = CartService.getInstance();
        // Update total when subtotal or shipping changes
        subtotal.addListener((obs, oldVal, newVal) -> 
            total.set(newVal.doubleValue() + shipping.get()));
        shipping.addListener((obs, oldVal, newVal) -> 
            total.set(subtotal.get() + newVal.doubleValue()));
    }

    public static CheckoutService getInstance() {
        if (instance == null) {
            instance = new CheckoutService();
        }
        return instance;
    }

    public void initializeCheckout() {
        // Calculate initial subtotal from cart
        double cartTotal = cartService.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrixDeVente() * item.getQuantity())
                .sum();
        subtotal.set(cartTotal);
        
        // Clear previous checkout data
        shippingName.set("");
        shippingEmail.set("");
        shippingPhone.set("");
        shippingAddress.set("");
        shippingCity.set("");
        shippingPostalCode.set("");
        paymentMethod.set("");
    }

    public boolean validateShippingInfo() {
        return !shippingName.get().trim().isEmpty() &&
               !shippingEmail.get().trim().isEmpty() &&
               shippingEmail.get().contains("@") &&
               !shippingPhone.get().trim().isEmpty() &&
               !shippingAddress.get().trim().isEmpty() &&
               !shippingCity.get().trim().isEmpty() &&
               !shippingPostalCode.get().trim().isEmpty();
    }

    public boolean validatePaymentInfo() {
        return !paymentMethod.get().trim().isEmpty();
    }

    public void processOrder() {
        // Here you would typically:
        // 1. Save order to database
        // 2. Process payment
        // 3. Send confirmation email
        // For now, just clear the cart
        cartService.clearCart();
    }

    // Getters and setters for properties
    public String getShippingName() { return shippingName.get(); }
    public void setShippingName(String value) { shippingName.set(value); }
    public SimpleStringProperty shippingNameProperty() { return shippingName; }

    public String getShippingEmail() { return shippingEmail.get(); }
    public void setShippingEmail(String value) { shippingEmail.set(value); }
    public SimpleStringProperty shippingEmailProperty() { return shippingEmail; }

    public String getShippingPhone() { return shippingPhone.get(); }
    public void setShippingPhone(String value) { shippingPhone.set(value); }
    public SimpleStringProperty shippingPhoneProperty() { return shippingPhone; }

    public String getShippingAddress() { return shippingAddress.get(); }
    public void setShippingAddress(String value) { shippingAddress.set(value); }
    public SimpleStringProperty shippingAddressProperty() { return shippingAddress; }

    public String getShippingCity() { return shippingCity.get(); }
    public void setShippingCity(String value) { shippingCity.set(value); }
    public SimpleStringProperty shippingCityProperty() { return shippingCity; }

    public String getShippingPostalCode() { return shippingPostalCode.get(); }
    public void setShippingPostalCode(String value) { shippingPostalCode.set(value); }
    public SimpleStringProperty shippingPostalCodeProperty() { return shippingPostalCode; }

    public String getPaymentMethod() { return paymentMethod.get(); }
    public void setPaymentMethod(String value) { paymentMethod.set(value); }
    public SimpleStringProperty paymentMethodProperty() { return paymentMethod; }

    public double getSubtotal() { return subtotal.get(); }
    public SimpleDoubleProperty subtotalProperty() { return subtotal; }

    public double getShipping() { return shipping.get(); }
    public SimpleDoubleProperty shippingProperty() { return shipping; }

    public double getTotal() { return total.get(); }
    public SimpleDoubleProperty totalProperty() { return total; }
} 