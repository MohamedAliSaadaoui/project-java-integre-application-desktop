package com.example.rewear.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class Product {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty objetAVendre = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final StringProperty taille = new SimpleStringProperty();
    private final StringProperty etat = new SimpleStringProperty();
    private final StringProperty couleur = new SimpleStringProperty();
    private final DoubleProperty prixDeVente = new SimpleDoubleProperty();
    private final DoubleProperty prixOriginal = new SimpleDoubleProperty();
    private final StringProperty telephone = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();
    private final StringProperty codePostal = new SimpleStringProperty();
    private final StringProperty ville = new SimpleStringProperty();
    private final StringProperty photo = new SimpleStringProperty();
    private final ObservableList<Command> commands = FXCollections.observableArrayList();

    public Product() {
    }

    public Product(int id, String objetAVendre, String description, double prixDeVente, String genre, String taille, String etat, String photo) {
        this.id.set(id);
        this.objetAVendre.set(objetAVendre);
        this.description.set(description);
        this.prixDeVente.set(prixDeVente);
        this.genre.set(genre);
        this.taille.set(taille);
        this.etat.set(etat);
        this.photo.set(photo);
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public int getUserId() { return userId.get(); }
    public IntegerProperty userIdProperty() { return userId; }
    public void setUserId(int userId) { this.userId.set(userId); }

    public String getObjetAVendre() { return objetAVendre.get(); }
    public StringProperty objetAVendreProperty() { return objetAVendre; }
    public void setObjetAVendre(String value) { this.objetAVendre.set(value); }

    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String value) { this.description.set(value); }

    public double getPrixDeVente() { return prixDeVente.get(); }
    public DoubleProperty prixDeVenteProperty() { return prixDeVente; }
    public void setPrixDeVente(double value) { this.prixDeVente.set(value); }

    public String getGenre() { return genre.get(); }
    public StringProperty genreProperty() { return genre; }
    public void setGenre(String value) { this.genre.set(value); }

    public String getTaille() { return taille.get(); }
    public StringProperty tailleProperty() { return taille; }
    public void setTaille(String value) { this.taille.set(value); }

    public String getEtat() { return etat.get(); }
    public StringProperty etatProperty() { return etat; }
    public void setEtat(String value) { this.etat.set(value); }

    public String getCouleur() { return couleur.get(); }
    public StringProperty couleurProperty() { return couleur; }
    public void setCouleur(String value) { this.couleur.set(value); }

    public double getPrixOriginal() { return prixOriginal.get(); }
    public DoubleProperty prixOriginalProperty() { return prixOriginal; }
    public void setPrixOriginal(double value) { this.prixOriginal.set(value); }

    public String getTelephone() { return telephone.get(); }
    public StringProperty telephoneProperty() { return telephone; }
    public void setTelephone(String value) { this.telephone.set(value); }

    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }
    public void setEmail(String value) { this.email.set(value); }

    public String getAdresse() { return adresse.get(); }
    public StringProperty adresseProperty() { return adresse; }
    public void setAdresse(String value) { this.adresse.set(value); }

    public String getCodePostal() { return codePostal.get(); }
    public StringProperty codePostalProperty() { return codePostal; }
    public void setCodePostal(String value) { this.codePostal.set(value); }

    public String getVille() { return ville.get(); }
    public StringProperty villeProperty() { return ville; }
    public void setVille(String value) { this.ville.set(value); }

    public String getPhoto() { return photo.get(); }
    public StringProperty photoProperty() { return photo; }
    public void setPhoto(String value) { this.photo.set(value); }

    public ObservableList<Command> getCommands() { return commands; }
} 