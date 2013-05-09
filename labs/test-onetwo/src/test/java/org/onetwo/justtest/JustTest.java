package org.onetwo.justtest;

import java.util.Date;



public class JustTest {
	

	public static void main(String[] args) {
		System.out.println("Rs: " + Integer.MAX_VALUE);
		Date now = new Date();
		System.out.println("now: "+ now.getTime());
		System.out.println("now: "+ System.currentTimeMillis());
	}
	
	public static int increate(int i){
		return 100+i;
	}
}
