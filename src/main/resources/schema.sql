CREATE DATABASE IF NOT EXISTS foodbridge;
USE foodbridge;

-- The application creates/updates these tables using JPA (spring.jpa.hibernate.ddl-auto=update).
-- Reference schema for the report:
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  organization_name VARCHAR(120) NOT NULL,
  location VARCHAR(160) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL,
  verified BOOLEAN NOT NULL DEFAULT FALSE,
  created_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS donations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  restaurant_id BIGINT NOT NULL,
  ngo_id BIGINT NULL,
  food_name VARCHAR(140) NOT NULL,
  food_type VARCHAR(40) NOT NULL,
  quantity DECIMAL(10,2) NOT NULL,
  unit VARCHAR(20) NOT NULL,
  pickup_location VARCHAR(250) NOT NULL,
  prepared_at DATETIME NOT NULL,
  expiry_at DATETIME NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  CONSTRAINT fk_donation_restaurant FOREIGN KEY (restaurant_id) REFERENCES users(id),
  CONSTRAINT fk_donation_ngo FOREIGN KEY (ngo_id) REFERENCES users(id)
);
