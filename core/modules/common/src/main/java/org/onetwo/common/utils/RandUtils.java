package org.onetwo.common.utils;

import java.util.Random;

public abstract class RandUtils {

    private static Random randomGenerator;
    static {
        randomGenerator = new Random();
    }
	
	public static String randomString(Integer length){
		if(length==null || length<1)
			return LangUtils.EMPTY_STRING;
		int end = (int)Math.pow(10d, (length).doubleValue());
		int rs = randomGenerator.nextInt(end);
		String str = MyUtils.append(String.valueOf(rs), length, "0");
		return str.toString();
	}

	public static String padRightWithRandom(String str, int maxlength){
		return padWithRandom(str, maxlength, true);
	}
	public static String padLeftWithRandom(String str, int maxlength){
		return padWithRandom(str, maxlength, false);
	}
	
	public static String padWithRandom(String str, int maxlength, boolean rigth){
		int diff = maxlength - str.length();
		if(diff<=0)
			return str.substring(0, maxlength);
		if(rigth)
			return str + randomString(diff);
		else
			return randomString(diff) + str;
	}
	
	public static int randomInt(int maxInt){
		return randomGenerator.nextInt(maxInt);
	}
	
	public static void main(String[] args){
		for(int i=0; i<100; i++){
			System.out.println(i+":"+randomString(i));
		}
	}
    
}
