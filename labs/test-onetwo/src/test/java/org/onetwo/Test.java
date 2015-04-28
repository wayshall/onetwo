package org.onetwo;

import org.onetwo.common.utils.RandUtils;

import test.entity.UserEntity;



public class Test {


	public static void main(String[] args){
		for(int i=0; i<100; i++){
			System.out.println(i+":"+RandUtils.randomString(6));
		}
	}
	
	public static UserEntity createUser(){
		UserEntity user = new UserEntity();
		user.setUserName("est");
		System.out.println("class: " + user.getClass().getClassLoader());
		return user;
	}
	
	
}
