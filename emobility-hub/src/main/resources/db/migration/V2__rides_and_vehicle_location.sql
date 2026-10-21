-- Where each vehicle is now, so operators can see the fleet on a map.
-- The client app moves a vehicle to the drop-off point when a ride ends.
ALTER TABLE `transport_vehicle`
  ADD COLUMN `latitude` double DEFAULT NULL,
  ADD COLUMN `longitude` double DEFAULT NULL;

-- A rental is created when the ride starts and completed when it ends, so the
-- end time, duration, distance and price stay empty while the ride is going.
-- Renting a car also needs an identity document (ID card for locals, passport
-- for foreigners) and a driver's licence number. Only the last four digits of
-- the payment card are kept.
ALTER TABLE `rental`
  MODIFY `rentalEnd` datetime(6) DEFAULT NULL,
  MODIFY `duration` double DEFAULT NULL,
  MODIFY `distance` double DEFAULT NULL,
  ADD COLUMN `documentType` enum('ID_CARD','PASSPORT') DEFAULT NULL,
  ADD COLUMN `documentNumber` varchar(64) DEFAULT NULL,
  ADD COLUMN `driverLicenseNumber` varchar(64) DEFAULT NULL,
  ADD COLUMN `paymentCardLast4` varchar(4) DEFAULT NULL;
