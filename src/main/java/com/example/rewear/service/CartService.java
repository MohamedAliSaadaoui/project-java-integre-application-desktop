package com.example.rewear.service;

import com.example.rewear.model.CartItem;
import com.example.rewear.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartService {
    private static CartService instance;
    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    private CartService() {}

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    public void addToCart(Product product, int quantity) {
        // Check if product already exists in cart
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // If not found, add new item
        cartItems.add(new CartItem(product, quantity));
    }

    public void removeFromCart(CartItem item) {
        cartItems.remove(item);
    }

    public void updateQuantity(CartItem item, int quantity) {
        if (quantity <= 0) {
            cartItems.remove(item);
        } else {
            item.setQuantity(quantity);
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotal() {
        return cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public double getCartTotal() {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrixDeVente() * item.getQuantity())
                .sum();
    }
} 