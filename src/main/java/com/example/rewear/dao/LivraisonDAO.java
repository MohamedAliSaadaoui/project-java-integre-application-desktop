package com.example.rewear.dao;

import com.example.rewear.util.DBUtil;
import com.example.rewear.model.Livraison;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivraisonDAO {
    
    public List<Livraison> getAllLivraisons() {
        List<Livraison> livraisons = new ArrayList<>();
        String query = "SELECT * FROM livraisons";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Livraison livraison = mapResultSetToLivraison(rs);
                livraisons.add(livraison);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    public Livraison getLivraisonById(int id) {
        String query = "SELECT * FROM livraisons WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLivraison(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Livraison> getLivraisonsByCommandId(int commandId) {
        List<Livraison> livraisons = new ArrayList<>();
        String query = "SELECT * FROM livraisons WHERE command_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, commandId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Livraison livraison = mapResultSetToLivraison(rs);
                livraisons.add(livraison);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    public boolean addLivraison(Livraison livraison) {
        String query = "INSERT INTO livraisons (livreur_id, command_id, etat) VALUES (?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, livraison.getLivreurId());
            pstmt.setInt(2, livraison.getCommandId());
            pstmt.setString(3, livraison.getEtat());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        livraison.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateLivraison(Livraison livraison) {
        String query = "UPDATE livraisons SET etat = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, livraison.getEtat());
            pstmt.setInt(2, livraison.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteLivraison(int id) {
        String query = "DELETE FROM livraisons WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Livraison mapResultSetToLivraison(ResultSet rs) throws SQLException {
        Livraison livraison = new Livraison();
        livraison.setId(rs.getInt("id"));
        livraison.setLivreurId(rs.getInt("livreur_id"));
        livraison.setCommandId(rs.getInt("command_id"));
        livraison.setEtat(rs.getString("etat"));
        return livraison;
    }
} 