package com.example.rewear.Gestionevent.Entity;

import java.time.LocalDate;

    public class Event {    private int id;
        private String titre;
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private String lieu;
        private String statut;
        private String categorie;
        private int creatorId; // ID de l'utilisateur créateur (clé étrangère)

        // Constructeurs
        public Event() {}

        public Event( String titre, LocalDate dateDebut, LocalDate dateFin, String lieu, String statut, String categorie, int creatorId) {
            this.titre = titre;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            this.lieu = lieu;
            this.statut = statut;
            this.categorie = categorie;
            this.creatorId = creatorId;
        }
        public Event(int id, String titre, LocalDate dateDebut, LocalDate dateFin,
                     String lieu, String statut, String categorie, int creatorId) {
            this.id = id;
            this.titre = titre;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            this.lieu = lieu;
            this.statut = statut;
            this.categorie = categorie;
            this.creatorId = creatorId;
        }


        // Getters & Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTitre() { return titre; }
        public void setTitre(String titre) { this.titre = titre; }

        public LocalDate getDateDebut() { return dateDebut; }
        public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

        public LocalDate getDateFin() { return dateFin; }
        public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

        public String getLieu() { return lieu; }
        public void setLieu(String lieu) { this.lieu = lieu; }

        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }

        public String getCategorie() { return categorie; }
        public void setCategorie(String categorie) { this.categorie = categorie; }

        public int getCreatorId() { return creatorId; }
        public void setCreatorId(int creatorId) { this.creatorId = creatorId; }

        public String toString() {
            return "Event{" +
                    "id=" + id +
                    ", titre='" + titre + '\'' +
                    ", dateDebut=" + dateDebut +
                    ", dateFin=" + dateFin +
                    ", lieu='" + lieu + '\'' +
                    ", statut='" + statut + '\'' +
                    ", categorie='" + categorie + '\'' +
                    ", creatorId=" + creatorId +
                    '}';
        }
    }

