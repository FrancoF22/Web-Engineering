-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: aule_web
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aula`
--

DROP TABLE IF EXISTS `aula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aula` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `capienza` int DEFAULT NULL,
  `prese_elettriche` int DEFAULT NULL,
  `prese_rete` int DEFAULT NULL,
  `attrezzatura` set('proiettore','schermo motorizzato','schermo manuale','impianto audio','pc fisso','microfono a filo','microfono senza filo','lavagna luminosa','Wi Fi') DEFAULT NULL,
  `nota` text,
  `luogo` varchar(255) DEFAULT NULL,
  `edificio` varchar(255) DEFAULT NULL,
  `piano` varchar(255) DEFAULT NULL,
  `id_responsabile` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`),
  KEY `aula_ibfk_1_idx` (`id_responsabile`),
  CONSTRAINT `aula_ibfk_1` FOREIGN KEY (`id_responsabile`) REFERENCES `utente` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `calendario`
--

DROP TABLE IF EXISTS `calendario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calendario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_aula` int NOT NULL,
  `id_evento` int NOT NULL,
  `ricorrenza` enum('nessuna','giornaliera','settimanale','mensile') NOT NULL DEFAULT 'nessuna',
  `giorno` date NOT NULL,
  `giorno_fine` date DEFAULT NULL,
  `ora_inizio` time NOT NULL,
  `ora_fine` time NOT NULL,
  `ripetizioni` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id_aula` (`id_aula`),
  KEY `id_evento` (`id_evento`),
  CONSTRAINT `calendario_ibfk_1` FOREIGN KEY (`id_aula`) REFERENCES `aula` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `calendario_ibfk_2` FOREIGN KEY (`id_evento`) REFERENCES `evento` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evento`
--

DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `descrizione` text,
  `tipologia` enum('lezione','seminario','esame','parziale','riunione','lauree') NOT NULL DEFAULT 'lezione',
  `id_responsabile` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `evento_ibfk_1_idx` (`id_responsabile`),
  CONSTRAINT `evento_ibfk_1` FOREIGN KEY (`id_responsabile`) REFERENCES `utente` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gruppo`
--

DROP TABLE IF EXISTS `gruppo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gruppo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `descrizione` text,
  `tipologia` enum('polo','dipartimento') NOT NULL DEFAULT 'dipartimento',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gruppo_aula`
--

DROP TABLE IF EXISTS `gruppo_aula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gruppo_aula` (
  `id_gruppo` int NOT NULL,
  `id_aula` int NOT NULL,
  PRIMARY KEY (`id_gruppo`,`id_aula`),
  KEY `id_aula` (`id_aula`),
  CONSTRAINT `gruppo_aula_ibfk_1` FOREIGN KEY (`id_gruppo`) REFERENCES `gruppo` (`id`),
  CONSTRAINT `gruppo_aula_ibfk_2` FOREIGN KEY (`id_aula`) REFERENCES `aula` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `cognome` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_account` varchar(255) NOT NULL,
  `ruolo` enum('admin','studente','responsabile') NOT NULL DEFAULT 'studente',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-11 17:39:12
