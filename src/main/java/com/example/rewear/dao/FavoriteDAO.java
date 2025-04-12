package com.example.rewear.dao;

import com.example.rewear.DBUtil;
import com.example.rewear.model.Favorite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {
    
    public List<Favorite> getAllFavorites() {
        List<Favorite> favorites = new ArrayList<>();
        String query = "SELECT * FROM favorites";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Favorite favorite = mapResultSetToFavorite(rs);
                favorites.add(favorite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }

    public List<Favorite> getFavoritesByUserId(int userId) {
        List<Favorite> favorites = new ArrayList<>();
        String query = "SELECT * FROM favorites WHERE user_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Favorite favorite = mapResultSetToFavorite(rs);
                favorites.add(favorite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }

    public boolean addFavorite(Favorite favorite) {
        String query = "INSERT INTO favorites (user_id, product_id) VALUES (?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, favorite.getUserId());
            pstmt.setInt(2, favorite.getProductId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        favorite.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFavorite(int userId, int productId) {
        String query = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isFavorite(int userId, int productId) {
        String query = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Favorite mapResultSetToFavorite(ResultSet rs) throws SQLException {
        Favorite favorite = new Favorite();
        favorite.setId(rs.getInt("id"));
        favorite.setUserId(rs.getInt("user_id"));
        favorite.setProductId(rs.getInt("product_id"));
        return favorite;
    }
} 