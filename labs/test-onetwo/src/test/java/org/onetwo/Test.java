package org.onetwo;

import java.util.regex.Pattern;



public class Test {
	
	public class Parent {
		public static final String s1 = "s1";
	}
	
	public class Child extends Parent {
//		public static final String s1 = "c1";
	}

	public static final Pattern IS_DIGIT = Pattern.compile("^\\d+$");
	
	public static void main(String[] args){
		boolean rs = IS_DIGIT.matcher("10").matches();
		System.out.println("match: " + rs);
		rs = IS_DIGIT.matcher("10").find();
		System.out.println("match: " + rs);
	}
	
}
