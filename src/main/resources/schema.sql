-- Ocean View Resort baseline schema (MySQL 8+)

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('ADMIN', 'STAFF') NOT NULL DEFAULT 'STAFF',
  active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rooms (
  id INT AUTO_INCREMENT PRIMARY KEY,
  room_no VARCHAR(20) NOT NULL UNIQUE,
  room_type VARCHAR(50) NOT NULL,
  price_per_night DECIMAL(12,2) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reservations (
  id INT AUTO_INCREMENT PRIMARY KEY,
  reservation_no VARCHAR(30) NOT NULL UNIQUE,
  guest_full_name VARCHAR(120) NOT NULL,
  guest_email VARCHAR(150) NOT NULL,
  contact_number VARCHAR(25) NOT NULL,
  room_type VARCHAR(50) NOT NULL,
  room_id INT NULL,
  number_of_guests INT NOT NULL,
  check_in_date DATE NOT NULL,
  check_out_date DATE NOT NULL,
  special_requests TEXT NULL,
  status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'CHECKED_IN') NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reservations_room
    FOREIGN KEY (room_id) REFERENCES rooms(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);

-- Backward-compatible migration for older reservation schema variants.
ALTER TABLE reservations ADD COLUMN IF NOT EXISTS room_id INT NULL AFTER room_type;
ALTER TABLE reservations
  MODIFY COLUMN status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'CHECKED_IN') NOT NULL DEFAULT 'PENDING';

DROP INDEX IF EXISTS idx_reservations_overlap ON reservations;
CREATE INDEX idx_reservations_overlap
ON reservations (room_id, status, check_in_date, check_out_date);

DROP INDEX IF EXISTS idx_reservations_created_at ON reservations;
CREATE INDEX idx_reservations_created_at
ON reservations (created_at);

INSERT INTO users (name, email, password, role, active)
VALUES ('System Admin', 'admin@oceanview.local', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE role = VALUES(role), active = VALUES(active);

INSERT INTO rooms (room_no, room_type, price_per_night, active) VALUES
  ('GV-101', 'Garden View', 12000.00, 1),
  ('GV-102', 'Garden View', 12000.00, 1),
  ('OS-201', 'Ocean Suite', 22000.00, 1),
  ('OS-202', 'Ocean Suite', 22000.00, 1),
  ('DK-301', 'Deluxe King', 18000.00, 1),
  ('DK-302', 'Deluxe King', 18000.00, 1),
  ('DQ-401', 'Deluxe Queen', 17000.00, 1),
  ('DQ-402', 'Deluxe Queen', 17000.00, 1),
  ('FV-501', 'Family Villa', 26000.00, 1),
  ('FV-502', 'Family Villa', 26000.00, 0)
ON DUPLICATE KEY UPDATE
  room_type = VALUES(room_type),
  price_per_night = VALUES(price_per_night),
  active = VALUES(active);
