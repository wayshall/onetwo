package org.onetwo;

import java.io.IOException;
import java.util.regex.Pattern;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;



public class Test {
	
	public class Parent {
		public static final String s1 = "s1";
	}
	
	public class Child extends Parent {
//		public static final String s1 = "c1";
	}

	public static final Pattern IS_DIGIT = Pattern.compile("^\\d+$");
	
	public static void main(String[] args) throws IOException{
		String[] str = new String[]{"aa", "bb"};
		String[] str2 = str.clone();
		str2[0] = "cc";
		System.out.println("str:" + str[0]+null);
	}
	
	public static void test2(String[] args){
		test(args);
	}
	
	public static void test(String[] args){
		String a = Thread.currentThread().getStackTrace()[1].getMethodName();
		throw new BaseException(a);
	}
	
}
