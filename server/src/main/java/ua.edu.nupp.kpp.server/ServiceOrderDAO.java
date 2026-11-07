package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrderDAO {

    public void add(ServiceOrder order) throws SQLException {
        String sql = "INSERT INTO service_order (car_id, state, f1, f2, f3, risk_index) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, order.getCarId());
            ps.setString(2, order.getState().name());
            ps.setDouble(3, order.getF1());
            ps.setDouble(4, order.getF2());
            ps.setDouble(5, order.getF3());
            ps.setDouble(6, order.getTotalScore());
            ps.executeUpdate();
        }
    }

    public List<ServiceOrder> getAll() throws SQLException {
        String sql = "SELECT id, car_id, state, f1, f2, f3, risk_index FROM service_order ORDER BY id";
        List<ServiceOrder> orders = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new ServiceOrder(
                        rs.getLong("id"),
                        rs.getLong("car_id"),
                        ServiceOrderState.valueOf(rs.getString("state")),
                        rs.getDouble("f1"),
                        rs.getDouble("f2"),
                        rs.getDouble("f3"),
                        rs.getDouble("risk_index")
                ));
            }
        }
        return orders;
    }

    public ServiceOrder getById(long id) throws SQLException {
        String sql = "SELECT id, car_id, state, f1, f2, f3, risk_index FROM service_order WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ServiceOrder(
                            rs.getLong("id"),
                            rs.getLong("car_id"),
                            ServiceOrderState.valueOf(rs.getString("state")),
                            rs.getDouble("f1"),
                            rs.getDouble("f2"),
                            rs.getDouble("f3"),
                            rs.getDouble("risk_index")
                    );
                }
            }
        }
        return null;
    }

    public void update(ServiceOrder order) throws SQLException {
        String sql = "UPDATE service_order SET car_id = ?, state = ?, f1 = ?, f2 = ?, f3 = ?, risk_index = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, order.getCarId());
            ps.setString(2, order.getState().name());
            ps.setDouble(3, order.getF1());
            ps.setDouble(4, order.getF2());
            ps.setDouble(5, order.getF3());
            ps.setDouble(6, order.getTotalScore());
            ps.setLong(7, order.getId());
            ps.executeUpdate();
        }
    }

    public void updateState(long id, ServiceOrderState state) throws SQLException {
        String sql = "UPDATE service_order SET state = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, state.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM service_order WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}