@batchInsert =
    insert 
    into
        test_user_autoid
        (create_time, birthday, email, gender, mobile, nick_name, password, status, user_name) 
    values
        (:createTime, :birthday, :email, :gender, :mobile, :nickName, :password, :status, :userName)