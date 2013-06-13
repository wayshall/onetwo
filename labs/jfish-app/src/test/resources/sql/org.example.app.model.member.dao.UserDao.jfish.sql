@save = insert into t_user(id, user_name, age) values(:id, :userName, :age)
@queryWithId = select t.* from t_user t where t.id=:0
@queryByUserName = select t.* from t_user t where t.user_name=:userName
@createUserNameQuery = select t.* from t_user t where t.user_name=:userName
@queryListByUserNameByAge = select t.* from t_user t where t.user_name like :userName and t.age > :age
@queryPageByUserName = select t.* from t_user t where t.user_name like :userName
@deleteByUserName = delete from t_user where user_name like :userName