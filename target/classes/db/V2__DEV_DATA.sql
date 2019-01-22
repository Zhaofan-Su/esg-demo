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
	(1,'排放管理',1,'统计该公司有害化工产物的数据',NULL,NULL,1,NULL,1),
	(2,'有害气体',2,NULL,NULL,1,1,NULL,NULL),
	(3,'有害液体',2,NULL,NULL,1,1,NULL,NULL),
	(4,'二氧化硫',3,'每月二氧化硫排放量','quantity',2,1,1,NULL),

  (5,'排放管理',1,'统计该公司有害化工产物的数据',NULL,NULL,2,NULL,1),
  (6,'有害气体',2,NULL,NULL,5,2,NULL,NULL),
  (7,'有害液体',2,NULL,NULL,5,2,NULL,NULL),
  (8,'二氧化硫',3,'每月二氧化硫排放量','quantity',6,2,2,NULL),

  (9,'排放管理',1,'统计该公司有害化工产物的数据',NULL,NULL,3,NULL,1),
  (10,'有害气体',2,NULL,NULL,9,3,NULL,NULL),
  (11,'有害液体',2,NULL,NULL,9,3,NULL,NULL),
  (12,'二氧化硫',3,'每月二氧化硫排放量','quantity',10,3,3,NULL),

  (13,'含镉污水',3,'记录含镉污水具体情况','quality',3,1,4,NULL),
  (14,'含铅污水',3,'记录污水含铅量具体情况','quality',7,2,5,NULL),

	(15,'管培生管理',1,'统计公司管培生项目相关数据',NULL,NULL,4,NULL,2),
	(16,'项目资金',2,NULL,NULL,15,4,NULL,NULL),
	(17,'管培生评估',2,NULL,NULL,15,4,NULL,NULL),
	(18,'每月开销',3,'一位管培生可用资金费用','quantity',16,4,6,NULL),

  (19,'管培生管理',1,'统计公司管培生项目相关数据',NULL,NULL,5,NULL,2),
  (20,'项目资金',2,NULL,NULL,19,5,NULL,NULL),
  (21,'管培生评估',2,NULL,NULL,19,5,NULL,NULL),
  (22,'每月开销',3,'一位管培生每月预估花费费用','quantity',20,5,7,NULL),

  (23,'管培生管理',1,'统计公司管培生项目相关数据',NULL,NULL,6,NULL,2),
  (24,'项目资金',2,NULL,NULL,23,6,NULL,NULL),
  (25,'管培生评估',2,NULL,NULL,23,6,NULL,NULL),
  (26,'每月开销',3,'一位管培生每月预估花费费用','quantity',20,6,8,NULL),

  (27,'管培生综合素质评价',3,'记录公司对于每年管培生的评价','quality',17,4,9,NULL),
  (28,'管培生综合素质评价',3,'记录公司对于每年管培生的看法评价','quality',21,5,10,NULL),
  (29,'管培生综合素质评价',3,'公司对于每年管培生的评估记录','quality',25,6,11,NULL),

	(30,'公司产品',1,'统计公司产品相关数据',NULL,NULL,7,NULL,3),
	(31,'云产品',2,NULL,NULL,30,7,NULL,NULL),
	(32,'SAP HANA',3,'SAP HANA相关评价','quality',31,7,12,NULL),

  (33,'公司产品',1,'统计公司产品相关数据',NULL,NULL,8,NULL,3),
  (34,'云产品',2,NULL,NULL,33,8,NULL,NULL),
  (35,'阿里云',3,'阿里云相关评价','quality',34,8,13,NULL);

/*!40000 ALTER TABLE `indicator` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table indicator_data
# ------------------------------------------------------------

LOCK TABLES `indicator_data` WRITE;
/*!40000 ALTER TABLE `indicator_data` DISABLE KEYS */;

