/*

 Source Server Type    : MySQL
 Source Server Version : 50718

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 29/11/2019 22:55:19
*/

SET NAMES utf8mb4;

-- ----------------------------
-- Table structure for data_mq_receive
-- ----------------------------
DROP TABLE IF EXISTS `data_mq_receive`;
CREATE TABLE `data_mq_receive` (
  `id` bigint(20) NOT NULL,
  `msgkey` varchar(128) NOT NULL,
  `raw_msgid` varchar(128) NOT NULL COMMENT '消息原始id',
  `consume_group` varchar(64) NOT NULL,
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '消息状态：\r\n            已消费：1\r\n            ',
  `create_at` datetime NOT NULL,
  `update_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_key_data_rmq_rec_grp_key` (`msgkey`,`consume_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='rmq消息接收记录表';

-- ----------------------------
-- Table structure for data_mq_send
-- ----------------------------
DROP TABLE IF EXISTS `data_mq_send`;
CREATE TABLE `data_mq_send` (
  `msgkey` varchar(128) NOT NULL,
  `body` blob,
  `state` tinyint(4) DEFAULT NULL COMMENT '消息状态：\r\n            待发送：0\r\n            已发送：1\r\n            ',
  `locker` varchar(50) DEFAULT NULL,
  `deliver_at` datetime DEFAULT NULL COMMENT '发送时间',
  `create_at` datetime DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`msgkey`),
  UNIQUE KEY `idx_data_rmq_send_key` (`msgkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='rmq消息发送暂存表';


ALTER TABLE `data_mq_send` 
ADD COLUMN `is_delay` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否延迟消息\n1:是\n0:否\n默认为0' AFTER `deliver_at`;
