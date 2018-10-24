-- 增加头像字段
ALTER TABLE `admin_user` 
ADD COLUMN `AVATAR` varchar(255) NULL COMMENT '头像' AFTER `APP_CODE`;

-- 权限表增加meta字段
ALTER TABLE `admin_permission` 
ADD COLUMN `META` varchar(1000) NULL COMMENT '元数据' AFTER `RESOURCES_PATTERN`;