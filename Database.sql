-- phpMyAdmin SQL Dump
-- version 4.7.1
-- https://www.phpmyadmin.net/
--
-- Host: sql2.freemysqlhosting.net
-- Generation Time: Feb 22, 2019 at 12:46 PM
-- Server version: 5.5.54-0ubuntu0.12.04.1
-- PHP Version: 7.0.33-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sql2280218`
--

-- --------------------------------------------------------

--
-- Table structure for table `jobs`
--

CREATE TABLE `jobs` (
  `id` int(11) NOT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `suspended` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `jobs_have_tasks`
--

CREATE TABLE `jobs_have_tasks` (
  `jobs_id` int(11) NOT NULL,
  `tasks_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `motors_have_parts`
--

CREATE TABLE `motors_have_parts` (
  `motor_details_id` int(11) NOT NULL,
  `parts_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `motor_details`
--

CREATE TABLE `motor_details` (
  `id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `motor_serial` varchar(100) NOT NULL,
  `manufactured_date` date NOT NULL,
  `date_collected` date NOT NULL,
  `estimated_time` float NOT NULL,
  `user_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `manufacturer` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `return_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `parts`
--

CREATE TABLE `parts` (
  `id` int(11) NOT NULL,
  `part` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `available` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `skills`
--

CREATE TABLE `skills` (
  `id` int(11) NOT NULL,
  `skill` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tasks`
--

CREATE TABLE `tasks` (
  `id` int(11) NOT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `report` int(11) NOT NULL,
  `duration` float DEFAULT NULL,
  `urgency` int(11) NOT NULL DEFAULT '1',
  `suspended` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='completed_by is a foreign key to a user';

-- --------------------------------------------------------

--
-- Table structure for table `tasks_have_skills`
--

CREATE TABLE `tasks_have_skills` (
  `tasks_id` int(11) NOT NULL,
  `skills_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `task_report`
--

CREATE TABLE `task_report` (
  `id` int(11) NOT NULL,
  `completed_by` int(11) NOT NULL,
  `completed_date` date NOT NULL,
  `report` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users_have_skills`
--

CREATE TABLE `users_have_skills` (
  `users_id` int(11) NOT NULL,
  `skills_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user_types`
--

CREATE TABLE `user_types` (
  `id` int(11) NOT NULL,
  `type` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jobs`
--
ALTER TABLE `jobs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `jobs_have_tasks`
--
ALTER TABLE `jobs_have_tasks`
  ADD PRIMARY KEY (`jobs_id`,`tasks_id`);

--
-- Indexes for table `motors_have_parts`
--
ALTER TABLE `motors_have_parts`
  ADD PRIMARY KEY (`parts_id`,`motor_details_id`);

--
-- Indexes for table `motor_details`
--
ALTER TABLE `motor_details`
  ADD PRIMARY KEY (`id`,`job_id`,`user_id`,`client_id`);

--
-- Indexes for table `parts`
--
ALTER TABLE `parts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `skills`
--
ALTER TABLE `skills`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `report` (`report`);

--
-- Indexes for table `tasks_have_skills`
--
ALTER TABLE `tasks_have_skills`
  ADD PRIMARY KEY (`tasks_id`,`skills_id`);

--
-- Indexes for table `task_report`
--
ALTER TABLE `task_report`
  ADD PRIMARY KEY (`id`,`completed_by`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users_have_skills`
--
ALTER TABLE `users_have_skills`
  ADD PRIMARY KEY (`users_id`,`skills_id`);

--
-- Indexes for table `user_types`
--
ALTER TABLE `user_types`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
