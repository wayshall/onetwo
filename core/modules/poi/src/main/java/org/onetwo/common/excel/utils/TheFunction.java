package org.onetwo.common.excel.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/*********
 * 
 * @author wayshall
 *
 */
public class TheFunction {
	private static final Logger logger = LoggerFactory.getLogger(TheFunction.class);
	
	public static final String ALIAS_NAME = "f";
	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_ONLY = "yyyy-MM-dd";
	public static final String TIME_ONLY = "HH:mm:ss";
	
	private static TheFunction instance = new TheFunction();
	
	private TheFunction(){}
	
	public static TheFunction getInstance() {
		return instance;
	}
	
	public String year(Date date){
		return format("yyyy", date);
	}
	
	public String month(Date date){
		return format("MM", date);
	}
	
	public String format(Date date, String pattern){
		return format(pattern, date);
	}
	
	public static String format(String pattern, Date date) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(TimeZone.getDefault());
		Assert.notNull(format, "format can not be null");
		String rs = "";
		if(date==null)
			return rs;
		try {
			rs = format.format(date);
		} catch (Exception e) {
			logger.error("format date[{}] error: {}", date, e.getMessage());
		}
		return rs;
	}
	
	public static String dateTime(Date date){
		return format(DATE_TIME, date);
	}
	
	public static String date(Date date){
		return format(DATE_ONLY, date);
	}
	
	public static String time(Date date){
		return format(TIME_ONLY, date);
	}
	
	public String format(Number num) {
		return new DecimalFormat("0.00").format(num);
	}

	public static String format(Number num, String pattern) {
		NumberFormat format = new DecimalFormat(pattern);
		return format.format(num);
	}

	public static Date parseDateTime(String dateStr) {
		return parseDate(createDateFormat(DATE_TIME), dateStr);
	}
	public static SimpleDateFormat createDateFormat(String pattern){
		Assert.hasText(pattern);
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		df.setTimeZone(TimeZone.getDefault());
		return df;
	}
	
	public static Date parseDate(SimpleDateFormat format, String dateStr) {
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (Exception e) {
//			logger.error("parse date["+dateStr+"] error with format["+format+"]:"+e.getMessage()+", ignore.");
			throw new RuntimeException("parse date["+dateStr+"] error with format : " + format, e);
		}
		return date;
	}
}
