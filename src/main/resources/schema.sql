CREATE INDEX idx_reservations_overlap
ON reservations (room_type, status, check_in_date, check_out_date);

CREATE TABLE IF NOT EXISTS rooms (
  id INT AUTO_INCREMENT PRIMARY KEY,
  room_no VARCHAR(20) NOT NULL UNIQUE,
  room_type VARCHAR(50) NOT NULL,
  price_per_night DECIMAL(12,2) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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
  ('FV-502', 'Family Villa', 26000.00, 0);
