DROP DATABASE IF EXISTS auto_service_db;
CREATE DATABASE auto_service_db;
USE auto_service_db;

CREATE TABLE client (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        phone VARCHAR(20) NOT NULL,
                        email VARCHAR(100)
);

CREATE TABLE car (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     client_id BIGINT NOT NULL,
                     brand VARCHAR(50) NOT NULL,
                     model VARCHAR(50) NOT NULL,
                     plate_number VARCHAR(20) NOT NULL,
                     year INT NOT NULL,
                     FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE TABLE service_order (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               car_id BIGINT NOT NULL,
                               state VARCHAR(30) NOT NULL,
                               f1 DOUBLE NOT NULL,
                               f2 DOUBLE NOT NULL,
                               f3 DOUBLE NOT NULL,
                               risk_index DOUBLE NOT NULL,
                               FOREIGN KEY (car_id) REFERENCES car(id)
);

CREATE TABLE work_item (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           order_id BIGINT NOT NULL,
                           description VARCHAR(255) NOT NULL,
                           cost DOUBLE NOT NULL,
                           FOREIGN KEY (order_id) REFERENCES service_order(id)
);

INSERT INTO client (name, phone, email) VALUES
                                            ('Іван Петренко', '+380501112233', 'ivan@gmail.com'),
                                            ('Олена Коваль', '+380679998877', 'olena@ukr.net');

INSERT INTO car (client_id, brand, model, plate_number, year) VALUES
                                                                  (1, 'Toyota', 'Camry', 'BI7777AA', 2020),
                                                                  (2, 'Volkswagen', 'Golf', 'BI1234CB', 2018);

INSERT INTO service_order (car_id, state, f1, f2, f3, risk_index) VALUES
                                                                      (1, 'CREATED', 80.0, 90.0, 85.0, 85.0),
                                                                      (2, 'DIAGNOSED', 40.0, 50.0, 60.0, 50.0);

INSERT INTO work_item (order_id, description, cost) VALUES
                                                        (1, 'Заміна мастила та фільтрів', 1500.0),
                                                        (1, 'Комп’ютерна діагностика', 600.0),
                                                        (2, 'Заміна гальмівних колодок', 2200.0);