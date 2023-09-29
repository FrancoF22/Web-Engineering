-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: aule_web
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `aule`
--

DROP TABLE IF EXISTS `aule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `capienza` int DEFAULT NULL,
  `prese_elettriche` int DEFAULT NULL,
  `prese_rete` int DEFAULT NULL,
  `attrezzatura` set('proiettore','schermo motorizzato','schermo manuale','impianto audio','pc fisso','microfono a filo','microfono senza filo','lavagna luminosa','Wi Fi') DEFAULT NULL,
  `note` text,
  `luogo` varchar(255) DEFAULT NULL,
  `edificio` varchar(255) DEFAULT NULL,
  `piano` varchar(255) DEFAULT NULL,
  `id_responsabile` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`),
  KEY `id_responsabile` (`id_responsabile`),
  CONSTRAINT `aule_ibfk_1` FOREIGN KEY (`id_responsabile`) REFERENCES `responsabile` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aule`
--

LOCK TABLES `aule` WRITE;
/*!40000 ALTER TABLE `aule` DISABLE KEYS */;
INSERT INTO `aule` VALUES (1,'Aula 1',50,10,5,'proiettore,schermo motorizzato','Aula con proiettore e schermo motorizzato','Coppito','Edificio A','Piano 1',1);
/*!40000 ALTER TABLE `aule` ENABLE KEYS */;
UNLOCK TABLES;

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
  `ricorrente` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id_aula` (`id_aula`),
  KEY `id_evento` (`id_evento`),
  CONSTRAINT `calendario_ibfk_1` FOREIGN KEY (`id_aula`) REFERENCES `aule` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `calendario_ibfk_2` FOREIGN KEY (`id_evento`) REFERENCES `eventi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calendario`
--

LOCK TABLES `calendario` WRITE;
/*!40000 ALTER TABLE `calendario` DISABLE KEYS */;
INSERT INTO `calendario` VALUES (1,1,1,'nessuna','2023-07-19',NULL,'10:00:00','12:00:00',0);
/*!40000 ALTER TABLE `calendario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eventi`
--

DROP TABLE IF EXISTS `eventi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventi` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `descrizione` text,
  `tipologia` enum('lezione','seminario','esame','parziale','riunione','lauree') NOT NULL DEFAULT 'lezione',
  `id_responsabile` int NOT NULL,
  `id_corso` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_responsabile` (`id_responsabile`),
  KEY `id_corso` (`id_corso`),
  CONSTRAINT `eventi_ibfk_1` FOREIGN KEY (`id_responsabile`) REFERENCES `responsabile` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `eventi_ibfk_2` FOREIGN KEY (`id_corso`) REFERENCES `gruppo` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventi`
--

LOCK TABLES `eventi` WRITE;
/*!40000 ALTER TABLE `eventi` DISABLE KEYS */;
INSERT INTO `eventi` VALUES (1,'Lezione di Matematica','Lezione di matematica avanzata','lezione',1,1);
/*!40000 ALTER TABLE `eventi` ENABLE KEYS */;
UNLOCK TABLES;

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
  `tipologia` enum('corso','polo','dipartimento') NOT NULL DEFAULT 'dipartimento',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gruppo`
--

LOCK TABLES `gruppo` WRITE;
/*!40000 ALTER TABLE `gruppo` DISABLE KEYS */;
INSERT INTO `gruppo` VALUES (1,'Dipartimento di Informatica','Dipartimento di Informatica dell\'Università','dipartimento');
/*!40000 ALTER TABLE `gruppo` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `gruppo_aula_ibfk_2` FOREIGN KEY (`id_aula`) REFERENCES `aule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gruppo_aula`
--

LOCK TABLES `gruppo_aula` WRITE;
/*!40000 ALTER TABLE `gruppo_aula` DISABLE KEYS */;
INSERT INTO `gruppo_aula` VALUES (1,1);
/*!40000 ALTER TABLE `gruppo_aula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `responsabile`
--

DROP TABLE IF EXISTS `responsabile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `responsabile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `cognome` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `responsabile`
--

LOCK TABLES `responsabile` WRITE;
/*!40000 ALTER TABLE `responsabile` DISABLE KEYS */;
INSERT INTO `responsabile` VALUES (1,'Luigi','Verdi','luigi.verdi@example.com');
/*!40000 ALTER TABLE `responsabile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `cognome` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_account` varchar(255) NOT NULL,
  `role` enum('admin','student') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (1,'Mario','Rossi','mario.rossi@example.com','password123','admin');
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'aule_web'
--

--
-- Dumping routines for database 'aule_web'
--
/*!50003 DROP PROCEDURE IF EXISTS `MostraEventiAttuali` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `MostraEventiAttuali`()
BEGIN
    DECLARE data_attuale DATETIME;
    DECLARE data_fine DATETIME;
    SET data_attuale = NOW();
    
    SET data_fine = DATE_ADD(data_attuale, INTERVAL 3 HOUR);

    SELECT e.*
    FROM Eventi e
    JOIN Calendario c ON e.id = c.id_evento
    WHERE (c.giorno = DATE(data_attuale) AND c.ora_inizio >= TIME(data_attuale))
        OR (c.giorno = DATE_ADD(DATE(data_attuale), INTERVAL 1 DAY))
        OR (c.giorno > DATE(data_attuale) AND c.giorno <= DATE(data_fine));
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MostraEventiAulaSettimana` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `MostraEventiAulaSettimana`(
    IN aula_id INT,
    IN data_inizio DATE
)
BEGIN
    DECLARE giorno_settimana INT;
	DECLARE data_fine DATE;
    SET giorno_settimana = DAYOFWEEK(data_inizio);
    SET data_inizio = DATE_SUB(data_inizio, INTERVAL giorno_settimana - 2 DAY);
    SET data_inizio = DATE(data_inizio);

   
    SET data_fine = DATE_ADD(data_inizio, INTERVAL 7 DAY);

    SELECT e.*
    FROM Eventi e
    JOIN Calendario c ON e.id = c.id_evento
    WHERE c.id_aula = aula_id
        AND c.giorno >= data_inizio
        AND c.giorno < data_fine;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MostraEventiAuleGiorno` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `MostraEventiAuleGiorno`(
    IN data_selezionata DATE
)
BEGIN
    SELECT a.nome AS nome_aula, e.*
    FROM Aule a
    LEFT JOIN Calendario c ON a.id = c.id_aula
    LEFT JOIN Eventi e ON c.id_evento = e.id
    WHERE c.giorno = data_selezionata OR c.giorno IS NULL;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MostraEventiCorsoSettimana` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `MostraEventiCorsoSettimana`(
    IN corso_id INT,
    IN data_inizio DATE
)
BEGIN
    DECLARE giorno_settimana INT;
     DECLARE data_fine DATE;
    SET giorno_settimana = DAYOFWEEK(data_inizio);
    SET data_inizio = DATE_SUB(data_inizio, INTERVAL giorno_settimana - 2 DAY);
    SET data_inizio = DATE(data_inizio);

   
    SET data_fine = DATE_ADD(data_inizio, INTERVAL 7 DAY);

    SELECT e.*
    FROM Eventi e
    JOIN Calendario c ON e.id = c.id_evento
    WHERE e.id_corso = corso_id
        AND c.giorno >= data_inizio
        AND c.giorno < data_fine;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-31 16:33:40
