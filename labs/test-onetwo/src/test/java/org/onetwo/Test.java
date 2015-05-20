package org.onetwo;

import test.entity.UserEntity;



public class Test {


	public static void main(String[] args){
		String str = "";
		System.out.println("1:"+(str!=""));
		str = new String("");
		System.out.println("2:"+(str!=""));
		str = str.intern();
		System.out.println("2:"+(str!=""));
	}
	
	public static UserEntity createUser(){
		UserEntity user = new UserEntity();
		user.setUserName("est");
		System.out.println("class: " + user.getClass().getClassLoader());
		return user;
	}
	
	
}
