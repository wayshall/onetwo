package org.onetwo.common.utils;

import java.util.Random;

public abstract class RandUtils {

    private static Random randomGenerator;
    static {
        randomGenerator = new Random();
    }
	
    /****
     * 
     * @param length 生成的长度
     * @return
     */
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
	
	/****
	 * 
	 * @param maxInt 0 到 (maxInt-1)
	 * @return
	 */
	public static int randomInt(int maxInt){
		return randomGenerator.nextInt(maxInt);
	}
	public static Object randomValue(Object[] array){
		Assert.notEmpty(array);
		return array[randomInt(array.length)];
	}


	public static String randomWithPadLeft(Integer maxInt, String padStr, Integer...excepts){
		Integer randInt = randomInt(maxInt);
		while(ArrayUtils.contains(excepts, randInt)){
			randInt = randomInt(maxInt);
		}
		String randStr = randInt.toString();
		int strLength = String.valueOf(maxInt).length();
		if(StringUtils.isNotBlank(padStr))
			randStr = LangUtils.padLeft(randStr, strLength, padStr);
		return randStr;
	}
    
}
