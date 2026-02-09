CREATE DATABASE IF NOT EXISTS market_db;
USE market_db;

CREATE TABLE IF NOT EXISTS concerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    available_seats INT NOT NULL,
    event_date_time DATETIME(6) NOT NULL,
    version BIGINT DEFAULT 0
);

INSERT IGNORE INTO concerts (id, available_seats, event_date_time, version)
VALUES (1, 100, NOW() + INTERVAL 10 DAY, 0);
