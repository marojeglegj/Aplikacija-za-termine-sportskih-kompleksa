-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jan 12, 2025 at 06:59 PM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `termini`
--

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
CREATE TABLE IF NOT EXISTS `appointments` (
  `appointment_ID` int NOT NULL AUTO_INCREMENT,
  `sport_id` int NOT NULL,
  `complex_id` int NOT NULL,
  `hall_id` int NOT NULL,
  `client_name` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `time_slot` varchar(255) NOT NULL,
  PRIMARY KEY (`appointment_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`appointment_ID`, `sport_id`, `complex_id`, `hall_id`, `client_name`, `date`, `time_slot`) VALUES
(74, 1, 2, 1, 'Pero', '2025-01-06', '20:00 - 21:00'),
(75, 1, 1, 1, 'Pero', '2025-01-08', '10:00 - 11:00'),
(76, 1, 1, 1, 'Pero', '2025-01-11', '13:00 - 14:00'),
(73, 4, 2, 1, 'Duro', '2025-01-06', '18:00 - 19:00'),
(72, 4, 1, 1, 'Duro', '2025-01-07', '18:00 - 19:00'),
(71, 3, 1, 1, 'Pero', '2025-01-07', '14:00 - 15:00'),
(70, 1, 1, 1, 'Pero', '2025-01-07', '16:00 - 17:00'),
(69, 1, 1, 1, 'Pero', '2025-01-06', '18:00 - 19:00'),
(67, 1, 1, 1, 'Pero', '2025-01-07', '21:00 - 22:00'),
(68, 1, 1, 1, 'Pero', '2025-01-07', '07:00 - 08:00'),
(65, 1, 1, 1, 'Pero', '2025-01-07', '12:00 - 13:00');

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `client_id` int NOT NULL AUTO_INCREMENT,
  `client_name` varchar(255) NOT NULL,
  `client_password` varchar(255) NOT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `client`
--

INSERT INTO `client` (`client_id`, `client_name`, `client_password`) VALUES
(1, 'Pero', 'p123'),
(2, 'Duro', 'd123');

-- --------------------------------------------------------

--
-- Table structure for table `complexes`
--

DROP TABLE IF EXISTS `complexes`;
CREATE TABLE IF NOT EXISTS `complexes` (
  `complex_id` int NOT NULL AUTO_INCREMENT,
  `complex_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`complex_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `complexes`
--

INSERT INTO `complexes` (`complex_id`, `complex_name`) VALUES
(1, 'Montovjerna'),
(2, 'Sportska dvorana'),
(3, 'Gruž');

-- --------------------------------------------------------

--
-- Table structure for table `halls`
--

DROP TABLE IF EXISTS `halls`;
CREATE TABLE IF NOT EXISTS `halls` (
  `hall_id` int NOT NULL AUTO_INCREMENT,
  `hall_name` varchar(255) NOT NULL,
  PRIMARY KEY (`hall_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `halls`
--

INSERT INTO `halls` (`hall_id`, `hall_name`) VALUES
(1, 'Dvorana'),
(2, 'Dvorana1'),
(3, 'Dvorana2'),
(4, 'Vanjski mali teren'),
(5, 'Vanjski veliki teren'),
(6, 'Travnati teren');

-- --------------------------------------------------------

--
-- Table structure for table `sports`
--

DROP TABLE IF EXISTS `sports`;
CREATE TABLE IF NOT EXISTS `sports` (
  `sport_id` int NOT NULL AUTO_INCREMENT,
  `sport_name` varchar(255) NOT NULL,
  PRIMARY KEY (`sport_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sports`
--

INSERT INTO `sports` (`sport_id`, `sport_name`) VALUES
(1, 'Nogomet'),
(2, 'Košarka'),
(3, 'Odbojka'),
(4, 'Tenis');

-- --------------------------------------------------------

--
-- Table structure for table `sport_complex_hall`
--

DROP TABLE IF EXISTS `sport_complex_hall`;
CREATE TABLE IF NOT EXISTS `sport_complex_hall` (
  `sport_id` int NOT NULL,
  `complex_id` int NOT NULL,
  `hall_id` int NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sport_complex_hall`
--

INSERT INTO `sport_complex_hall` (`sport_id`, `complex_id`, `hall_id`) VALUES
(1, 1, 1),
(2, 1, 1),
(3, 1, 1),
(4, 1, 1),
(1, 2, 1),
(2, 2, 1),
(3, 2, 1),
(4, 2, 1),
(1, 2, 2),
(2, 2, 2),
(3, 2, 2),
(4, 2, 2),
(2, 2, 4),
(1, 2, 6),
(1, 3, 1),
(2, 3, 1),
(3, 3, 1),
(4, 3, 1),
(1, 3, 5),
(1, 3, 4),
(2, 3, 4);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
