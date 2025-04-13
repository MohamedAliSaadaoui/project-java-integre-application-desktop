package com.example.rewear.model;

import javafx.beans.property.*;

public class CartItem {
    private final Product product;
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty totalPrice = new SimpleDoubleProperty();

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity.set(quantity);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        totalPrice.set(product.getPrixDeVente() * quantity.get());
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        updateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }
} 