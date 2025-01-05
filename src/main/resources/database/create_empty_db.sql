DROP DATABASE IF EXISTS `ah_blacklist`;
CREATE DATABASE `ah_blacklist`;
USE `ah_blacklist`;

-- Create users
CREATE USER IF NOT EXISTS 'blacklist'@'localhost' IDENTIFIED BY 'bl@ckl1st';
CREATE USER IF NOT EXISTS 'blacklist'@'%' IDENTIFIED BY 'bl@ckl1st';

-- Grant privileges
REVOKE ALL, GRANT OPTION FROM 'blacklist'@'localhost';
GRANT ALL PRIVILEGES ON `ah_blacklist`.`entry` TO 'blacklist'@'localhost';

REVOKE ALL, GRANT OPTION FROM 'blacklist'@'%';
GRANT ALL PRIVILEGES ON `ah_blacklist`.`entry` TO 'blacklist'@'%';

FLUSH PRIVILEGES;