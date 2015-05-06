/*
Navicat MySQL Data Transfer

Source Server         : xampp
Source Server Version : 50153
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50153
File Encoding         : 65001

Date: 2012-12-31 18:29:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bm_region`
-- ----------------------------
DROP TABLE IF EXISTS `bm_region`;
CREATE TABLE `bm_region` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ccode` varchar(255) DEFAULT NULL,
  `cname` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `grade` smallint(6) DEFAULT NULL,
  `parentcode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bm_region
-- ----------------------------

-- ----------------------------
-- Table structure for `h_content`
-- ----------------------------
DROP TABLE IF EXISTS `h_content`;
CREATE TABLE `h_content` (
  `ID` int(11) NOT NULL,
  `LANGUAGE` varchar(50) DEFAULT NULL,
  `SITE_ID` decimal(65,30) DEFAULT NULL,
  `BLOCK_ID` decimal(65,30) DEFAULT NULL,
  `TITLE` varchar(200) DEFAULT NULL,
  `ARTABSTRACT` text,
  `PIC_URL` varchar(200) DEFAULT NULL,
  `CREATE_USER_ID` decimal(65,30) DEFAULT NULL,
  `LAST_UPDATE_USER_ID` decimal(65,30) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  `ENTITY_ID` decimal(65,30) DEFAULT NULL,
  `SORT` decimal(65,30) DEFAULT NULL,
  `COLOR` varchar(100) DEFAULT NULL,
  `FONT_SIZE` varchar(100) DEFAULT NULL,
  `TAG` text,
  `PUBLISH_TIME` datetime DEFAULT NULL,
  `STATE` decimal(65,30) DEFAULT NULL,
  `COLUMN_CODE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of h_content
-- ----------------------------

-- ----------------------------
-- Table structure for `test`
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `int` int(11) NOT NULL DEFAULT '0',
  `bigint` bigint(20) DEFAULT NULL,
  `id` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`int`),
  UNIQUE KEY `id_index` (`id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test
-- ----------------------------

-- ----------------------------
-- Table structure for `t_address`
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `DETAIL` varchar(255) DEFAULT NULL,
  `POSTCODE` varchar(255) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `LAST_UPDATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_address
-- ----------------------------
INSERT INTO `t_address` VALUES ('106', 'DynamicUpdate address', null, '111997', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_address` VALUES ('107', 'DynamicUpdate address', null, '111997', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_address` VALUES ('108', 'DynamicUpdate address', null, '111997', '2012-12-31 18:26:52', '2012-12-31 18:26:52');

-- ----------------------------
-- Table structure for `t_article`
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=745 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_article
-- ----------------------------
INSERT INTO `t_article` VALUES ('737', null, 'new artile title 0', 'new artile content 0', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_article` VALUES ('738', '111997', 'artitle title update', 'article content update', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_article` VALUES ('739', null, 'testSaveUserWithManyArticle 0 title 0', 'testSaveUserWithManyArticle 0 content 0', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_article` VALUES ('740', null, 'testUpdateUserWithManyArticle title 1', null, '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_article` VALUES ('741', null, 'testUpdateUserWithManyArticle title 2', 'testSaveUserWithManyArticle 2 content 2', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_article` VALUES ('742', '111999', 'save testDynamicUpdateUserOneToManyArticle 0 title 0', 'save testDynamicUpdateUserOneToManyArticle 0 content 0', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_article` VALUES ('743', '111999', 'save testDynamicUpdateUserOneToManyArticle 1 title 1', 'save testDynamicUpdateUserOneToManyArticle 1 content 1', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_article` VALUES ('744', '111999', 'save testDynamicUpdateUserOneToManyArticle 2 title 2', 'save testDynamicUpdateUserOneToManyArticle 2 content 2', '2012-12-31 18:26:52', '2012-12-31 18:26:52');

-- ----------------------------
-- Table structure for `t_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_item`;
CREATE TABLE `t_item` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `count` tinyint(4) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_item
-- ----------------------------
INSERT INTO `t_item` VALUES ('36', '1', '11', '2', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_item` VALUES ('37', '2', '11', '2', '2012-12-31 18:26:52', '2012-12-31 18:26:52');
INSERT INTO `t_item` VALUES ('38', '3', '11', '2', '2012-12-31 18:26:52', '2012-12-31 18:26:52');

-- ----------------------------
-- Table structure for `t_order`
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('21', 'order address', '2012-12-31 18:26:52', '2012-12-31 18:26:52');

-- ----------------------------
-- Table structure for `t_order_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item` (
  `ITEM_ID` bigint(20) NOT NULL,
  `ORDER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ORDER_ID`,`ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order_item
-- ----------------------------
INSERT INTO `t_order_item` VALUES ('36', '21');
INSERT INTO `t_order_item` VALUES ('37', '21');
INSERT INTO `t_order_item` VALUES ('38', '21');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('5', 'admin');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(50) NOT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `BIRTH_DAY` datetime DEFAULT NULL,
  `HEIGHT` float DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `READ_ONLY_FIELD` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `id_index` (`ID`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=112000 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('111997', 'artilce author update', null, null, null, null, '3.3', '2012-12-31 18:26:52', '2012-12-31 18:26:52', null, null);
INSERT INTO `t_user` VALUES ('111998', 'testUpdateUserWithManyArticle', '1username@qq.com', null, null, '2012-12-31 18:26:52', '3.3', '2012-12-31 18:26:52', '2012-12-31 18:26:52', null, null);
INSERT INTO `t_user` VALUES ('111999', 'update testDynamicUpdateUserOneToManyArticle', '1username@qq.com', null, null, '2012-12-31 18:26:52', '3.3', '2012-12-31 18:26:52', '2012-12-31 18:26:52', null, null);

-- ----------------------------
-- Table structure for `t_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1');
INSERT INTO `t_user_role` VALUES ('111132', '5');
INSERT INTO `t_user_role` VALUES ('111153', '5');
INSERT INTO `t_user_role` VALUES ('114094', '5');
INSERT INTO `t_user_role` VALUES ('114296', '5');
INSERT INTO `t_user_role` VALUES ('114498', '5');
INSERT INTO `t_user_role` VALUES ('1564090', '5');
INSERT INTO `t_user_role` VALUES ('1564091', '5');
INSERT INTO `t_user_role` VALUES ('1564094', '5');

-- ----------------------------
-- Table structure for `uc_user`
-- ----------------------------
DROP TABLE IF EXISTS `uc_user`;
CREATE TABLE `uc_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `yooyo_card_no` varchar(255) DEFAULT NULL,
  `bnet_id` varchar(255) DEFAULT NULL,
  `bnet_account` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of uc_user
-- ----------------------------
INSERT INTO `uc_user` VALUES ('1', '20120629mrs', 'bnetId0', 'bnetAccount0', null, null);
INSERT INTO `uc_user` VALUES ('2', '20120629mrs', 'bnetId1', 'bnetAccount1', null, null);
INSERT INTO `uc_user` VALUES ('3', '20120629mrs', 'bnetId2', 'bnetAccount2', null, null);
INSERT INTO `uc_user` VALUES ('4', '20120629mrs', 'bnetId3', 'bnetAccount3', null, null);
INSERT INTO `uc_user` VALUES ('5', '20120629mrs', 'bnetId4', 'bnetAccount4', null, null);
INSERT INTO `uc_user` VALUES ('6', '20120629mrs', 'bnetId5', 'bnetAccount5', null, null);
INSERT INTO `uc_user` VALUES ('7', '20120629mrs', 'bnetId6', 'bnetAccount6', null, null);
INSERT INTO `uc_user` VALUES ('8', '20120629mrs', 'bnetId7', 'bnetAccount7', null, null);
INSERT INTO `uc_user` VALUES ('9', '20120629mrs', 'bnetId8', 'bnetAccount8', null, null);
INSERT INTO `uc_user` VALUES ('10', '20120629mrs', 'bnetId9', 'bnetAccount9', null, null);
INSERT INTO `uc_user` VALUES ('11', '20120629mrs', 'bnetId10', 'bnetAccount10', null, null);
INSERT INTO `uc_user` VALUES ('12', '20120629mrs', 'bnetId11', 'bnetAccount11', null, null);
INSERT INTO `uc_user` VALUES ('13', '20120629mrs', 'bnetId12', 'bnetAccount12', null, null);
INSERT INTO `uc_user` VALUES ('14', '20120629mrs', 'bnetId13', 'bnetAccount13', null, null);
INSERT INTO `uc_user` VALUES ('15', '20120629mrs', 'bnetId14', 'bnetAccount14', null, null);
INSERT INTO `uc_user` VALUES ('16', '20120629mrs', 'bnetId15', 'bnetAccount15', null, null);
INSERT INTO `uc_user` VALUES ('17', '20120629mrs', 'bnetId16', 'bnetAccount16', null, null);
INSERT INTO `uc_user` VALUES ('18', '20120629mrs', 'bnetId17', 'bnetAccount17', null, null);
INSERT INTO `uc_user` VALUES ('19', '20120629mrs', 'bnetId18', 'bnetAccount18', null, null);
INSERT INTO `uc_user` VALUES ('20', '20120629mrs', 'bnetId19', 'bnetAccount19', null, null);
INSERT INTO `uc_user` VALUES ('21', '20120629mrs', 'bnetId20', 'bnetAccount20', null, null);
INSERT INTO `uc_user` VALUES ('22', '20120629mrs', 'bnetId21', 'bnetAccount21', null, null);
INSERT INTO `uc_user` VALUES ('23', '20120629mrs', 'bnetId22', 'bnetAccount22', null, null);
INSERT INTO `uc_user` VALUES ('24', '20120629mrs', 'bnetId23', 'bnetAccount23', null, null);
INSERT INTO `uc_user` VALUES ('25', '20120629mrs', 'bnetId24', 'bnetAccount24', null, null);
INSERT INTO `uc_user` VALUES ('26', '20120629mrs', 'bnetId25', 'bnetAccount25', null, null);
INSERT INTO `uc_user` VALUES ('27', '20120629mrs', 'bnetId26', 'bnetAccount26', null, null);
INSERT INTO `uc_user` VALUES ('28', '20120629mrs', 'bnetId27', 'bnetAccount27', null, null);
INSERT INTO `uc_user` VALUES ('29', '20120629mrs', 'bnetId28', 'bnetAccount28', null, null);
INSERT INTO `uc_user` VALUES ('30', '20120629mrs', 'bnetId29', 'bnetAccount29', null, null);
INSERT INTO `uc_user` VALUES ('31', '20120629mrs', 'bnetId30', 'bnetAccount30', null, null);
INSERT INTO `uc_user` VALUES ('32', '20120629mrs', 'bnetId31', 'bnetAccount31', null, null);
INSERT INTO `uc_user` VALUES ('33', '20120629mrs', 'bnetId32', 'bnetAccount32', null, null);
INSERT INTO `uc_user` VALUES ('34', '20120629mrs', 'bnetId33', 'bnetAccount33', null, null);
INSERT INTO `uc_user` VALUES ('35', '20120629mrs', 'bnetId34', 'bnetAccount34', null, null);
INSERT INTO `uc_user` VALUES ('36', '20120629mrs', 'bnetId35', 'bnetAccount35', null, null);
INSERT INTO `uc_user` VALUES ('37', '20120629mrs', 'bnetId36', 'bnetAccount36', null, null);
INSERT INTO `uc_user` VALUES ('38', '20120629mrs', 'bnetId37', 'bnetAccount37', null, null);
INSERT INTO `uc_user` VALUES ('39', '20120629mrs', 'bnetId38', 'bnetAccount38', null, null);
INSERT INTO `uc_user` VALUES ('40', '20120629mrs', 'bnetId39', 'bnetAccount39', null, null);
INSERT INTO `uc_user` VALUES ('41', '20120629mrs', 'bnetId40', 'bnetAccount40', null, null);
INSERT INTO `uc_user` VALUES ('42', '20120629mrs', 'bnetId41', 'bnetAccount41', null, null);
INSERT INTO `uc_user` VALUES ('43', '20120629mrs', 'bnetId42', 'bnetAccount42', null, null);
INSERT INTO `uc_user` VALUES ('44', '20120629mrs', 'bnetId43', 'bnetAccount43', null, null);
INSERT INTO `uc_user` VALUES ('45', '20120629mrs', 'bnetId44', 'bnetAccount44', null, null);
INSERT INTO `uc_user` VALUES ('46', '20120629mrs', 'bnetId45', 'bnetAccount45', null, null);
INSERT INTO `uc_user` VALUES ('47', '20120629mrs', 'bnetId46', 'bnetAccount46', null, null);
INSERT INTO `uc_user` VALUES ('48', '20120629mrs', 'bnetId47', 'bnetAccount47', null, null);
INSERT INTO `uc_user` VALUES ('49', '20120629mrs', 'bnetId48', 'bnetAccount48', null, null);
INSERT INTO `uc_user` VALUES ('50', '20120629mrs', 'bnetId49', 'bnetAccount49', null, null);
INSERT INTO `uc_user` VALUES ('51', '20120629mrs', 'bnetId50', 'bnetAccount50', null, null);
INSERT INTO `uc_user` VALUES ('52', '20120629mrs', 'bnetId51', 'bnetAccount51', null, null);
INSERT INTO `uc_user` VALUES ('53', '20120629mrs', 'bnetId52', 'bnetAccount52', null, null);
INSERT INTO `uc_user` VALUES ('54', '20120629mrs', 'bnetId53', 'bnetAccount53', null, null);
INSERT INTO `uc_user` VALUES ('55', '20120629mrs', 'bnetId54', 'bnetAccount54', null, null);
INSERT INTO `uc_user` VALUES ('56', '20120629mrs', 'bnetId55', 'bnetAccount55', null, null);
INSERT INTO `uc_user` VALUES ('57', '20120629mrs', 'bnetId56', 'bnetAccount56', null, null);
INSERT INTO `uc_user` VALUES ('58', '20120629mrs', 'bnetId57', 'bnetAccount57', null, null);
INSERT INTO `uc_user` VALUES ('59', '20120629mrs', 'bnetId58', 'bnetAccount58', null, null);
INSERT INTO `uc_user` VALUES ('60', '20120629mrs', 'bnetId59', 'bnetAccount59', null, null);
INSERT INTO `uc_user` VALUES ('61', '20120629mrs', 'bnetId60', 'bnetAccount60', null, null);
INSERT INTO `uc_user` VALUES ('62', '20120629mrs', 'bnetId61', 'bnetAccount61', null, null);
INSERT INTO `uc_user` VALUES ('63', '20120629mrs', 'bnetId62', 'bnetAccount62', null, null);
INSERT INTO `uc_user` VALUES ('64', '20120629mrs', 'bnetId63', 'bnetAccount63', null, null);
INSERT INTO `uc_user` VALUES ('65', '20120629mrs', 'bnetId64', 'bnetAccount64', null, null);
INSERT INTO `uc_user` VALUES ('66', '20120629mrs', 'bnetId65', 'bnetAccount65', null, null);
INSERT INTO `uc_user` VALUES ('67', '20120629mrs', 'bnetId66', 'bnetAccount66', null, null);
INSERT INTO `uc_user` VALUES ('68', '20120629mrs', 'bnetId67', 'bnetAccount67', null, null);
INSERT INTO `uc_user` VALUES ('69', '20120629mrs', 'bnetId68', 'bnetAccount68', null, null);
INSERT INTO `uc_user` VALUES ('70', '20120629mrs', 'bnetId69', 'bnetAccount69', null, null);
INSERT INTO `uc_user` VALUES ('71', '20120629mrs', 'bnetId70', 'bnetAccount70', null, null);
INSERT INTO `uc_user` VALUES ('72', '20120629mrs', 'bnetId71', 'bnetAccount71', null, null);
INSERT INTO `uc_user` VALUES ('73', '20120629mrs', 'bnetId72', 'bnetAccount72', null, null);
INSERT INTO `uc_user` VALUES ('74', '20120629mrs', 'bnetId73', 'bnetAccount73', null, null);
INSERT INTO `uc_user` VALUES ('75', '20120629mrs', 'bnetId74', 'bnetAccount74', null, null);
INSERT INTO `uc_user` VALUES ('76', '20120629mrs', 'bnetId75', 'bnetAccount75', null, null);
INSERT INTO `uc_user` VALUES ('77', '20120629mrs', 'bnetId76', 'bnetAccount76', null, null);
INSERT INTO `uc_user` VALUES ('78', '20120629mrs', 'bnetId77', 'bnetAccount77', null, null);
INSERT INTO `uc_user` VALUES ('79', '20120629mrs', 'bnetId78', 'bnetAccount78', null, null);
INSERT INTO `uc_user` VALUES ('80', '20120629mrs', 'bnetId79', 'bnetAccount79', null, null);
INSERT INTO `uc_user` VALUES ('81', '20120629mrs', 'bnetId80', 'bnetAccount80', null, null);
INSERT INTO `uc_user` VALUES ('82', '20120629mrs', 'bnetId81', 'bnetAccount81', null, null);
INSERT INTO `uc_user` VALUES ('83', '20120629mrs', 'bnetId82', 'bnetAccount82', null, null);
INSERT INTO `uc_user` VALUES ('84', '20120629mrs', 'bnetId83', 'bnetAccount83', null, null);
INSERT INTO `uc_user` VALUES ('85', '20120629mrs', 'bnetId84', 'bnetAccount84', null, null);
INSERT INTO `uc_user` VALUES ('86', '20120629mrs', 'bnetId85', 'bnetAccount85', null, null);
INSERT INTO `uc_user` VALUES ('87', '20120629mrs', 'bnetId86', 'bnetAccount86', null, null);
INSERT INTO `uc_user` VALUES ('88', '20120629mrs', 'bnetId87', 'bnetAccount87', null, null);
INSERT INTO `uc_user` VALUES ('89', '20120629mrs', 'bnetId88', 'bnetAccount88', null, null);
INSERT INTO `uc_user` VALUES ('90', '20120629mrs', 'bnetId89', 'bnetAccount89', null, null);
INSERT INTO `uc_user` VALUES ('91', '20120629mrs', 'bnetId90', 'bnetAccount90', null, null);
INSERT INTO `uc_user` VALUES ('92', '20120629mrs', 'bnetId91', 'bnetAccount91', null, null);
INSERT INTO `uc_user` VALUES ('93', '20120629mrs', 'bnetId92', 'bnetAccount92', null, null);
INSERT INTO `uc_user` VALUES ('94', '20120629mrs', 'bnetId93', 'bnetAccount93', null, null);
INSERT INTO `uc_user` VALUES ('95', '20120629mrs', 'bnetId94', 'bnetAccount94', null, null);
INSERT INTO `uc_user` VALUES ('96', '20120629mrs', 'bnetId95', 'bnetAccount95', null, null);
INSERT INTO `uc_user` VALUES ('97', '20120629mrs', 'bnetId96', 'bnetAccount96', null, null);
INSERT INTO `uc_user` VALUES ('98', '20120629mrs', 'bnetId97', 'bnetAccount97', null, null);
INSERT INTO `uc_user` VALUES ('99', '20120629mrs', 'bnetId98', 'bnetAccount98', null, null);
INSERT INTO `uc_user` VALUES ('100', '20120629mrs', 'bnetId99', 'bnetAccount99', null, null);
