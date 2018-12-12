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

INSERT INTO `indicator` (`id`, `name`, `level`, `description`,`type`,`parent`,`company_id`,`data_id`,`module_id`)
VALUES
	(1,'排放管理',1,NULL,NULL,NULL,1,NULL,1),
	(2,'资源管理',1,NULL,NULL,NULL,1,NULL,1),
	(3,'应对气候变化',1,NULL,NULL,NULL,1,NULL,1),
	(4,'有害气体',2,NULL,NULL,1,1,NULL,NULL),
	(5,'有害液体',2,NULL,NULL,1,1,NULL,NULL),
	(6,'二氧化硫',3,'二氧化硫','quantity',4,1,1,NULL);

/*!40000 ALTER TABLE `indicator` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table indicator_data
# ------------------------------------------------------------

LOCK TABLES `indicator_data` WRITE;
/*!40000 ALTER TABLE `indicator_data` DISABLE KEYS */;

INSERT INTO `indicator_data` (`id`, `unit`, `context`, `sections`,`new_context`,`new_sections`)
VALUES
  (1,'m^3',NULL,'0.1,0.1,0.2,0.1,0.2,0.3,0.2,0.12,0.15,0.23,0.18,0.2',NULL,NULL);

/*!40000 ALTER TABLE `indicator_data` ENABLE KEYS */;
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

INSERT INTO `user` (`id`, `username`,`password`,`company_id`)
VALUES
	(1,'ADMIN','$2a$10$qe.2q0.icyebaiqar.7TguXLnmgqpVwIFdb53WsNRXBfkpx1..rRC',0),
	(2,'ADMIN_ABC','$2a$10$XoGCTiHj8H7RchTXgVb8.u03rCUEkdFVvt916VILqUa4k68cnfcKa',1),
	(3,'ADMIN_DEF','$2a$10$OYCTlY4DQqBX/jQvt5Z8bOWz/xD7BXXVGa4OfP07IdJvrKFsJWPx2',2),
  (4,'ADMIN_JDK','$2a$10$.kbEUwW1pHChwYua0UWlQ.ELTJRpoYBNKHtceXQZl8wvgdjxm.ocC',3);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_roles
# ------------------------------------------------------------

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;

INSERT INTO `user_roles` (`user_id`, `role_id`)
VALUES
	(1,1),
  (2,2),
  (3,2),
  (4,2);

/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;

INSERT INTO `company` (`id`, `name`,`template_id`,`industry`)
VALUES
	(0,'PWC',null,null),
	(1,'ABC',1,'日用化工'),
  (2,'DEF',2,'生活用品'),
  (3,'JDK',null,'金融');

/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
