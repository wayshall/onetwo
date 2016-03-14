package org.onetwo.common.convert;

import java.math.BigDecimal;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;

public final class MoneyConvertUtils {

	private static final String END_STR = "整"; 
	private static final String[] INT_UNIT_NAME = new String[]{
		"元", "拾", "佰", "仟", "万", "拾", "佰", "仟","亿", "拾", "佰", "仟", "万"
	}; 
	private static final String[] SCALE_UNIT_NAME = new String[]{
		"角", "分"
	}; 
	
	private static final String[] NUMBER_UPPER_CASE_NAME = new String[]{
		 "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒","捌", "玖"
	}; 
	private MoneyConvertUtils(){
	}
	
	public static String convert(Double money){
		BigDecimal big = new BigDecimal(money.toString());
		return convert(big);
	}
	
	public static int[] stringToIntArray(String str){
		int[] rs = new int[str.length()];
		int index = 0;
		for(Character ch : str.toCharArray()){
			rs[index++] = Integer.parseInt(ch.toString());
		}
		return rs;
	}
	
	public static String convert(BigDecimal money){
		int intLenght = money.precision()-money.scale();
		if(intLenght>INT_UNIT_NAME.length){
			throw new BaseException("只支持"+INT_UNIT_NAME.length+"位整数：" + intLenght);
		}
		if(money.scale()>SCALE_UNIT_NAME.length){
			throw new BaseException("只支持"+SCALE_UNIT_NAME.length+"位小数精度：" + money.scale());
		}
		String str = money.toPlainString();
		String scale = money.toString().substring(str.length()-money.scale());
		int scaleLength = money.scale()>0?money.scale()+1:money.scale();
		String intStr = money.toString().substring(0, str.length()-scaleLength);
		
		int[] intChars = stringToIntArray(intStr);
		StringBuilder rs = new StringBuilder();
		int currentNumberUnitIndex = intChars.length-1;
		int lastUnitIndex = 0;
		for (int i = 0; i < intChars.length; i++) {
			String upname = NUMBER_UPPER_CASE_NAME[intChars[i]];
			String unitname = INT_UNIT_NAME[currentNumberUnitIndex];
			
			
			if(intChars[i]==0){
				if(currentNumberUnitIndex==0){
					rs.append(unitname);
					lastUnitIndex = currentNumberUnitIndex;
					
				}else if(currentNumberUnitIndex==4 && lastUnitIndex!=8){
					rs.append(unitname);
					lastUnitIndex = currentNumberUnitIndex;
					
				}else if(currentNumberUnitIndex==8){
					rs.append(unitname);
					lastUnitIndex = currentNumberUnitIndex;
				}
			}else{
				if(i>0 && intChars[i-1]==0){
					rs.append(NUMBER_UPPER_CASE_NAME[0]);
				}
				rs.append(upname);
				rs.append(unitname);

				lastUnitIndex = currentNumberUnitIndex;
			}

			currentNumberUnitIndex--;
		}
		
		if(StringUtils.isBlank(scale) || Integer.parseInt(scale)==0){
			rs.append(END_STR);
			return rs.toString();
		}
		
		int[] scaleChars = stringToIntArray(scale);
		
		for (int i = 0; i < scaleChars.length; i++) {
			String upname = "";//NUMBER_UPPER_CASE_NAME[scaleChars[i]];
			String unitname = "";
			if(scaleChars[i]!=0){
				upname = NUMBER_UPPER_CASE_NAME[scaleChars[i]];
				unitname = SCALE_UNIT_NAME[i];
			}else if(i==scaleChars.length-1){
				unitname = END_STR;
			}
			rs.append(upname).append(unitname);
		}
		return rs.toString();
	}

}
