package org.onetwo;

import org.onetwo.common.utils.LangUtils;

import test.entity.UserEntity;



public class Test {

	public static void main(String[] args) throws Exception {
		float a = 0.2f;
		float b = a;
		System.out.println("a: " + a);
		System.out.println("b: " + b);
	}
	
	public static UserEntity createUser(){
		UserEntity user = new UserEntity();
		user.setUserName("est");
		System.out.println("class: " + user.getClass().getClassLoader());
		return user;
	}
	
	
}
