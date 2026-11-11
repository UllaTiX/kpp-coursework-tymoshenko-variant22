package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceOrder {
    private Long id;
    private Long carId;
    private ServiceOrderState state;
    private Double t1;
    private Double t2;
    private Double t3;
    private Double riskIndex;

    public ServiceOrder() {
    }

    public ServiceOrder(Long id, Long carId, ServiceOrderState state, Double t1, Double t2, Double t3, Double riskIndex) {
        this.id = id;
        this.carId = carId;
        this.state = state;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.riskIndex = riskIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public ServiceOrderState getState() {
        return state;
    }

    public void setState(ServiceOrderState state) {
        this.state = state;
    }

    public Double getT1() {
        return t1;
    }

    public void setT1(Double t1) {
        this.t1 = t1;
    }

    public Double getT2() {
        return t2;
    }

    public void setT2(Double t2) {
        this.t2 = t2;
    }

    public Double getT3() {
        return t3;
    }

    public void setT3(Double t3) {
        this.t3 = t3;
    }

    public Double getRiskIndex() {
        return riskIndex;
    }

    public void setRiskIndex(Double riskIndex) {
        this.riskIndex = riskIndex;
    }
}