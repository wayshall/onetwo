INSERT INTO admin_app
   (`code`, `name`)
VALUES
   ('QingxinMenu', '清新连通宽带管理系统');


   

INSERT INTO admin_role
   (`id`, `create_time`, `last_update_time`, `app_code`, `name`, `remark`, `code`, `status`)
VALUES
   (1, NULL, NULL, NULL, '系统管理员', '拥有所有权限的角色，一般用作维护。', 'COMMON	', 'NORMAL');


   
   
   
INSERT INTO admin_user
   (`id`, `create_time`, `last_update_time`, `app_code`, `birthday`, `email`, `gender`, `mobile`, `nick_name`, `password`, `status`, `user_name`)
VALUES
   (1, NULL, NULL, NULL, NULL, NULL, 0, NULL, '系统管理员', '{SHA}fEqNCco3Yq9h5ZUglD3CZJT4lBs=', 'NORMAL', 'root');


   
   
   
INSERT INTO admin_user_role
   (`user_id`, `role_id`)
VALUES
   (1, 1);

