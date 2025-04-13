package com.example.rewear.Gestionevent.Test;

import com.example.rewear.DBUtil;
import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.Gestionevent.Service.EventDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EventDAOTest {
    public static void main(String[] args) {
        // Connexion à la base de données
        try (Connection connection = DBUtil.getConnection()) {
            EventDAO eventDAO = new EventDAO(connection);

            // Ajouter un événement
            Event event = new Event("Test1", LocalDate.of(2025, 5, 20), LocalDate.of(2025, 5, 21),
                    "Tunis", "En cours", "Actions", 19);
            eventDAO.addEvent(event);
            System.out.println("✅ Événement ajouté avec succès !");

            // Afficher tous les événements
            List<Event> events = eventDAO.getAllEvents();
            for (Event e : events) {
                System.out.println("ID: " + e.getId());
                System.out.println("Titre: " + e.getTitre());
                System.out.println("Date début: " + e.getDateDebut());
                System.out.println("Date fin: " + e.getDateFin());
                System.out.println("Lieu: " + e.getLieu());
                System.out.println("Statut: " + e.getStatut());
                System.out.println("Catégorie: " + e.getCategorie());
                System.out.println("Créateur ID: " + e.getCreatorId());
                System.out.println("------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

