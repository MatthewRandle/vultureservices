-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 15, 2019 at 05:48 PM
-- Server version: 8.0.13-4
-- PHP Version: 7.2.17-0ubuntu0.18.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `TiqtIjwWJq`
--

-- --------------------------------------------------------

--
-- Table structure for table `alerts`
--

CREATE TABLE `alerts` (
  `id` int(11) NOT NULL,
  `alert` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `user_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `inspection_failure`
--

CREATE TABLE `inspection_failure` (
  `id` int(11) NOT NULL,
  `manufacturer` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `frequency` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `inspection_failure`
--

INSERT INTO `inspection_failure` (`id`, `manufacturer`, `frequency`) VALUES
(15, 'Biro', 2),
(18, 'standard', 1),
(20, 'Buhler', 0);

-- --------------------------------------------------------

--
-- Table structure for table `jobs`
--

CREATE TABLE `jobs` (
  `id` int(11) NOT NULL,
  `job_number` int(6) NOT NULL,
  `client` varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `arrival_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `quoted_parts` varchar(5000) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `motor_serial` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `manufacturer` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `manufacture_year` int(4) DEFAULT NULL,
  `labour_time` int(11) DEFAULT NULL,
  `checked_by` varchar(150) DEFAULT NULL,
  `checked_date` date DEFAULT NULL,
  `inspected_by` varchar(150) DEFAULT NULL,
  `inspected_date` date DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `approved` tinyint(1) DEFAULT NULL,
  `notApproved` tinyint(1) DEFAULT NULL,
  `start_date` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `end_date` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `jobs`
--

INSERT INTO `jobs` (`id`, `job_number`, `client`, `arrival_date`, `return_date`, `quoted_parts`, `motor_serial`, `manufacturer`, `manufacture_year`, `labour_time`, `checked_by`, `checked_date`, `inspected_by`, `inspected_date`, `status`, `approved`, `notApproved`, `start_date`, `end_date`) VALUES
(143, 1, 'Daedalus', '2019-05-12', '2019-05-16', '4 new magnets, bearings, connection wiring', 'G-DL2', 'Buhler', 1990, 57, 'brian', '2019-05-14', '', NULL, 'Active', 0, 0, 'Wed May 15 16:31:32 BST 2019', 'Wed May 15 18:41:49 BST 2019'),
(146, 2, 'Daedalus', '2019-05-07', '2019-05-21', 'Large 20x20 Brush\nD43 Seal ring, Solenoid wiring', 'Runmax-4000', 'Actiro', 1973, 26, 'brian', '2019-05-14', 'brian', '2019-05-14', 'Active', 1, 0, 'Wed May 15 16:34:41 BST 2019', NULL),
(147, 3, 'Big Dig', '2019-05-14', '2019-05-29', 'Bearings', 'G-DL17', 'Buhler', 1992, 7, 'brian', '2019-05-14', '', NULL, 'Suspended', 0, 0, 'Wed May 15 16:36:43 BST 2019', NULL),
(151, 10, 'Northumbria', '2019-05-06', '2019-05-13', 'New Vent cover', 'jsk-992', 'Biro', 1992, 2, 'tech', '2019-05-14', 'tech', '2019-05-14', 'Active', 0, 1, 'Wed May 15 16:40:22 BST 2019', 'Wed May 15 16:40:26 BST 2019'),
(153, 80, 'Northumbria', '2019-05-14', '2019-05-15', 'New flaps, 20x20 bolts', 'shh-228', 'Biro', 1992, 5, 'brian', '2019-05-14', 'brian', '2019-05-14', 'Active', 0, 1, 'Wed May 15 16:47:30 BST 2019', NULL),
(154, 882, 'Vaux', '2019-05-04', '2019-05-12', 'Oil replacement', 'jhs-444', 'Biro', 1920, 0, 'tech', '2019-05-13', 'brian', '2019-05-14', 'Active', 0, 1, 'Wed May 15 16:50:18 BST 2019', 'Wed May 15 16:56:24 BST 2019'),
(156, 992, 'Vaux', '2019-05-11', '2019-05-13', 'screws', '882-xs', 'standard', 1992, 2, 'brian', '2019-05-13', 'brian', '2019-05-14', 'Active', 0, 1, 'Wed May 15 16:50:55 BST 2019', 'Wed May 15 16:56:38 BST 2019'),
(158, 9923, 'Vaux', '2019-05-06', '2019-05-22', 'New bearings', '882-xxc', 'Standard', 1992, 292, 'brian', '2019-05-14', 'brian', '2019-05-14', 'Active', 0, 1, 'Wed May 15 16:54:19 BST 2019', 'Wed May 15 17:00:56 BST 2019'),
(162, 9292, '', NULL, NULL, '', '', '', 0, 0, '', NULL, '', NULL, 'Active', 0, 0, 'Wed May 15 17:56:59 BST 2019', NULL),
(163, 23434, '', NULL, NULL, '', '', '', 0, 0, '', NULL, '', NULL, 'Active', 0, 0, 'Wed May 15 18:25:12 BST 2019', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `order_parts`
--

CREATE TABLE `order_parts` (
  `id` int(11) NOT NULL,
  `motor` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `frequency` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `order_parts`
--

INSERT INTO `order_parts` (`id`, `motor`, `frequency`) VALUES
(1, 'Gold', 10),
(2, 'VW', 4),
(3, 'Ford', 16),
(4, 'Porsche', 8),
(5, 'Vauxhall', 3),
(6, 'Actiro', 1);

-- --------------------------------------------------------

--
-- Table structure for table `parts`
--

CREATE TABLE `parts` (
  `id` int(11) NOT NULL,
  `part` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `available` varchar(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parts`
--

INSERT INTO `parts` (`id`, `part`, `available`) VALUES
(1, 'Bearing', 'Available'),
(2, 'Spring', 'Available'),
(3, 'Filter', 'Available'),
(4, 'Wheel', 'Available'),
(5, 'Battery', 'Available'),
(6, 'Ignition Box', 'Available'),
(7, 'Ignition Coil', 'Available'),
(8, 'Spark Plug', 'Available'),
(9, 'Sparking Cable', 'Available'),
(10, 'Fuel level sensor', 'Available'),
(11, 'Knock Sensor', 'Available'),
(12, 'Light Sensor', 'Available'),
(13, 'Airbag Sensor', 'Available');

-- --------------------------------------------------------

--
-- Table structure for table `suspended`
--

CREATE TABLE `suspended` (
  `ID` int(11) NOT NULL,
  `manufacturer` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `frequency` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tasks`
--

CREATE TABLE `tasks` (
  `id` int(11) NOT NULL,
  `task_name` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `job_number` int(11) NOT NULL,
  `completed` tinyint(1) NOT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `urgency` int(11) DEFAULT '0',
  `suspended` tinyint(1) NOT NULL DEFAULT '0',
  `username` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `date_due` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_assigned` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_completed` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='completed_by is a foreign key to a user';

--
-- Dumping data for table `tasks`
--

INSERT INTO `tasks` (`id`, `task_name`, `job_number`, `completed`, `description`, `duration`, `urgency`, `suspended`, `username`, `date_due`, `date_assigned`, `date_completed`) VALUES
(166, 'Reduce magnets', 1, 0, 'On hold - new magnets on order', 8, 6, 1, 'Ryan', 'Thu May 16 00:44:24 BST 2019', 'Wed May 15 16:44:24 BST 2019', NULL),
(167, 'Rewire Contacts', 1, 1, '', 24, 6, 0, 'tech', 'Thu May 16 16:44:41 BST 2019', 'Wed May 15 16:44:41 BST 2019', 'Wed May 15 18:26:49 BST 2019'),
(168, 'Restore Bearings', 1, 1, '', 20, 6, 0, 'tech', 'Thu May 16 12:44:38 BST 2019', 'Wed May 15 16:44:38 BST 2019', 'Wed May 15 16:31:34 BST 2019'),
(169, 'Clean Housing', 1, 0, 'Finish cleaning', 5, 6, 0, 'Ryan', 'Wed May 15 21:43:54 BST 2019', 'Wed May 15 16:43:54 BST 2019', NULL),
(170, 'Reduce top brush', 2, 1, '', 3, 6, 0, 'tech', 'Wed May 15 19:44:33 BST 2019', 'Wed May 15 16:44:33 BST 2019', NULL),
(171, 'Rewire solenoid', 2, 1, 'Existing wiring should be replaced', 2, 6, 0, 'tech', 'Wed May 15 18:44:46 BST 2019', 'Wed May 15 16:44:46 BST 2019', NULL),
(172, 'Reduce ring', 2, 1, '', 20, 6, 0, 'Ryan', 'Thu May 16 12:44:29 BST 2019', 'Wed May 15 16:44:29 BST 2019', NULL),
(173, 'CLean bearings', 2, 1, '', 1, 6, 0, 'Ryan', 'Wed May 15 17:43:47 BST 2019', 'Wed May 15 16:43:47 BST 2019', NULL),
(174, 'Clean rollers', 3, 1, '', 3, 6, 0, 'Ryan', 'Wed May 15 19:43:58 BST 2019', 'Wed May 15 16:43:58 BST 2019', 'Wed May 15 20:36:45 BST 2019'),
(175, 'rewire solenoid', 3, 0, 'needs solenoid wiring', 2, 6, 1, 'tech', 'Wed May 15 18:44:43 BST 2019', 'Wed May 15 16:44:43 BST 2019', NULL),
(176, 'clean brushes', 3, 1, '', 1, 6, 0, 'Ryan', 'Wed May 15 17:43:51 BST 2019', 'Wed May 15 16:43:51 BST 2019', 'Wed May 15 19:36:43 BST 2019'),
(177, 'fix broken pin', 3, 0, 'needs new pin', 1, 6, 1, 'Ryan', 'Wed May 15 17:44:04 BST 2019', 'Wed May 15 16:44:04 BST 2019', NULL),
(178, 'Replace vent cover', 10, 1, '', 2, 6, 0, 'tech', 'Wed May 15 18:44:35 BST 2019', 'Wed May 15 16:44:35 BST 2019', 'Wed May 15 19:40:22 BST 2019'),
(179, 'Fix flaps', 80, 0, '', 2, 6, 0, 'Ryan', 'Wed May 15 18:51:20 BST 2019', 'Wed May 15 16:51:20 BST 2019', NULL),
(180, 'New bolts', 80, 0, '', 3, 6, 0, 'Ryan', 'Wed May 15 19:51:23 BST 2019', 'Wed May 15 16:51:23 BST 2019', NULL),
(181, 'Fix screws', 992, 0, 'New screws needed', 2, 6, 0, 'Ryan', 'Wed May 15 18:55:10 BST 2019', 'Wed May 15 16:55:10 BST 2019', NULL),
(182, 'Replace bearings', 9923, 0, '', 24, 6, 0, 'Ryan', 'Wed May 15 18:55:13 BST 2019', 'Wed May 15 16:55:13 BST 2019', NULL),
(183, 'Change oil', 9923, 0, '', 24, 6, 0, 'Ryan', 'Wed May 15 18:55:08 BST 2019', 'Wed May 15 16:55:08 BST 2019', NULL),
(184, 'Replace indicator fluid', 9923, 0, 'need 2 quarts', 244, 6, 0, 'Ryan', 'Wed May 15 18:55:16 BST 2019', 'Wed May 15 16:55:16 BST 2019', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_types_id` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `user_types_id`) VALUES
(1, 'tech', 'test', 1),
(2, 'manager', 'test', 2),
(3, 'hr', 'test', 3),
(4, 'cs', 'test', 6),
(9, 'finance', 'test', 5),
(11, 'Ryan', 'test', 1),
(12, 'brian', 'test', 2);

-- --------------------------------------------------------

--
-- Table structure for table `user_types`
--

CREATE TABLE `user_types` (
  `id` int(11) NOT NULL,
  `type` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_types`
--

INSERT INTO `user_types` (`id`, `type`) VALUES
(1, 'Technician'),
(2, 'Manager'),
(3, 'HR'),
(4, 'IT'),
(5, 'Finance'),
(6, 'Customer Services');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alerts`
--
ALTER TABLE `alerts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `inspection_failure`
--
ALTER TABLE `inspection_failure`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `manufacturer` (`manufacturer`);

--
-- Indexes for table `jobs`
--
ALTER TABLE `jobs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `job_number` (`job_number`);

--
-- Indexes for table `order_parts`
--
ALTER TABLE `order_parts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `parts`
--
ALTER TABLE `parts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `suspended`
--
ALTER TABLE `suspended`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `manufacturer` (`manufacturer`);

--
-- Indexes for table `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_types`
--
ALTER TABLE `user_types`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `alerts`
--
ALTER TABLE `alerts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `inspection_failure`
--
ALTER TABLE `inspection_failure`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `jobs`
--
ALTER TABLE `jobs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=164;

--
-- AUTO_INCREMENT for table `order_parts`
--
ALTER TABLE `order_parts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `parts`
--
ALTER TABLE `parts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `suspended`
--
ALTER TABLE `suspended`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tasks`
--
ALTER TABLE `tasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=185;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `user_types`
--
ALTER TABLE `user_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
