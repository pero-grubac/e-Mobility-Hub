-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: localhost    Database: emobility
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `announcement`
--

DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `creationDate` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL,
  `updateDate` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `avatarImage` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `idCardNumber` varchar(255) NOT NULL,
  `isBlocked` bit(1) NOT NULL,
  `isDeactivated` bit(1) NOT NULL,
  `phoneNumber` varchar(255) NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKbfgjs3fem0hmjhvih80158x29` (`email`),
  UNIQUE KEY `UKi8phni7u5da26q6723bc7u058` (`idCardNumber`),
  UNIQUE KEY `UKi3v2milvmr69os4i51m84j5dc` (`phoneNumber`),
  CONSTRAINT `FK4ai00u1wkkeysdaq92yicsf6e` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `electric_bicycle`
--

DROP TABLE IF EXISTS `electric_bicycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `electric_bicycle` (
  `rangePerCharge` double NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKklqu27nnwsp7xgevp581ore36` FOREIGN KEY (`id`) REFERENCES `transport_vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `electric_car`
--

DROP TABLE IF EXISTS `electric_car`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `electric_car` (
  `description` varchar(1500) NOT NULL,
  `purchaseDate` datetime(6) NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKbheb45k40e48heaflric9i7p5` FOREIGN KEY (`id`) REFERENCES `transport_vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `electric_scooter`
--

DROP TABLE IF EXISTS `electric_scooter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `electric_scooter` (
  `maxSpeed` double NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK83q14jgxctc9he69mfp0eymhn` FOREIGN KEY (`id`) REFERENCES `transport_vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fault`
--

DROP TABLE IF EXISTS `fault`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fault` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creationDateTime` datetime(6) NOT NULL,
  `description` varchar(1500) NOT NULL,
  `updateDateTime` datetime(6) NOT NULL,
  `vehicle_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKovralvgd9csxtsdgduu470wqk` (`vehicle_id`),
  CONSTRAINT `FKovralvgd9csxtsdgduu470wqk` FOREIGN KEY (`vehicle_id`) REFERENCES `transport_vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `manufacturer`
--

DROP TABLE IF EXISTS `manufacturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manufacturer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `contactEmail` varchar(255) NOT NULL,
  `contactFax` varchar(255) NOT NULL,
  `contactPhone` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKf8ig5ed0kjjr3x1rcoj0kdg6e` (`contactEmail`),
  UNIQUE KEY `UKhsg5bkpn4j5rtqeqlofhdjtuy` (`contactFax`),
  UNIQUE KEY `UKj7qntacv0h3d89bijl6qott99` (`contactPhone`),
  UNIQUE KEY `UKfvhf6l0xkf8hnay7lvwimnwu1` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(1500) NOT NULL,
  `endDate` datetime(6) NOT NULL,
  `startDate` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rental`
--

DROP TABLE IF EXISTS `rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `distance` double NOT NULL,
  `duration` double NOT NULL,
  `end_latitude` double DEFAULT NULL,
  `end_longitude` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `rentalEnd` datetime(6) NOT NULL,
  `rentalStart` datetime(6) NOT NULL,
  `start_latitude` double DEFAULT NULL,
  `start_longitude` double DEFAULT NULL,
  `client_id` bigint NOT NULL,
  `vehicle_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfurpp295i3dhumquorur054dw` (`client_id`),
  KEY `FKqut92ecm3ty58cf0o71amec` (`vehicle_id`),
  CONSTRAINT `FKfurpp295i3dhumquorur054dw` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
  CONSTRAINT `FKqut92ecm3ty58cf0o71amec` FOREIGN KEY (`vehicle_id`) REFERENCES `transport_vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transport_vehicle`
--

DROP TABLE IF EXISTS `transport_vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transport_vehicle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image` varchar(255) NOT NULL DEFAULT '',
  `isBroken` bit(1) NOT NULL,
  `isRented` bit(1) NOT NULL,
  `model` varchar(255) NOT NULL,
  `purchasePrice` double NOT NULL,
  `rentPrice` double NOT NULL,
  `uniqueIdentifier` varchar(255) NOT NULL,
  `manufacturer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKoosr15w2dc0p29bc4tsri3ge3` (`uniqueIdentifier`),
  KEY `FKi6uoyui6kcaobh9dsgbvjpn5m` (`manufacturer_id`),
  CONSTRAINT `FKi6uoyui6kcaobh9dsgbvjpn5m` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ROLE_ADMIN','ROLE_CLIENT','ROLE_MANAGER','ROLE_OPERATOR','ROLE_PENDING') NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-17 18:31:43
