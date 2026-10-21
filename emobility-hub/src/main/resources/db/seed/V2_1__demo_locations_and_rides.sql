-- Demo data for the map, the ride flow and the statistics page.

-- Park the seeded fleet around Banja Luka (centre 44.7722, 17.1910).
UPDATE transport_vehicle SET latitude = 44.7722, longitude = 17.1910 WHERE uniqueIdentifier = 'CAR-001';
UPDATE transport_vehicle SET latitude = 44.7759, longitude = 17.1855 WHERE uniqueIdentifier = 'CAR-002';
UPDATE transport_vehicle SET latitude = 44.7681, longitude = 17.2003 WHERE uniqueIdentifier = 'CAR-003';
UPDATE transport_vehicle SET latitude = 44.7795, longitude = 17.1962 WHERE uniqueIdentifier = 'BIC-001';
UPDATE transport_vehicle SET latitude = 44.7703, longitude = 17.1801 WHERE uniqueIdentifier = 'BIC-002';
UPDATE transport_vehicle SET latitude = 44.7738, longitude = 17.2078 WHERE uniqueIdentifier = 'SCO-001';
UPDATE transport_vehicle SET latitude = 44.7664, longitude = 17.1889 WHERE uniqueIdentifier = 'SCO-002';

-- The older demo rentals used a longitude far east of the city.
UPDATE rental SET start_longitude = 17.1910 WHERE start_longitude = 17.9089;

-- More vehicles (manufacturer 1 = Tesla, 2 = BMW, 3 = Xiaomi).
INSERT INTO transport_vehicle (image, isBroken, isRented, model, purchasePrice, rentPrice, uniqueIdentifier, manufacturer_id, latitude, longitude)
VALUES ('', 0, 0, 'i4 eDrive40', 60000, 40, 'CAR-004', 2, 44.7812, 17.1824);
INSERT INTO electric_car (id, description, purchaseDate)
VALUES (LAST_INSERT_ID(), 'BMW i4 Gran Coupe', '2024-02-01 00:00:00');

INSERT INTO transport_vehicle (image, isBroken, isRented, model, purchasePrice, rentPrice, uniqueIdentifier, manufacturer_id, latitude, longitude)
VALUES ('', 1, 0, 'Mi Electric Bike City', 1400, 6, 'BIC-003', 3, 44.7650, 17.1975);
INSERT INTO electric_bicycle (id, rangePerCharge)
VALUES (LAST_INSERT_ID(), 80);

INSERT INTO transport_vehicle (image, isBroken, isRented, model, purchasePrice, rentPrice, uniqueIdentifier, manufacturer_id, latitude, longitude)
VALUES ('', 0, 0, 'Mi Scooter 4 Pro', 900, 5, 'SCO-003', 3, 44.7771, 17.1931);
INSERT INTO electric_scooter (id, maxSpeed)
VALUES (LAST_INSERT_ID(), 25);

INSERT INTO fault (creationDateTime, description, updateDateTime, vehicle_id)
SELECT '2026-10-02 08:20:00', 'Chain keeps slipping', '2026-10-02 08:20:00', id
FROM transport_vehicle WHERE uniqueIdentifier = 'BIC-003';

-- Completed rides in October 2026, so the statistics page has a month to show.
-- Clients 4 and 5 are client1 and client2.
INSERT INTO rental (rentalStart, rentalEnd, duration, distance, price, start_latitude, start_longitude, end_latitude, end_longitude, client_id, vehicle_id, documentType, documentNumber, driverLicenseNumber, paymentCardLast4)
SELECT '2026-10-01 09:00:00', '2026-10-01 10:30:00', 1.5, 9.4, 75.00, 44.7722, 17.1910, 44.7801, 17.2201, 4, id, 'ID_CARD', 'ID-001-JD', 'DL-4411', '4242'
FROM transport_vehicle WHERE uniqueIdentifier = 'CAR-001';
INSERT INTO rental (rentalStart, rentalEnd, duration, distance, price, start_latitude, start_longitude, end_latitude, end_longitude, client_id, vehicle_id, documentType, documentNumber, driverLicenseNumber, paymentCardLast4)
SELECT '2026-10-03 17:10:00', '2026-10-03 18:10:00', 1.0, 6.1, 35.00, 44.7759, 17.1855, 44.7722, 17.1910, 5, id, 'PASSPORT', 'P-7781223', 'DL-9002', '1881'
FROM transport_vehicle WHERE uniqueIdentifier = 'CAR-002';
INSERT INTO rental (rentalStart, rentalEnd, duration, distance, price, start_latitude, start_longitude, end_latitude, end_longitude, client_id, vehicle_id, paymentCardLast4)
SELECT '2026-10-05 12:00:00', '2026-10-05 12:45:00', 0.75, 4.2, 3.75, 44.7795, 17.1962, 44.7703, 17.1801, 4, id, '4242'
FROM transport_vehicle WHERE uniqueIdentifier = 'BIC-001';
INSERT INTO rental (rentalStart, rentalEnd, duration, distance, price, start_latitude, start_longitude, end_latitude, end_longitude, client_id, vehicle_id, paymentCardLast4)
SELECT '2026-10-08 07:30:00', '2026-10-08 07:50:00', 0.33, 2.8, 1.00, 44.7738, 17.2078, 44.7722, 17.1910, 5, id, '1881'
FROM transport_vehicle WHERE uniqueIdentifier = 'SCO-001';
INSERT INTO rental (rentalStart, rentalEnd, duration, distance, price, start_latitude, start_longitude, end_latitude, end_longitude, client_id, vehicle_id, documentType, documentNumber, driverLicenseNumber, paymentCardLast4)
SELECT '2026-10-12 10:00:00', '2026-10-12 13:00:00', 3.0, 28.5, 135.00, 44.7681, 17.2003, 44.8123, 17.2101, 4, id, 'ID_CARD', 'ID-001-JD', 'DL-4411', '4242'
FROM transport_vehicle WHERE uniqueIdentifier = 'CAR-003';
INSERT INTO rental (rentalStart, rentalEnd, duration, distance, price, start_latitude, start_longitude, end_latitude, end_longitude, client_id, vehicle_id, paymentCardLast4)
SELECT '2026-10-15 16:00:00', '2026-10-15 17:30:00', 1.5, 8.0, 12.00, 44.7703, 17.1801, 44.7795, 17.1962, 5, id, '1881'
FROM transport_vehicle WHERE uniqueIdentifier = 'BIC-002';
INSERT INTO rental (rentalStart, rentalEnd, duration, distance, price, start_latitude, start_longitude, end_latitude, end_longitude, client_id, vehicle_id, paymentCardLast4)
SELECT '2026-10-18 08:15:00', '2026-10-18 08:45:00', 0.5, 3.6, 2.00, 44.7664, 17.1889, 44.7738, 17.2078, 4, id, '4242'
FROM transport_vehicle WHERE uniqueIdentifier = 'SCO-002';
INSERT INTO rental (rentalStart, rentalEnd, duration, distance, price, start_latitude, start_longitude, end_latitude, end_longitude, client_id, vehicle_id, documentType, documentNumber, driverLicenseNumber, paymentCardLast4)
SELECT '2026-10-20 09:00:00', '2026-10-20 11:00:00', 2.0, 15.3, 80.00, 44.7812, 17.1824, 44.7722, 17.1910, 5, id, 'PASSPORT', 'P-7781223', 'DL-9002', '1881'
FROM transport_vehicle WHERE uniqueIdentifier = 'CAR-004';
