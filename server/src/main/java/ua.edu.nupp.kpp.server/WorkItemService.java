package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class WorkItemService {
    private static final Logger LOG = Logger.getLogger(WorkItemService.class.getName());
    private final WorkItemDAO dao = new WorkItemDAO();

    public List<WorkItem> getAll() throws SQLException {
        return dao.getAll();
    }

    public WorkItem getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(WorkItem item) throws SQLException {
        ValidationUtil.validateWorkItem(item);
        dao.add(item);
        LOG.info("Додано роботу");
    }

    public void update(WorkItem item) throws SQLException {
        ValidationUtil.validateWorkItem(item);
        dao.update(item);
        LOG.info("Оновлено роботу");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено роботу");
    }
}