package com.example.rewear.Gestionevent.Entity;
import java.time.LocalDate;
public class Participe {
    private int id;         // ID auto-incrémenté par la base de données
    private Long id_event_id; // Référence à l'id de l'événement
    private Long user_id;     // Référence à l'id de l'utilisateur
    private LocalDate dateParticipation;

    public Participe() {
    }

    // Constructeur sans l'id (puisqu'il sera généré automatiquement par la base de données)
    public Participe(Long id_event_id, Long user_id, LocalDate dateParticipation) {
        this.id_event_id = id_event_id;
        this.user_id = user_id;
        this.dateParticipation = dateParticipation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getId_event_id() {
        return id_event_id;
    }

    public void setId_event_id(Long id_event_id) {
        this.id_event_id = id_event_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public LocalDate getDateParticipation() {
        return dateParticipation;
    }

    public void setDateParticipation(LocalDate dateParticipation) {
        this.dateParticipation = dateParticipation;
    }

    @Override
    public String toString() {
        return "Participe{" +
                "id=" + id +
                ", id_event_id=" + id_event_id +
                ", user_id=" + user_id +
                ", dateParticipation=" + dateParticipation +
                '}';
    }
}