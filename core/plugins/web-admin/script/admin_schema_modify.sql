-- 增加头像字段
ALTER TABLE `admin_user` 
ADD COLUMN `AVATAR` varchar(255) NULL COMMENT '头像' AFTER `APP_CODE`;


