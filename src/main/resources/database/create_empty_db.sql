DROP DATABASE IF EXISTS `ah_blacklist`;
CREATE DATABASE `ah_blacklist`;
USE `ah_blacklist`;

-- Create tables
source create_tables.sql

-- Set up privileges
CREATE USER IF NOT EXISTS 'blacklist'@'localhost' IDENTIFIED BY 'bl@ckl1st';
CREATE USER IF NOT EXISTS 'blacklist'@'%' IDENTIFIED BY 'bl@ckl1st';
source grant_privileges.sql