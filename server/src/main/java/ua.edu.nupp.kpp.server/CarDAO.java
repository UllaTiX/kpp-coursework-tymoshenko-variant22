package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    public void add(Car car) throws SQLException {
        String sql = "INSERT INTO car (client_id, brand, model, plate_number, year) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, car.getClientId());
            ps.setString(2, car.getBrand());
            ps.setString(3, car.getModel());
            ps.setString(4, car.getPlateNumber());
            ps.setInt(5, car.getYear());
            ps.executeUpdate();
        }
    }

    public List<Car> getAll() throws SQLException {
        String sql = "SELECT id, client_id, brand, model, plate_number, year FROM car ORDER BY id";
        List<Car> cars = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cars.add(new Car(
                        rs.getLong("id"),
                        rs.getLong("client_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("plate_number"),
                        rs.getInt("year")
                ));
            }
        }
        return cars;
    }

    public Car getById(long id) throws SQLException {
        String sql = "SELECT id, client_id, brand, model, plate_number, year FROM car WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Car(
                            rs.getLong("id"),
                            rs.getLong("client_id"),
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getString("plate_number"),
                            rs.getInt("year")
                    );
                }
            }
        }
        return null;
    }

    public void update(Car car) throws SQLException {
        String sql = "UPDATE car SET client_id = ?, brand = ?, model = ?, plate_number = ?, year = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, car.getClientId());
            ps.setString(2, car.getBrand());
            ps.setString(3, car.getModel());
            ps.setString(4, car.getPlateNumber());
            ps.setInt(5, car.getYear());
            ps.setLong(6, car.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM car WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}