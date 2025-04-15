package GestionArticle.Service;

import GestionArticle.Entites.Article;
import GestionArticle.Entites.Commentaire;
import GestionArticle.Entites.User;
import GestionArticle.Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommentaire {

    private final Connection conn;

    public ServiceCommentaire() {
        this.conn = DataSource.getInstance().getCnx();
    }

    // ✅ Ajouter un commentaire
    public void ajouter(Commentaire commentaire) {
        String query = "INSERT INTO commentaire (id_user, article_id, date_comm, contenu_comm) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, commentaire.getUser().getId());
            pst.setInt(2, commentaire.getArticle().getId());
            pst.setDate(3, new Date(commentaire.getDate_comm().getTime()));
            pst.setString(4, commentaire.getContenu_comm());

            pst.executeUpdate();
            System.out.println("✅ Commentaire ajouté avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du commentaire : " + e.getMessage());
        }
    }

    // ✅ Modifier un commentaire
    public void modifier(Commentaire commentaire) {
        String query = "UPDATE commentaire SET contenu_comm = ?, date_comm = ?, article_id = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, commentaire.getContenu_comm());
            pst.setDate(2, new Date(commentaire.getDate_comm().getTime()));
            pst.setInt(3, commentaire.getArticle().getId());  // Update article_id
            pst.setInt(4, commentaire.getId());  // The comment's ID for WHERE clause

            pst.executeUpdate();
            System.out.println("✅ Commentaire modifié avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification du commentaire : " + e.getMessage());
        }
    }


    // ✅ Supprimer un commentaire
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM commentaire WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("🗑️ Commentaire supprimé avec succès.");
        } catch (SQLException e) {
            throw new SQLException("❌ Erreur lors de la suppression du commentaire avec id " + id + " : " + e.getMessage(), e);
        }
    }

    // ✅ Récupérer tous les commentaires
    public List<Commentaire> afficher() {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int userId = rs.getInt("id_user");
                int articleId = rs.getInt("article_id");

                User user = getUserById(userId); // Ensure this method is correctly implemented
                Article article = getArticleById(articleId); // Ensure this method is correctly implemented

                if (user != null && article != null) {
                    Commentaire commentaire = new Commentaire(
                            rs.getInt("id"),
                            user,
                            article,
                            rs.getDate("date_comm"),
                            rs.getString("contenu_comm")
                    );
                    commentaires.add(commentaire);
                } else {
                    // You could log a message here or handle the situation if the user or article is not found
                    System.out.println("A comment has missing User or Article. Comment ID: " + rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des commentaires : " + e.getMessage());
        }
        return commentaires;
    }


    public List<Commentaire> getAll() {
        return afficher();
    }

    // ✅ Récupérer les commentaires d'un article spécifique
    public List<Commentaire> getCommentairesByArticleId(int articleId) {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire WHERE article_id = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, articleId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    User user = getUserById(rs.getInt("is_user"));
                    Article article = getArticleById(rs.getInt("article_id"));

                    if (user != null && article != null) {
                        Commentaire commentaire = new Commentaire(
                                rs.getInt("id"),
                                user,
                                article,
                                rs.getDate("date_comm"),
                                rs.getString("contenu_comm")
                        );
                        commentaires.add(commentaire);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des commentaires de l'article : " + e.getMessage());
        }
        return commentaires;
    }

    // ✅ Helper - Récupérer un utilisateur par son ID
    private User getUserById(int userId) {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    // ✅ Helper - Récupérer un article par son ID
    public Article getArticleById(int articleId) {
        Article article = null;
        String query = "SELECT * FROM article WHERE id = ?";  // Ensure 'title' is used here, not 'titre'
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    article = new Article(
                            rs.getInt("id"),
                            rs.getString("title"), // Ensure column name is 'title'
                            rs.getDate("date"),
                            rs.getString("content"),
                            rs.getString("image"),
                            new User(rs.getInt("id_user"))
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de l'article : " + e.getMessage());
        }
        return article;
    }
    // Inside ServiceArticle class

}
