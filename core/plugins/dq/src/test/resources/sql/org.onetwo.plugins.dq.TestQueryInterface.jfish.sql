@findPage = select * from admin_user
@findList = select * from admin_user
@findOne = select * from admin_user
@countUser = select * from admin_user
@batchInsert = insert admin_user(user_name, age) values (:userName, :age);
@batchInsertWithNamed = insert admin_user(user_name, age) values (:data.userName, :data.age);

