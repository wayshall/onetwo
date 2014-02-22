package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

final public class CommonBizUtils {

	private static final int[] YEARS = new int[]{5, 6, 7, 8, 9};
//	private static final String[] MONTHS = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
	

	final static String[] CHECK_CODE = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
	final static int[] COEFFICIENT = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

	private static final BiMap<Integer, String> PROVINCE_CODE_MAP;
	private static final List<Integer> PROVINCE_CODE_LIST;
	
	static {
		BiMap<Integer, String> temp = HashBiMap.create(40);
		temp.put(11, "北京");
		temp.put(12, "天津");
		temp.put(13, "河北");
		temp.put(14, "山西");
		temp.put(15, "内蒙古");
		temp.put(21, "辽宁");
		temp.put(22, "吉林");
		temp.put(23, "黑龙江");
		temp.put(31, "上海");
		temp.put(32, "江苏");
		temp.put(33, "浙江");
		temp.put(34, "安徽");
		temp.put(35, "福建");
		temp.put(36, "江西");
		temp.put(37, "山东");
		temp.put(41, "河南");
		temp.put(42, "湖北");
		temp.put(43, "湖南");
		temp.put(44, "广东");
		temp.put(45, "广西");
		temp.put(46, "海南");
		temp.put(50, "重庆");
		temp.put(51, "四川");
		temp.put(52, "贵州");
		temp.put(53, "云南");
		temp.put(54, "西藏");
		temp.put(61, "陕西");
		temp.put(62, "甘肃");
		temp.put(63, "青海");
		temp.put(64, "宁夏");
		temp.put(65, "新疆");
		temp.put(71, "台湾");
		temp.put(81, "香港");
		temp.put(82, "澳门");
		temp.put(91, "外国");
		
		PROVINCE_CODE_MAP = Maps.unmodifiableBiMap(temp);
		PROVINCE_CODE_LIST = new ArrayList<Integer>(PROVINCE_CODE_MAP.keySet());
	}
	

	public static String generatedIdCardNo(){
		int provinceCode = PROVINCE_CODE_LIST.get(RandUtils.randomInt(PROVINCE_CODE_LIST.size()));
		return generatedIdCardNo(provinceCode);
	}

	public static String generatedIdCardNo(String province){
		int provinceCode = PROVINCE_CODE_MAP.inverse().get(province);
		return generatedIdCardNo(provinceCode);
	}
	
	public static String generatedIdCardNo(int provinceCode){
		StringBuilder idCardNo = new StringBuilder();
		idCardNo.append(provinceCode)//2
				.append(RandUtils.randomString(4))//4 市县
				.append("19").append(YEARS[RandUtils.randomInt(YEARS.length)]).append(RandUtils.randomInt(10))//4 year
				.append(RandUtils.randomWithPadLeft(13, "0", 0)).append(RandUtils.randomWithPadLeft(29, "0", 0))//4 month day
				.append(RandUtils.randomString(3));
		
		int total = 0;
		int index = 0;
		for(Character ch : idCardNo.toString().toCharArray()){
			total += Integer.parseInt(ch.toString()) * COEFFICIENT[index++];
		}
		idCardNo.append(CHECK_CODE[total%11]);
		return idCardNo.toString();
	}
	
	public static boolean checkIdCardNo(String cardNo){
		String s=cardNo;
		//身份证长度检查
		if(s == null || (s.length() != 15 && s.length() != 18)){
			return false;
		}
		//身份证的前面为数字
		String ai="";
		if (s.length() == 18) {
			ai = s.substring(0, 17);
		} else if (s.length() == 15) {
			ai = s.substring(0, 6) + "19" + s.substring(6, 15);
		}
		if (!LangUtils.isDigitString(ai)) {
			return false;
			
		}
		
		//校验区位码
		if(!PROVINCE_CODE_MAP.containsKey(Integer.valueOf(s.substring(0,2)))){
			return false;
		}
		
		//校验日期
		String dates = s.length() == 15 ? "19" + s.substring(6,14) :s.substring(6, 14);
		String regexp = "^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\\2(?:29))$";
		Pattern pat = Pattern.compile(regexp);
		Matcher mat = pat.matcher(dates);
		if(!mat.find()){
			return false;
		}
		
		
		//校验"校验码"
		int TotalmulAiWi = 0;   
        for (int i = 0; i < 17; i++) {   
            TotalmulAiWi = TotalmulAiWi  + Integer.parseInt(String.valueOf(ai.charAt(i))) * COEFFICIENT[i];   
        }   
        int modValue = TotalmulAiWi % 11;   
        String strVerifyCode = CHECK_CODE[modValue];   
        ai = ai + strVerifyCode;   
        if(!ai.equalsIgnoreCase(s)){
        	return false;
        }
        return true;
	}
	
	private CommonBizUtils(){
	}

}
