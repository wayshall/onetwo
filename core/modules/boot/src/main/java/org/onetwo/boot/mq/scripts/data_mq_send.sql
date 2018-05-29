/*
Navicat MySQL Data Transfer

Source Server         : 腾讯云-测试
Source Server Version : 50628
Source Host           : 596f0c823c306.gz.cdb.myqcloud.com:5034
Source Database       : neo

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-05-29 11:42:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_mq_send
-- ----------------------------
DROP TABLE IF EXISTS `data_mq_send`;
CREATE TABLE `data_mq_send` (
  `msgkey` varchar(64) COLLATE utf8_bin NOT NULL,
  `body` blob,
  `state` tinyint(4) DEFAULT NULL COMMENT '消息状态：\r\n            待发送：0\r\n            已发送：1\r\n            正常来说只有待发送，因为发送后一般不保留，直接删掉',
  `deliver_at` datetime DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`msgkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='rmq消息发送暂存表';
