-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 10, 2018 at 10:13 PM
-- Server version: 10.1.26-MariaDB
-- PHP Version: 7.1.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_librarymanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_books`
--

CREATE TABLE `tbl_books` (
  `book_id` int(11) NOT NULL,
  `ISBN` varchar(250) NOT NULL,
  `barcode_identification` varchar(250) DEFAULT NULL,
  `availability` varchar(250) NOT NULL,
  `title` varchar(250) NOT NULL,
  `author` varchar(250) NOT NULL,
  `description` text,
  `edition` varchar(250) DEFAULT NULL,
  `edition_year` varchar(250) DEFAULT NULL,
  `date_created` date DEFAULT NULL,
  `date_updated` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_books`
--

INSERT INTO `tbl_books` (`book_id`, `ISBN`, `barcode_identification`, `availability`, `title`, `author`, `description`, `edition`, `edition_year`, `date_created`, `date_updated`) VALUES
(1, 'bla v3', '9789713003431', 'Available', 'The quick brown fox', 'jane doe', 'this is perfect for kids', '1st', '2016', '2017-11-23', '2017-11-26'),
(13, 'black sheep : the awekening v2', '9789713003432', 'Unavailable', 'asdasd', 'asdasd', 'asdasd', '1st', '2009', '2017-11-25', '2017-11-26'),
(14, '9713003438', '9789713003430', 'Unavailable', 'mastering the art of war', 'Sun Tzu', 'Book about self improvement and something nice', '1st', '2010', '2017-12-13', '2017-12-13'),
(15, 'ABCDEFGH123', '123456789', 'Unavailable', 'The Blaberring Nutcase', 'NutCase', 'Book about Nutcase', '1st', '2015', '2017-12-14', '2017-12-14'),
(16, '123123', '1231231', 'Available', 'asd', 'asd', 'asd', 'asd', '', '2018-02-25', '2018-02-25'),
(17, 'asdasdasd', '1231232', 'Available', 'adasda', 'sdasd', 'asdasd', '', '', '2018-02-25', '2018-02-25'),
(18, '123123', '1231233', 'Available', '123123', '123123123123', '123123', '', '', '2018-02-25', '2018-02-25'),
(19, 'asdasd', '1231234', 'Available', 'asdas', 'dasd', 'asdas', 'dasd', 'asd', '2018-02-25', '2018-02-25'),
(21, 'asd', '1231235', 'Available', 'asdasdasdasd', 'asd', 'asd', 'asdasdasd', '', '2018-02-25', '2018-02-25'),
(22, 'asdasd', '1231236', 'Available', 'asd', 'asd', 'asd', 'asd', '', '2018-02-25', '2018-02-25'),
(23, 'asdasdasd', '1231237', 'Available', 'asd', 'asd', 'asddd', '', '', '2018-02-25', '2018-02-25'),
(24, 'asdasdasd', '12312388', 'Available', 'asdasd', 'asdasd', '', '', '', '2018-02-25', '2018-02-25'),
(25, 'asdasdasd', 'asdasdasd', 'Available', 'asdasd', 'asdasdasdas', 'dasdasdasd', '', '', '2018-02-25', '2018-02-25'),
(26, 'asdasdasd', '12312311', 'Available', 'asdas', 'dasdasd', 'asdasd', '', '', '2018-02-25', '2018-02-26'),
(27, 'asd', '123123111', 'Available', 'asd', 'asd', 'asd', '', '', '2018-02-25', '2018-02-25'),
(28, 'asdasd', '1231231111', 'Available', 'sda', 'sdas', 'dasd', '', '', '2018-02-25', '2018-02-26'),
(29, 'asdasdasd', '12312311111111', 'Available', 'asd', 'asd', '', '', '', '2018-02-26', '2018-02-26');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_book_borrower`
--

CREATE TABLE `tbl_book_borrower` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `borrower_id` int(11) NOT NULL,
  `date_returned` date DEFAULT NULL,
  `expected_return_date` date DEFAULT NULL,
  `date_borrowed` date DEFAULT NULL,
  `notes` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_book_borrower`
--

