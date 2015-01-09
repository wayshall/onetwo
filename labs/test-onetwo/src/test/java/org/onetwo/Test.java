package org.onetwo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.onetwo.common.exception.BaseException;


public class Test {
	
	public class Parent {
		public static final String s1 = "s1";
	}
	
	public class Child extends Parent {
//		public static final String s1 = "c1";
	}

	public static final Pattern IS_DIGIT = Pattern.compile("^\\d+$");
	
	public static void main(String[] args) throws IOException{
		String str = "连中北街62号 新时代幼儿园";
		for(char ch : str.toCharArray()){
			System.out.println(ch+":"+((int)ch));
		}
		System.out.println("new space :"+((int)' '));
		str = "连中北街62号 新时代幼儿园".replace(" ", "");
		for(char ch : str.toCharArray()){
			System.out.println(ch+":"+((int)ch));
		}
	}
	
	public static void test2(String[] args){
		test(args);
	}
	
	public static void test(String[] args){
		String a = Thread.currentThread().getStackTrace()[1].getMethodName();
		throw new BaseException(a);
	}
	
}
