package com.example.rewear.dao;

import com.example.rewear.DBUtil;
import com.example.rewear.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Product product = mapResultSetToProduct(rs);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(int id) {
        String query = "SELECT * FROM product WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getProductsByUserId(int userId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE user_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = mapResultSetToProduct(rs);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean addProduct(Product product) {
        String query = "INSERT INTO product (user_id, objet_avendre, description, genre, etat, " +
                      "taille, couleur, prix_de_vente, prix_original, telephone, email, adresse, " +
                      "code_postal, ville, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            setProductParameters(pstmt, product);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        String query = "UPDATE product SET objet_a_vendre = ?, description = ?, genre = ?, " +
                      "etat = ?, taille = ?, couleur = ?, prix_de_vente = ?, prix_original = ?, " +
                      "telephone = ?, email = ?, adresse = ?, code_postal = ?, ville = ?, photo = ? " +
                      "WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            setProductParameters(pstmt, product);
            pstmt.setInt(15, product.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        String query = "DELETE FROM product WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setUserId(rs.getInt("user_id"));
        product.setObjetAVendre(rs.getString("objet_avendre"));
        product.setDescription(rs.getString("description"));
        product.setGenre(rs.getString("genre"));
        product.setEtat(rs.getString("etat"));
        product.setTaille(rs.getString("taille"));
        product.setCouleur(rs.getString("couleur"));
        product.setPrixDeVente(rs.getDouble("prix_de_vente"));
        product.setPrixOriginal(rs.getDouble("prix_original"));
        product.setTelephone(rs.getString("telephone"));
        product.setEmail(rs.getString("email"));
        product.setAdresse(rs.getString("adresse"));
        product.setCodePostal(rs.getString("code_postal"));
        product.setVille(rs.getString("ville"));
        product.setPhoto(rs.getString("photo"));
        return product;
    }

    private void setProductParameters(PreparedStatement pstmt, Product product) throws SQLException {
        pstmt.setInt(1, product.getUserId());
        pstmt.setString(2, product.getObjetAVendre());
        pstmt.setString(3, product.getDescription());
        pstmt.setString(4, product.getGenre());
        pstmt.setString(5, product.getEtat());
        pstmt.setString(6, product.getTaille());
        pstmt.setString(7, product.getCouleur());
        pstmt.setDouble(8, product.getPrixDeVente());
        pstmt.setDouble(9, product.getPrixOriginal());
        pstmt.setString(10, product.getTelephone());
        pstmt.setString(11, product.getEmail());
        pstmt.setString(12, product.getAdresse());
        pstmt.setString(13, product.getCodePostal());
        pstmt.setString(14, product.getVille());
        pstmt.setString(15, product.getPhoto());
    }
} 