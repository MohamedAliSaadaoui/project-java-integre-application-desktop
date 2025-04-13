package services;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.rewear.DBUtil;

public class UserService implements Service<User> {
    private Connection cnx;

    public UserService() {
        cnx = DBUtil.getConnection();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        // Mentionnez explicitement toutes les colonnes, y compris roles
        String sql = "INSERT INTO user (username, password, email, num_tel, date_naiss, adresse, nb_article_achetes, nb_article_vendus, roles) VALUES (?, ?, ?, ?, ?, ?, 0, 0, '[\"ROLE_USER\"]')";

        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getNum_tel());

        // Gérer la date si elle existe
        if (user.getDate_naiss() != null) {
            ps.setDate(5, new java.sql.Date(user.getDate_naiss().getTime()));
        } else {
            ps.setNull(5, java.sql.Types.DATE);
        }

        ps.setString(6, user.getAdresse());

        ps.executeUpdate();
    }

    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE user SET username = ?, password = ?, email = ?, num_tel = ?, date_naiss = ?, adresse = ? WHERE id = ?";

        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getNum_tel());

        // Gérer la date si elle existe
        if (user.getDate_naiss() != null) {
            ps.setDate(5, new java.sql.Date(user.getDate_naiss().getTime()));
        } else {
            ps.setNull(5, java.sql.Types.DATE);
        }

        ps.setString(6, user.getAdresse());
        ps.setInt(7, user.getId());

        ps.executeUpdate();
    }

    @Override
    public void supprimer(User user) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";

        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, user.getId());

        ps.executeUpdate();
    }

    @Override
    public List<User> recuperer() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, email, photo, num_tel, date_naiss, adresse FROM user";

        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setPhoto(rs.getString("photo"));
            user.setNum_tel(rs.getString("num_tel"));
            user.setDate_naiss(rs.getDate("date_naiss"));
            user.setAdresse(rs.getString("adresse"));

            users.add(user);
        }

        return users;
    }

    // Méthode pour récupérer un utilisateur par son ID
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";

        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setPhoto(rs.getString("photo"));
            user.setNum_tel(rs.getString("num_tel"));
            user.setDate_naiss(rs.getDate("date_naiss"));
            user.setAdresse(rs.getString("adresse"));

            return user;
        }

        return null;
    }

    // Méthode pour rechercher des utilisateurs
    public List<User> searchUsers(String keyword) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE username LIKE ? OR email LIKE ? OR num_tel LIKE ? OR adresse LIKE ?";

        PreparedStatement ps = cnx.prepareStatement(sql);

        String searchKeyword = "%" + keyword + "%";
        ps.setString(1, searchKeyword);
        ps.setString(2, searchKeyword);
        ps.setString(3, searchKeyword);
        ps.setString(4, searchKeyword);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setPhoto(rs.getString("photo"));
            user.setNum_tel(rs.getString("num_tel"));
            user.setDate_naiss(rs.getDate("date_naiss"));
            user.setAdresse(rs.getString("adresse"));

            users.add(user);
        }

        return users;
    }
}
