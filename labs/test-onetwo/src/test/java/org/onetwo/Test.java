package org.onetwo;

import org.onetwo.common.utils.LangUtils;






public class Test {
	
	public class Parent {
		public static final String s1 = "s1";
	}
	
	public class Child extends Parent {
//		public static final String s1 = "c1";
	}
	
	public static void main(String[] args){
		test(new int[]{});
	}
	
	public static void test(Object objs){
		System.out.println("objs: " + objs);
	}
	
	public static void test(Object[] objs){
		System.out.println("objs: " + objs);
	}
	
}