INSERT INTO `indicator_data` (`id`, `unit`, `context`, `sections`,`new_context`,`new_sections`,`status`)
VALUES
  (1,'m^3',NULL,'10000,10000,20000,10000,20000,30000,20000,12000,15000,23000,18000,20000',NULL,NULL,'待审核'),
  (2,'m^3',NULL,'15000,12000,19000,9000,21000,35000,15000,10000,12500,22500,17500,15000',NULL,NULL,'待审核'),
  (3,'m^3',NULL,'9000,9500,18000,12000,22000,32000,21000,11500,15400,23400,17600,21000',NULL,NULL,'待审核'),
  (4,NULL,'镉含量：0.004mg/L，符合标准',NULL,NULL,NULL,'待审核'),
  (5,NULL,'铅含量：0.01mg/L',NULL,NULL,NULL,'待审核'),
  (6,'$',NULL,'800,800,800,850,900,1000,1500,2000,2500,3000,4000,5000',NULL,NULL,'待审核'),
  (7,'$',NULL,'750,750,750,750,800,900,1200,1500,2000,3000,4000,5000',NULL,NULL,'待审核'),
  (8,'$',NULL,'750,750,750,750,800,950,1250,1550,2400,3200,4000,5500',NULL,NULL,'待审核'),
  (9,NULL,'相较于2017年，2018年管培生业务能力普遍较高，经培训之后均有较大提升',NULL,NULL,NULL,'待审核'),
  (10,NULL,'2018年管培生综合素质水平与往年没有过大差别，保持在平均水平',NULL,NULL,NULL,'通过'),
  (11,NULL,'2018年管培生综合素质为历年最高，但培训之后仍有部分不合格',NULL,NULL,NULL,'待审核'),
  (12,NULL,'软硬件结合体,提供高性能的数据查询功能',NULL,NULL,NULL,'提交'),
  (13,NULL,'云翼计划助力学生未来反展',NULL,NULL,NULL,'提交');

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
	(2,'培训管理'),
	(3,'产品管理'),
	(4,'公益管理'),
  (5,'员工管理'),
  (6,'安全管理');

/*!40000 ALTER TABLE `module` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table template
# ------------------------------------------------------------

LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;

INSERT INTO `template` (`id`, `name`)
VALUES
	(1,'巴斯夫综合数据'),
	(2,'陶氏杜邦综合数据'),
	(3,'中石化综合数据'),
	(4,'联合健康保险综合数据'),
	(5,'安盛集团综合数据'),
	(6,'中国工商银行综合数据'),
	(7,'SAP综合数据'),
	(8,'阿里巴巴综合数据');

/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table template_has_module
# ------------------------------------------------------------

LOCK TABLES `template_has_module` WRITE;
/*!40000 ALTER TABLE `template_has_module` DISABLE KEYS */;

INSERT INTO `template_has_module` (`template_id`, `module_id`)
VALUES
	(1,5),
	(1,6),
	(1,1),
	(2,5),
	(2,6),
	(2,1),
	(3,5),
	(3,6),
	(3,1),
	(4,5),
	(4,6),
	(4,2),
	(5,5),
	(5,6),
	(5,2),
	(6,5),
	(6,6),
	(6,2),
	(7,5),
	(7,6),
	(7,3),
	(8,5),
	(8,6),
	(8,3);

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

