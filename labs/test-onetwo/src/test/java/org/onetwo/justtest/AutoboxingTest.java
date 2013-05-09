package org.onetwo.justtest;

import org.onetwo.common.utils.TimeCounter;

public class AutoboxingTest {
	
	public static void main(String[] args){
		int count = 100000000;
		testAutoboxing(count);
		testBase(count);
		testAutoboxing(count);
	}
	
	public static void testAutoboxing(int max){
		TimeCounter t = new TimeCounter("testAutoboxing");
		t.start();
		Long sum = 0l;
		for(long i=0; i<max; i++){
			sum +=i;
		}
//		System.out.println("sum1:"+sum);
		t.stop();
	}
	
	public static void testBase(int max){
		TimeCounter t = new TimeCounter("testBase");
		t.start();
		long sum2 = 0l;
		for(long i=0; i<max; i++){
			sum2 +=i;
		}
//		System.out.println("sum2:"+sum2);
		t.stop();
	}
	

}
