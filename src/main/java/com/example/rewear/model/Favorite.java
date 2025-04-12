package com.example.rewear.model;

import javafx.beans.property.*;

public class Favorite {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final IntegerProperty productId = new SimpleIntegerProperty();

    // Getters and Setters
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public int getUserId() { return userId.get(); }
    public IntegerProperty userIdProperty() { return userId; }
    public void setUserId(int userId) { this.userId.set(userId); }

    public int getProductId() { return productId.get(); }
    public IntegerProperty productIdProperty() { return productId; }
    public void setProductId(int productId) { this.productId.set(productId); }
} 