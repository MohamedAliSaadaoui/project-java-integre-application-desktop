package com.example.rewear.model;

import javafx.beans.property.*;

public class Livraison {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty livreurId = new SimpleIntegerProperty();
    private final IntegerProperty commandId = new SimpleIntegerProperty();
    private final StringProperty etat = new SimpleStringProperty();
    private final DoubleProperty tarif = new SimpleDoubleProperty();

    // Getters and Setters
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public int getLivreurId() { return livreurId.get(); }
    public IntegerProperty livreurIdProperty() { return livreurId; }
    public void setLivreurId(int livreurId) { this.livreurId.set(livreurId); }

    public int getCommandId() { return commandId.get(); }
    public IntegerProperty commandIdProperty() { return commandId; }
    public void setCommandId(int commandId) { this.commandId.set(commandId); }

    public String getEtat() { return etat.get(); }
    public StringProperty etatProperty() { return etat; }
    public void setEtat(String value) { this.etat.set(value); }

    public double getTarif() { return tarif.get(); }
    public DoubleProperty tarifProperty() { return tarif; }
    public void setTarif(double value) { this.tarif.set(value); }
} 