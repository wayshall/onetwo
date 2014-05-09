package test.entity;



public class Test {

	public static void main(String[] args){
		String s1="123:123";
		String s2="234:234";
		String s3="123:123-/52254255";
		String s4=s3.replaceAll("123:123","234:234");
		String s5=s3.replaceAll(s1,s2);
		System.out.println("s4:"+s4);
		System.out.println("s5:"+s5);
		System.out.println(s4.equals(s5));
	}
}
