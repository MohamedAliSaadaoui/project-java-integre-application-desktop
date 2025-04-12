package com.example.rewear.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;

public class Command {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final StringProperty etat = new SimpleStringProperty();
    private final StringProperty methodePaiement = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final StringProperty adresseLivraison = new SimpleStringProperty();
    private final StringProperty codePostalLivraison = new SimpleStringProperty();
    private final StringProperty villeLivraison = new SimpleStringProperty();
    private final StringProperty paysLivraison = new SimpleStringProperty();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<Livraison> livraisons = FXCollections.observableArrayList();

    public Command() {
        this.createdAt.set(LocalDateTime.now());
    }

    public Command(int id, int userId, int productId, String etat, String dateCommande) {
        this.id.set(id);
        this.userId.set(userId);
        this.productId.set(productId);
        this.etat.set(etat);
        this.createdAt.set(LocalDateTime.parse(dateCommande));
    }

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

    public String getEtat() { return etat.get(); }
    public StringProperty etatProperty() { return etat; }
    public void setEtat(String value) { this.etat.set(value); }

    public String getMethodePaiement() { return methodePaiement.get(); }
    public StringProperty methodePaiementProperty() { return methodePaiement; }
    public void setMethodePaiement(String value) { this.methodePaiement.set(value); }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public void setCreatedAt(LocalDateTime value) { this.createdAt.set(value); }

    public String getAdresseLivraison() { return adresseLivraison.get(); }
    public StringProperty adresseLivraisonProperty() { return adresseLivraison; }
    public void setAdresseLivraison(String value) { this.adresseLivraison.set(value); }

    public String getCodePostalLivraison() { return codePostalLivraison.get(); }
    public StringProperty codePostalLivraisonProperty() { return codePostalLivraison; }
    public void setCodePostalLivraison(String value) { this.codePostalLivraison.set(value); }

    public String getVilleLivraison() { return villeLivraison.get(); }
    public StringProperty villeLivraisonProperty() { return villeLivraison; }
    public void setVilleLivraison(String value) { this.villeLivraison.set(value); }

    public String getPaysLivraison() { return paysLivraison.get(); }
    public StringProperty paysLivraisonProperty() { return paysLivraison; }
    public void setPaysLivraison(String value) { this.paysLivraison.set(value); }

    public ObservableList<Product> getProducts() { return products; }
    public ObservableList<Livraison> getLivraisons() { return livraisons; }

    public double calculateTotal() {
        return products.stream()
                      .mapToDouble(Product::getPrixDeVente)
                      .sum() + 
               (livraisons.isEmpty() ? 0 : livraisons.get(0).getTarif());
    }
} 