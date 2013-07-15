@createTemplateTable = CREATE TABLE `codegen_template` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `package_name` varchar(255) DEFAULT NULL,
  `file_name_postfix` varchar(255) DEFAULT NULL,
  `file_postfix` varchar(255) NOT NULL,
  `content` text,
  `file_path` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_template_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

@insertEntityTemplate = INSERT INTO `codegen_template` (`id`, `name`, `package_name`, `file_name_postfix`, `file_postfix`, `content`, `file_path`)
VALUES
	(10, 'entity', 'entity', 'Entity', 'java', '', :filePath);
