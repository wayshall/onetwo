package org.onetwo.common.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

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
	
	public String format(Date date, String pattern){
		return DateUtil.format(pattern, date);
	}
	
	public String format(Number num, String pattern) {
		NumberFormat format = new DecimalFormat(pattern);
		return format.format(num);
	}
}
