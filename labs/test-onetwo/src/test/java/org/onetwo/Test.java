package org.onetwo;

import org.onetwo.common.utils.LangUtils;

import test.entity.UserEntity;



public class Test {

	public static void main(String[] args) throws Exception {
		UserEntity user = createUser();
		System.out.println("user: " + user.getUserName());
		System.out.println("user: " + user.getUserName());
		

		UserEntity user1 = createUser();
		System.out.println("user: " + user1.getUserName());
		

		user1 = createUser();
		System.out.println("user: " + user1.getUserName());
		

		user1 = createUser(); 
		System.out.println("user: " + user1.getUserName());
	}
	
	public static UserEntity createUser(){
		UserEntity user = new UserEntity();
		user.setUserName("est");
		System.out.println("class: " + user.getClass().getClassLoader());
		return user;
	}
	
	
}
