# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.21)
# Database: db_example
# Generation Time: 2018-11-04 13:03:35 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table indicator
# ------------------------------------------------------------

LOCK TABLES `indicator` WRITE;
/*!40000 ALTER TABLE `indicator` DISABLE KEYS */;

INSERT INTO `indicator` (`id`, `name`, `level`, `parent`)
VALUES
	(1,'排放管理',1,NULL),
	(2,'资源管理',1,NULL),
	(3,'应对气候变化',1,NULL);

/*!40000 ALTER TABLE `indicator` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table indicator_attribute
# ------------------------------------------------------------



# Dump of table module
# ------------------------------------------------------------

LOCK TABLES `module` WRITE;
/*!40000 ALTER TABLE `module` DISABLE KEYS */;

INSERT INTO `module` (`id`, `name`)
VALUES
	(1,'环境管理'),
	(2,'员工管理'),
	(3,'安全管理');

/*!40000 ALTER TABLE `module` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table module_has_indicator
# ------------------------------------------------------------

LOCK TABLES `module_has_indicator` WRITE;
/*!40000 ALTER TABLE `module_has_indicator` DISABLE KEYS */;

INSERT INTO `module_has_indicator` (`module_id`, `indicator_id`)
VALUES
	(1,1),
	(1,2),
	(1,3);

/*!40000 ALTER TABLE `module_has_indicator` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table module_has_indicator
# ------------------------------------------------------------

LOCK TABLES `indicator_for_company` WRITE;
/*!40000 ALTER TABLE `indicator_for_company` DISABLE KEYS */;

INSERT INTO `indicator_for_company` (`indicator_id`, `company_id`)
VALUES
	(1,1),
	(2,1),
	(3,1);

/*!40000 ALTER TABLE `indicator_for_company` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table template
# ------------------------------------------------------------

LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;

INSERT INTO `template` (`id`, `name`)
VALUES
	(1,'生活用纸'),
	(2,'饮用水');


/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table template_has_module
# ------------------------------------------------------------

LOCK TABLES `template_has_module` WRITE;
/*!40000 ALTER TABLE `template_has_module` DISABLE KEYS */;

INSERT INTO `template_has_module` (`template_id`, `module_id`)
VALUES
	(1,1),
	(1,2),
	(1,3);

/*!40000 ALTER TABLE `template_has_module` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`id`, `name`)
VALUES
	(1,'ROLE_ADMIN1'),
	(2,'ROLE_ADMIN2'),
	(3,'ROLE_AUDITOR'),
	(4,'ROLE_OPERATOR');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `username`,`password`)
VALUES
	(1,'ADMIN','$2a$10$qe.2q0.icyebaiqar.7TguXLnmgqpVwIFdb53WsNRXBfkpx1..rRC');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_roles
# ------------------------------------------------------------

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;

INSERT INTO `user_roles` (`user_id`, `role_id`)
VALUES
	(1,1);

/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;

INSERT INTO `company` (`id`, `name`)
VALUES
	(1,'ABC'),
	(2,'DEF');

/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table company_template
# ------------------------------------------------------------

LOCK TABLES `company_template` WRITE;
/*!40000 ALTER TABLE `company_template` DISABLE KEYS */;

INSERT INTO `company_template` (`company_id`, `template_id`)
VALUES
	(1,1),
	(2,2);

/*!40000 ALTER TABLE `company_template` ENABLE KEYS */;
UNLOCK TABLES;


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
