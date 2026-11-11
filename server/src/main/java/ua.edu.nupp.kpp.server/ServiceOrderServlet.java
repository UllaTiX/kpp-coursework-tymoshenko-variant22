package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class ServiceOrderServlet extends HttpServlet {
    private final ServiceOrderService service = new ServiceOrderService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                ServiceOrder order = new ServiceOrder();
                order.setCarId(Long.valueOf(req.getParameter("carId")));
                order.setState(ServiceOrderState.CREATED);
                order.setF1(Double.parseDouble(req.getParameter("f1")));
                order.setF2(Double.parseDouble(req.getParameter("f2")));
                order.setF3(Double.parseDouble(req.getParameter("f3")));
                service.add(order);
                writeJson(resp, Collections.singletonMap("status", "ok"));
            } else if ("update".equals(action)) {
                ServiceOrder order = new ServiceOrder();
                order.setId(Long.valueOf(req.getParameter("id")));
                order.setCarId(Long.valueOf(req.getParameter("carId")));
                order.setState(ServiceOrderState.valueOf(req.getParameter("state")));
                order.setF1(Double.parseDouble(req.getParameter("f1")));
                order.setF2(Double.parseDouble(req.getParameter("f2")));
                order.setF3(Double.parseDouble(req.getParameter("f3")));
                service.update(order);
                writeJson(resp, Collections.singletonMap("status", "ok"));
            } else if ("transition".equals(action)) {
                Long id = Long.valueOf(req.getParameter("id"));
                ServiceOrderState nextState = ServiceOrderState.valueOf(req.getParameter("state"));
                service.changeState(id, nextState);
                writeJson(resp, Collections.singletonMap("status", "ok"));
            } else if ("delete".equals(action)) {
                Long id = Long.valueOf(req.getParameter("id"));
                service.delete(id);
                writeJson(resp, Collections.singletonMap("status", "ok"));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeJson(resp, Collections.singletonMap("error", "Невідома дія: " + action));
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeJson(resp, Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeJson(resp, Collections.singletonMap("error", e.getMessage()));
        }
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        mapper.writeValue(resp.getWriter(), data);
    }

    private void writeError(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJson(resp, Collections.singletonMap("error", e.getMessage()));
    }
}