-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 21, 2019 at 07:27 AM
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
(15, 'ABCDEFG', '123456789', 'Unavailable', 'The Art of War', 'Sun Tzu', 'Short Description', '1st', '2017', '2017-12-14', '2019-05-21'),
(16, 'aaaaa', 'bbbbb', 'Available', 'asd', 'asd', '', '', '', '2019-05-20', '2019-05-21'),
(17, 'dddddd', 'dddddd', 'Available', 'asdasd', 'asdasd', 'asdasd', '', '', '2019-05-20', '2019-05-21');

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
(4, 15, 9, '2019-05-20', '2019-05-22', '2019-05-20', NULL),
(5, 15, 9, NULL, '2019-05-29', '2019-05-20', NULL),
(6, 15, 7, NULL, '2019-05-24', '2019-05-21', NULL);

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
(7, 123456, 'asd', 'asd', 'asd', '2018-03-21', 'MALE', 'asd', 'asdasd', NULL, '', '', NULL, '0997347505054', ''),
(9, 1234567, 'Mrs', 'Jane', 'Dee', '2019-05-16', 'MALE', 'Gaddang', 'Quirino', NULL, '3709', 'Solano', NULL, '07321654987', ''),
(10, 123456789123, 'asd', 'asdasd', 'asdasdasd', '2019-05-08', 'MALE', '', '', NULL, '', '', NULL, '', '');

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
(4, 'SMS_SENDER_NAME', 'SEMAPHORE'),
(5, 'BOOK_PENALTY_PER_DAY', '1.0');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sms_notification_log`
--

CREATE TABLE `tbl_sms_notification_log` (
  `id` int(11) NOT NULL,
  `book_borrower_id` int(11) DEFAULT NULL,
  `status` varchar(250) NOT NULL,
  `date_sent` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  ADD KEY `tbl_book_borrower_bookfk` (`book_id`),
  ADD KEY `borrower_id` (`borrower_id`);

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
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `tbl_book_borrower`
--
ALTER TABLE `tbl_book_borrower`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `tbl_book_overdue`
--
ALTER TABLE `tbl_book_overdue`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_borrower`
--
ALTER TABLE `tbl_borrower`
  MODIFY `borrower_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `tbl_sms_notification_log`
--
ALTER TABLE `tbl_sms_notification_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
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
  ADD CONSTRAINT `tbl_book_borrower_bookfk` FOREIGN KEY (`book_id`) REFERENCES `tbl_books` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_book_borrower_ibfk_1` FOREIGN KEY (`borrower_id`) REFERENCES `tbl_borrower` (`borrower_id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
