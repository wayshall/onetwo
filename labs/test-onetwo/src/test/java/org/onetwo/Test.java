package org.onetwo;

import org.onetwo.common.utils.RandUtils;

import test.entity.UserEntity;



public class Test {


	public static void main(String[] args){
		System.out.println("teste:"+(null==Object.class));
	}
	
	public static UserEntity createUser(){
		UserEntity user = new UserEntity();
		user.setUserName("est");
		System.out.println("class: " + user.getClass().getClassLoader());
		return user;
	}
	
	
}
