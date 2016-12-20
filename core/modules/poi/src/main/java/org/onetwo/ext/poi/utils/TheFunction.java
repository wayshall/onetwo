package org.onetwo.ext.poi.utils;

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
		return formatDateByPattern("yyyy", date);
	}
	
	public String month(Date date){
		return formatDateByPattern("MM", date);
	}
	
	public String formatDate(Date date, String pattern){
		return formatDateByPattern(pattern, date);
	}
	
	public String formatDateByPattern(String pattern, Date date) {
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
	
	public String dateTime(Date date){
		return formatDateByPattern(DATE_TIME, date);
	}
	
	public String date(Date date){
		return formatDateByPattern(DATE_ONLY, date);
	}
	
	public String time(Date date){
		return formatDateByPattern(TIME_ONLY, date);
	}
	
	public String formatNumber(Number num) {
		return new DecimalFormat("0.00").format(num);
	}

	public String formatNumber(Number num, String pattern) {
		NumberFormat format = new DecimalFormat(pattern);
		return format.format(num);
	}

	public Date parseDateTime(String dateStr) {
		return parseDate(createDateFormat(DATE_TIME), dateStr);
	}
	public SimpleDateFormat createDateFormat(String pattern){
		Assert.hasText(pattern);
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		df.setTimeZone(TimeZone.getDefault());
		return df;
	}
	
	public Date parseDate(SimpleDateFormat format, String dateStr) {
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
