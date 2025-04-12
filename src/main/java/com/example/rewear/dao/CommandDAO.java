package com.example.rewear.dao;

import com.example.rewear.DBUtil;
import com.example.rewear.model.Command;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandDAO {
    
    public List<Command> getAllCommands() {
        List<Command> commands = new ArrayList<>();
        String query = "SELECT * FROM commands";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Command command = mapResultSetToCommand(rs);
                commands.add(command);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    public Command getCommandById(int id) {
        String query = "SELECT * FROM commands WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCommand(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Command> getCommandsByUserId(int userId) {
        List<Command> commands = new ArrayList<>();
        String query = "SELECT * FROM commands WHERE user_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Command command = mapResultSetToCommand(rs);
                commands.add(command);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    public boolean addCommand(Command command) {
        String query = "INSERT INTO commands (user_id, etat, methode_paiement, created_at, " +
                      "adresse_livraison, code_postal_livraison, ville_livraison, pays_livraison) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            setCommandParameters(pstmt, command);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        command.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCommand(Command command) {
        String query = "UPDATE commands SET etat = ?, methode_paiement = ?, " +
                      "adresse_livraison = ?, code_postal_livraison = ?, " +
                      "ville_livraison = ?, pays_livraison = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, command.getEtat());
            pstmt.setString(2, command.getMethodePaiement());
            pstmt.setString(3, command.getAdresseLivraison());
            pstmt.setString(4, command.getCodePostalLivraison());
            pstmt.setString(5, command.getVilleLivraison());
            pstmt.setString(6, command.getPaysLivraison());
            pstmt.setInt(7, command.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCommand(int id) {
        String query = "DELETE FROM commands WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addProductToCommand(int commandId, int productId) {
        String query = "INSERT INTO command_products (command_id, product_id) VALUES (?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, commandId);
            pstmt.setInt(2, productId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Command mapResultSetToCommand(ResultSet rs) throws SQLException {
        Command command = new Command();
        command.setId(rs.getInt("id"));
        command.setUserId(rs.getInt("user_id"));
        command.setEtat(rs.getString("etat"));
        command.setMethodePaiement(rs.getString("methode_paiement"));
        command.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        command.setAdresseLivraison(rs.getString("adresse_livraison"));
        command.setCodePostalLivraison(rs.getString("code_postal_livraison"));
        command.setVilleLivraison(rs.getString("ville_livraison"));
        command.setPaysLivraison(rs.getString("pays_livraison"));
        return command;
    }

    private void setCommandParameters(PreparedStatement pstmt, Command command) throws SQLException {
        pstmt.setInt(1, command.getUserId());
        pstmt.setString(2, command.getEtat());
        pstmt.setString(3, command.getMethodePaiement());
        pstmt.setTimestamp(4, Timestamp.valueOf(command.getCreatedAt()));
        pstmt.setString(5, command.getAdresseLivraison());
        pstmt.setString(6, command.getCodePostalLivraison());
        pstmt.setString(7, command.getVilleLivraison());
        pstmt.setString(8, command.getPaysLivraison());
    }
} 