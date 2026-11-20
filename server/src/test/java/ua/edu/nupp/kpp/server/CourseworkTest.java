package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseworkTest {

    // --- 1. Тести RiskCalculator.calculate(f1, f2, f3) ---

    @Test
    void calculateRiskNormalValues() {
        double result = RiskCalculator.calculate(10.0, 20.0, 30.0);
        // 0.45 * 10 + 0.35 * 20 + 0.15 * 30 = 4.5 + 7.0 + 4.5 = 16.0
        assertEquals(18.5, result, 0.0001);
    }

    @Test
    void calculateRiskZeroValues() {
        double result = RiskCalculator.calculate(0.0, 0.0, 0.0);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    void calculateRiskMaxValues() {
        double result = RiskCalculator.calculate(100.0, 100.0, 100.0);
        // 0.45*100 + 0.35*100 + 0.15*100 = 95.0
        assertEquals(100.0, result, 0.0001);
    }

    @Test
    void calculateRiskOutOfRangeThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> RiskCalculator.calculate(-1.0, 20.0, 30.0));
        assertThrows(IllegalArgumentException.class,
                () -> RiskCalculator.calculate(10.0, 101.0, 30.0));
        assertThrows(IllegalArgumentException.class,
                () -> RiskCalculator.calculate(10.0, 20.0, 150.0));
    }

    // --- 2. Тести ValidationUtil ---

    @Test
    void validateClientNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateClient(null));
    }

    @Test
    void validateClientEmptyNameOrPhoneThrowsException() {
        Client emptyName = new Client();
        emptyName.setName("   ");
        emptyName.setPhone("+380501234567");
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateClient(emptyName));

        Client emptyPhone = new Client();
        emptyPhone.setName("Іван Петренко");
        emptyPhone.setPhone("");
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateClient(emptyPhone));
    }

    @Test
    void validateValidClientPasses() {
        Client client = new Client();
        client.setName("Іван Петренко");
        client.setPhone("+380501234567");
        assertDoesNotThrow(() -> ValidationUtil.validateClient(client));
    }

    @Test
    void validateCarNullAndInvalidYearThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateCar(null));

        Car badYearCar = new Car();
        badYearCar.setBrand("Toyota");
        badYearCar.setModel("Camry");
        badYearCar.setPlateNumber("AA1234BB");
        badYearCar.setYear(1800);
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateCar(badYearCar));
    }

    @Test
    void validateValidCarPasses() {
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Camry");
        car.setPlateNumber("AA1234BB");
        car.setYear(2020);
        assertDoesNotThrow(() -> ValidationUtil.validateCar(car));
    }

    @Test
    void validateServiceOrderNullAndFactorsOutOfRangeThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateServiceOrder(null));

        ServiceOrder badOrder = new ServiceOrder();
        badOrder.setF1(150.0);
        badOrder.setF2(20.0);
        badOrder.setF3(30.0);
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateServiceOrder(badOrder));
    }

    @Test
    void validateWorkItemNullAndNegativeCostThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateWorkItem(null));

        WorkItem badItem = new WorkItem();
        badItem.setDescription("Заміна масла");
        badItem.setCost(-50.0);
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateWorkItem(badItem));
    }

    // --- 3. Тести ServiceOrderState ---

    @Test
    void serviceOrderStateContainsExpectedValues() {
        assertNotNull(ServiceOrderState.valueOf("NEW"));
        assertNotNull(ServiceOrderState.valueOf("CREATED"));
        assertNotNull(ServiceOrderState.valueOf("DIAGNOSED"));
        assertNotNull(ServiceOrderState.valueOf("IN_PROGRESS"));
        assertNotNull(ServiceOrderState.valueOf("CLOSED"));
        assertEquals(5, ServiceOrderState.values().length);
    }
}