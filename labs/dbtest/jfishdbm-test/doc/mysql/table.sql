CREATE TABLE `test_user_autoid` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `nick_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mobile` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `status` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  `app_code` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=445 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
