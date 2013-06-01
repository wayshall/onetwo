/*
Navicat MySQL Data Transfer

Source Server         : xampp
Source Server Version : 50153
Source Host           : localhost:3306
Source Database       : fishcalc

Target Server Type    : MYSQL
Target Server Version : 50153
File Encoding         : 65001

Date: 2012-11-27 18:32:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `calc_fee_book`
-- ----------------------------
DROP TABLE IF EXISTS `calc_fee_book`;
CREATE TABLE `calc_fee_book` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `memo` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_fee_book_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of calc_fee_book
-- ----------------------------
INSERT INTO `calc_fee_book` VALUES ('2', '2', '小鱼的账本', null);

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
  `fee_book_id` bigint(20) DEFAULT NULL,
  `type_id` bigint(20) NOT NULL,
  `used_time` timestamp NULL DEFAULT NULL COMMENT '使用时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_fee_details_id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of calc_fee_details
-- ----------------------------
INSERT INTO `calc_fee_details` VALUES ('12', '2012-10', 'test', '11', 'asdf', '1', '3', '2012-10-23 23:31:00', '2012-10-23 23:31:04', '2012-10-23 23:31:04', '1');
INSERT INTO `calc_fee_details` VALUES ('6', '2012-10', '羊城通', '2', '羊城通', '1', '4', '2012-10-19 23:29:00', '2012-10-19 23:29:32', '2012-10-23 18:16:04', '1');
INSERT INTO `calc_fee_details` VALUES ('7', '2012-10', '猪尾巴肉', '11.23', '猪尾巴肉', '1', '3', '2012-10-22 23:17:00', '2012-10-22 23:17:09', '2012-10-23 18:15:48', '1');
INSERT INTO `calc_fee_details` VALUES ('11', '2012-10', '猪头肉', '45', '猪头肉', '1', '3', '2012-10-23 18:03:00', '2012-10-23 18:04:14', '2012-10-23 23:30:38', '1');
INSERT INTO `calc_fee_details` VALUES ('13', '2012-09', '9月买菜', '232.22', '9月买菜', '1', '3', '2012-09-11 01:01:00', '2012-10-24 01:01:29', '2012-10-24 01:01:29', '1');
INSERT INTO `calc_fee_details` VALUES ('14', '2012-09', '9月交通', '22.32', '9月交通', '1', '4', '2012-10-01 01:01:00', '2012-10-24 01:02:05', '2012-10-24 01:02:05', '1');
INSERT INTO `calc_fee_details` VALUES ('15', '2012-09', '羊城通充值', '158', '羊城通充值', '1', '4', '2012-09-11 01:18:00', '2012-10-24 01:18:35', '2012-10-24 01:19:08', '1');
INSERT INTO `calc_fee_details` VALUES ('16', '2012-10', '10月份工资', '1000', '10月份工资', '1', '5', '2012-10-10 08:44:00', '2012-10-24 08:44:39', '2012-10-24 08:44:39', '1');
INSERT INTO `calc_fee_details` VALUES ('29', '2012-11', '午餐', '17', '一个人，麦当当，衰猪头毛煮饭，要自己出去吃，吃了个菠菜卷～～～', '2', '10', '2012-11-01 20:43:00', '2012-11-01 22:44:06', '2012-11-01 22:44:06', '2');
INSERT INTO `calc_fee_details` VALUES ('28', '2012-11', '买菜', '37', '车陂市场，一个人，骑单车去～～～两块肉，莲藕，油麦，萝卜，鱼……', '2', '10', '2012-11-02 06:20:00', '2012-11-01 22:42:56', '2012-11-01 22:42:56', '2');
INSERT INTO `calc_fee_details` VALUES ('30', '2012-11', '水果', '14', '梨，桔子。衰猪不吃柚子，不吃石榴，还说不挑食。', '2', '10', '2012-11-03 03:01:00', '2012-11-03 21:02:30', '2012-11-03 21:02:30', '2');
INSERT INTO `calc_fee_details` VALUES ('31', '2012-11', '饮料', '7', '和衰猪一起踩单车瞎逛，又不记得自己带茶。衰猪踩着新单车得瑟。', '2', '10', '2012-11-04 00:04:00', '2012-11-03 21:05:44', '2012-11-03 21:05:44', '2');
INSERT INTO `calc_fee_details` VALUES ('32', '2012-11', '买菜', '80', '青菜6，蟹30，烧鹅28，鸡蛋7.5，醸三宝8.5', '2', '10', '2012-11-04 02:08:00', '2012-11-03 21:08:49', '2012-11-03 21:08:49', '2');
INSERT INTO `calc_fee_details` VALUES ('33', '2012-11', '单车', '43', '后架25，琐18', '2', '12', '2012-11-04 03:09:00', '2012-11-03 21:10:09', '2012-11-03 21:10:09', '2');
INSERT INTO `calc_fee_details` VALUES ('34', '2012-11', '出去吃饭，烤鱼', '68', '被床垫的事折腾死了，然后晚上在车陂什么和味厨神，咸香烤鱼，本来说好谁猜得最接近价钱的就谁不用付钱，结果我一分不差地猜中了68，还是给了钱。结论：别和女人赌。', '2', '10', '2012-11-05 03:28:00', '2012-11-06 15:29:30', '2012-11-06 15:29:30', '2');
INSERT INTO `calc_fee_details` VALUES ('35', '2012-11', '搬床垫', '30', '床垫很折腾，快递很糟糕。。', '2', '11', '2012-11-05 00:53:00', '2012-11-07 09:53:53', '2012-11-07 09:53:53', '2');
INSERT INTO `calc_fee_details` VALUES ('36', '2012-11', '零食', '30', '午餐过后随便逛逛，买了些凉果和瓜子作为下午消遣。。', '2', '10', '2012-11-06 20:56:00', '2012-11-07 09:56:13', '2012-11-08 17:21:57', '2');
INSERT INTO `calc_fee_details` VALUES ('37', '2012-11', '栗子', '8', '额，下班在路口没有忍住诱惑。。', '2', '10', '2012-11-07 02:56:00', '2012-11-07 09:57:00', '2012-11-07 09:57:00', '2');
INSERT INTO `calc_fee_details` VALUES ('38', '2012-11', '水果', '15', '居然看到有西瓜卖，1块一斤。。后来猪头去买居然降价到6块一只，真没天理', '2', '10', '2012-11-06 02:58:00', '2012-11-07 09:58:25', '2012-11-08 11:08:58', '2');
INSERT INTO `calc_fee_details` VALUES ('39', '2012-11', '水果', '15', '树菠萝买了三盒，吃得好撑，臭死猪头～～\r\n桔子5块3斤，好便宜，可惜不是很甜。。', '2', '10', '2012-11-05 04:01:00', '2012-11-07 10:01:24', '2012-11-07 10:01:24', '2');
INSERT INTO `calc_fee_details` VALUES ('40', '2012-11', '楼上怎么买的全是零食？都胖成那样了！', '60', '车陂菜市场买菜，各种蔬菜和肉和鱼～～发现钱越来越不耐花了，10块钱只能买2个青菜。', '2', '10', '2012-11-06 03:05:00', '2012-11-07 10:06:23', '2012-11-07 10:06:47', '2');
INSERT INTO `calc_fee_details` VALUES ('41', '2012-11', '床垫', '400', '硬床垫一个，房东配的软床垫太流，太不舒服，因为猪头太重，居然坍塌了，唯有自己掏钱买一个，硬的～～', '2', '11', '2012-11-04 20:12:00', '2012-11-07 10:13:43', '2012-11-07 10:13:43', '2');
INSERT INTO `calc_fee_details` VALUES ('45', '2012-11', '买菜', '25', '吃完饭，很累的猪头陪我去超市买菜。。半只鸡，打算用来炒苦瓜，一包菜心，还有烤鸭做第二天的午餐', '2', '10', '2012-11-08 05:19:00', '2012-11-08 17:19:46', '2012-11-08 17:24:53', '2');
INSERT INTO `calc_fee_details` VALUES ('43', '2012-11', '水果', '12', '下班时又看到卖葡萄的，但他称有问题所以不买。最后买了梨子，4只居然差不多4斤。坑爹啊。', '2', '10', '2012-11-08 03:10:00', '2012-11-07 20:11:15', '2012-11-08 17:20:36', '2');
INSERT INTO `calc_fee_details` VALUES ('46', '2012-11', '榨菜', '15', '榨菜，橄榄菜。早餐煲粥吃，省钱～', '2', '10', '2012-11-08 03:23:00', '2012-11-08 17:23:22', '2012-11-08 17:23:22', '2');
INSERT INTO `calc_fee_details` VALUES ('47', '2012-11', '买菜', '10', '回到广州，好累。老妈给了一盒鹅肉，买了几把青菜将就一餐', '2', '10', '2012-11-12 02:06:00', '2012-11-11 21:06:43', '2012-11-11 21:06:43', '2');
INSERT INTO `calc_fee_details` VALUES ('48', '2012-11', '车费', '70', '从清远回广州', '2', '12', '2012-11-11 23:07:00', '2012-11-11 21:07:33', '2012-11-11 21:07:33', '2');
INSERT INTO `calc_fee_details` VALUES ('49', '2012-11', '网购', '400', '老妈衣服100，老爸衣服200，电话费100', '2', '13', '2012-11-11 21:11:00', '2012-11-11 21:11:59', '2012-11-11 21:11:59', '2');
INSERT INTO `calc_fee_details` VALUES ('50', '2012-11', '网购', '764', '老爸衣服152，奶粉172，尿片130；裤子80，移动电源130', '2', '13', '2012-11-11 20:18:00', '2012-11-11 21:18:47', '2012-11-11 21:18:47', '2');
INSERT INTO `calc_fee_details` VALUES ('51', '2012-11', '咖啡猫', '43', '同小雁仔在咖啡猫喝奶茶', '2', '10', '2012-11-11 05:20:00', '2012-11-11 21:20:14', '2012-11-11 21:20:14', '2');
INSERT INTO `calc_fee_details` VALUES ('52', '2012-11', '奶茶', '40', '赢之城雅致咖啡，跟老哥一起', '2', '10', '2012-11-10 06:21:00', '2012-11-11 21:22:22', '2012-11-11 21:22:22', '2');
INSERT INTO `calc_fee_details` VALUES ('53', '2012-11', '车费', '72', '回清远', '2', '12', '2012-11-10 05:23:00', '2012-11-11 21:23:22', '2012-11-11 21:23:22', '2');
INSERT INTO `calc_fee_details` VALUES ('54', '2012-11', '晚餐', '56', '回去之前去麦当当吃晚餐', '2', '10', '2012-11-10 04:25:00', '2012-11-11 21:26:04', '2012-11-11 21:26:04', '2');
INSERT INTO `calc_fee_details` VALUES ('55', '2012-11', '午餐', '15', '猪头又在麦当当解决', '2', '10', '2012-11-09 20:26:00', '2012-11-11 21:26:48', '2012-11-11 21:26:48', '2');
INSERT INTO `calc_fee_details` VALUES ('56', '2012-11', '网购', '115', '床上4件套。选了好久，都是差不多的价格，算了，希望这家质量够好吧', '2', '11', '2012-11-12 06:12:00', '2012-11-11 22:12:11', '2012-11-11 22:12:11', '2');
INSERT INTO `calc_fee_details` VALUES ('57', '2012-11', '光棍节后继续血拼，只为苏宁0元购', '303', '光棍节后继续血拼，只为苏宁0元购～～详细订单https://member.suning.com/emall/MyOrderDisplayView?storeId=10052&orderId=3000358329&catalogId=10051', '2', '13', '2012-11-12 19:41:00', '2012-11-12 11:41:59', '2012-11-12 11:41:59', '2');
INSERT INTO `calc_fee_details` VALUES ('58', '2012-11', '美的洗衣机', '599', '在苏宁易购买了个美的洗衣机，899.00，用了血拼回来的300返券，599。因为我坚信，人不是用来洗衣服的，不能这样虐待臭猪，让她那么辛苦洗衣服～～！！！但为什么她那么狠心让我天天洗碗呢？——这是一个问题。', '2', '13', '2012-11-12 20:31:00', '2012-11-12 14:34:53', '2012-11-12 14:34:53', '2');
INSERT INTO `calc_fee_details` VALUES ('59', '2012-11', '药', '17', '氯雷他定', '2', '13', '2012-11-13 03:27:00', '2012-11-12 23:27:55', '2012-11-12 23:27:55', '2');
INSERT INTO `calc_fee_details` VALUES ('60', '2012-11', '零食', '15', '栗子13，牛奶2', '2', '10', '2012-11-13 01:29:00', '2012-11-12 23:30:24', '2012-11-12 23:30:24', '2');
INSERT INTO `calc_fee_details` VALUES ('61', '2012-11', '晚餐', '67', '潮一吃火锅', '2', '10', '2012-11-13 03:31:00', '2012-11-12 23:31:49', '2012-11-12 23:31:49', '2');
INSERT INTO `calc_fee_details` VALUES ('62', '2012-11', '水果', '10', '西瓜10元3个', '2', '10', '2012-11-13 07:32:00', '2012-11-12 23:32:46', '2012-11-12 23:32:46', '2');
INSERT INTO `calc_fee_details` VALUES ('63', '2012-11', '电影票', '56', '团购票', '2', '13', '2012-11-13 05:35:00', '2012-11-12 23:35:54', '2012-11-12 23:35:54', '2');
INSERT INTO `calc_fee_details` VALUES ('64', '2012-11', '豆令的工资', '7500', '苦逼程序员的豆令的工资', '2', '15', '2012-11-10 01:46:00', '2012-11-13 09:46:51', '2012-11-13 09:46:51', '2');
INSERT INTO `calc_fee_details` VALUES ('65', '2012-11', '供楼', '2000', '房奴嘛～～', '2', '14', '2012-11-12 17:48:00', '2012-11-13 09:48:19', '2012-11-13 09:48:19', '2');
INSERT INTO `calc_fee_details` VALUES ('66', '2012-11', '寄回去的生活费', '1500', '孝子嘛～～～', '2', '14', '2012-11-12 17:48:00', '2012-11-13 09:48:49', '2012-11-13 09:48:49', '2');
INSERT INTO `calc_fee_details` VALUES ('67', '2012-11', '车陂市场买一周的菜肉～～', '82', '各种青菜瓜果～～哦，没果～～因为前天10块买了3个西瓜，还有2个～～', '2', '10', '2012-11-15 03:50:00', '2012-11-14 21:50:16', '2012-11-14 21:50:16', '2');
INSERT INTO `calc_fee_details` VALUES ('68', '2012-11', '麦茶～～～', '30', '臭猪买给小雁仔的茶叶～～ps：严重怀疑这是一对好基友～～', '2', '10', '2012-11-13 05:51:00', '2012-11-14 21:52:09', '2012-11-14 21:52:09', '2');
INSERT INTO `calc_fee_details` VALUES ('69', '2012-11', '一号店各种生活食物用品～～', '200', '米、擦上下两个口的纸、各种猪零食……每月都要照例为一号店做贡献～～！！！', '2', '10', '2012-11-15 05:53:00', '2012-11-14 21:53:43', '2012-11-14 21:53:43', '2');
INSERT INTO `calc_fee_details` VALUES ('70', '2012-11', '毛巾一条～～', '10', '都不知道这个猪头为什么买这么多毛巾。。。毛巾买得多，就就系得条毛……巾罗。。。', '2', '16', '2012-11-14 07:02:00', '2012-11-14 23:02:45', '2012-11-14 23:02:45', '2');
INSERT INTO `calc_fee_details` VALUES ('71', '2012-11', '碟两只～～', '20', '不知道是马克碟还是亚麻碟，反正就是一只十元～～～', '2', '10', '2012-11-14 07:03:00', '2012-11-14 23:03:46', '2012-11-14 23:03:46', '2');

-- ----------------------------
-- Table structure for `calc_fee_type`
-- ----------------------------
DROP TABLE IF EXISTS `calc_fee_type`;
CREATE TABLE `calc_fee_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `source` tinyint(4) NOT NULL,
  `fee_book_id` bigint(20) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_fee_type_id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of calc_fee_type
-- ----------------------------
INSERT INTO `calc_fee_type` VALUES ('10', '食', '-1', '2', '', '2012-10-25 17:33:12', '2012-10-25 17:33:12', '2');
INSERT INTO `calc_fee_type` VALUES ('11', '住', '-1', '2', '', '2012-10-25 17:33:37', '2012-10-25 17:33:37', '2');
INSERT INTO `calc_fee_type` VALUES ('12', '行', '-1', '2', '', '2012-10-25 17:33:50', '2012-10-25 17:33:50', '2');
INSERT INTO `calc_fee_type` VALUES ('13', '其他', '-1', '2', '', '2012-10-25 17:34:03', '2012-10-25 17:34:03', '2');
INSERT INTO `calc_fee_type` VALUES ('14', '固定', '-1', '2', '', '2012-10-25 17:34:14', '2012-10-26 09:58:22', '2');
INSERT INTO `calc_fee_type` VALUES ('15', '工资', '1', '2', '', '2012-10-25 17:34:25', '2012-10-25 17:34:25', '2');
INSERT INTO `calc_fee_type` VALUES ('16', '衣', '-1', '2', '', '2012-11-11 21:09:50', '2012-11-11 21:09:50', '2');

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

-- ----------------------------
-- Table structure for `codegen_database`
-- ----------------------------
DROP TABLE IF EXISTS `codegen_database`;
CREATE TABLE `codegen_database` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `label` varchar(255) NOT NULL,
  `driver_class` varchar(255) NOT NULL,
  `jdbc_url` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_database_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of codegen_database
-- ----------------------------
INSERT INTO `codegen_database` VALUES ('2', 'fishcalc', 'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/fishcalc?useUnicode=true&amp;characterEncoding=UTF-8', 'root', '', '2012-11-18 15:58:06', '2012-11-18 15:58:06');
INSERT INTO `codegen_database` VALUES ('3', 'mall', 'oracle.jdbc.driver.OracleDriver', 'jdbc:oracle:thin:@db-cluster-scan.yooyo.local:1521/db.yooyo.local', 'mall', 'mall2012', '2012-11-22 10:03:18', '2012-11-22 10:03:18');

-- ----------------------------
-- Table structure for `codegen_template`
-- ----------------------------
DROP TABLE IF EXISTS `codegen_template`;
CREATE TABLE `codegen_template` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `package_name` varchar(255) DEFAULT NULL,
  `file_name_postfix` varchar(255) DEFAULT NULL,
  `file_postfix` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `create_time` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_template_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of codegen_template
-- ----------------------------
INSERT INTO `codegen_template` VALUES ('1', 'entity', 'entity', 'Entity', 'java', 'package ${fullPackage};\r\n\r\n<#list importClasses as clz>\r\n	<#lt>import ${clz};\r\n</#list>\r\n\r\nimport javax.persistence.Entity;\r\nimport javax.persistence.Table;\r\nimport javax.persistence.Column;\r\nimport javax.persistence.GeneratedValue;\r\nimport javax.persistence.GenerationType;\r\nimport javax.persistence.Id;\r\nimport javax.persistence.SequenceGenerator;\r\n\r\n<#--\r\nimport com.yooyo.openapi.utils.BaseEntity;\r\n-->\r\nimport net.yooyo.mall.assist.model.BaseEntity;\r\n\r\n/*****\r\n * ${table.comment?default(\"\")}\r\n * @Entity\r\n */\r\n<#assign uncapitalClassName = selfClassName?capitalize/>\r\n@SuppressWarnings(\"serial\")\r\n@Entity\r\n@Table(name=\"${table.name}\")\r\n@SequenceGenerator(name=\"${selfClassName}Generator\", sequenceName=\"SEQ_${table.name}\")\r\npublic class ${selfClassName} extends BaseEntity<${table.primaryKey.javaType.simpleName}> {\r\n	\r\n<#list table.columnCollection as column>\r\n  <#if column.javaName!=\'createTime\' && column.javaName!=\'lastUpdateTime\'>\r\n	protected ${column.primaryKey?string(table.primaryKey.javaType.simpleName, column.javaType.simpleName)} ${column.javaName};\r\n  </#if>\r\n  \r\n</#list>\r\n	\r\n	public ${selfClassName}(){\r\n	}\r\n	\r\n<#list table.columnCollection as column>\r\n  <#if column.javaName!=\'createTime\' && column.javaName!=\'lastUpdateTime\'>\r\n	\r\n	/*****\r\n	 * ${column.comment?default(\"\")}\r\n	 * @return\r\n	 */\r\n	<#if column.primaryKey>\r\n	@Id\r\n	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"${selfClassName}Generator\")\r\n	</#if>\r\n	<#if column.dateType>\r\n	@Temporal(TemporalType.TIMESTAMP)\r\n	</#if>\r\n	@Column(name=\"${column.name}\")\r\n	public ${column.primaryKey?string(table.primaryKey.javaType.simpleName, column.javaType.simpleName)} ${column.readMethodName}() {\r\n		return this.${column.javaName};\r\n	}\r\n	\r\n	public void ${column.writeMethodName}(${column.primaryKey?string(table.primaryKey.javaType.simpleName, column.javaType.simpleName)} ${column.javaName}) {\r\n		this.${column.javaName} = ${column.javaName};\r\n	}\r\n  </#if>\r\n</#list>\r\n	\r\n}', '2012-11-17 23:21:56', '2012-11-27 10:01:53');
INSERT INTO `codegen_template` VALUES ('2', 'service', 'service', 'Service', 'java', 'package ${fullPackage};\r\n\r\n<#assign entityName = commonName+\"Entity\"/>\r\nimport ${basePackage+\".entity.\"+entityName};\r\n\r\nimport org.onetwo.common.db.CrudEntityManager;\r\n\r\npublic interface ${selfClassName} extends CrudEntityManager<${entityName}, ${table.primaryKey.javaType.simpleName}> {\r\n\r\n}\r\n', '2012-11-18 23:55:44', '2012-11-19 00:25:48');
INSERT INTO `codegen_template` VALUES ('3', 'serviceImpl', 'service.impl', 'ServiceImpl', 'java', 'package ${fullPackage};\r\n<#--\r\n<#assign serviceInterfaceName = table.className+\"Service\"/>\r\nimport ${basePackage+\".service.\"+serviceInterfaceName};\r\n-->\r\n<#assign entityName = commonName+\"Entity\"/>\r\nimport org.springframework.stereotype.Service;\r\n\r\nimport ${basePackage+\".entity.\"+entityName};\r\n\r\nimport org.onetwo.common.fish.JFishCrudServiceImpl;\r\n\r\n\r\n@Service\r\npublic class ${selfClassName} extends JFishCrudServiceImpl<${entityName}, ${table.primaryKey.javaType.simpleName}> {\r\n\r\n}\r\n', '2012-11-18 23:56:24', '2012-11-19 00:25:34');
INSERT INTO `codegen_template` VALUES ('4', 'controller', 'controller', 'Controller', 'java', 'package ${fullPackage};\r\n\r\n<#assign entityName=\"${commonName}Entity\"/>\r\n<#assign ename=\"${commonName?uncap_first}\"/>\r\n<#assign serviceImplName=\"${commonName}ServiceImpl\"/>\r\n<#assign serviceName=\"${commonName}Service\"/>\r\n<#assign eserviceName=\"${serviceName?uncap_first}\"/>\r\n\r\n<#assign entityPackageName=\"${basePackage}.entity\"/>\r\n<#assign servicePackageName=\"${basePackage}.service\"/>\r\n<#assign serviceImplPackageName=\"${basePackage}.service.impl\"/>\r\n\r\nimport javax.validation.Valid;\r\n\r\nimport org.onetwo.common.db.ExtQuery.K;\r\nimport org.onetwo.common.exception.BusinessException;\r\nimport org.onetwo.common.spring.web.BaseController;\r\nimport org.onetwo.common.utils.Page;\r\nimport org.onetwo.common.utils.StringUtils;\r\nimport org.springframework.beans.factory.annotation.Autowired;\r\nimport org.springframework.stereotype.Controller;\r\nimport org.springframework.validation.BindingResult;\r\nimport org.springframework.web.bind.annotation.ModelAttribute;\r\nimport org.springframework.web.bind.annotation.PathVariable;\r\nimport org.springframework.web.bind.annotation.RequestMapping;\r\nimport org.springframework.web.bind.annotation.RequestMethod;\r\nimport org.springframework.web.bind.annotation.RequestParam;\r\nimport org.springframework.web.servlet.ModelAndView;\r\nimport org.springframework.web.servlet.mvc.support.RedirectAttributes;\r\n\r\nimport ${entityPackageName+\".\"+entityName};\r\nimport ${servicePackageName+\".\"+serviceName};\r\n\r\n@RequestMapping(\"/${ename}\")\r\n@Controller\r\npublic class ${commonName}Controller extends BaseController<${entityName}> {\r\n	 \r\n	@Autowired\r\n	private ${serviceName} ${eserviceName};\r\n	\r\n\r\n	@RequestMapping(method=RequestMethod.GET)\r\n	public ModelAndView index(Page<${entityName}> page){\r\n		${eserviceName}.findPage(page, K.DESC, \"lastUpdateTime\");\r\n		return indexView(\"page\", page);\r\n	}\r\n	\r\n	@RequestMapping(value=\"new\", method=RequestMethod.GET)\r\n	public ModelAndView _new(@ModelAttribute(\"${ename}\") ${entityName} ${ename}){\r\n		return newView();\r\n	}\r\n\r\n	@RequestMapping(method=RequestMethod.POST)\r\n	public ModelAndView create(String redirectUrl, @Valid @ModelAttribute(\"${ename}\")${entityName} ${ename}, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{\r\n		if(bind.hasErrors()){\r\n			return newView();\r\n		}\r\n		this.${eserviceName}.save(${ename});\r\n		this.addCreateMessage(redirectAttributes);\r\n		if(StringUtils.isNotBlank(redirectUrl)){\r\n			return mv(redirect(redirectUrl));\r\n		}else{\r\n			return indexAction();\r\n		}\r\n	}\r\n	\r\n	@RequestMapping(value=\"/{id}/edit\", method=RequestMethod.GET)\r\n	public ModelAndView edit(@PathVariable(\"id\") Long id){\r\n		${entityName} ${ename} = this.${eserviceName}.findById(id);\r\n		return editView(\"${ename}\", ${ename});\r\n	}\r\n	\r\n\r\n	@RequestMapping(value=\"/{id}\", method=RequestMethod.PUT)\r\n	public ModelAndView update(@ModelAttribute(\"${ename}\") @Valid ${entityName} ${ename}, BindingResult binding, RedirectAttributes redirectAttributes){\r\n		if(binding.hasErrors()){\r\n			return editView();\r\n		}\r\n		this.${eserviceName}.save(${ename});\r\n		this.addUpdateMessage(redirectAttributes);\r\n		return showAction(${ename}.getId());\r\n	}\r\n	\r\n\r\n	@RequestMapping(method=RequestMethod.DELETE)\r\n	public ModelAndView deleteBatch(@RequestParam(value=\"ids\")long[] ids, RedirectAttributes redirectAttributes){\r\n		for(long id : ids){\r\n			this.${eserviceName}.removeById(id);\r\n		}\r\n		this.addDeleteMessage(redirectAttributes);\r\n		return indexAction();\r\n	}\r\n	\r\n	@RequestMapping(value=\"/{id}\", method=RequestMethod.GET)\r\n	public ModelAndView show(@PathVariable(\"id\") long id) throws BusinessException{\r\n		${entityName} ${ename} =  this.${eserviceName}.findById(id);\r\n		return showView(\"${ename}\", ${ename});\r\n	}\r\n	\r\n	\r\n}\r\n', '2012-11-19 00:41:53', '2012-11-19 00:55:15');

-- ----------------------------
-- Table structure for `current_states`
-- ----------------------------
DROP TABLE IF EXISTS `current_states`;
CREATE TABLE `current_states` (
  `id` smallint(6) NOT NULL,
  `state_code` char(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of current_states
-- ----------------------------
INSERT INTO `current_states` VALUES ('1', 'MA', 'Massachusetts');
INSERT INTO `current_states` VALUES ('2', 'NH', 'New Hampshire');
INSERT INTO `current_states` VALUES ('3', 'ME', 'Maine');
INSERT INTO `current_states` VALUES ('4', 'VT', 'Vermont');
