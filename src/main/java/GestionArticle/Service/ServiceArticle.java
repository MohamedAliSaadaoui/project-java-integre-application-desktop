package GestionArticle.Service;

import GestionArticle.Entites.Article;
import GestionArticle.Entites.User;
import GestionArticle.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceArticle implements IService<Article> {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Article article) throws SQLException {
        String req = "INSERT INTO article (title, date, content, image, id_user) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, article.getTitle());
        ps.setDate(2, new Date(article.getDate().getTime()));
        ps.setString(3, article.getContent());
        ps.setString(4, article.getImage());
        ps.setInt(5, article.getUser().getId());
        ps.executeUpdate();
        System.out.println("Article ajouté avec succès !");
    }

    @Override
    public void modifier(Article article) throws SQLException {
        String req = "UPDATE article SET title=?, date=?, content=?, image=?, id_user=? WHERE id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, article.getTitle());
        ps.setDate(2, new Date(article.getDate().getTime()));
        ps.setString(3, article.getContent());
        ps.setString(4, article.getImage());
        ps.setInt(5, article.getUser().getId());
        ps.setInt(6, article.getId());
        int rows = ps.executeUpdate();
        System.out.println(rows > 0 ? "Article mis à jour avec succès !" : "Aucune mise à jour effectuée.");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM article WHERE id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Article supprimé avec succès !");
    }

    @Override
    public Article getOneById(int id) throws SQLException {
        Article article = null;
        String req = "SELECT * FROM article WHERE id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet res = ps.executeQuery();
        if (res.next()) {
            article = new Article(
                    res.getInt("id"),
                    res.getString("title"),
                    res.getDate("date"),
                    res.getString("content"),
                    res.getString("image"),
                    new User(res.getInt("id_user"))
            );
        }
        return article;
    }

    public Article getOneByTitle(String title) throws SQLException {
        Article article = null;
        String req = "SELECT * FROM article WHERE title=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, title);
        ResultSet res = ps.executeQuery();
        if (res.next()) {
            article = new Article(
                    res.getInt("id"),
                    res.getString("title"),
                    res.getDate("date"),
                    res.getString("content"),
                    res.getString("image"),
                    new User(res.getInt("id_user"))
            );
        }
        return article;
    }

    @Override
    public Set<Article> getAll() throws SQLException {
        Set<Article> articles = new HashSet<>();
        String req = "SELECT * FROM article";
        Statement st = cnx.createStatement();
        ResultSet res = st.executeQuery(req);
        while (res.next()) {
            Article a = new Article(
                    res.getInt("id"),
                    res.getString("title"),
                    res.getDate("date"),
                    res.getString("content"),
                    res.getString("image"),
                    new User(res.getInt("id_user"))
            );
            articles.add(a);
        }
        return articles;
    }
    // Inside ServiceArticle class
    public List<Article> getAllArticles() throws SQLException {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article";  // Adjust according to your table structure

        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {  // Use cnx here
            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("date"),
                        rs.getString("content"),
                        rs.getString("image"),
                        new User(rs.getInt("id_user"))
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching articles", e);
        }
        return articles;
    }


    public Set<String> getAllTitles() throws SQLException {
        Set<String> titles = new HashSet<>();
        String req = "SELECT title FROM article";
        Statement st = cnx.createStatement();
        ResultSet res = st.executeQuery(req);
        while (res.next()) {
            titles.add(res.getString("title"));
        }
        return titles;
    }
}
