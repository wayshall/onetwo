--@batchInsert =
--批量插入
    insert 
    into
        test_user_autoid
        (birthday, email, gender, mobile, nick_name, password, status, user_name) 
    values
        (:birthday, :email, :gender, :mobile, :nickName, :password, :status, :userName)
--@batchInsert.parser=template
        
--@batchInsert2 =
    insert 
    into
        test_user_autoid
        (birthday, email, gender, mobile, nick_name, password, status, user_name) 
--多个参数的批量插入
    values
        (:allBirthday, :email, :gender, :mobile, :nickName, :password, :status, :userName)
        

--@removeByUserName =
    delete from test_user_autoid 
    	[#if userName?has_content]
    	where user_name like :userName?likeString
    	[/#if]
--@removeByUserName.parser=template
