package org.onetwo;

import java.io.IOException;
import java.lang.reflect.Field;
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
	
	public static void main(String[] args) throws IOException, Exception{
		Class cache = Integer.class.getDeclaredClasses()[0];
	    Field c = cache.getDeclaredField("cache");
	    c.setAccessible(true);
	    Integer[] array = (Integer[]) c.get(cache);
	    array[128] = array[129];

	    System.out.printf("%d",0);
	}
	
	public static void test2(String[] args){
		test(args);
	}
	
	public static void test(String[] args){
		String a = Thread.currentThread().getStackTrace()[1].getMethodName();
		throw new BaseException(a);
	}
	
}
