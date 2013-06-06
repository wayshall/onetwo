@queryById = select t.* from t_user t where t.id=:id
@queryListByUserName = select t.* from t_user t where t.user_name=:userName