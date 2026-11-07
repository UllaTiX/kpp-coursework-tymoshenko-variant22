package ua.edu.nupp.kpp.server;

public class ValidationUtil {

    public static void validateClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Клієнт не може бути null");
        }
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Ім'я клієнта не може бути порожнім");
        }
        if (client.getPhone() == null || client.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон клієнта не може бути порожнім");
        }
    }

    public static void validateCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Автомобіль не може бути null");
        }
        if (car.getBrand() == null || car.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Марка автомобіля не може бути порожньою");
        }
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Модель автомобіля не може бути порожньою");
        }
        if (car.getPlateNumber() == null || car.getPlateNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Номерний знак не може бути порожнім");
        }
        if (car.getYear() < 1900 || car.getYear() > 2026) {
            throw new IllegalArgumentException("Некоректний рік випуску автомобіля");
        }
    }

    public static void validateServiceOrder(ServiceOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Замовлення не може бути null");
        }
        if (order.getF1() < 0 || order.getF1() > 100 ||
                order.getF2() < 0 || order.getF2() > 100 ||
                order.getF3() < 0 || order.getF3() > 100) {
            throw new IllegalArgumentException("Значення факторів f1, f2, f3 повинні бути в межах від 0 до 100");
        }
    }

    public static void validateWorkItem(WorkItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Робота не може бути null");
        }
        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Опис роботи не може бути порожнім");
        }
        if (item.getCost() < 0) {
            throw new IllegalArgumentException("Вартість роботи не може бути від'ємною");
        }
    }
}
