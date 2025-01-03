USE `ah_blacklist`;

-- Entries

CREATE TABLE IF NOT EXISTS `entry` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`system_name` varchar(63) NOT NULL,
	`expires_at` timestamp,
	`active` tinyint(1) NOT NULL DEFAULT 1,
	`created_by` varchar(63) NOT NULL,
	`revoked_by` varchar(63) DEFAULT NULL,
	`reason` varchar(1024) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;  