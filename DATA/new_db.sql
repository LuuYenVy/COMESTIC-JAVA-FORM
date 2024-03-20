-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: db_store
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `UserName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `PassWord` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `FullName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DateCreated` date NOT NULL,
  `employeeid` int DEFAULT NULL,
  PRIMARY KEY (`UserName`),
  KEY `fk_employee` (`employeeid`),
  CONSTRAINT `fk_employee` FOREIGN KEY (`employeeid`) REFERENCES `employees` (`EmployeeCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES ('Admin','123','Huỳnh khả tú','2023-06-16',5),('dun','123','Phạm Thị Thùy Dương','2023-06-16',6),('nhi','123','Vương Thị Yến Nhi','2023-06-16',7),('su huynh','123','huỳnh su','2023-06-22',9),('susu','234','Huỳnh Khả Tú','2023-06-20',5),('User','123','Võ Hưng Khang','2023-06-16',8);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `accounts_insert_trigger` BEFORE INSERT ON `accounts` FOR EACH ROW BEGIN
    SET NEW.DateCreated = CURDATE();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (11,'LIPSTICK'),(12,'FOAM CLEANSER'),(13,'LIP SLEEPING MASK'),(14,'SUNSCREEN'),(15,'HAIRCARE'),(16,'EYEBROW LINER'),(17,'PERFUME'),(18,'CLEANSER GEL'),(19,'tui');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detailorders`
--

DROP TABLE IF EXISTS `detailorders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detailorders` (
  `DetailOrderID` int NOT NULL AUTO_INCREMENT,
  `OrderID` int DEFAULT NULL,
  `productid` int DEFAULT NULL,
  `Quantity` int DEFAULT NULL,
  `UnitPrice` decimal(10,2) DEFAULT NULL,
  `Discount` decimal(5,2) DEFAULT NULL,
  `Subtotal` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`DetailOrderID`),
  KEY `OrderID` (`OrderID`),
  KEY `fk_pro` (`productid`),
  CONSTRAINT `detailorders_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`),
  CONSTRAINT `fk_pro` FOREIGN KEY (`productid`) REFERENCES `products` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detailorders`
--

