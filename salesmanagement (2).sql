-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 23, 2025 at 04:30 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `salesmanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
('C001', 'Đồ uống'),
('C002', 'Đồ ăn nhanh'),
('C003', 'Mỹ phẩm'),
('C004', 'Đồ điện tử'),
('C005', 'Đồ thể thao');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` varchar(10) NOT NULL,
  `order_date` date NOT NULL,
  `delivery_date` date NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `address` varchar(255) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `total_amount` decimal(15,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `order_date`, `delivery_date`, `created_by`, `address`, `note`, `total_amount`) VALUES
('O001', '2025-09-30', '2025-10-01', 'admin', 'Số 9 Phan Thị Ràng', 'Khách đặt Pepsi và Mì trộn', 0.00),
('O002', '2025-09-30', '2025-10-02', 'user1', 'Số 7 Phan Thị Ràng', 'Khách chỉ gọi Pepsi', 0.00),
('O003', '2025-09-30', '2025-10-03', 'user2', 'Đại học Kiên Giang', 'Khách chỉ gọi Mì trộn', 0.00),
('O045D022F4', '2025-10-17', '2025-10-18', 'user1', '100 pdl', 'o có j hết', 20000.00),
('O150CD3561', '2025-10-04', '2025-10-05', 'user1', '188 Đinh Bộ Lĩnh', 'Kế bv Bình An', 35000.00),
('O1A3D00EFF', '2025-10-04', '2025-10-05', 'user1', '134 Phan Bội Châu', 'xin thêm nước sốt', 55000.00),
('O4F7B46A1D', '2025-10-23', '2025-10-24', 'user1', 'o có', 'o có luôn', 55000.00),
('O538A2C6EC', '2025-10-22', '2025-10-23', 'user1', '1111111111', '1111111111', 15000.00),
('O5665355CD', '2025-10-17', '2025-10-18', 'user1', '133 pdl', 'o có', 205000.00),
('O7B24BA462', '2025-10-22', '2025-10-23', 'user1', '123', '0', 35000.00),
('O87362021A', '2025-10-08', '2025-10-09', 'user5', '213 Bùi Viện', 'lấy kèm nước đá', 100000.00),
('O90D107D79', '2025-10-08', '2025-10-09', 'user7', '133 phan đăng lưu', 'ko có', 210000.00),
('O9896219BB', '2025-10-22', '2025-10-23', 'user1', '22222252', '22222222', 55000.00),
('OD37F907F1', '2025-10-08', '2025-10-09', 'user5', '222 Phan Châu Trinh', 'mì trộn không cay', 90000.00),
('ODA93410E1', '2025-10-04', '2025-10-05', 'user1', '133 Phan Đăng Lưu', 'Mì trộn không cay', 55000.00),
('OE53FE31B1', '2025-10-08', '2025-10-09', 'user7', 'a', 'a', 55000.00);

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `id` varchar(10) NOT NULL,
  `order_id` varchar(10) NOT NULL,
  `product_id` varchar(10) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `amount` decimal(15,2) GENERATED ALWAYS AS (`price` * `quantity`) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `order_details`
--

INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `price`, `quantity`) VALUES
('OD002', 'O001', 'P002', 35000.00, 1),
('OD004', 'O003', 'P002', 35000.00, 2),
('OD162E18DE', 'O4F7B46A1D', 'P004', 10000.00, 1),
('OD2CDB80E2', 'OD37F907F1', 'P002', 35000.00, 2),
('OD3DB3AD5C', 'O5665355CD', 'P002', 35000.00, 1),
('OD4F7C3320', 'O4F7B46A1D', 'P003', 30000.00, 1),
('OD73359976', 'OE53FE31B1', 'P002', 35000.00, 1),
('OD74FF3A5E', 'O9896219BB', 'P002', 35000.00, 1),
('OD7A170F36', 'O150CD3561', 'P002', 35000.00, 1),
('ODA1FC328F', 'O5665355CD', 'P003', 30000.00, 3),
('ODA46C4460', 'O1A3D00EFF', 'P002', 35000.00, 1),
('ODA7D72308', 'O9896219BB', 'P003', 30000.00, 1),
('ODB003ABB1', 'O538A2C6EC', 'P002', 35000.00, 1),
('ODB1CC7D7D', 'O90D107D79', 'P002', 35000.00, 6),
('ODB5630AA8', 'ODA93410E1', 'P002', 35000.00, 1),
('ODC02B31D9', 'O7B24BA462', 'P002', 35000.00, 1),
('ODE7082F13', 'O4F7B46A1D', 'P002', 35000.00, 1);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `ID` varchar(10) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Category_ID` varchar(10) DEFAULT NULL,
  `Price` decimal(15,2) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Description` text DEFAULT NULL,
  `promotion_id` varchar(10) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`ID`, `Name`, `Category_ID`, `Price`, `Quantity`, `Description`, `promotion_id`, `image_path`) VALUES
('P002', 'Mì trộn cay', 'C002', 35000.00, 40, 'Mì trộn cay', NULL, NULL),
('P003', 'Galaxy s21 5g', 'C004', 15000000.00, 20, '256gb', NULL, 'D:\\\\HuongDoiTuongJava(ChuongTrinhChinh)\\\\Sales_Management\\\\images\\\\Screenshot 2025-10-23 212539.png'),
('P004', 'Pepsi đen', 'C001', 10000.00, 100, 'o có', NULL, 'D:\\\\HuongDoiTuongJava(ChuongTrinhChinh)\\\\Sales_Management\\\\images\\\\Screenshot 2025-10-23 185454.png'),
('P005', 'ip17 ProMax', 'C004', 30000000.00, 20, '1TB', NULL, 'D:\\\\HuongDoiTuongJava(ChuongTrinhChinh)\\\\Sales_Management\\\\images\\\\Screenshot 2025-10-23 212144.png');

-- --------------------------------------------------------

--
-- Table structure for table `promotions`
--

CREATE TABLE `promotions` (
  `id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `discount` decimal(15,2) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `promotions`
--

INSERT INTO `promotions` (`id`, `name`, `discount`, `start_date`, `end_date`, `description`) VALUES
('KM1', 'Giảm giá sập sàn', 20000.00, '2025-10-25', '2025-10-26', 'o có'),
('KM3', 'Mừng khai trương', 30000.00, '2025-10-22', '2025-10-23', 'có');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `Role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`, `Role`) VALUES
('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'admin'),
('user1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user'),
('user2', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user'),
('user3', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user'),
('user4', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user'),
('user5', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user'),
('user7', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `created_by` (`created_by`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `order_details_ibfk_2` (`product_id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Category_ID` (`Category_ID`),
  ADD KEY `fk_product_promotion` (`promotion_id`);

--
-- Indexes for table `promotions`
--
ALTER TABLE `promotions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`username`);

--
-- Constraints for table `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `fk_product_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`Category_ID`) REFERENCES `categories` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
