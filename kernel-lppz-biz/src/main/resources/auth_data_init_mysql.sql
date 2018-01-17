-- MySQL dump 10.13  Distrib 5.6.24, for Linux (x86_64)
--
-- Host: localhost    Database: edep
-- ------------------------------------------------------
-- Server version	5.6.24-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authpermission`
--
DROP TABLE IF EXISTS `authpermission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authpermission` (
  `updatetime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `menudesc` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `updator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `permcode` varchar(255) COLLATE utf8_bin NOT NULL,
  `menuurl` varchar(255) COLLATE utf8_bin NOT NULL,
  `creator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `creationtime` timestamp NULL DEFAULT NULL,
  `createtime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id` varchar(128) COLLATE utf8_bin NOT NULL,
  `authpermid` varchar(255) COLLATE utf8_bin NOT NULL,
  `menuname` varchar(255) COLLATE utf8_bin NOT NULL,
  `modifiedtime` timestamp NULL DEFAULT NULL,
  `parentcode` varchar(255) COLLATE utf8_bin NOT NULL,
  `permtype` varchar(255) COLLATE utf8_bin NOT NULL,
  `typecode` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `visible` varchar(128) COLLATE utf8_bin NOT NULL,
  `permindex` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_authpermission` (`authpermid`,`tenant`),
  UNIQUE KEY `UX_authpermission_authpermid` (`authpermid`,`tenant`),
  UNIQUE KEY `UX_authpermission_permcode` (`permcode`,`tenant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authpermission`
--

LOCK TABLES `authpermission` WRITE;
/*!40000 ALTER TABLE `authpermission` DISABLE KEYS */;
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'F_103_110_1022', '/pages/right/userAdd.jsp', 'admin', NULL, NULL, 'single|PermissionData|1022', '1022', '员工管理_新增', NULL, 'M-103_118', 'func', 'PermissionData', 'single', '1', 1022);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'F_103_110_1023', '/pages/right/userAuth.jsp', 'admin', NULL, NULL, 'single|PermissionData|1023', '1023', '员工管理_授权', NULL, 'M-103_118', 'func', 'PermissionData', 'single', '1', 1023);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'F_103_110_1024', '/webresources/ui/user/status/1', 'admin', NULL, NULL, 'single|PermissionData|1024', '1024', '员工管理_启用', NULL, 'M-103_118', 'func', 'PermissionData', 'single', '1', 1024);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'F_103_110_1025', '/webresources/ui/user/status/2', 'admin', NULL, NULL, 'single|PermissionData|1025', '1025', '员工管理_禁用', NULL, 'M-103_118', 'func', 'PermissionData', 'single', '1', 1025);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'F_103_110_1395', '/webresources/ui/user', 'admin', NULL, NULL, 'single|PermissionData|1395', '1395', '员工管理_查询', NULL, 'M-103_118', 'func', 'PermissionData', 'single', '1', 1395);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'F_103_111_1362', '/webresources/ui/authRole', 'admin', NULL, NULL, 'single|PermissionData|1362', '1362', '角色管理_查询', NULL, 'M-103_119', 'func', 'PermissionData', 'single', '1', 1362);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'F_103_111_1363', '/pages/auth/roleEdit.jsp', 'admin', NULL, NULL, 'single|PermissionData|1363', '1363', '角色管理_修改', NULL, 'M-103_119', 'func', 'PermissionData', 'single', '1', 1363);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'F_103_111_1364', '/pages/auth/roleAdd.jsp', 'admin', NULL, NULL, 'single|PermissionData|1364', '1364', '角色管理_新增', NULL, 'M-103_119', 'func', 'PermissionData', 'single', '1', 1364);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'M-103_118', '/pages/right/userList.jsp', 'admin', NULL, NULL, 'single|PermissionData|118', '118', '员工管理', NULL, 'M-103_3', 'menu', 'PermissionData', 'single', '1', 118);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'M-103_119', '/pages/auth/roleList.jsp', 'admin', NULL, NULL, 'single|PermissionData|119', '119', '角色管理', NULL, 'M-103_3', 'menu', 'PermissionData', 'single', '1', 119);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'M-103_120', '/pages/perm/permList.jsp', 'admin', NULL, NULL, 'single|PermissionData|120', '120', '权限管理', NULL, 'M-103_3', 'menu', 'PermissionData', 'single', '1', 120);
INSERT INTO `authpermission` VALUES (NULL, NULL, 'admin', 'M-103_3', '/xxx/xxx/xxx.jsp', 'admin', NULL, NULL, 'single|PermissionData|3', '3', '系统管理', NULL, 'M-root', 'menu', 'PermissionData', 'single', '1', 3);
/*!40000 ALTER TABLE `authpermission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authrole`
--

DROP TABLE IF EXISTS `authrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authrole` (
  `updatetime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `updator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `rolecode` varchar(255) COLLATE utf8_bin NOT NULL,
  `rolelevel` varchar(255) COLLATE utf8_bin NOT NULL,
  `superrolecode` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `enableflg` varchar(255) COLLATE utf8_bin NOT NULL,
  `creator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `creationtime` timestamp NULL DEFAULT NULL,
  `createtime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id` varchar(128) COLLATE utf8_bin NOT NULL,
  `roledesc` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `rolename` varchar(255) COLLATE utf8_bin NOT NULL,
  `modifiedtime` timestamp NULL DEFAULT NULL,
  `authroleid` varchar(255) COLLATE utf8_bin NOT NULL,
  `typecode` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_authrole` (`authroleid`,`tenant`),
  UNIQUE KEY `UX_authrole_authroleid` (`authroleid`,`tenant`),
  UNIQUE KEY `UX_authrole_rolecode` (`rolecode`,`tenant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authrole`
--

LOCK TABLES `authrole` WRITE;
/*!40000 ALTER TABLE `authrole` DISABLE KEYS */;
INSERT INTO `authrole` VALUES ('2016-03-01 09:40:53', 'admin', 'R01', '1', NULL, '1', 'admin', NULL, NULL, 'single|RoleData|R01', '系统管理员', '管理员', NULL, 'R01', 'RoleData', 'single');
/*!40000 ALTER TABLE `authrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authrolepermissionrel`
--

DROP TABLE IF EXISTS `authrolepermissionrel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authrolepermissionrel` (
  `creationtime` datetime DEFAULT NULL,
  `id` varchar(128) COLLATE utf8_bin NOT NULL,
  `createtime` timestamp NULL DEFAULT NULL,
  `authrolepermrelid` varchar(255) COLLATE utf8_bin NOT NULL,
  `updatetime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `updator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `modifiedtime` timestamp NULL DEFAULT NULL,
  `permcode` varchar(255) COLLATE utf8_bin NOT NULL,
  `authroleid` varchar(255) COLLATE utf8_bin NOT NULL,
  `creator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `typecode` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_authrolepermissionrel` (`authrolepermrelid`,`tenant`),
  UNIQUE KEY `UX_authrolepermissionrel_authrolepermrelid` (`authrolepermrelid`,`tenant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authrolepermissionrel`
--

LOCK TABLES `authrolepermissionrel` WRITE;
/*!40000 ALTER TABLE `authrolepermissionrel` DISABLE KEYS */;
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_M-103_3', NULL, 'R01_M-103_3', NULL, 'admin', NULL, 'M-103_3', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_M-103_118', NULL, 'R01_M-103_118', NULL, 'admin', NULL, 'M-103_118', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_F_103_110_1022', NULL, 'R01_F_103_110_1022', NULL, 'admin', NULL, 'F_103_110_1022', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_F_103_110_1023', NULL, 'R01_F_103_110_1023', NULL, 'admin', NULL, 'F_103_110_1023', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_F_103_110_1024', NULL, 'R01_F_103_110_1024', NULL, 'admin', NULL, 'F_103_110_1024', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_F_103_110_1025', NULL, 'R01_F_103_110_1025', NULL, 'admin', NULL, 'F_103_110_1025', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_F_103_110_1395', NULL, 'R01_F_103_110_1395', NULL, 'admin', NULL, 'F_103_110_1395', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_M-103_119', NULL, 'R01_M-103_119', NULL, 'admin', NULL, 'M-103_119', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_F_103_111_1362', NULL, 'R01_F_103_111_1362', NULL, 'admin', NULL, 'F_103_111_1362', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_F_103_111_1363', NULL, 'R01_F_103_111_1363', NULL, 'admin', NULL, 'F_103_111_1363', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_F_103_111_1364', NULL, 'R01_F_103_111_1364', NULL, 'admin', NULL, 'F_103_111_1364', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_M-103_120', NULL, 'R01_M-103_120', NULL, 'admin', NULL, 'M-103_120', 'R01', 'admin', 'RolePermissionData', 'single');
INSERT INTO `authrolepermissionrel` VALUES (NULL, 'single|RolePermissionData|R01_M-103_3_121', NULL, 'R01_M-103_3_121', NULL, 'admin', NULL, 'M-103_3_121', 'R01', 'admin', 'RolePermissionData', 'single');
/*!40000 ALTER TABLE `authrolepermissionrel` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Table structure for table `authuser`
--

DROP TABLE IF EXISTS `authuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authuser` (
  `userpwd` varchar(255) COLLATE utf8_bin NOT NULL,
  `updatetime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `status` varchar(255) COLLATE utf8_bin NOT NULL,
  `updator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `userid` varchar(255) COLLATE utf8_bin NOT NULL,
  `employeeno` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `creationtime` timestamp NULL DEFAULT NULL,
  `createtime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id` varchar(128) COLLATE utf8_bin NOT NULL,
  `username` varchar(255) COLLATE utf8_bin NOT NULL,
  `authuserid` varchar(255) COLLATE utf8_bin NOT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `modifiedtime` timestamp NULL DEFAULT NULL,
  `typecode` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `parentuserid` varchar(255)  COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_authuser` (`authuserid`,`tenant`),
  UNIQUE KEY `UX_authuser_userid` (`userid`,`tenant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authuser`
--

LOCK TABLES `authuser` WRITE;
/*!40000 ALTER TABLE `authuser` DISABLE KEYS */;
INSERT INTO `authuser` VALUES ('admin', NULL, '1', 'admin', 'admin', '001', 'admin', NULL, NULL, 'single|UserData|admin', '管理员', 'admin', '', NULL, 'UserData', 'single','admin');
/*!40000 ALTER TABLE `authuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authuserrolerel`
--

DROP TABLE IF EXISTS `authuserrolerel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authuserrolerel` (
  `creationtime` timestamp NULL DEFAULT NULL,
  `id` varchar(128) COLLATE utf8_bin NOT NULL,
  `createtime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `updatetime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `authuserid` varchar(128) COLLATE utf8_bin NOT NULL,
  `updator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `modifiedtime` timestamp NULL DEFAULT NULL,
  `authroleid` varchar(255) COLLATE utf8_bin NOT NULL,
  `creator` varchar(255) COLLATE utf8_bin NOT NULL,
  `authuserrolerelid` varchar(255) COLLATE utf8_bin NOT NULL,
  `typecode` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_authuserrolerel_authuserid` (`authuserid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authuserrolerel`
--

LOCK TABLES `authuserrolerel` WRITE;
/*!40000 ALTER TABLE `authuserrolerel` DISABLE KEYS */;
INSERT INTO `authuserrolerel` VALUES (NULL, 'single|UserRoleData|admin_R01', NULL, NULL, 'admin', 'admin', NULL, 'R01', 'admin', 'admin_R01', 'UserRoleData', 'single');
/*!40000 ALTER TABLE `authuserrolerel` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-25  9:41:48