LOCK TABLES `detailorders` WRITE;
/*!40000 ALTER TABLE `detailorders` DISABLE KEYS */;
INSERT INTO `detailorders` VALUES (43,42,5,1,780000.00,NULL,780000.00),(44,42,4,1,780000.00,NULL,780000.00),(45,43,8,2,240000.00,NULL,240000.00),(46,43,6,1,250000.00,NULL,250000.00),(47,44,6,1,250000.00,NULL,250000.00),(48,44,4,1,358000.00,NULL,358000.00),(49,44,7,1,220000.00,NULL,220000.00),(50,44,2,2,300000.00,NULL,300000.00),(51,45,5,3,2340000.00,NULL,2340000.00),(52,45,2,1,150000.00,NULL,150000.00),(53,46,2,3,450000.00,NULL,450000.00),(54,46,6,1,250000.00,NULL,250000.00),(55,46,7,1,250000.00,NULL,250000.00),(56,47,4,1,358000.00,NULL,358000.00),(57,47,2,2,300000.00,NULL,300000.00),(58,48,8,1,120000.00,NULL,120000.00),(59,48,5,1,120000.00,NULL,120000.00),(60,49,9,1,300000.00,NULL,300000.00),(61,49,5,2,1560000.00,NULL,1560000.00),(62,50,3,1,450000.00,NULL,450000.00),(63,51,5,1,780000.00,NULL,780000.00),(64,51,2,2,300000.00,NULL,300000.00),(65,52,6,1,250000.00,NULL,250000.00),(66,53,6,1,250000.00,NULL,250000.00),(67,56,9,4,150000.00,NULL,600000.00),(68,57,8,1,120000.00,NULL,120000.00),(69,58,7,2,220000.00,NULL,440000.00),(70,58,9,1,150000.00,NULL,150000.00);
/*!40000 ALTER TABLE `detailorders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `Level` int NOT NULL,
  `EmployeeCode` int NOT NULL AUTO_INCREMENT,
  `FullName` char(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `YearofBirth` date NOT NULL,
  `Sex` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Address` char(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Phone` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Email` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Payroll` char(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `status` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'active',
  `positionid` int DEFAULT NULL,
  PRIMARY KEY (`EmployeeCode`),
  KEY `fk_position` (`positionid`),
  CONSTRAINT `fk_position` FOREIGN KEY (`positionid`) REFERENCES `positionn` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (3,5,'Huỳnh Khả Tú','2002-10-30','Female','nhà tú','0123456789','su@gmail.com','60,000,000 VND','active',11),(2,6,'Phạm Thị Thùy Dương','2002-12-04','Female','nhà gà','0987654321','dun@gmail.com','11,000,000 VND','active',11),(20,7,'Vương Thị Yến Nhi','2002-04-04','Female','nhà nhi','0974214678','nhi@gmail.com','100,000,000 VND','active',11),(2,8,'Võ Hưng Khang','2002-11-12','Male','nhà khang','013579257','khang@gmail.com','10,000,000 VND','active',11),(10,9,'huỳnh su','2002-10-30','Male','gv','09324242','SUSU@GM.COM','48,000,000 VND','inactive',11);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `OrderID` int NOT NULL AUTO_INCREMENT,
  `CustomerName` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `OrderDate` date DEFAULT NULL,
  `TotalAmount` decimal(10,2) DEFAULT NULL,
  `ShippingAddress` varchar(100) DEFAULT NULL,
  `payment` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `status` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'active',
  PRIMARY KEY (`OrderID`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (42,'','2023-06-19',1560000.00,'','Cash','inactive'),(43,'mama','2023-06-19',490000.00,'nhà mềnh','Cash','active'),(44,'khả tú','2023-06-19',1128000.00,'nhà mềnh','Bank','active'),(45,'nhi nè','2023-06-19',2490000.00,'bình nguyên','Cash','inactive'),(46,'Khang đây','2023-06-19',950000.00,'vô tận','Cash','active'),(47,'dưn đâu','2023-06-19',658000.00,'nhà dậu','Bank','active'),(48,'','2023-06-20',240000.00,'','Cash','active'),(49,'susu','2023-06-20',1860000.00,'gv','Cash','active'),(50,'','2023-06-20',450000.00,'','Cash','active'),(51,'Su su','2023-06-22',1080000.00,'gò vấp','Cash','active'),(52,'su ','2023-06-22',250000.00,'Gò vấp','Cash','active'),(53,'su tú','2023-06-22',250000.00,'HCM','Bank','active'),(54,'su tú','2023-06-22',1470000.00,'HCM','Cash','active'),(55,'','2023-06-22',300000.00,'','Cash','inactive'),(56,'su','2023-06-22',300000.00,'gv','Cash','inactive'),(57,'su','2023-06-22',120000.00,'sy','Cash','inactive'),(58,'khả tú','2023-06-22',590000.00,'HCM VN','Bank','active');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `SetDefaultOrderDate2` BEFORE INSERT ON `orders` FOR EACH ROW BEGIN
  SET NEW.OrderDate = CURDATE();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `positionn`
--

DROP TABLE IF EXISTS `positionn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `positionn` (
  `Position` char(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Payroll` char(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `positionn`
--

LOCK TABLES `positionn` WRITE;
/*!40000 ALTER TABLE `positionn` DISABLE KEYS */;
INSERT INTO `positionn` VALUES ('Bán Hàng','5,500,000 VND',11),('Bảo Vệ','3,500,000 VND',12),('Giám Đốc','20,000,000 VND',13),('Kế Toán','5,000,000 VND',14),('Thủ Kho','4,800,000 VND',15),('Thu Ngân','4,500,000 VND',16),('Tiếp Tân','8,000,000 VND',17),('Nhân Viên Tiếp Thị','3,000,000 VND',18),('Nhân Viên Tư Vấn','4,000,000 VND',19),('Tạp Vụ','3,200,000 VND',20);
/*!40000 ALTER TABLE `positionn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producer`
--

DROP TABLE IF EXISTS `producer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producer` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ProducerName` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `Address` char(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `Phone` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `Email` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producer`
--

LOCK TABLES `producer` WRITE;
/*!40000 ALTER TABLE `producer` DISABLE KEYS */;
INSERT INTO `producer` VALUES (1,'Bioderma','Hồ Chí Minh','09898989898','bio@gmail.com'),(2,'CeraVe','Hồ Chí Minh','0900009838','cerave@gmail.com.vn'),(3,'Vichy','Hồ Chí Minh','0900990987','vichy@gmail.com'),(4,'Cocoon','Hồ Chí Minh','0900097460','cocoon@mobiistar.com.vn'),(5,'3CE','Hồ Chí Minh','0900090000','3ce@gmail.com'),(6,'Laneige','Hồ Chí Minh','01670000021','laneige@hg.email.vn'),(7,'DHC','Hồ Chí Minh','0909090909','dhc@gmail.com'),(8,'Merzy','Hồ Chí Minh','012700001221','merzy@hg.email.vn');
/*!40000 ALTER TABLE `producer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` char(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `sold` int DEFAULT '0',
  `QuantityRemaining` int NOT NULL,
  `Unit` char(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Price` char(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Path` char(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `categoryid` int DEFAULT NULL,
  `producerid` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `categoryid` (`categoryid`),
  KEY `producerid` (`producerid`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`categoryid`) REFERENCES `category` (`ID`),
  CONSTRAINT `products_ibfk_2` FOREIGN KEY (`producerid`) REFERENCES `producer` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'3CE Blur Water Tint',0,2,'1','300,000 VND','D:PTTKProject-PTTKGroup4imgSon Kem Lì 3CE Blur Water Tint.webp',11,5),(2,'Merzy The First Brow Pencil 0.3g',10,20,'1','150,000 VND','D:PTTKProject-PTTKGroup4imgChì Kẻ Mày Merzy The First Brow Pencil 0.3g.avif',16,8),(3,' Bioderma Atoderm Intensive Gel Moussant',1,0,'1','450,000 VND','D:PTTKProject-PTTKGroup4imgGel Rửa Mặt Bioderma Atoderm Intensive Gel Moussant.webp',12,1),(4,' Cerave Developed With Dermatologists Foaming Cleanser',3,7,'1','358,000 VND','D:PTTKProject-PTTKGroup4imgSữa Rửa Mặt Sạch Sâu Cerave Developed With Dermatologists Foaming Cleanse.webp',12,2),(5,'Vichy Capital Soleil Mattifying 3-In-1 SPF50+ 50ml',9,1,'1','780,000 VND','D:PTTKProject-PTTKGroup4imgKem Chống Nắng Vichy Capital Soleil Mattifying 3-In-1 SPF50+ 50ml.webp',14,3),(6,'Laneige Water Sleeping Mask EX',6,4,'1','250,000 VND','C:Usersvyyy0Documentsjavajava-do-animgcaa2n.png',13,6),(7,'Cocoon Pomelo Hair Mask 200ml',6,24,'1','220,000 VND','D:PTTKProject-PTTKGroup4imgMặt Nạ Ngủ Dưỡng Da Căng Mịn Laneige Water Sleeping Mask EX.webp',15,4),(8,'Merzy Bite The Beat Mellow Tint 4g',4,12,'1','120,000 VND','D:PTTKProject-PTTKGroup4imgSon Kem Lì Merzy Bite The Beat Mellow Tint 4g.jpg',11,8),(9,'srm',6,17,'1','150,000 VND','D:\\PTTK\\Project-PTTK\\Group4\\img\\Kem Ủ Tóc Bưởi Cocoon Pomelo Hair Mask 200ml.jpg',12,1),(13,'hu',0,24,'2','134,444 VND','C:\\Users\\Admin\\Documents\\Vũng tàu set up.txt',11,1);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `revenue`
--

DROP TABLE IF EXISTS `revenue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `revenue` (
  `Name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Date` date NOT NULL,
  `Time` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TotalMoney` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Money` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Surplus` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `id_revenue` int NOT NULL AUTO_INCREMENT,
  `orderid` int DEFAULT NULL,
  `employeeid` int DEFAULT NULL,
  PRIMARY KEY (`id_revenue`),
  KEY `orderid` (`orderid`),
  KEY `employeeid` (`employeeid`),
  CONSTRAINT `revenue_ibfk_1` FOREIGN KEY (`orderid`) REFERENCES `orders` (`OrderID`),
  CONSTRAINT `revenue_ibfk_2` FOREIGN KEY (`employeeid`) REFERENCES `employees` (`EmployeeCode`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `revenue`
--

LOCK TABLES `revenue` WRITE;
/*!40000 ALTER TABLE `revenue` DISABLE KEYS */;
INSERT INTO `revenue` VALUES ('Huỳnh khả tú','2023-06-17','01:23:48','0 VND','2,600,000 VND','2,600,000 VND',44,NULL,NULL),('Huỳnh khả tú','2023-06-17','20:51:06','3,400,000 VND','4,000,000 VND','600,000 VND',45,NULL,NULL),('Huỳnh khả tú','2023-06-17','20:54:50','1,800,000 VND','2,000,000 VND','200,000 VND',46,NULL,NULL),('Huỳnh khả tú','2023-06-17','20:56:44','1,290,000 VND','1,300,000 VND','10,000 VND',47,NULL,NULL),('Huỳnh khả tú','2023-06-17','20:57:52','1,020,000 VND','1,200,000 VND','180,000 VND',48,NULL,NULL),('Huỳnh khả tú','2023-06-17','21:03:55','1,790,000 VND','1,800,000 VND','10,000 VND',49,NULL,NULL),('Huỳnh khả tú','2023-06-17','21:42:29','2,090,000 VND','2,500,000 VND','410,000 VND',50,NULL,NULL),('Huỳnh khả tú','2023-06-17','21:55:39','1,000,000 VND','1,000,000 VND','0 VND',51,NULL,NULL),('Huỳnh khả tú','2023-06-17','22:32:00','1,220,000 VND','1,300,000 VND','80,000 VND',52,NULL,NULL),('Huỳnh khả tú','2023-06-18','00:11:36','2,400,000 VND','2,500,000 VND','100,000 VND',53,NULL,NULL),('Huỳnh khả tú','2023-06-18','00:15:23','3,200,000 VND','3,300,000 VND','100,000 VND',54,NULL,NULL),('Huỳnh khả tú','2023-06-18','00:20:29','3,200,000 VND','3,300,000 VND','100,000 VND',55,NULL,NULL),('Huỳnh khả tú','2023-06-18','00:23:23','2,400,000 VND','2,500,000 VND','100,000 VND',56,NULL,NULL),('Huỳnh khả tú','2023-06-19','14:50:36','3,200,000 VND','3,300,000 VND','100,000 VND',57,NULL,NULL),('Huỳnh khả tú','2023-06-19','14:59:58','2,290,000 VND','2,300,000 VND','10,000 VND',58,NULL,NULL),('Huỳnh khả tú','2023-06-19','15:03:45','1,300,000 VND','1,400,000 VND','100,000 VND',59,NULL,NULL),('Huỳnh khả tú','2023-06-19','15:05:19','1,300,000 VND','1,400,000 VND','100,000 VND',60,NULL,NULL),('Huỳnh khả tú','2023-06-19','15:06:39','1,510,000 VND','1,600,000 VND','90,000 VND',61,NULL,NULL),('Huỳnh khả tú','2023-06-19','15:12:36','500,000 VND','500,000 VND','0 VND',62,NULL,NULL),('Huỳnh khả tú','2023-06-19','15:15:25','2,300,000 VND','2,500,000 VND','200,000 VND',63,NULL,NULL),('Huỳnh khả tú','2023-06-19','21:00:20','1,560,000 VND','1,600,000 VND','40,000 VND',64,NULL,NULL),('Huỳnh khả tú','2023-06-19','21:02:36','490,000 VND','490,000 VND','0 VND',65,NULL,NULL),('Huỳnh khả tú','2023-06-19','21:03:51','1,128,000 VND','1,128,000 VND','0 VND',66,NULL,NULL),('Huỳnh khả tú','2023-06-19','21:05:46','2,490,000 VND','2,500,000 VND','10,000 VND',67,NULL,NULL),('Huỳnh khả tú','2023-06-19','21:08:03','950,000 VND','950,000 VND','0 VND',68,NULL,NULL),('Huỳnh khả tú','2023-06-19','21:09:26','658,000 VND','658,000 VND','0 VND',69,NULL,NULL),('Huỳnh khả tú','2023-06-20','13:58:21','240,000 VND','250,000 VND','10,000 VND',70,NULL,NULL),('Điện Tử Số 1','2023-06-20','22:44:54','1,860,000 VND','2,000,000 VND','140,000 VND',71,NULL,NULL),('Điện Tử Số 1','2023-06-20','22:45:54','450,000 VND','500,000 VND','50,000 VND',72,NULL,NULL),('Huỳnh khả tú','2023-06-22','15:04:35','250,000 VND','300,000 VND','50,000 VND',74,52,5),('Huỳnh khả tú','2023-06-22','15:07:13','250,000 VND','300,000 VND','50,000 VND',75,53,5),('Huỳnh khả tú','2023-06-22','15:51:37','0','300,000 VND','0 VND',78,56,5),('Huỳnh khả tú','2023-06-22','16:26:31','0','150,000 VND','30,000 VND',79,57,5),('Huỳnh khả tú','2023-06-22','16:53:58','590,000 VND','600,000 VND','10,000 VND',80,58,5);
/*!40000 ALTER TABLE `revenue` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-22 17:34:00
