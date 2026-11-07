package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkItemDAO {

    public void add(WorkItem item) throws SQLException {
        String sql = "INSERT INTO work_item (order_id, description, cost) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, item.getOrderId());
            ps.setString(2, item.getDescription());
            ps.setDouble(3, item.getCost());
            ps.executeUpdate();
        }
    }

    public List<WorkItem> getAll() throws SQLException {
        String sql = "SELECT id, order_id, description, cost FROM work_item ORDER BY id";
        List<WorkItem> items = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new WorkItem(
                        rs.getLong("id"),
                        rs.getLong("order_id"),
                        rs.getString("description"),
                        rs.getDouble("cost")
                ));
            }
        }
        return items;
    }

    public WorkItem getById(long id) throws SQLException {
        String sql = "SELECT id, order_id, description, cost FROM work_item WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new WorkItem(
                            rs.getLong("id"),
                            rs.getLong("order_id"),
                            rs.getString("description"),
                            rs.getDouble("cost")
                    );
                }
            }
        }
        return null;
    }

    public void update(WorkItem item) throws SQLException {
        String sql = "UPDATE work_item SET order_id = ?, description = ?, cost = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, item.getOrderId());
            ps.setString(2, item.getDescription());
            ps.setDouble(3, item.getCost());
            ps.setLong(4, item.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM work_item WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}