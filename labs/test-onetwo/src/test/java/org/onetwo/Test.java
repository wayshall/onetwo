package org.onetwo;

import java.io.IOException;
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
		System.out.println(Long.parseLong("-100"));
		System.out.println(Long.parseLong("+100"));
	}
	
	public static void test2(String[] args){
		test(args);
	}
	
	public static void test(String[] args){
		String a = Thread.currentThread().getStackTrace()[1].getMethodName();
		throw new BaseException(a);
	}
	
}
