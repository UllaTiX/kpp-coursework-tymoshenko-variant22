package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ClientService {
    private static final Logger LOG = Logger.getLogger(ClientService.class.getName());
    private final ClientDAO dao = new ClientDAO();

    public List<Client> getAll() throws SQLException {
        return dao.getAll();
    }

    public Client getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Client client) throws SQLException {
        ValidationUtil.validateClient(client);
        dao.add(client);
        LOG.info("Додано клієнта");
    }

    public void update(Client client) throws SQLException {
        ValidationUtil.validateClient(client);
        dao.update(client);
        LOG.info("Оновлено клієнта");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено клієнта");
    }
}