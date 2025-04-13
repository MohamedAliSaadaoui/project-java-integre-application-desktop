package com.example.rewear.Gestionevent.Service;
import com.example.rewear.Gestionevent.Entity.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class EventDAO {
    private Connection connection;

    // Constructeur de la classe EventDAO
    public EventDAO(Connection connection) {
        this.connection = connection;
    }

    // Méthode pour ajouter un événement
    public void addEvent(Event event) throws SQLException {
        String query = "INSERT INTO Event (titre, date_debut, date_fin, lieu, statut, categorie, creator_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Utilisation d'un PreparedStatement pour insérer un événement dans la base de données
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, event.getTitre());
            stmt.setDate(2, java.sql.Date.valueOf(event.getDateDebut()));
            stmt.setDate(3, java.sql.Date.valueOf(event.getDateFin()));
            stmt.setString(4, event.getLieu());
            stmt.setString(5, event.getStatut());
            stmt.setString(6, event.getCategorie());
            stmt.setInt(7, event.getCreatorId());

            stmt.executeUpdate();  // Exécution de l'insertion


        }
    }

    // Afficher tous les événements
    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM Event";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getString("statut"),
                        rs.getString("categorie"),
                        rs.getInt("creator_id")
                );


                events.add(event);
            }
        }

        return events;
    }

    public void updateEvent(Event event) throws SQLException {
        String query = "UPDATE Event SET titre = ?, date_debut = ?, date_fin = ?, lieu = ?, statut = ?, categorie = ?, creator_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, event.getTitre());
            stmt.setDate(2, java.sql.Date.valueOf(event.getDateDebut()));
            stmt.setDate(3, java.sql.Date.valueOf(event.getDateFin()));
            stmt.setString(4, event.getLieu());
            stmt.setString(5, event.getStatut());
            stmt.setString(6, event.getCategorie());
            stmt.setInt(7, event.getCreatorId());
            stmt.setInt(8, event.getId());  // Important : l'ID de l'événement à modifier

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Événement mis à jour avec succès !");
            } else {
                System.out.println("❌ Aucun événement trouvé avec cet ID.");
            }
        }
    }
    public void deleteEvent(int eventId) throws SQLException {
        String query = "DELETE FROM Event WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();  // Exécute la suppression
        } catch (SQLException e) {
            throw e;  // Lance l'exception à l'extérieur pour la gestion des erreurs dans le contrôleur
        }
    }

}
