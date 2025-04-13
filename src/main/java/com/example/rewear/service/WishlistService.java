package com.example.rewear.service;

import com.example.rewear.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WishlistService {
    private static WishlistService instance;
    private final ObservableList<Product> wishlistItems;

    private WishlistService() {
        wishlistItems = FXCollections.observableArrayList();
    }

    public static WishlistService getInstance() {
        if (instance == null) {
            instance = new WishlistService();
        }
        return instance;
    }

    public void addToWishlist(Product product) {
        if (!wishlistItems.contains(product)) {
            wishlistItems.add(product);
        }
    }

    public void removeFromWishlist(Product product) {
        wishlistItems.remove(product);
    }

    public ObservableList<Product> getWishlistItems() {
        return wishlistItems;
    }

    public boolean isInWishlist(Product product) {
        return wishlistItems.contains(product);
    }
} 