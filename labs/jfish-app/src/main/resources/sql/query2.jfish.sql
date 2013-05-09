@tuserService.findUser = select t.* from t_user t where t.user_name='wayshall'
@tuserService.findUser.mapped.entity = org.example.app.model.member.entity.UserEntity