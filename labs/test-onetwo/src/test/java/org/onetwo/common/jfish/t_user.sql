/*
Navicat MySQL Data Transfer

Source Server         : xampp
Source Server Version : 50153
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50153
File Encoding         : 65001

Date: 2012-06-05 18:21:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(50) NOT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `BIRTH_DAY` datetime DEFAULT NULL,
  `HEIGHT` float DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('29', 'userNameTeset', 'username@qq.com', '0', '2012-01-05 23:00:58', '3.3', null, null);
INSERT INTO `t_user` VALUES ('32', 'test', 'username@qq.com', '11', '2012-01-09 00:07:22', '3.4', null, null);
INSERT INTO `t_user` VALUES ('33', 'userNameTeset', 'username@qq.com', '0', '2012-01-10 23:31:43', '3.3', null, null);
INSERT INTO `t_user` VALUES ('34', 'userNameTeset', 'username@qq.com', null, '2012-01-11 15:35:00', '3.3', null, null);
INSERT INTO `t_user` VALUES ('35', 'userNameTeset', 'username@qq.com', null, '2012-01-11 15:56:24', '3.3', null, null);
INSERT INTO `t_user` VALUES ('36', 'userNameTeset-update2012-01-11 16:18:15 949', 'username@qq.com', null, '2012-01-11 16:18:15', '3.3', null, null);
INSERT INTO `t_user` VALUES ('38', 'userNameTeset-update2012-01-11 16:15:54 767', null, null, '2012-01-11 16:15:54', '3.3', null, null);
INSERT INTO `t_user` VALUES ('39', 'userNameTeset-update2012-01-11 16:15:20 649', null, null, '2012-01-11 16:15:20', '3.3', null, null);
INSERT INTO `t_user` VALUES ('42', 'userNameMapTest', 'username@qq.com', null, '2012-05-30 11:08:02', '3.3', null, null);
INSERT INTO `t_user` VALUES ('43', 'userNameMapTest', 'username@qq.com', null, '2012-05-30 11:09:08', '3.3', null, null);
INSERT INTO `t_user` VALUES ('44', 'userNameMapTest', 'username@qq.com', null, '2012-05-30 11:11:19', '3.3', null, null);
INSERT INTO `t_user` VALUES ('64', 'userNameMapTest', 'username@qq.com', null, '2012-05-30 12:35:46', '3.3', null, null);
INSERT INTO `t_user` VALUES ('65', 'update_userNameMapTeset', 'username@qq.com', '11', '2012-05-30 12:37:39', '3.4', null, null);
