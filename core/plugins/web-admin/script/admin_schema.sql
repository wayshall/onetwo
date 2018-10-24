/*
Navicat MySQL Data Transfer

Source Server         : 腾讯云-测试
Source Server Version : 50628
Source Host           : 596f0c823c306.gz.cdb.myqcloud.com:5034
Source Database       : lego

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2017-11-30 18:20:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_application
-- ----------------------------
DROP TABLE IF EXISTS `admin_application`;
CREATE TABLE `admin_application` (
  `CODE` varchar(20) COLLATE utf8_bin NOT NULL,
  `NAME` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `BASE_URL` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_AT` datetime DEFAULT NULL,
  `UPDATE_AT` datetime DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统模块表';

-- ----------------------------
-- Table structure for admin_login_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_login_log`;
CREATE TABLE `admin_login_log` (
  `ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `USER_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `NICK_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `NGID` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `OPERATION_TYE` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `OPERATION_TIME` datetime DEFAULT NULL COMMENT '操作时间',
  `IS_SUCCESS` bit(1) DEFAULT NULL COMMENT '是否成功',
  `USER_IP` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `BROWSER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `USER_AGENT` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `SESSION_ID` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `SECURITY_TOKEN` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ERROR_MSG` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for admin_permission
-- ----------------------------
DROP TABLE IF EXISTS `admin_permission`;
CREATE TABLE `admin_permission` (
  `CODE` varchar(255) COLLATE utf8_bin NOT NULL,
  `PTYPE` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `DATA_FROM` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `URL` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `METHOD` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_CODE` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `SORT` int(11) DEFAULT NULL,
  `HIDDEN` smallint(6) DEFAULT NULL,
  `CHILDREN_SIZE` int(11) DEFAULT NULL COMMENT '子节点数量',
  `APP_CODE` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `RESOURCES_PATTERN` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `STATUS` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `REMARK` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_AT` datetime DEFAULT NULL,
  `UPDATE_AT` datetime DEFAULT NULL,
  `APP_CODE` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

-- ----------------------------
-- Table structure for admin_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_permission`;
CREATE TABLE `admin_role_permission` (
  `ROLE_ID` decimal(8,0) NOT NULL,
  `PERMISSION_CODE` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`PERMISSION_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色权限表';

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `NICK_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `PASSWORD` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `EMAIL` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `MOBILE` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `GENDER` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `STATUS` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `BIRTHDAY` datetime DEFAULT NULL COMMENT '出生日期',
  `CREATE_AT` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_AT` datetime DEFAULT NULL COMMENT '最后更新时间',
  `APP_CODE` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

-- ----------------------------
-- Table structure for admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '用户主键',
  PRIMARY KEY (`ROLE_ID`,`USER_ID`),
  KEY `FK_R_AD_ROLE_USER` (`USER_ID`),
  CONSTRAINT `FK_R_AD_ROLE_USER` FOREIGN KEY (`USER_ID`) REFERENCES `admin_user` (`ID`),
  CONSTRAINT `FK_R_AD_USER_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `admin_role` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户角色关联表';

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary`;
CREATE TABLE `data_dictionary` (
  `CODE` varchar(50) COLLATE utf8_bin NOT NULL,
  `NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `VALUE` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_CODE` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `IS_VALID` tinyint(1) DEFAULT NULL COMMENT '是否有效\r\n            0:无效\r\n            1:有效\r\n            默认有效\r\n            ',
  `IS_ENUM_VALUE` tinyint(1) DEFAULT NULL COMMENT '是否枚举常量',
  `SORT` int(11) DEFAULT NULL COMMENT '排序',
  `REMARK` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_AT` datetime DEFAULT NULL COMMENT '创建日期',
  `UPDATE_AT` datetime DEFAULT NULL COMMENT '最后更新日期',
  PRIMARY KEY (`CODE`),
  KEY `AK_DICT_UNIQUE_CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据字典表';
