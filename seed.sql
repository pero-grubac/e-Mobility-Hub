-- admin/admin123, operator/operator, manager/manager
INSERT INTO user (username, password, firstName, lastName, role)
VALUES 
  ('admin', '$2b$10$kHDwVvuefRO/s7UCRuzu8ewulwZ0hW4lWhgy0fB4Xmd7CJ8dNAj0q', 'Admin', 'Admin', 'ROLE_ADMIN'),
  ('operator', '$2a$10$or7i45/QIqoVeJ/lGTEqie0mA63JBU23zfLDzz1QN/sQmPGOZd6uG', 'Operator', 'Operator', 'ROLE_OPERATOR'),
  ('manager', '$2a$10$zDOBi8hTCFWyG369GaPH0evYgMWCwwaVg7ZBnoOP1dV36V3VWINFK', 'Manager', 'Manager', 'ROLE_MANAGER'),
  ('client1', '$2a$10$or7i45/QIqoVeJ/lGTEqie0mA63JBU23zfLDzz1QN/sQmPGOZd6uG', 'John', 'Doe', 'ROLE_CLIENT'),
  ('client2', '$2a$10$or7i45/QIqoVeJ/lGTEqie0mA63JBU23zfLDzz1QN/sQmPGOZd6uG', 'Jane', 'Smith', 'ROLE_CLIENT');

-- Sample clients (password: operator for both)
INSERT INTO client (id, email, phoneNumber, idCardNumber, avatarImage, isDeactivated, isBlocked)
VALUES 
  (4, 'john.doe@example.com', '+387-65-111-111', 'ID-001-JD', NULL, 0, 0),
  (5, 'jane.smith@example.com', '+387-65-222-222', 'ID-002-JS', NULL, 0, 0);

-- Sample manufacturers
INSERT INTO manufacturer (name, country, address, contactEmail, contactPhone, contactFax)
VALUES 
  ('Tesla', 'USA', '1 Tesla Road, Austin, TX', 'contact@tesla.com', '+1-800-000-0001', '+1-800-000-0002'),
  ('BMW', 'Germany', 'Petuelring 130, Munich', 'contact@bmw.com', '+49-89-382-0', '+49-89-382-1'),
  ('Xiaomi', 'China', 'No. 68 Qinghe Middle Street, Beijing', 'contact@xiaomi.com', '+86-10-6095-0000', '+86-10-6095-0001');

-- Sample electric cars (manufacturer_id 1 = Tesla, 2 = BMW)
INSERT INTO transport_vehicle (image, isBroken, isRented, model, purchasePrice, rentPrice, uniqueIdentifier, manufacturer_id)
VALUES 
  ('', 0, 0, 'Model S', 80000, 50, 'CAR-001', 1),
  ('', 0, 0, 'Model 3', 45000, 35, 'CAR-002', 1),
  ('', 0, 0, 'iX', 75000, 45, 'CAR-003', 2);

INSERT INTO electric_car (id, description, purchaseDate)
VALUES 
  (1, 'Tesla Model S Long Range', '2023-01-15 00:00:00'),
  (2, 'Tesla Model 3 Standard', '2023-03-20 00:00:00'),
  (3, 'BMW iX xDrive50', '2023-06-10 00:00:00');

-- Sample electric bicycles (manufacturer_id 3 = Xiaomi)
INSERT INTO transport_vehicle (image, isBroken, isRented, model, purchasePrice, rentPrice, uniqueIdentifier, manufacturer_id)
VALUES 
  ('', 0, 0, 'Mi Electric Bike', 1200, 5, 'BIC-001', 3),
  ('', 0, 0, 'Mi Electric Bike Pro', 1800, 8, 'BIC-002', 3);

INSERT INTO electric_bicycle (id, rangePerCharge)
VALUES 
  (4, 100),
  (5, 150);

-- Sample electric scooters (manufacturer_id 3 = Xiaomi)
INSERT INTO transport_vehicle (image, isBroken, isRented, model, purchasePrice, rentPrice, uniqueIdentifier, manufacturer_id)
VALUES 
  ('', 0, 0, 'Mi Scooter 3', 500, 3, 'SCO-001', 3),
  ('', 0, 0, 'Mi Scooter Pro 2', 800, 4, 'SCO-002', 3);

INSERT INTO electric_scooter (id, maxSpeed)
VALUES 
  (6, 25),
  (7, 30);

-- Sample faults
INSERT INTO fault (creationDateTime, description, updateDateTime, vehicle_id)
VALUES 
  ('2024-01-10 10:00:00', 'Battery not charging properly', '2024-01-10 10:00:00', 1),
  ('2024-02-15 14:30:00', 'Brake issue on rear wheel', '2024-02-15 14:30:00', 4),
  ('2024-03-20 09:15:00', 'Motor making unusual noise', '2024-03-20 09:15:00', 6);

-- Sample rentals
INSERT INTO rental (distance, duration, end_latitude, end_longitude, price, rentalEnd, rentalStart, start_latitude, start_longitude, client_id, vehicle_id)
VALUES 
  (12.50, 1.50, 44.7800, 17.9100, 9.38, '2024-03-01 11:30:00', '2024-03-01 10:00:00', 44.7722, 17.9089, 4, 2),
  (8.20, 0.75, 44.7850, 17.9200, 3.08, '2024-03-05 15:45:00', '2024-03-05 15:00:00', 44.7722, 17.9089, 5, 4),
  (25.00, 2.00, 44.7900, 17.9300, 15.00, '2024-03-10 14:00:00', '2024-03-10 12:00:00', 44.7722, 17.9089, 4, 1),
  (5.30, 0.50, 44.7750, 17.9150, 1.59, '2024-03-15 10:30:00', '2024-03-15 10:00:00', 44.7722, 17.9089, 5, 6);

-- Sample promotions
INSERT INTO promotion (content, endDate, startDate, title)
VALUES 
  ('Get 20% off on all electric car rentals this weekend!', '2024-12-31 23:59:59', '2024-12-01 00:00:00', 'Weekend Car Special'),
  ('Rent any bicycle for free on your first rental!', '2024-11-30 23:59:59', '2024-11-01 00:00:00', 'Free First Bicycle Ride'),
  ('50% discount on scooter rentals every Monday!', '2025-06-30 23:59:59', '2025-01-01 00:00:00', 'Monday Scooter Deal');

-- Sample announcements
INSERT INTO announcement (content, creationDate, title, updateDate)
VALUES 
  ('We have added 10 new Tesla Model 3 vehicles to our fleet in Banja Luka.', '2024-02-15 10:00:00', 'New Vehicles Added', '2024-02-15 10:00:00'),
  ('Our offices will be closed on January 1st for New Year.', '2024-12-20 08:00:00', 'Holiday Notice', '2024-12-20 08:00:00');