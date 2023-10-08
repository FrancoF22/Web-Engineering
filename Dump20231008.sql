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
  `id_corso` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_corso` (`id_corso`),
  KEY `evento_ibfk_1_idx` (`id_responsabile`),
  CONSTRAINT `evento_ibfk_1` FOREIGN KEY (`id_responsabile`) REFERENCES `utente` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `evento_ibfk_2` FOREIGN KEY (`id_corso`) REFERENCES `gruppo` (`id`) ON UPDATE CASCADE
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
  `tipologia` enum('corso','polo','dipartimento') NOT NULL DEFAULT 'dipartimento',
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

--
-- Dumping routines for database 'aule_web'
--
/*!50003 DROP PROCEDURE IF EXISTS `EsportaConfigurazioneAule` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EsportaConfigurazioneAule`()
BEGIN
    SELECT a.nome, a.capienza, a.prese_elettriche, a.prese_rete, a.attrezzatura, a.nota, a.luogo, a.edificio, a.piano, r.email FROM Aule AS a
    JOIN Responsabile AS r ON r.id = a.id_responsabile
    INTO OUTFILE 'C:\ProgramData\MySQL\MySQL Server 8.0\Uploads\aule.csv'
    FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
    LINES TERMINATED BY '\n';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciModificaAula` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`website`@`localhost` PROCEDURE `InserisciModificaAula`(
    IN new_nome_aula VARCHAR(255),
    IN new_capienza INT,
    IN new_prese_elettriche INT,
    IN new_prese_rete INT,
    IN new_attrezzatura SET("proiettore","schermo motorizzato", "schermo manuale", "impianto audio", "pc fisso", "microfono a filo", "microfono senza filo", "lavagna luminosa", "Wi Fi"),
    IN new_note TEXT,
    IN new_luogo VARCHAR(255),
    IN new_edificio VARCHAR(255),
    IN new_piano VARCHAR(255),
    IN new_id_responsabile INT
)
BEGIN
    DECLARE aula_id INT;
    
    -- Controlla se l'aula già esiste
    SELECT id INTO aula_id FROM Aula WHERE nome = new_nome_aula;
    
    IF aula_id IS NULL THEN
        -- Inserimento di una nuova aula
        INSERT INTO Aula (nome, capienza, prese_elettriche, prese_rete, attrezzatura, nota, luogo, edificio, piano, id_responsabile)
        VALUES (new_nome_aula, new_capienza, new_prese_elettriche, new_prese_rete, new_attrezzatura, new_note, new_luogo, new_edificio, new_piano, new_id_responsabile);
    ELSE
        -- Aggiornamento di un'aula esistente
        UPDATE Aula
        SET nome = new_nome_aula,
			capienza = new_capienza,
            prese_elettriche = new_prese_elettriche,
            prese_rete = new_prese_rete,
            attrezzatura = new_attrezzatura,
            nota = new_note,
            luogo = new_luogo,
            edificio = new_edificio,
            piano = new_piano,
            id_responsabile = new_id_responsabile
        WHERE id = aula_id;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciModificaEvento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`website`@`localhost` PROCEDURE `InserisciModificaEvento`(
    IN new_nome_evento VARCHAR(255),
    IN new_descrizione TEXT,
    IN new_tipologia ENUM('lezione', 'seminario', 'esame', 'parziale', 'riunione', 'lauree'),
    IN new_id_responsabile INT,
    IN new_id_corso INT
)
BEGIN
    DECLARE evento_id INT;
    
    -- Controlla se l'evento già esiste
    SELECT id INTO evento_id FROM Evento WHERE nome = new_nome_evento;
    
    IF evento_id IS NULL THEN
        -- Inserimento di un nuovo evento
        INSERT INTO Evento (nome, descrizione, tipologia, id_responsabile, id_corso)
        VALUES (new_nome_evento, new_descrizione, new_tipologia, new_id_responsabile, new_id_corso);
    ELSE
        -- Aggiornamento di un evento esistente
        UPDATE Evento
        SET descrizione = new_descrizione,
            tipologia = new_tipologia,
            id_responsabile = new_id_responsabile,
            id_corso = new_id_corso
        WHERE id = evento_id;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
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
CREATE DEFINER=`website`@`localhost` PROCEDURE `MostraEventiAttuali`(
	IN gruppo_id INT
)
BEGIN
    DECLARE data_attuale DATETIME;
    DECLARE data_fine DATETIME;
    SET data_attuale = NOW();
    
    SET data_fine = DATE_ADD(data_attuale, INTERVAL 3 HOUR);

    SELECT e.*
    FROM Evento e
    JOIN Calendario c ON e.id = c.id_evento
    LEFT JOIN Gruppo_Aula g ON g.id_aula = c.id_aula
    WHERE (c.giorno = DATE(data_attuale) AND c.ora_inizio >= TIME(data_attuale))
        OR (c.giorno = DATE_ADD(DATE(data_attuale), INTERVAL 1 DAY))
        OR (c.giorno > DATE(data_attuale) AND c.giorno <= DATE(data_fine)) AND g.id_gruppo = gruppo_id;
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
CREATE DEFINER=`website`@`localhost` PROCEDURE `MostraEventiAulaSettimana`(
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
    FROM Evento e
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
CREATE DEFINER=`website`@`localhost` PROCEDURE `MostraEventiAuleGiorno`(
    IN data_selezionata DATE,
    IN gruppo_id INT
)
BEGIN
    SELECT a.nome AS nome_aula, e.*
    FROM Aula a
    LEFT JOIN Calendario c ON a.id = c.id_aula
    LEFT JOIN Evento e ON c.id_evento = e.id
    LEFT JOIN Gruppo_Aula g ON g.id_aula = a.id
    WHERE c.giorno = data_selezionata OR c.giorno IS NULL AND g.id_gruppo = gruppo_id;
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
CREATE DEFINER=`website`@`localhost` PROCEDURE `MostraEventiCorsoSettimana`(
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
    FROM Evento e
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

-- Dump completed on 2023-10-08 21:10:21
