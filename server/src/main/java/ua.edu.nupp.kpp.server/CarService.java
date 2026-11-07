package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class CarService {
    private static final Logger LOG = Logger.getLogger(CarService.class.getName());
    private final CarDAO dao = new CarDAO();

    public List<Car> getAll() throws SQLException {
        return dao.getAll();
    }

    public Car getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Car car) throws SQLException {
        ValidationUtil.validateCar(car);
        dao.add(car);
        LOG.info("Додано автомобіль");
    }

    public void update(Car car) throws SQLException {
        ValidationUtil.validateCar(car);
        dao.update(car);
        LOG.info("Оновлено автомобіль");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено автомобіль");
    }
}