package org.onetwo.common.utils;

import java.math.BigDecimal;
import java.util.Date;

import org.onetwo.common.utils.convert.MoneyConvertUtils;

/*********
 * 
 * @author wayshall
 *
 */
public class TheFunction {
	public static final String ALIAS_NAME = "f";
	
	private static TheFunction instance = new TheFunction();
	
	private TheFunction(){}
	
	public static TheFunction getInstance() {
		return instance;
	}
	
	public String year(Date date){
		return DateUtil.format("yyyy", date);
	}
	
	public String month(Date date){
		return DateUtil.format("MM", date);
	}
	
	public String format(Date date, String pattern){
		return DateUtil.format(pattern, date);
	}

	public static String asDateTime(Date date){
		return DateUtil.formatDateTime(date);
	}
	
	public static String asDate(Date date){
		return DateUtil.formatDate(date);
	}
	
	public static String asTime(Date date){
		return DateUtil.formatTime(date);
	}
	
	public String format(Number num, String pattern) {
		return LangUtils.format(num, pattern);
	}
	
	public String format(Number num) {
		return LangUtils.format(num);
	}
	
	public static String chineseMoney(double money){
		BigDecimal big = new BigDecimal(money);
		return MoneyConvertUtils.convert(big);
	}
	
	public static String chineseMoney(String money){
		BigDecimal big = new BigDecimal(money);
		return MoneyConvertUtils.convert(big);
	}
}
