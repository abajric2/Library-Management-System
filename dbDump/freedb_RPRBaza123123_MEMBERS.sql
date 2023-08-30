CREATE DATABASE  IF NOT EXISTS `freedb_RPRBaza123123` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `freedb_RPRBaza123123`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: sql.freedb.tech    Database: freedb_RPRBaza123123
-- ------------------------------------------------------
-- Server version	8.0.34-0ubuntu0.22.04.1

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
-- Table structure for table `MEMBERS`
--

DROP TABLE IF EXISTS `MEMBERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEMBERS` (
  `MEMBER_ID` int NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(45) NOT NULL,
  `LAST_NAME` varchar(45) NOT NULL,
  `USERNAME` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `IS_ADMIN` tinyint NOT NULL,
  PRIMARY KEY (`MEMBER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=468 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEMBERS`
--

LOCK TABLES `MEMBERS` WRITE;
/*!40000 ALTER TABLE `MEMBERS` DISABLE KEYS */;
INSERT INTO `MEMBERS` VALUES (1,'Haso','Hasic','hasohasic','g45498zv4hhff',0),(2,'Bajro','Bajric','bajrobajric','bd88z--jkl',0),(3,'Musa','Music','musamusic','12lk.**asd',0),(4,'Mesa','Mesic','mesamesic','123awtufmg45',0),(6,'Sara','Saric','sarasaric','p78o4ifnfjsg',0),(7,'Sejo','Sejic','sejosejic','54uhuifjetrbgui',0),(8,'Mujo','Mujic','mujomujic','hg695hiu4v',0),(9,'Sara','Saric','sarasaric1','jjgiz895vuz849',0),(11,'Nikola','Nikic','nikolanikic','84vt98mlkkl',0),(12,'Haso','Hasic','hasohasic1','84vut5t8vh4',1),(13,'Nedeljkoo','Nedeljkic','nnedeljkicc','v54ut54u4z.,vv3',0),(14,'Amina','Bajric','abajric2','12345678',1),(15,'Nikola','Nikic','nikolanikic123','jrurfhg7n111k',0),(16,'Sena','Senic','senasenic','i4v5t0540',0),(17,'Neko','Nekic','neko','123456789',0),(20,'Adla','Bajric','adlabajric','00000000',0),(21,'Edna','Basic','ednabasic','88888888',0),(22,'Huso','Husovic','husohusovic','abcdefgkkk',0),(23,'Anja','Anjic','aanjic','Amina123!',0),(24,'Proba','Proba','proba123','sifraaaab',0),(25,'test','test','testtestaaa','44444444',0),(26,'Amina','Tucakovic','atucakovic','55555555',0),(27,'Tarik','Velic','tvelic1','velavela',0),(30,'Amina','Bajric','aminaaab','Amina123!',0),(32,'Amina','Amina','aminaaaa','114684868',0),(39,'Novi','Novi','novinovinovi','11111111',0),(47,'Amina','Bajric','aminaa','11111111',0),(48,'Amina','Bajric','aminab','11111111',0),(160,'a','a','abababababa','11111111',0),(344,'Amina','Bajric','abajric22','12345678',1),(375,'amina','amina','abajric21','11111111',0),(376,'AminaCLI','BajricCLI','cliii','11111111',0),(391,'Aminacli','test','user','password',0),(406,'am','am','abajric','11111111',0),(407,'amina','bajric','amamamam','11111111',0),(409,'aa','aa','amina','111111111',0),(413,'am','amina','ama','11111111',0),(414,'amina','a','aaa','11111111',0),(462,'a','a a','amm','11111111',0),(463,'amina','bajric','amina1233','11111111',0),(464,'amina','amina','ABAJRIC222','11111111',0);
/*!40000 ALTER TABLE `MEMBERS` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-30 21:56:04
