package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

public class MainApp extends Application {

    private final HttpAutoServiceClient api = new HttpAutoServiceClient();

    private final TableView<Client> clientTable = new TableView<>();
    private final TableView<Car> carTable = new TableView<>();
    private final TableView<ServiceOrder> orderTable = new TableView<>();
    private final TableView<WorkItem> workItemTable = new TableView<>();

    private final TextField clientNameField = new TextField();
    private final TextField clientPhoneField = new TextField();
    private final TextField clientEmailField = new TextField();
    private Long selectedClientId = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Інформаційна система сервісного обслуговування автомобілів (Клієнт)");

        TabPane tabPane = new TabPane();

        Tab clientTab = new Tab("Клієнти", createClientView());
        Tab carTab = new Tab("Автомобілі", createCarView());
        Tab orderTab = new Tab("Замовлення", createOrderView());
        Tab workItemTab = new Tab("Роботи", createWorkItemView());

        tabPane.getTabs().addAll(clientTab, carTab, orderTab, workItemTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        refreshAll();
    }

    private VBox createClientView() {
        TableColumn<Client, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Client, String> nameCol = new TableColumn<>("Ім'я");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Client, String> phoneCol = new TableColumn<>("Телефон");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Client, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        clientTable.getColumns().addAll(idCol, nameCol, phoneCol, emailCol);

        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedClientId = newVal.getId();
                clientNameField.setText(newVal.getName());
                clientPhoneField.setText(newVal.getPhone());
                clientEmailField.setText(newVal.getEmail());
            }
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        form.add(new Label("Ім'я:"), 0, 0);
        form.add(clientNameField, 1, 0);
        form.add(new Label("Телефон:"), 0, 1);
        form.add(clientPhoneField, 1, 1);
        form.add(new Label("Email:"), 0, 2);
        form.add(clientEmailField, 1, 2);

        Button addBtn = new Button("Додати");
        Button updateBtn = new Button("Змінити");
        Button deleteBtn = new Button("Видалити");
        Button refreshBtn = new Button("Оновити");

        addBtn.setOnAction(e -> {
            try {
                api.addClient(clientNameField.getText(), clientPhoneField.getText(), clientEmailField.getText());
                refreshClients();
                clearClientForm();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        deleteBtn.setOnAction(ev -> {
            if (selectedClientId == null) {
                showError(new RuntimeException("Виберіть запис для видалення"));
                return;
            }
            try {
                api.deleteClient(selectedClientId);
                refreshClients();
                clearClientForm();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        refreshBtn.setOnAction(e -> refreshAll());

        HBox buttons = new HBox(10, addBtn, updateBtn, deleteBtn, refreshBtn);
        buttons.setPadding(new Insets(10));

        VBox layout = new VBox(10, clientTable, form, buttons);
        layout.setPadding(new Insets(10));
        return layout;
    }

    private VBox createCarView() {
        TableColumn<Car, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Car, Long> clientCol = new TableColumn<>("ID Клієнта");
        clientCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        TableColumn<Car, String> brandCol = new TableColumn<>("Марка");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Car, String> modelCol = new TableColumn<>("Модель");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<Car, String> plateCol = new TableColumn<>("Держ. номер");
        plateCol.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));
        TableColumn<Car, Integer> yearCol = new TableColumn<>("Рік");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        carTable.getColumns().addAll(idCol, clientCol, brandCol, modelCol, plateCol, yearCol);

        Button refreshBtn = new Button("Оновити");
        refreshBtn.setOnAction(e -> refreshCars());

        VBox layout = new VBox(10, carTable, refreshBtn);
        layout.setPadding(new Insets(10));
        return layout;
    }

    private VBox createOrderView() {
        TableColumn<ServiceOrder, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<ServiceOrder, Long> carCol = new TableColumn<>("ID Авто");
        carCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        TableColumn<ServiceOrder, ServiceOrderState> stateCol = new TableColumn<>("Статус");
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        TableColumn<ServiceOrder, Double> t1Col = new TableColumn<>("T1");
        t1Col.setCellValueFactory(new PropertyValueFactory<>("t1"));
        TableColumn<ServiceOrder, Double> t2Col = new TableColumn<>("T2");
        t2Col.setCellValueFactory(new PropertyValueFactory<>("t2"));
        TableColumn<ServiceOrder, Double> t3Col = new TableColumn<>("T3");
        t3Col.setCellValueFactory(new PropertyValueFactory<>("t3"));
        TableColumn<ServiceOrder, Double> riskCol = new TableColumn<>("Ризик");
        riskCol.setCellValueFactory(new PropertyValueFactory<>("riskIndex"));

        orderTable.getColumns().addAll(idCol, carCol, stateCol, t1Col, t2Col, t3Col, riskCol);

        Button refreshBtn = new Button("Оновити");
        refreshBtn.setOnAction(e -> refreshOrders());

        VBox layout = new VBox(10, orderTable, refreshBtn);
        layout.setPadding(new Insets(10));
        return layout;
    }

    private VBox createWorkItemView() {
        TableColumn<WorkItem, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<WorkItem, Long> orderCol = new TableColumn<>("ID Замовлення");
        orderCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        TableColumn<WorkItem, String> descCol = new TableColumn<>("Опис робіт");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<WorkItem, Double> costCol = new TableColumn<>("Вартість");
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));

        workItemTable.getColumns().addAll(idCol, orderCol, descCol, costCol);

        Button refreshBtn = new Button("Оновити");
        refreshBtn.setOnAction(e -> refreshWorkItems());

        VBox layout = new VBox(10, workItemTable, refreshBtn);
        layout.setPadding(new Insets(10));
        return layout;
    }

    private void refreshAll() {
        refreshClients();
        refreshCars();
        refreshOrders();
        refreshWorkItems();
    }

    private void refreshClients() {
        try {
            List<Client> clients = api.getClients();
            clientTable.setItems(FXCollections.observableArrayList(clients));
        } catch (Exception e) {
            showError(e);
        }
    }

    private void refreshCars() {
        try {
            List<Car> cars = api.getCars();
            carTable.setItems(FXCollections.observableArrayList(cars));
        } catch (Exception e) {
            showError(e);
        }
    }

    private void refreshOrders() {
        try {
            List<ServiceOrder> orders = api.getOrders();
            orderTable.setItems(FXCollections.observableArrayList(orders));
        } catch (Exception e) {
            showError(e);
        }
    }

    private void refreshWorkItems() {
        try {
            List<WorkItem> items = api.getWorkItems();
            workItemTable.setItems(FXCollections.observableArrayList(items));
        } catch (Exception e) {
            showError(e);
        }
    }

    private void clearClientForm() {
        clientNameField.clear();
        clientPhoneField.clear();
        clientEmailField.clear();
        selectedClientId = null;
    }



    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText("Сталася помилка під час виконання операції");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void importCarsFromCsv() {
        try {
            Path file = Path.of("sample-data", "cars.csv");
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

            if (lines.isEmpty()) {
                return;
            }

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank()) continue;

                String[] parts = line.split(";");
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Некоректна кількість стовпців у рядку: " + parts.length);
                }

                String licensePlate = parts[0].trim();
                String brand = parts[1].trim();
                String model = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                String clientName = parts[4].trim();
                String clientPhone = parts[5].trim();

                new HttpAutoServiceClient().addCar(licensePlate, brand, model, year, clientName, clientPhone);
            }

            refreshAll();
            System.out.println("Імпорт автомобілів з CSV успішно виконано!");
        } catch (Exception e) {
            showError(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}