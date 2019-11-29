
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_mq_receive
-- ----------------------------
DROP TABLE IF EXISTS `data_mq_receive`;
CREATE TABLE `data_mq_receive`  (
  `id` bigint(20) NOT NULL,
  `msgkey` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `raw_msgid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息原始id',
  `consume_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `state` tinyint(4) NOT NULL DEFAULT 1 COMMENT '消息状态：\r\n            已消费：1\r\n            ',
  `create_at` datetime(0) NOT NULL,
  `update_at` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `AK_key_data_rmq_rec_grp_key`(`msgkey`, `consume_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'rmq消息接收记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_mq_send
-- ----------------------------
DROP TABLE IF EXISTS `data_mq_send`;
CREATE TABLE `data_mq_send`  (
  `msgkey` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `body` blob NULL,
  `state` tinyint(4) NULL DEFAULT NULL COMMENT '消息状态：\r\n            待发送：0\r\n            已发送：1\r\n            ',
  `locker` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deliver_at` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `create_at` datetime(0) NULL DEFAULT NULL,
  `update_at` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`msgkey`) USING BTREE,
  UNIQUE INDEX `idx_data_rmq_send_key`(`msgkey`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'rmq消息发送暂存表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
