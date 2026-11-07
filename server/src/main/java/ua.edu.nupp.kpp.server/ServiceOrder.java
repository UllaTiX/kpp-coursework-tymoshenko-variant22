package ua.edu.nupp.kpp.server;

public class ServiceOrder {
    private Long id;
    private long carId;
    private ServiceOrderState state;
    private double f1;
    private double f2;
    private double f3;
    private double totalScore;

    public ServiceOrder() {}

    public ServiceOrder(Long id, long carId, ServiceOrderState state, double f1, double f2, double f3, double totalScore) {
        this.id = id;
        this.carId = carId;
        this.state = state;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.totalScore = totalScore;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public long getCarId() { return carId; }
    public void setCarId(long carId) { this.carId = carId; }

    public ServiceOrderState getState() { return state; }
    public void setState(ServiceOrderState state) { this.state = state; }

    public double getF1() { return f1; }
    public void setF1(double f1) { this.f1 = f1; }

    public double getF2() { return f2; }
    public void setF2(double f2) { this.f2 = f2; }

    public double getF3() { return f3; }
    public void setF3(double f3) { this.f3 = f3; }

    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }
}