
@userService.findByUserName = select * from t_user t 
										where t.user_name like :userName
										
@userService.findByUserName.ignore.null = true										