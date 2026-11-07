package ua.edu.nupp.kpp.server;

public class WorkItem {
    private Long id;
    private long orderId;
    private String description;
    private double cost;

    public WorkItem() {}

    public WorkItem(Long id, long orderId, String description, double cost) {
        this.id = id;
        this.orderId = orderId;
        this.description = description;
        this.cost = cost;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public long getOrderId() { return orderId; }
    public void setOrderId(long orderId) { this.orderId = orderId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
}