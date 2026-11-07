package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void add(Client client) throws SQLException {
        String sql = "INSERT INTO client (name, phone) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getPhone());
            ps.executeUpdate();
        }
    }

    public List<Client> getAll() throws SQLException {
        String sql = "SELECT id, name, phone FROM client ORDER BY id";
        List<Client> clients = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }
        }
        return clients;
    }

    public Client getById(long id) throws SQLException {
        String sql = "SELECT id, name, phone FROM client WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("phone")
                    );
                }
            }
        }
        return null;
    }

    public void update(Client client) throws SQLException {
        String sql = "UPDATE client SET name = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getPhone());
            ps.setLong(3, client.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}