package org.onetwo;

import java.util.List;

import org.onetwo.common.utils.LangUtils;

import test.entity.UserEntity;

public class TestUtils {

	public static UserEntity createUser(String userName, int age){
		UserEntity user = new UserEntity();
		user.setUserName(userName);
		user.setAge(age);
		return user;
	}
	
	public static List<UserEntity> createUserList(String userName, int count){
		List<UserEntity> users = LangUtils.newArrayList(count);
		for (int i = 0; i < count; i++) {
			users.add(createUser(userName, i));
		}
		return users;
	}

}
