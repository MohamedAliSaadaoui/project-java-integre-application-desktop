package com.example.rewear.Gestionevent.Service;
import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class EventDAO {
    private Connection connection;

    // Constructeur sans paramètre (Option 1)
    public EventDAO() {
        // Ce constructeur utilisera une connexion obtenue à chaque appel de méthode
        this.connection = null;
    }

    // Constructeur avec connexion
    public EventDAO(Connection connection) {
        this.connection = connection;
    }

    // Méthode pour ajouter un événement
    public void addEvent(Event event) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Utiliser la connexion de classe ou obtenir une nouvelle connexion
            conn = this.connection != null ? this.connection : DBUtil.getConnection();

            String query = "INSERT INTO Event (titre, date_debut, date_fin, lieu, statut, categorie, creator_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Utilisation d'un PreparedStatement pour insérer un événement dans la base de données
            stmt = conn.prepareStatement(query);
            stmt.setString(1, event.getTitre());
            stmt.setDate(2, java.sql.Date.valueOf(event.getDateDebut()));
            stmt.setDate(3, java.sql.Date.valueOf(event.getDateFin()));
            stmt.setString(4, event.getLieu());
            stmt.setString(5, event.getStatut());
            stmt.setString(6, event.getCategorie());
            stmt.setInt(7, event.getCreatorId());

            stmt.executeUpdate();  // Exécution de l'insertion
        } finally {
            // Fermer les ressources
            if (stmt != null) stmt.close();
            if (conn != null && this.connection == null) conn.close(); // Ne ferme la connexion que si elle a été créée localement
        }
    }

    // Afficher tous les événements
    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Utiliser la connexion de classe ou obtenir une nouvelle connexion
            conn = this.connection != null ? this.connection : DBUtil.getConnection();

            String query = "SELECT * FROM Event";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

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
        } finally {
            // Fermer les ressources
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null && this.connection == null) conn.close();
        }

        return events;
    }

    public void updateEvent(Event event) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Utiliser la connexion de classe ou obtenir une nouvelle connexion
            conn = this.connection != null ? this.connection : DBUtil.getConnection();

            String query = "UPDATE Event SET titre = ?, date_debut = ?, date_fin = ?, lieu = ?, statut = ?, categorie = ?, creator_id = ? WHERE id = ?";

            stmt = conn.prepareStatement(query);
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
        } finally {
            // Fermer les ressources
            if (stmt != null) stmt.close();
            if (conn != null && this.connection == null) conn.close();
        }
    }

    public void deleteEvent(int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Utiliser la connexion de classe ou obtenir une nouvelle connexion
            conn = this.connection != null ? this.connection : DBUtil.getConnection();

            String query = "DELETE FROM Event WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, eventId);
            stmt.executeUpdate();  // Exécute la suppression
        } finally {
            // Fermer les ressources
            if (stmt != null) stmt.close();
            if (conn != null && this.connection == null) conn.close();
        }
    }

    // Méthode pour récupérer un événement par son ID
    public Event getEventById(Long eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Utiliser la connexion de classe ou obtenir une nouvelle connexion
            conn = this.connection != null ? this.connection : DBUtil.getConnection();

            String query = "SELECT * FROM Event WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setLong(1, eventId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Event(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getString("statut"),
                        rs.getString("categorie"),
                        rs.getInt("creator_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'événement: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }

        return null; // Retourne null si aucun événement n'est trouvé ou en cas d'erreur
    }
}