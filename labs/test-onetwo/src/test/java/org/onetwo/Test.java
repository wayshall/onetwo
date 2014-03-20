package org.onetwo;





public class Test {
	
	public class Parent {
		public static final String s1 = "s1";
	}
	
	public class Child extends Parent {
//		public static final String s1 = "c1";
	}
	
	public static void main(String[] args){
		System.out.println(Child.s1);
	}
	
}
