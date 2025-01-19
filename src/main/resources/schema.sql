DROP DATABASE IF EXISTS `ah_blacklist`;
CREATE DATABASE `ah_blacklist`;
USE `ah_blacklist`;

-- Create users
CREATE USER IF NOT EXISTS 'blacklist'@'localhost' IDENTIFIED BY 'bl@ckl1st';
CREATE USER IF NOT EXISTS 'blacklist'@'%' IDENTIFIED BY 'bl@ckl1st';

-- Grant privileges
REVOKE ALL, GRANT OPTION FROM 'blacklist'@'localhost';
GRANT ALL PRIVILEGES ON `ah_blacklist`.`entry` TO 'blacklist'@'localhost';
GRANT ALL PRIVILEGES ON `ah_blacklist`.`logs` TO 'blacklist'@'localhost';

REVOKE ALL, GRANT OPTION FROM 'blacklist'@'%';
GRANT ALL PRIVILEGES ON `ah_blacklist`.`entry` TO 'blacklist'@'%';
GRANT ALL PRIVILEGES ON `ah_blacklist`.`logs` TO 'blacklist'@'%';

FLUSH PRIVILEGES;

-- Create tables

-- Logs

CREATE TABLE IF NOT EXISTS `logs` (
  `log_id` varchar(100) NOT NULL,
  `entry_date` timestamp(3) NULL DEFAULT NULL,
  `logger` varchar(100) DEFAULT NULL,
  `log_level` varchar(100) DEFAULT NULL,
  `message` mediumtext,
  `exception` mediumtext,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;  

-- Entry

CREATE TABLE IF NOT EXISTS `entry` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`system_name` varchar(63) NOT NULL,
	`expires_at` timestamp,
	`active` tinyint(1) NOT NULL DEFAULT 1,
	`created_by` varchar(63) NOT NULL,
	`revoked_by` varchar(63) DEFAULT NULL,
	`reason` varchar(1024) NOT NULL,
	`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;