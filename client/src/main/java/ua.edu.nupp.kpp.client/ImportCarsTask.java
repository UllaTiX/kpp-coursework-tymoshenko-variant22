package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportCarsTask extends Task<Integer> {

    private static final Logger LOG = Logger.getLogger(ImportCarsTask.class.getName());
    private static final String EXPECTED_HEADER =
            "license_plate;brand;model;year;client_name;client_phone";

    private final Path csvPath;
    private final HttpAutoServiceClient api;

    public ImportCarsTask(Path csvPath, HttpAutoServiceClient api) {
        this.csvPath = csvPath;
        this.api = api;
    }

    private long resolveClientId(List<Client> clients, String clientName, String clientPhone) {
        if (clients == null || clients.isEmpty()) {
            throw new IllegalStateException("У базі немає жодного клієнта для прив'язки автомобіля.");
        }
        for (Client c : clients) {
            boolean phoneMatch = clientPhone != null && !clientPhone.isBlank()
                    && c.getPhone() != null && c.getPhone().trim().equalsIgnoreCase(clientPhone.trim());
            boolean nameMatch = clientName != null && !clientName.isBlank()
                    && c.getName() != null && c.getName().trim().equalsIgnoreCase(clientName.trim());
            if (phoneMatch || nameMatch) {
                return c.getId();
            }
        }
        return clients.get(0).getId();
    }

    @Override
    protected Integer call() throws Exception {
        LOG.info("Початок CSV-імпорту автомобілів з файлу: " + csvPath.getFileName());
        updateMessage("Читання CSV-файлу...");

        if (csvPath == null || !Files.exists(csvPath) || !Files.isRegularFile(csvPath)) {
            String msg = "CSV-файл не знайдено або недоступний: " + csvPath;
            LOG.warning(msg);
            throw new IOException(msg);
        }

        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            String msg = "CSV-файл порожній (відсутній заголовок).";
            LOG.warning(msg);
            throw new IllegalArgumentException(msg);
        }

        String header = lines.get(0).replace("\uFEFF", "").strip();
        if (!EXPECTED_HEADER.equals(header)) {
            String msg = "Некоректний заголовок CSV. Очікувалося: " + EXPECTED_HEADER
                    + ", отримано: " + header;
            LOG.warning(msg);
            throw new IllegalArgumentException(msg);
        }

        int total = 0;
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).trim().isEmpty()) {
                total++;
            }
        }

        if (total == 0) {
            updateProgress(1, 1);
            updateMessage("Немає рядків для імпорту.");
            LOG.info("CSV містить лише заголовок, записів для імпорту немає.");
            return 0;
        }

        List<Client> clients = api.getClients();

        int processed = 0;
        int imported = 0;
        int skipped = 0;

        for (int i = 1; i < lines.size(); i++) {
            if (isCancelled()) {
                LOG.info("Імпорт скасовано користувачем. Опрацьовано: " + processed + " з " + total);
                updateMessage("Імпорт скасовано. Додано записів: " + imported);
                break;
            }

            String rawLine = lines.get(i);
            if (rawLine.trim().isEmpty()) {
                continue;
            }

            int lineNumber = i + 1;
            String[] fields = rawLine.split(";", -1);

            if (fields.length != 6) {
                skipped++;
                processed++;
                LOG.warning("Рядок " + lineNumber + " пропущено: очікується 6 полів, отримано " + fields.length);
                updateProgress(processed, total);
                updateMessage("Опрацьовано " + processed + " з " + total + " (додано: " + imported + ")");
                continue;
            }

            String licensePlate = fields[0].strip();
            String brand = fields[1].strip();
            String model = fields[2].strip();
            String yearStr = fields[3].strip();
            String clientName = fields[4].strip();
            String clientPhone = fields[5].strip();

            if (licensePlate.isEmpty() || brand.isEmpty() || model.isEmpty()) {
                skipped++;
                processed++;
                LOG.warning("Рядок " + lineNumber + " пропущено: є порожні обов'язкові текстові поля.");
                updateProgress(processed, total);
                updateMessage("Опрацьовано " + processed + " з " + total + " (додано: " + imported + ")");
                continue;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException ex) {
                skipped++;
                processed++;
                LOG.warning("Рядок " + lineNumber + " пропущено: некоректне значення року '" + yearStr + "'.");
                updateProgress(processed, total);
                updateMessage("Опрацьовано " + processed + " з " + total + " (додано: " + imported + ")");
                continue;
            }

            long clientId = resolveClientId(clients, clientName, clientPhone);

            try {
                api.addCar(clientId, licensePlate, brand, model, year);
                imported++;
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Помилка HTTP-запиту при імпорті рядка " + lineNumber + ": " + licensePlate, ex);
                throw ex;
            }

            processed++;
            updateProgress(processed, total);
            updateMessage("Опрацьовано " + processed + " з " + total + " (додано: " + imported + ")");
        }

        if (!isCancelled()) {
            String summary = "Імпорт завершено. Успішно додано: " + imported
                    + ", пропущено: " + skipped + " (усього рядків: " + total + ").";
            updateMessage(summary);
            LOG.info(summary);
        }

        return imported;
    }
}