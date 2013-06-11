@save = insert into t_user(id, user_name) values(:id, :userName)
@queryById = select t.* from t_user t where t.id=:0
@queryByUserName = select t.* from t_user t where t.user_name=:userName
@createUserNameQuery = select t.* from t_user t where t.user_name=:userName
@queryListByUserName = select t.* from t_user t where t.user_name like :userName
@queryPageByUserName = select t.* from t_user t where t.user_name like :userName
@deleteByUserName = delete from t_user where user_name like :userName