package com.example.rewear;
import com.example.rewear.Gestionevent.Entity.Event;
import com.example.rewear.Gestionevent.Service.EventDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        try (Connection connection = DBUtil.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            EventDAO eventDAO = new EventDAO(connection);

            while (true) {
                System.out.println("\n====== MENU ======");
                System.out.println("1. Ajouter un événement");
                System.out.println("2. Afficher tous les événements");
                System.out.println("3. Modifier un événement");
                System.out.println("4. Supprimer un événement");
                System.out.println("0. Quitter");
                System.out.print("Choix : ");
                int choix = scanner.nextInt();
                scanner.nextLine(); // consommer le retour à la ligne

                switch (choix) {
                    case 1:
                        System.out.println("=== Ajout d'un événement ===");
                        System.out.print("Titre : ");
                        String titre = scanner.nextLine();

                        System.out.print("Date début (YYYY-MM-DD) : ");
                        LocalDate dateDebut = LocalDate.parse(scanner.nextLine());

                        System.out.print("Date fin (YYYY-MM-DD) : ");
                        LocalDate dateFin = LocalDate.parse(scanner.nextLine());

                        System.out.print("Lieu : ");
                        String lieu = scanner.nextLine();

                        System.out.print("Statut : ");
                        String statut = scanner.nextLine();

                        System.out.print("Catégorie : ");
                        String categorie = scanner.nextLine();

                        System.out.print("ID du créateur : ");
                        int creatorId = scanner.nextInt();

                        Event newEvent = new Event(titre, dateDebut, dateFin, lieu, statut, categorie, creatorId);
                        eventDAO.addEvent(newEvent);
                        System.out.println("✅ Événement ajouté avec succès !");
                        break;

                    case 2:
                        System.out.println("=== Liste des événements ===");
                        List<Event> events = eventDAO.getAllEvents();
                        for (Event event : events) {
                            System.out.println("ID : " + event.getId());
                            System.out.println("Titre : " + event.getTitre());
                            System.out.println("Date début : " + event.getDateDebut());
                            System.out.println("Date fin : " + event.getDateFin());
                            System.out.println("Lieu : " + event.getLieu());
                            System.out.println("Statut : " + event.getStatut());
                            System.out.println("Catégorie : " + event.getCategorie());
                            System.out.println("Créateur ID : " + event.getCreatorId());
                            System.out.println("----------------------");
                        }
                        break;

                    case 3:
                        System.out.println("=== Mise à jour d'un événement ===");
                        System.out.print("ID de l'événement à modifier : ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // consommer le retour à la ligne

                        System.out.print("Nouveau titre : ");
                        String titreMod = scanner.nextLine();

                        System.out.print("Nouvelle date début (YYYY-MM-DD) : ");
                        LocalDate dateDebutMod = LocalDate.parse(scanner.nextLine());

                        System.out.print("Nouvelle date fin (YYYY-MM-DD) : ");
                        LocalDate dateFinMod = LocalDate.parse(scanner.nextLine());

                        System.out.print("Nouveau lieu : ");
                        String lieuMod = scanner.nextLine();

                        System.out.print("Nouveau statut : ");
                        String statutMod = scanner.nextLine();

                        System.out.print("Nouvelle catégorie : ");
                        String categorieMod = scanner.nextLine();

                        System.out.print("ID du créateur : ");
                        int creatorIdMod = scanner.nextInt();

                        Event updatedEvent = new Event(id, titreMod, dateDebutMod, dateFinMod, lieuMod, statutMod, categorieMod, creatorIdMod);
                        eventDAO.updateEvent(updatedEvent);
                        System.out.println("✅ Événement mis à jour !");
                        break;
                    case 4:
                        System.out.println("=== Suppression d'un événement ===");
                        System.out.print("ID de l'événement à supprimer : ");
                        int eventId = scanner.nextInt();

                        eventDAO.deleteEvent(eventId);
                        System.out.println("✅ Événement supprimé avec succès !");
                        break;


                    case 0:
                        System.out.println("👋 Au revoir !");
                        return;

                    default:
                        System.out.println("❌ Choix invalide !");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