INSERT INTO `user` (`id`, `username`,`password`,`company_id`,`contactor`,`telephone`,`email`)
VALUES
	(0,'ADMIN','$2a$10$qe.2q0.icyebaiqar.7TguXLnmgqpVwIFdb53WsNRXBfkpx1..rRC',0,'大牛','010-123456789','88888888@gmail.com'),
	(1,'ADMIN_BSF','$2a$10$TIAgXdgLiNHH4VYqTQO8k.XNgZBPx3ev/7vzLwZ0TjpzhsXV4TtGq',1,'Lee','86-15503748989','1015655239@qq.com'),
	(2,'ADMIN_TSDB','$2a$10$bIayB.iYYoRZH.VomCj3IOXlnn2mt6hhPp5.aBiMmltAU4BTpa8KG',2,'Brouce','987654321','zhangsansheng@gmail.com'),
  (3,'ADMIN_ZSH','$2a$10$LwDI.mDz7SmtwnMuGV0j4uMo.RbDf8G9/oQIhcbMqC/SFdTGAxVVe',3,'王建国','989899988','woxiangzhongjiang@126.com'),
	(4,'ADMIN_LHJKBX','$2a$10$NPUT4ne7V.pH.G/W9LYVWOI4/3qUD/dJTX.UO0uWAzPxBAWwvONpi',4,'Merry','010-78698324','merry@gamil.com'),
	(5,'ADMIN_ASJT','$2a$10$AVLo/pEt01HGwq8Irzqqsev5XBePxBAwUbfWTNa0LM2DAGdTahNJ6',5,'苏晓','18717710887','suxiao@163.com'),
	(6,'ADMIN_ZGGSYH','$2a$10$Jz.1fk4NS8u48UWQW2JjveSx9nEhsNKSIxN1WK5f5G7R9j0zaYQTe',6,'岳小珏','15903748787','xiaojue.yue@gamil.com'),
	(7,'ADMIN_SAP','$2a$10$XDxB3TS.Kf1JIbaZU0ZQR.uUZkD3eWAiPcGPl0Kt5bQrHuTMZyjXq',7,'Celia','13479802342','celia.fei@sap.com'),
	(8,'ADMIN_ALBB','$2a$10$dFUUZgP2nFGQrOnHKSiQ1uQZiqCR61JUL1UyIR6yjRiPFuT5oEx4K',8,'马雲','999988887','jack.ma@ali.com'),
	(9,'ADMIN_IBM','$2a$10$FViaHxlE5n3xm6hNXQr7J.gJptPIPbvbLjyTT0co643d2cJsaGkA2',9,'Martin','010-2789874','martin@ibm.com'),
	(10,'ADMIN_UMG','$2a$10$zfqdmd928GDTWecM9CLxVuc1uzjjrPPiAP8zqAngYulMFJmTHdk0K',10,'Mr.R','9824332','r@gamil.com'),
	(11,'BSF_AU','$2a$10$b3WvCVsixO/JC4XrBpFHeO9hnJ/ZADdkJVI.oD43hC0SZATRlkj.2',1,'张三','19842332448','zhangsan@163.com'),
	(12,'BSF_OP','$2a$10$bCpTkXG7VEmLbS9qFg2jh.j7fKghFjorbXuIXVAsg50YkgL6J3kEq',1,'渡辺ゆり子','91323993','yuriko@yahoo.com');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_roles
# ------------------------------------------------------------

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;

INSERT INTO `user_roles` (`user_id`, `role_id`)
VALUES
	(0,1),
  (1,2),
  (2,2),
  (3,2),
	(4,2),
	(5,2),
	(6,2),
	(7,2),
	(8,2),
	(9,2),
	(10,2),
	(11,3),
	(12,4);

/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;

INSERT INTO `company` (`id`, `name`,`template_id`,`industry`)
VALUES
	(0,'PWC',null,null),
	(1,'巴斯夫',1,'化工'),
  (2,'陶氏杜邦',2,'化工'),
  (3,'中石化',3,'化工'),
	(4,'联合健康保险',4,'金融'),
	(5,'安盛集团',5,'金融'),
	(6,'中国工商银行',6,'金融'),
	(7,'SAP',7,'互联网'),
	(8,'阿里巴巴',8,'互联网'),
	(9,'IBM',null,'互联网'),
	(10,'Universal Music Group',null,'娱乐');

/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `domain` WRITE;
/*!40000 ALTER TABLE `domain` DISABLE KEYS */;

INSERT INTO `domain` (`id`, `name`)
VALUES
	(1,'环境'),
	(2,'社会'),
	(3,'治理');

/*!40000 ALTER TABLE `domain` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `topic` WRITE;
/*!40000 ALTER TABLE `topic` DISABLE KEYS */;

INSERT INTO `topic` (`id`, `name`,`domain_id`)
VALUES
	(1,'空气质量检测',1),
	(2,'污水排放管理',1),
	(3,'test1_E',1),
	(4,'test2_E',1),
	(5,'test3_E',1),
	(6,'职员福利保障',2),
	(7,'社会贡献度',2),
	(8,'test1_S',2),
	(9,'test2_S',2),
	(10,'test3_S',2),
	(11,'员工管理制度',3),
	(12,'公司管理评估',3),
	(13,'test1_G',3),
	(14,'test2_G',3),
	(15,'test3_G',3);

/*!40000 ALTER TABLE `topic` ENABLE KEYS */;
UNLOCK TABLES;


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