INSERT INTO `tbl_book_borrower` (`id`, `book_id`, `borrower_id`, `date_returned`, `expected_return_date`, `date_borrowed`, `notes`) VALUES
(38, 14, 3, '2017-12-18', '2017-12-29', '2017-12-18', NULL),
(39, 1, 3, '2017-12-18', '2017-12-29', '2017-12-18', NULL),
(40, 1, 3, '2017-12-18', '2017-12-22', '2017-12-18', NULL),
(41, 14, 3, '2017-12-18', '2017-12-22', '2017-12-18', NULL),
(42, 1, 3, '2017-12-18', '2017-12-16', '2017-12-18', NULL),
(43, 14, 3, '2017-12-18', '2017-12-16', '2017-12-18', NULL),
(44, 1, 3, '2017-12-18', '2017-12-15', '2017-12-18', NULL),
(45, 1, 3, '2017-12-19', '2017-12-15', '2017-12-19', NULL),
(46, 14, 3, '2017-12-19', '2017-12-15', '2017-12-19', NULL),
(47, 1, 3, '2017-12-19', '2017-12-15', '2017-12-19', NULL),
(48, 14, 3, '2017-12-19', '2017-12-15', '2017-12-19', NULL),
(49, 14, 3, '2017-12-19', '2017-12-15', '2017-12-19', NULL),
(50, 13, 3, '2018-02-17', '2017-12-28', '2017-12-19', NULL),
(51, 13, 3, '2017-12-19', '2017-12-27', '2017-12-19', NULL),
(52, 15, 3, '2018-02-17', '2018-02-18', '2018-02-17', NULL),
(53, 15, 3, '2018-02-17', '2018-02-16', '2018-02-17', NULL),
(54, 15, 3, NULL, '2018-02-14', '2018-02-17', NULL),
(55, 14, 3, NULL, '2018-02-15', '2018-02-17', NULL),
(56, 13, 1, NULL, '2018-02-28', '2018-02-26', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_book_category`
--

CREATE TABLE `tbl_book_category` (
  `book_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_book_overdue`
--

CREATE TABLE `tbl_book_overdue` (
  `book_borrower_ref_id` int(11) DEFAULT NULL,
  `computed_fee` float DEFAULT NULL,
  `paid` float DEFAULT NULL,
  `balance` float DEFAULT NULL,
  `notes` text,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_book_overdue`
--

INSERT INTO `tbl_book_overdue` (`book_borrower_ref_id`, `computed_fee`, `paid`, `balance`, `notes`, `id`) VALUES
(49, 8, 8, 0, 'Paid overdue fee from total 8.0', 1),
(50, 0, 0, 0, 'Paid overdue fee from total 0.0', 2),
(51, 0, 0, 0, 'Paid overdue fee from total 0.0', 3),
(50, 102, 102, 0, 'Paid overdue fee from total 102.0', 4),
(53, 2, 2, 0, 'Paid overdue fee from total 2.0', 5);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_borrower`
--

CREATE TABLE `tbl_borrower` (
  `borrower_id` int(11) NOT NULL,
  `borrower_barcode_id` double NOT NULL,
  `title` varchar(250) NOT NULL,
  `firstname` varchar(250) NOT NULL,
  `lastname` varchar(250) NOT NULL,
  `birthday` date DEFAULT NULL,
  `gender` varchar(100) DEFAULT NULL,
  `address1` varchar(250) DEFAULT NULL,
  `address2` varchar(250) DEFAULT NULL,
  `address3` varchar(250) DEFAULT NULL,
  `postal_code` varchar(100) DEFAULT NULL,
  `town` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `mobile_number` varchar(100) DEFAULT NULL,
  `email_address` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_borrower`
--

INSERT INTO `tbl_borrower` (`borrower_id`, `borrower_barcode_id`, `title`, `firstname`, `lastname`, `birthday`, `gender`, `address1`, `address2`, `address3`, `postal_code`, `town`, `country`, `mobile_number`, `email_address`) VALUES
(1, 1, 'Mr', 'Kevin', 'Daus1', '2017-11-22', 'MALE', 'asdasdasdasd', 'asd', NULL, 'sdasd', 'municipality', NULL, '09973470558', 'test@gmail.com'),
(2, 2, 'Mrs', 'Janey', 'Dee', '2017-11-15', 'FEMALE', 'asd', 'asd', 'sdasd', 'asd', 'municipality', NULL, '07312654978', 'asdasd@gmail.com'),
(3, 3, 'Mrs', 'Janey', 'Tester1', '2017-12-13', 'FEMALE', 'Street1', 'Barangay1', NULL, '3709', 'Solano', NULL, '09973470558', 'tester@gmail.com'),
(4, 123123123, 'sd', 'asd', 'asd', '2017-12-13', 'MALE', '', '', NULL, '', '', NULL, '09999999', ''),
(5, 1234567891, 'asd', 'asd', 'dsddddd', '2017-12-13', 'MALE', '', '', NULL, '', '', NULL, '099999999', ''),
(6, 123123124, 'asd', 'asd', 'asd', NULL, 'MALE', '', '', NULL, '', '', NULL, '', '');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_category`
--

CREATE TABLE `tbl_category` (
  `category_id` int(11) NOT NULL,
  `title` varchar(250) NOT NULL,
  `description` text NOT NULL,
  `date_created` date NOT NULL,
  `date_updated` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_profile`
--

CREATE TABLE `tbl_profile` (
  `profile_id` int(11) NOT NULL,
  `firstname` varchar(250) NOT NULL,
  `lastname` varchar(250) NOT NULL,
  `email_address` varchar(250) NOT NULL,
  `phone_number` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_profile`
--

INSERT INTO `tbl_profile` (`profile_id`, `firstname`, `lastname`, `email_address`, `phone_number`) VALUES
(1, 'john', 'doe', 'john@gmail.com', '07312645987'),
(2, 'asd', 'asd', 'asd@asd.com', '07321654987'),
(3, 'asd', 'asd', 'asd@gmail.com', '321654987'),
(4, 'asd', 'asd', 'asd@gmail.com', '321654987'),
(5, 'asd', 'asd', 'asd@gmail.com', '321654987'),
(6, 'asd', 'asd', 'asd@gmail.com', '321654987'),
(7, 'qwe', 'qwe', 'qwe@gmail.com', '321654987'),
(8, 'qweewq', 'qweewq', 'qwe@gmail.com', '321654987'),
(9, 'ewq', 'ewq', 'ewq@gmail.com', '321654987'),
(10, 'asd', 'asd', 'asdasd@gmail.com', '07321654987'),
(11, 'qweewqasd', 'qweewq', 'qwe@gmail.com', '321654987'),
(12, 'qwe', 'qwe', 'qwe@gmail.com', '07321654987'),
(13, 'janete', 'de guzman', 'janet_deguzman@gmail.com', '07321654987'),
(14, 'ja', 'ne', 'jane@gmail.com', '07321654987');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_settings`
--

CREATE TABLE `tbl_settings` (
  `id` int(11) NOT NULL,
  `setting_name` varchar(250) NOT NULL,
  `setting_value` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_settings`
--

INSERT INTO `tbl_settings` (`id`, `setting_name`, `setting_value`) VALUES
(1, 'SMS_API_CODE', 'db29df73eccd5dee9a059d60eba739bd'),
(2, 'NOTIFICATION_STATUS', 'enabled'),
(4, 'SMS_SENDER_NAME', 'SEMAPHORE');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sms_notification_log`
--

CREATE TABLE `tbl_sms_notification_log` (
  `id` int(11) NOT NULL,
  `book_borrower_id` int(11) NOT NULL,
  `status` varchar(250) NOT NULL,
  `date_sent` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_sms_notification_log`
--

INSERT INTO `tbl_sms_notification_log` (`id`, `book_borrower_id`, `status`, `date_sent`) VALUES
(8, 50, 'SENT', '2018-01-09 05:53:53'),
(9, 53, 'SENT', '2018-02-17 22:49:55'),
(12, 55, 'SENT', '2018-02-17 23:54:59'),
(13, 54, 'SENT', '2018-02-17 23:55:45'),
(14, 56, 'SENT', '2018-03-01 02:43:54');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `id` int(11) NOT NULL,
  `username` varchar(250) NOT NULL,
  `password` varchar(250) NOT NULL,
  `first_secret_question` varchar(250) NOT NULL,
  `first_secret_answer` varchar(250) NOT NULL,
  `second_secret_question` varchar(250) NOT NULL,
  `second_secret_answer` varchar(250) NOT NULL,
  `third_secret_question` varchar(250) NOT NULL,
  `third_secret_answer` varchar(250) NOT NULL,
  `role` varchar(100) NOT NULL,
  `profile_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`id`, `username`, `password`, `first_secret_question`, `first_secret_answer`, `second_secret_question`, `second_secret_answer`, `third_secret_question`, `third_secret_answer`, `role`, `profile_id`) VALUES
(1, 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'Whats the name of your dog?', 'osama', 'Who is your highschool crush?', 'dyan', 'Who is your favorite hero ?', 'Tony Stark', 'ADMIN', 1),
(10, 'janet', 'f10e2821bbbea527ea02200352313bc059445190', 'Whats the name of your dog?', 'janet', 'First name of your trusted cousin?', 'janet', 'Who is your favorite hero ?', 'janet', 'Librarian', 13),
(11, 'user1', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 'Whats the name of your dog?', 'dog', 'First name of your trusted cousin?', 'dog', 'Who is your favorite hero ?', 'dog', 'Librarian', 14);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_books`
--
ALTER TABLE `tbl_books`
  ADD PRIMARY KEY (`book_id`);

--
-- Indexes for table `tbl_book_borrower`
--
ALTER TABLE `tbl_book_borrower`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tbl_book_borrower_ibfk_1` (`borrower_id`),
  ADD KEY `tbl_book_borrower_bookfk` (`book_id`);

--
-- Indexes for table `tbl_book_category`
--
ALTER TABLE `tbl_book_category`
  ADD KEY `book_id` (`book_id`),
  ADD KEY `tbl_book_category_ibfk_2` (`category_id`);

--
-- Indexes for table `tbl_book_overdue`
--
ALTER TABLE `tbl_book_overdue`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_borrower_ref_id` (`book_borrower_ref_id`);

--
-- Indexes for table `tbl_borrower`
--
ALTER TABLE `tbl_borrower`
  ADD PRIMARY KEY (`borrower_id`),
  ADD UNIQUE KEY `borrower_barcode_id` (`borrower_barcode_id`);

--
-- Indexes for table `tbl_category`
--
ALTER TABLE `tbl_category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `tbl_profile`
--
ALTER TABLE `tbl_profile`
  ADD PRIMARY KEY (`profile_id`);

--
-- Indexes for table `tbl_settings`
--
ALTER TABLE `tbl_settings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_sms_notification_log`
--
ALTER TABLE `tbl_sms_notification_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tbl_sms_notification_log_ibfk_1` (`book_borrower_id`);

--
-- Indexes for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `profile_id` (`profile_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_books`
--
ALTER TABLE `tbl_books`
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
--
-- AUTO_INCREMENT for table `tbl_book_borrower`
--
ALTER TABLE `tbl_book_borrower`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;
--
-- AUTO_INCREMENT for table `tbl_book_overdue`
--
ALTER TABLE `tbl_book_overdue`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `tbl_borrower`
--
ALTER TABLE `tbl_borrower`
  MODIFY `borrower_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `tbl_category`
--
ALTER TABLE `tbl_category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_profile`
--
ALTER TABLE `tbl_profile`
  MODIFY `profile_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- AUTO_INCREMENT for table `tbl_settings`
--
ALTER TABLE `tbl_settings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `tbl_sms_notification_log`
--
ALTER TABLE `tbl_sms_notification_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- AUTO_INCREMENT for table `tbl_user`
--
ALTER TABLE `tbl_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_book_borrower`
--
ALTER TABLE `tbl_book_borrower`
  ADD CONSTRAINT `tbl_book_borrower_bookfk` FOREIGN KEY (`book_id`) REFERENCES `tbl_books` (`book_id`);

--
-- Constraints for table `tbl_book_category`
--
ALTER TABLE `tbl_book_category`
  ADD CONSTRAINT `tbl_book_category_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `tbl_books` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_book_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `tbl_category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_book_overdue`
--
ALTER TABLE `tbl_book_overdue`
  ADD CONSTRAINT `tbl_book_overdue_ibfk_1` FOREIGN KEY (`book_borrower_ref_id`) REFERENCES `tbl_book_borrower` (`id`);

--
-- Constraints for table `tbl_sms_notification_log`
--
ALTER TABLE `tbl_sms_notification_log`
  ADD CONSTRAINT `tbl_sms_notification_log_ibfk_1` FOREIGN KEY (`book_borrower_id`) REFERENCES `tbl_book_borrower` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD CONSTRAINT `tbl_user_ibfk_1` FOREIGN KEY (`profile_id`) REFERENCES `tbl_profile` (`profile_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
