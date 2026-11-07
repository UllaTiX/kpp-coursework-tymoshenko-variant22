package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ServiceOrderService {
    private static final Logger LOG = Logger.getLogger(ServiceOrderService.class.getName());
    private final ServiceOrderDAO dao = new ServiceOrderDAO();

    public List<ServiceOrder> getAll() throws SQLException {
        return dao.getAll();
    }

    public ServiceOrder getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(ServiceOrder order) throws SQLException {
        ValidationUtil.validateServiceOrder(order);
        order.setState(ServiceOrderState.CREATED);
        double calculatedRisk = RiskCalculator.calculate(order.getF1(), order.getF2(), order.getF3());
        order.setTotalScore(calculatedRisk);
        dao.add(order);
        LOG.info("Створено замовлення");
    }

    public void update(ServiceOrder order) throws SQLException {
        ValidationUtil.validateServiceOrder(order);
        double calculatedRisk = RiskCalculator.calculate(order.getF1(), order.getF2(), order.getF3());
        order.setTotalScore(calculatedRisk);
        dao.update(order);
        LOG.info("Оновлено замовлення");
    }

    public static boolean isAllowedTransition(ServiceOrderState from, ServiceOrderState to) {
        return (from == ServiceOrderState.CREATED && to == ServiceOrderState.DIAGNOSED)
                || (from == ServiceOrderState.DIAGNOSED && to == ServiceOrderState.IN_PROGRESS)
                || (from == ServiceOrderState.IN_PROGRESS && to == ServiceOrderState.CLOSED);
    }

    public void changeState(long id, ServiceOrderState nextState) throws SQLException {
        ServiceOrder current = dao.getById(id);
        if (current == null) {
            LOG.warning("Замовлення з id = " + id + " не знайдено");
            throw new IllegalArgumentException("Замовлення не знайдено");
        }
        if (!isAllowedTransition(current.getState(), nextState)) {
            LOG.warning("Недопустимий перехід стану: " + current.getState() + " -> " + nextState);
            throw new IllegalArgumentException("Недопустимий перехід стану");
        }
        dao.updateState(id, nextState);
        LOG.info("Змінено стан замовлення: " + current.getState() + " -> " + nextState);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено замовлення");
    }
}