/*
Navicat MySQL Data Transfer

Source Server         : xampp
Source Server Version : 50153
Source Host           : localhost:3306
Source Database       : fishcalc

Target Server Type    : MYSQL
Target Server Version : 50153
File Encoding         : 65001

Date: 2012-11-09 14:11:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `calc_fee_details`
-- ----------------------------
DROP TABLE IF EXISTS `calc_fee_details`;
CREATE TABLE `calc_fee_details` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `date_tag` varchar(30) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `cost` double NOT NULL,
  `detail` text,
  `user_id` bigint(20) NOT NULL,
  `type_id` bigint(20) NOT NULL,
  `used_time` timestamp NULL DEFAULT NULL COMMENT '使用时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_fee_details_id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of calc_fee_details
-- ----------------------------
INSERT INTO `calc_fee_details` VALUES ('6', '2012-10', '羊城通', '2', '羊城通', '2', '4', '2012-10-19 15:29:00', '2012-10-19 15:29:32', '2012-10-23 10:16:04');
INSERT INTO `calc_fee_details` VALUES ('7', '2012-10', '猪尾巴肉', '11.23', '猪尾巴肉', '2', '3', '2012-10-22 15:17:00', '2012-10-22 15:17:09', '2012-10-23 10:15:48');
INSERT INTO `calc_fee_details` VALUES ('11', '2012-10', '猪头肉', '45', '猪头肉', '2', '8', '2012-10-23 10:03:00', '2012-10-23 10:04:14', '2012-11-08 10:24:54');
INSERT INTO `calc_fee_details` VALUES ('13', '2012-09', '9月买菜', '232.22', '9月买菜', '1', '3', '2012-09-10 17:01:00', '2012-10-23 17:01:29', '2012-10-23 17:01:29');
INSERT INTO `calc_fee_details` VALUES ('14', '2012-09', '9月交通', '22.32', '9月交通', '1', '4', '2012-09-30 17:01:00', '2012-10-23 17:02:05', '2012-10-23 17:02:05');
INSERT INTO `calc_fee_details` VALUES ('15', '2012-09', '羊城通充值', '158', '羊城通充值', '1', '4', '2012-09-10 17:18:00', '2012-10-23 17:18:35', '2012-10-23 17:19:08');
INSERT INTO `calc_fee_details` VALUES ('16', '2012-10', '10月份工资', '1000', '10月份工资', '1', '5', '2012-10-10 00:44:00', '2012-10-24 00:44:39', '2012-10-24 00:44:39');
INSERT INTO `calc_fee_details` VALUES ('23', '2012-11', '出去吃饭，烤鱼 ', '68', '被床垫的事折腾死了，然后晚上在车陂什么和味厨神，咸香烤鱼，本来说好谁猜得最接近价钱的就谁不用付钱，结果我一分不差地猜中了68，还是给了钱。结论：别和女人赌。 ', '2', '8', '2012-11-07 10:33:00', '2012-11-07 10:33:40', '2012-11-08 10:49:24');

-- ----------------------------
-- Table structure for `calc_fee_type`
-- ----------------------------
DROP TABLE IF EXISTS `calc_fee_type`;
CREATE TABLE `calc_fee_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `source` tinyint(4) NOT NULL,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `remark` varchar(1000) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_fee_type_id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of calc_fee_type
-- ----------------------------
INSERT INTO `calc_fee_type` VALUES ('3', '买菜', '-1', '1', '买菜', '2012-10-20 23:50:58', '2012-10-23 10:05:08');
INSERT INTO `calc_fee_type` VALUES ('4', '交通', '-1', '1', '交通', '2012-10-23 10:15:13', '2012-10-23 10:15:13');
INSERT INTO `calc_fee_type` VALUES ('5', '工资', '1', '1', '', '2012-10-24 00:44:13', '2012-10-24 00:44:13');
INSERT INTO `calc_fee_type` VALUES ('7', '工资', '1', '2', '', '2012-11-06 15:32:29', '2012-11-06 15:32:29');
INSERT INTO `calc_fee_type` VALUES ('8', '食', '-1', '2', '', '2012-11-07 10:33:28', '2012-11-07 10:33:28');

-- ----------------------------
-- Table structure for `cm_roles`
-- ----------------------------
DROP TABLE IF EXISTS `cm_roles`;
CREATE TABLE `cm_roles` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_role_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cm_roles
-- ----------------------------

-- ----------------------------
-- Table structure for `cm_users`
-- ----------------------------
DROP TABLE IF EXISTS `cm_users`;
CREATE TABLE `cm_users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '-1:锁定，0:未激活，1:可用',
  `create_time` timestamp NULL DEFAULT NULL,
  `laste_update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_user_id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cm_users
-- ----------------------------
INSERT INTO `cm_users` VALUES ('2', 'yingning03', '{SMD5}9Tv3zZ+MMppwr2FzJgwqjQ+ZDNYzQFlx', '1', null, null);
INSERT INTO `cm_users` VALUES ('1', 'way', '{SMD5}DLr26KNxGhRf4iiixW2ffMD2cx4D2/4A', '1', null, null);

-- ----------------------------
-- Table structure for `cm_users_roles`
-- ----------------------------
DROP TABLE IF EXISTS `cm_users_roles`;
CREATE TABLE `cm_users_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cm_users_roles
-- ----------------------------
