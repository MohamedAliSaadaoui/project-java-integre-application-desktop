package com.example.rewear.Gestionevent.Service;
import com.example.rewear.Gestionevent.Entity.Participe;
import com.example.rewear.DBUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticipeDAO {
    private static final String INSERT_QUERY = "INSERT INTO Participe (id_event_id, user_id, date_participation) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM Participe";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM Participe WHERE id = ?";
    private static final String SELECT_BY_EVENT_ID_QUERY = "SELECT * FROM Participe WHERE id_event_id = ?";
    private static final String SELECT_BY_USER_ID_QUERY = "SELECT * FROM Participe WHERE user_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM Participe WHERE id = ?";
    private static final String DELETE_BY_EVENT_USER_QUERY = "DELETE FROM Participe WHERE id_event_id = ? AND user_id = ?";
    private static final String COUNT_BY_EVENT_ID_QUERY = "SELECT COUNT(*) FROM Participe WHERE id_event_id = ?";
    private static final String CHECK_PARTICIPATION_QUERY = "SELECT COUNT(*) FROM Participe WHERE id_event_id = ? AND user_id = ?";
    private static final String CHECK_DATE_VALIDITY_QUERY =
            "SELECT COUNT(*) FROM Participe p JOIN Event e ON p.id_event_id = e.id " +
                    "WHERE p.id_event_id = ? AND p.date_participation >= e.date_debut AND p.date_participation <= e.date_fin";

    // Nombre maximum de places par événement
    private static final int MAX_PLACES_PAR_EVENEMENT = 30;

    private Connection connection;

    // Constructeur par défaut
    public ParticipeDAO() {
        // Utilise les connexions à la demande via DBUtil.getConnection()
    }

    // Constructeur avec connexion
    public ParticipeDAO(Connection connection) {
        this.connection = connection;
    }

    // Méthode pour ajouter une participation
    public boolean ajouter(Participe participe) {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            // Utiliser la connexion injectée si disponible, sinon obtenir une nouvelle connexion
            conn = this.connection != null ? this.connection : DBUtil.getConnection();

            // Vérifier si l'utilisateur participe déjà à l'événement
            if (utilisateurParticipeDejaAEvenement(participe.getId_event_id(), participe.getUser_id())) {
                System.out.println("L'utilisateur participe déjà à cet événement");
                return false;
            }

            // Vérifier s'il reste des places disponibles
            if (!placesDisponibles(participe.getId_event_id())) {
                System.out.println("Plus de places disponibles pour cet événement");
                return false;
            }

            // Vérifier si la date est valide (entre dateDebut et dateFin de l'événement)
            if (!dateEstValide(participe.getId_event_id(), participe.getDateParticipation())) {
                System.out.println("Date de participation invalide");
                return false;
            }

            // Préparation de la requête
            statement = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, participe.getId_event_id());
            statement.setLong(2, participe.getUser_id());
            statement.setDate(3, Date.valueOf(participe.getDateParticipation()));

            // Exécution de la requête
            int affectedRows = statement.executeUpdate();

            // Récupération de l'ID généré
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        participe.setId((int)generatedKeys.getLong(1));
                        System.out.println("Participation ajoutée avec succès. ID: " + participe.getId());
                        return true;
                    }
                }
            }

            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la participation: " + e.getMessage());
            return false;
        } finally {
            // Fermer les ressources
            try {
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close(); // Ne ferme la connexion que si elle a été créée localement
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }
    }

    // Méthode pour récupérer toutes les participations
    public List<Participe> obtenirTout() {
        List<Participe> participations = new ArrayList<>();
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_QUERY);

            while (resultSet.next()) {
                participations.add(extraireParticipation(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participations: " + e.getMessage());
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }

        return participations;
    }

    // Méthode pour récupérer une participation par son ID
    public Optional<Participe> obtenirParId(Long id) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.prepareStatement(SELECT_BY_ID_QUERY);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(extraireParticipation(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la participation: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }

        return Optional.empty();
    }

    // Méthode pour récupérer les participations par ID d'événement
    public List<Participe> obtenirParEventId(Long eventId) {
        List<Participe> participations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.prepareStatement(SELECT_BY_EVENT_ID_QUERY);
            statement.setLong(1, eventId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                participations.add(extraireParticipation(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participations par événement: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }

        return participations;
    }

    // Méthode pour récupérer les participations par ID d'utilisateur
    public List<Participe> obtenirParUserId(Long userId) {
        List<Participe> participations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.prepareStatement(SELECT_BY_USER_ID_QUERY);
            statement.setLong(1, userId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                participations.add(extraireParticipation(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participations par utilisateur: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }

        return participations;
    }

    // Méthode pour supprimer une participation
    public boolean supprimer(Long id) {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.prepareStatement(DELETE_QUERY);
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la participation: " + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }
    }

    // Méthode pour supprimer une participation par ID d'événement et ID d'utilisateur
    public boolean supprimerParticipation(Long eventId, Long userId) {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.prepareStatement(DELETE_BY_EVENT_USER_QUERY);
            statement.setLong(1, eventId);
            statement.setLong(2, userId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la participation: " + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }
    }

    // Méthode pour obtenir le nombre de places restantes pour un événement
    public int obtenirPlacesRestantes(Long eventId) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.prepareStatement(COUNT_BY_EVENT_ID_QUERY);
            statement.setLong(1, eventId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int nombreParticipations = resultSet.getInt(1);
                return Math.max(0, MAX_PLACES_PAR_EVENEMENT - nombreParticipations);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul des places restantes: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }

        return 0; // Par défaut, considérer qu'il n'y a plus de places disponibles en cas d'erreur
    }

    // Méthode pour vérifier s'il reste des places disponibles
    private boolean placesDisponibles(Long eventId) {
        return obtenirPlacesRestantes(eventId) > 0;
    }

    // Méthode pour vérifier si un utilisateur participe déjà à un événement
    public boolean utilisateurParticipeDejaAEvenement(Long eventId, Long userId) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.prepareStatement(CHECK_PARTICIPATION_QUERY);
            statement.setLong(1, eventId);
            statement.setLong(2, userId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de participation: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }

        return false;
    }

    // Méthode pour vérifier si une date est valide pour un événement
    private boolean dateEstValide(Long eventId, LocalDate date_participation) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            conn = this.connection != null ? this.connection : DBUtil.getConnection();
            statement = conn.prepareStatement("SELECT date_debut, date_fin FROM Event WHERE id = ?");
            statement.setLong(1, eventId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                LocalDate dateDebut = resultSet.getDate("date_debut").toLocalDate();
                LocalDate dateFin = resultSet.getDate("date_fin").toLocalDate();

                return !date_participation.isBefore(dateDebut) && !date_participation.isAfter(dateFin);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la date: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }

        return false;
    }

    // Méthode pour extraire une participation d'un ResultSet
    private Participe extraireParticipation(ResultSet resultSet) throws SQLException {
        Participe participe = new Participe();
        participe.setId((int)resultSet.getLong("id"));
        participe.setId_event_id(resultSet.getLong("id_event_id"));
        participe.setUser_id(resultSet.getLong("user_id"));
        participe.setDateParticipation(resultSet.getDate("date_participation").toLocalDate());
        return participe;
    }
    // Méthode pour modifier une participation existante
    public boolean modifier(Participe participe) {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            // Utiliser la connexion injectée si disponible, sinon obtenir une nouvelle connexion
            conn = this.connection != null ? this.connection : DBUtil.getConnection();

            // Vérifier si la date est valide (entre date_debut et date_fin de l'événement)
            if (!dateEstValide(participe.getId_event_id(), participe.getDateParticipation())) {
                System.out.println("Date de participation invalide");
                return false;
            }

            // Préparation de la requête de mise à jour
            String updateQuery = "UPDATE Participe SET date_participation = ? WHERE id = ? AND id_event_id = ? AND user_id = ?";
            statement = conn.prepareStatement(updateQuery);
            statement.setDate(1, Date.valueOf(participe.getDateParticipation()));
            statement.setLong(2, participe.getId());
            statement.setLong(3, participe.getId_event_id());
            statement.setLong(4, participe.getUser_id());

            // Exécution de la requête
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Participation modifiée avec succès. ID: " + participe.getId());
                return true;
            } else {
                System.out.println("Aucune participation n'a été modifiée.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la participation: " + e.getMessage());
            return false;
        } finally {
            // Fermer les ressources
            try {
                if (statement != null) statement.close();
                if (conn != null && this.connection == null) conn.close(); // Ne ferme la connexion que si elle a été créée localement
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }
    }
}