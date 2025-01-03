USE `ah_blacklist`;

REVOKE ALL, GRANT OPTION FROM 'blacklist'@'localhost';
GRANT ALL PRIVILEGES ON `ah_blacklist`.`entry` TO 'blacklist'@'localhost';

REVOKE ALL, GRANT OPTION FROM 'blacklist'@'%';
GRANT ALL PRIVILEGES ON `ah_blacklist`.`entry` TO 'blacklist'@'%';

FLUSH PRIVILEGES;