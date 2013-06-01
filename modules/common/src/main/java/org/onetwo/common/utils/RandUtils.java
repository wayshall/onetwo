package org.onetwo.common.utils;

import java.util.Random;

public abstract class RandUtils {

    private static Random randomGenerator;
    static {
        randomGenerator = new Random();
    }
	
	public static String randomString(Integer length){
		int end = (int)Math.pow(10d, (length).doubleValue());
		int rs = randomGenerator.nextInt(end);
		String str = MyUtils.append(String.valueOf(rs), length, "0");
		return str.toString();
	}
	
	public static void main(String[] args){
		for(int i=0; i<100; i++){
			System.out.println(i+":"+randomString(i));
		}
	}
    
}
