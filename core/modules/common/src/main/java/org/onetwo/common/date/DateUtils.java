package org.onetwo.common.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

/**
 * 时间通用方法
 * 
 * @author way
 * 
 */
abstract public class DateUtils {

	private static final Logger logger = JFishLoggerFactory.getLogger(DateUtils.class);
	public static enum DateType {

		year(Calendar.YEAR), month(Calendar.MONTH), 
			//dateOfWeek(Calendar.DAY_OF_WEEK), 
			date(Calendar.DATE), 
		hour(Calendar.HOUR_OF_DAY), min(Calendar.MINUTE), sec(
				Calendar.SECOND), misec(Calendar.MILLISECOND);

		private int field;

		DateType(int val) {
			this.field = val;
		}

		public int getField() {
			return field;
		}

	}
	

    public static final Pattern PATTERN_YYYY_MM_DD_HH_MM_SS = Pattern.compile("^\\d{4}[\\-|\\/|\\.][01]{0,1}[0-9](\\-|\\/|\\.)[0-3]{0,1}[0-9]\\s+[0-2]{0,1}[0-9][:][0-5]{0,1}[0-9][:][0-5]{0,1}[0-9]$");
    public static final Pattern PATTERN_YYYY_MM_DD_HH_MM = Pattern.compile("^\\d{4}[\\-|\\/|\\.][01]{0,1}[0-9](\\-|\\/|\\.)[0-3]{0,1}[0-9]\\s+[0-2]{0,1}[0-9][:][0-5]{0,1}[0-9]$");
    public static final Pattern PATTERN_YYYY_MM_DD_HH = Pattern.compile("^\\d{4}[\\-|\\/|\\.][01]{0,1}[0-9](\\-|\\/|\\.)[0-3]{0,1}[0-9]\\s+[0-2]{0,1}[0-9]$");
    public static final Pattern PATTERN_YYYY_MM_DD = Pattern.compile("^\\d{4}[\\-|\\/|\\.][01]{0,1}[0-9](\\-|\\/|\\.)[0-3]{0,1}[0-9]$");
    public static final Pattern PATTERN_YYYY_MM = Pattern.compile("^\\d{4}[\\-|\\/|\\.][01]{0,1}[0-9]$");
    public static final Pattern PATTERN_YYYY = Pattern.compile("^\\d{4}$");
    public static final Pattern PATTERN_HH_MM = Pattern.compile("^[0-2]{0,1}[0-9][:][0-5]{0,1}[0-9]$");
    public static final Pattern PATTERN_HH_MM_SS = Pattern.compile("^[0-2]{0,1}[0-9][:][0-5]{0,1}[0-9][:][0-5]{0,1}[0-9]$");

    public static final int MILLIS_PER_SECOND = 1000;
	public static final int SECONDS_PER_MINUTE = 60;
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * 60;
    public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * 24;

    public static final int MILLIS_PER_DAY = SECONDS_PER_DAY * MILLIS_PER_SECOND;
    public static final int MILLIS_PER_HOUR = SECONDS_PER_HOUR * MILLIS_PER_SECOND;
    public static final int MILLIS_PER_MINUTE = SECONDS_PER_MINUTE * MILLIS_PER_SECOND;

	public static final String YEAR_ONLY = "yyyy";
	public static final String YEAR_MONTH = "yyyy-MM";
	public static final String DATE_ONLY = "yyyy-MM-dd";
	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_TIME_MILLS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_TIME_MILLS2 = "yyyy-MM-dd HH:mm:ss SSS";
	public static final String DATE_SHORT_TIME = "yyyy-MM-dd HH:mm";
	public static final String TIME_ONLY = "HH:mm:ss";
	public static final String SHORT_TIME_ONLY = "HH:mm";
	public static final String DATEONLY = "yyyyMMdd";
	public static final String DATETIME = "yyyyMMddHHmmss";
	public static final String TIMEONLY = "HHmmss";

	public static final String DATEMILLS = "yyyyMMddHHmmssSSS";

	public static final int UNIT_SECOND = 1000;
	public static final int UNIT_MINUTE = 60 * 1000;
	public static final int UNIT_HOUR = 60 * 60 * 1000;

	
	public static SimpleDateFormat createDateFormat(String pattern){
		Assert.hasText(pattern);
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		df.setTimeZone(TimeZone.getDefault());
		return df;
	}
	/**
	 * 获取本月的第一天
	 * 
	 * @return
	 */
	public static String getMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return format("yyyy-MM-dd", calendar.getTime());
	}

	/**
	 * 获取本月的最后一天
	 * 
	 * @return
	 */
	public static String getMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return format("yyyy-MM-dd", calendar.getTime());
	}

	// 需要注意的是：月份是从0开始的，比如说如果输入5的话，实际上显示的是6月份的最后一天，千万不要搞错了哦
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return format("yyyy-MM-dd", cal.getTime());
	}

	public static String getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		return format("yyyy-MM-dd", cal.getTime());
	}

	/**
	 * 获取跟date间隔days天的时间
	 * 
	 * @param date
	 *            时间
	 * @param days
	 *            间隔天数
	 */
	public static Date getDiffDay(Date date, long days) {
		long datetime = date.getTime();
		long difftime = days * 24 * 60 * 60 * 1000;

		return new Date(datetime + difftime);
	}
	
	public static String formatDateByPattern(Date date, String p) {
		/*if(date==null)
			return "";
		if(StringUtils.isBlank(p))
			p = Date_Time;
		SimpleDateFormat sdf = createDateFormat(p);
		String rs = null;
		try {
			rs = sdf.format(date);
		} catch (Exception e) {
			logger.warn("format error : {}", e.getMessage());
		}
		return rs;*/
		return format(p, date);
	}

	public static String formatDateTime(Date date) {
		if(date==null)
			return "";
		return formatDateByPattern(date, DATE_TIME);
	}
	public static String formatDateTimeMillis(Date date) {
		if(date==null)
			return "";
		return formatDateByPattern(date, DATE_TIME_MILLS);
	}
	public static String formatDateTimeMillis2(Date date) {
		if(date==null)
			return "";
		return formatDateByPattern(date, DATE_TIME_MILLS2);
	}

	public static String format(String pattern, Date date) {
		if(date==null)
			return "";
		if (StringUtils.isBlank(pattern))
			pattern = DATE_TIME;
		SimpleDateFormat sdf = getDateFormat(pattern);
		return format(sdf, date);
	}

	public static String format(SimpleDateFormat format, Date date) {
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

	public static Date parse(SimpleDateFormat format, String dateStr) {
		return parse(format, dateStr, true);
	}
	public static Date parse(SimpleDateFormat format, String dateStr, boolean throwIfError) {
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (Exception e) {
//			logger.error("parse date["+dateStr+"] error with format["+format+"]:"+e.getMessage()+", ignore.");
			if (throwIfError) {
				throw new BaseException("parse date["+dateStr+"] error with format : " + format, e);
			} else {
				return null;
			}
		}
		return date;
	}

	public static Date parse(String dateStr, SimpleDateFormat... dateFormats) {
		if(StringUtils.isBlank(dateStr))
			return null;
		if (dateFormats == null || dateFormats.length == 0)
			return parse(createDateFormat(DATE_TIME), dateStr);
		Date date = null;
		for (SimpleDateFormat sdf : dateFormats) {
			date = parse(sdf, dateStr, false);
			if (date != null)
				return date;
		}
		throw new BaseException("parse date string [" + dateStr + "] error with format: " + LangUtils.toString(dateFormats));
	}

	public static Date parseByPatterns(String dateStr, String... patterns) {
		if(StringUtils.isBlank(dateStr))
			return null;
		if (patterns == null || patterns.length == 0)
			return parse(createDateFormat(DATE_TIME), dateStr);
		Date date = null;
		for (String p : patterns) {
			SimpleDateFormat sdf = getDateFormat(p);
			date = parse(sdf, dateStr, false);
			if (date != null)
				return date;
		}
		return date;
	}

	public static Date parseDate(String dateStr) {
		return parseByPatterns(dateStr, DATE_ONLY);
	}

	public static Date parseTime(String dateStr) {
		return parseByPatterns(dateStr, TIME_ONLY);
	}

	public static Date parseShortTime(String dateStr) {
		return parseByPatterns(dateStr, SHORT_TIME_ONLY);
	}

	public static Date parseDateTime(String dateStr) {
		return parseByPatterns(dateStr, DATE_TIME);
	}

	public static Date parseDateTimeMills(String dateStr) {
		return parseByPatterns(dateStr, DATE_TIME_MILLS);
	}
	public static Date parseDateTimeMills2(String dateStr) {
		return parseByPatterns(dateStr, DATE_TIME_MILLS2);
	}

	public static Date parseDateShortTime(String dateStr) {
		return parseByPatterns(dateStr, DATE_SHORT_TIME);
	}

	public static Date parse(String dateStr) {
		return StringUtils.isBlank(dateStr)?null:parseByPatterns(dateStr, matchPattern(dateStr));
	}

	/*private static Date parse2(String dateStr) {
		return parse(dateStr, "-", ":");
	}*/
	
	/*private static Date parse(String dateStr, String dateSeperator, String timeSeperator) {
		if(StringUtils.isBlank(dateStr))
			return null;
		
		Assert.hasLength(dateSeperator);
		Assert.hasLength(timeSeperator);
		
		Date date = null;
		String format = "";
		String[] strs = StringUtils.split(dateStr, " ");
		String ymdStr = "";
		String hmsStr = "";
		if(strs[0].indexOf(dateSeperator)!=-1){
			ymdStr = strs[0];
			if(strs.length>1){
				hmsStr = strs[1];
			}
		}else{
			hmsStr = strs[0];
		}
		if(StringUtils.isNotBlank(ymdStr)){
			String[] ymd = StringUtils.split(ymdStr, dateSeperator);
			if(ymd.length==1){
				format = "yyyy";
			}else if(ymd.length==2){
				format = "yyyy"+dateSeperator+"MM";
			}else if(ymd.length==3){
				format = "yyyy"+dateSeperator+"MM"+dateSeperator+"dd";
			}
		}
		if(StringUtils.isNotBlank(hmsStr)){
			if(format.length()>0)
				format += " ";
			String[] hms = StringUtils.split(hmsStr, timeSeperator);
			if(hms.length==1){
				format += "HH";
			}else if(hms.length==2){
				format += "HH"+timeSeperator+"mm";
			}else if(hms.length==3){
				format += "HH"+timeSeperator+"mm"+timeSeperator+"ss";
			}
		}
		date = parseByPatterns(dateStr, format);
		return date;
//		return parse(dateStr, YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD_HH_MM, YYYY_MM_DD);
	}*/


	public static SimpleDateFormat getDateFormat(String p) {
		if (StringUtils.isBlank(p))
			p = DATE_ONLY;
		SimpleDateFormat sdf = createDateFormat(p);
		return sdf;
	}

	public static Date addMinutes(Date date, int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	public static Date addHours(Date date, int amount) {
		return add(date, Calendar.HOUR_OF_DAY, amount);
	}

	public static Date addHours(Calendar cal, int amount) {
		return add(cal, Calendar.HOUR_OF_DAY, amount);
	}

	public static Date addSeconds(Date date, int amount) {
		return add(date, Calendar.SECOND, amount);
	}
	
	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static Date add(Calendar c, int calendarField, int amount) {
		if (c == null) {
			throw new IllegalArgumentException("The Calendar must not be null");
		}
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static void addByDateType(Calendar calendar, DateType dt, int numb) {
		Assert.notNull(calendar, "calendar can not be null");
		calendar.add(dt.getField(), numb);
	}
	
	public static Date setDateStart(Date date) {
		Assert.notNull(date, "date can not be null");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return setDateStart(calendar);
	}

	public static Date setDateEnd(Date date) {
		Assert.notNull(date, "date can not be null");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return setDateEnd(calendar);
	}

	public static Date setDateStart(Calendar calendar) {
		Assert.notNull(calendar, "calendar can not be null");
		return start(calendar, DateType.date);
	}


	/*****
	 * 把时间精确度设置到DateType类型的开始
	 * @param calendar
	 * @param dt
	 * @return
	 */

	public static Date start(Calendar calendar, DateType dt) {
		accurateToBeginningAt(calendar, dt);
		return calendar.getTime();
	}
	
	public static void accurateToBeginningAt(Calendar calendar, DateType dt) {
		Assert.notNull(calendar, "calendar can not be null");
		if (dt == null)
			return ;

		for (DateType d : DateType.values()) {
			if (d.getField() <= dt.getField()) {
				continue;
			}
			calendar.set(d.getField(), calendar.getActualMinimum(d.getField()));
		}
//		return calendar.getTime();
	}

	public static Date beginningOf(Date date, DateType dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		accurateToBeginningAt(cal, dt);
		return cal.getTime();
	}

	/************
	 * 
	 * 把时间精确度设置到DateType类型的结束
	 * @param calendar
	 * @param dt
	 * @return
	 */

	public static Date end(Calendar calendar, DateType dt) {
		accurateToEndAt(calendar, dt);
		return calendar.getTime();
	}
	
	public static void accurateToEndAt(Calendar calendar, DateType dt) {
		Assert.notNull(calendar, "calendar can not be null");

		if (dt == null)
			return ;

		for (DateType d : DateType.values()) {
			if (d.getField() <= dt.getField())
				continue;
			calendar.set(d.getField(), calendar.getActualMaximum(d.getField()));
		}
	}

	public static Date endOf(Date date, DateType dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		accurateToEndAt(cal, dt);
		return cal.getTime();
	}


	public static void setDateStart(Calendar calendar, int... fields) {
		accurateToBeginningAt(calendar, fields);
	}
	
	public static void accurateToBeginningAt(Calendar calendar, int... fields) {
		Assert.notNull(calendar, "calendar can not be null");
		if (fields == null)
			return;
		for (int field : fields) {
			calendar.set(field, calendar.getActualMinimum(field));
		}
	}

	public static void setDateEnd(Calendar calendar, int... fields) {
		accurateToEndAt(calendar, fields);
	}
	
	public static void accurateToEndAt(Calendar calendar, int... fields) {
		Assert.notNull(calendar, "calendar can not be null");
		if (fields == null)
			return;
		for (int field : fields) {
			calendar.set(field, calendar.getActualMaximum(field));
		}
	}

	public static Date setDateEnd(Calendar calendar) {
		Assert.notNull(calendar, "calendar can not be null");
		return end(calendar, DateType.date);
	}

	public static Date addDay(Calendar calendar, int numb) {
		Assert.notNull(calendar, "calendar can not be null");
		calendar.add(Calendar.DAY_OF_MONTH, numb);
		return calendar.getTime();
	}

	public static Date addDay(Date date, int numb) {
		Assert.notNull(date, "date can not be null");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		increaseDay(calendar, numb);
		return calendar.getTime();
	}

	public static Date addMonth(Date date, int numb) {
		Assert.notNull(date, "date can not be null");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		addMonth(calendar, numb);
		return calendar.getTime();
	}

	public static void increaseDay(Calendar calendar, int numb) {
		Assert.notNull(calendar, "calendar can not be null");
		calendar.add(Calendar.DAY_OF_MONTH, numb);
	}

	public static void addMonth(Calendar calendar, int numb) {
		Assert.notNull(calendar, "calendar can not be null");
		calendar.add(Calendar.MONTH, numb);
	}

	public static void addYear(Calendar calendar, int numb) {
		Assert.notNull(calendar, "calendar can not be null");
		calendar.add(Calendar.YEAR, numb);
	}

	public static Date addYear(Date date, int numb) {
		Assert.notNull(date, "date can not be null");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		addYear(calendar, numb);
		return calendar.getTime();
	}

	public static long secondToMicroSecond(long microSecond) {
		return microSecond * UNIT_SECOND;
	}

	public static long minuteToMicroSecond(long microSecond) {
		return microSecond * UNIT_MINUTE;
	}

	public static long hourToMicroSecond(long microSecond) {
		return microSecond * UNIT_HOUR;
	}

	public static Date now() {
		return new Date();
	}

	public static Date date(String exp) {
		if (exp.startsWith(":")) {
			NiceDate nd = NiceDate.New();
			exp = exp.substring(1);
			if (exp.indexOf(':') != -1) {
				exp = exp.replace(':', '.').replace(" ", "");
			}
			NiceDate nd2 = (NiceDate) ReflectUtils.getExpr(nd, exp);
			if(nd2==null){
				nd2 = (NiceDate)ReflectUtils.invokeMethod(exp, nd);
			}
			return nd2.getTime();
		} else {
			return parse(exp);
		}
	}

	public static String nowString() {
		return format(createDateFormat("yyyy-MM-dd HH:mm:ss SSS"), now());
	}

	public static String getString(Date date) {
//		Assert.notNull(date, "date can not be null");
		if(date==null)
			return "";
		return createDateFormat(DATETIME).format(date);
	}

	public static String getString(Date date, String format) {
//		Assert.notNull(date, "date can not be null");
		if(date==null)
			return "";
		if(StringUtils.isBlank(format))
			return getString(date);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String getString(String format) {
		return getString(new Date(), format);
	}

	public static String formatDate(Date date) {
		return formatDateByPattern(date, DATE_ONLY);
	}

	public static String formatTime(Date date) {
		return formatDateByPattern(date, TIME_ONLY);
	}
	
	public static int compareAtField(Date d1, Date d2, int field){
		return compareAtField(asCalendar(d1), asCalendar(d2), field);
	}

	/*******
	 * 比较且只比较两个Calendar 的某个字段上的值是否相等
	 * @param c1
	 * @param c2
	 * @param field
	 * @return
	 */
	public static int compareAtField(Calendar c1, Calendar c2, int field){
		return c1.get(field) - c2.get(field);
	}
	
	public static boolean isSameYear(Calendar c1, Calendar c2){
		return compareAtField(c1, c2, Calendar.YEAR)==0;
	}
	
	public static boolean isSameMonth(Calendar c1, Calendar c2){
		return compareAtField(c1, c2, Calendar.MONTH)==0;
	}
	
	public static boolean isSameDate(Calendar c1, Calendar c2){
		return compareAtField(c1, c2, Calendar.DAY_OF_MONTH)==0;
	}

	/******
	 * 比较两个Calendar在DateType精度上是否相等
	 * @param c1
	 * @param c2
	 * @param dt
	 * @return
	 */
	public static boolean isSameAccurateAt(Calendar c1, Calendar c2, DateType dt) {
		if(c1==null || c2==null || dt==null)
			return false;

		boolean rs = false;
		for (DateType d : DateType.values()) {
			if (d.getField() > dt.getField() || (rs = c1.get(d.getField()) == c2.get(d.getField()))==false)
				return rs;
		}
		return rs;
	}
	

	public static boolean isSameAt(Date date1, Date date2, DateType dt) {
		Assert.notNull(date1, "date1 can not be null");
		Assert.notNull(date2, "date2 can not be null");
		Assert.notNull(dt, "dt can not be null");
		return isSameAt(asCalendar(date1), asCalendar(date2), dt);
	}
	/***
	 * 递归版
	 * @param c1
	 * @param c2
	 * @param dt
	 * @return
	 */
	public static boolean isSameAt(Calendar c1, Calendar c2, DateType dt) {
		Assert.notNull(c1, "c1 can not be null");
		Assert.notNull(c2, "c2 can not be null");
		Assert.notNull(dt, "dt can not be null");
		
//		return dt.ordinal()>0?(isSameAt(c1, c2, dt.values()[dt.ordinal()-1])?c1.get(dt.getField())==c2.get(dt.getField()):false):c1.get(dt.getField())==c2.get(dt.getField());
		if(dt.ordinal()>0){
			return isSameAt(c1, c2, dt.values()[dt.ordinal()-1])?c1.get(dt.getField())==c2.get(dt.getField()):false;
		}else{
			return c1.get(dt.getField())==c2.get(dt.getField());
		}
	}
	/***
	 * 根据DateType的精度，比较两个世界是否相等
	 * @param c1
	 * @param c2
	 * @param dt
	 * @return
	 */
	public static int compareAccurateAt(Calendar c1, Calendar c2, DateType dt) {
		Assert.notNull(c1, "c1 can not be null");
		Assert.notNull(c2, "c2 can not be null");
		Assert.notNull(dt, "dt can not be null");

		int rs = 0;
		for (DateType d : DateType.values()) {
			if (d.getField() > dt.getField() || (rs = c1.get(d.getField()) - c2.get(d.getField()))!=0)
				return rs==0?0:(rs>0?1:-1);
		}
		return rs;
	}
	
	public static Calendar asCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static boolean isYyyy_MM_dd_HH_mm_ss(String dateStr){
		return PATTERN_YYYY_MM_DD_HH_MM_SS.matcher(dateStr).matches();
	}
	
	public static boolean isYyyy_MM_dd_HH_mm(String dateStr){
		return PATTERN_YYYY_MM_DD_HH_MM.matcher(dateStr).matches();
	}
	
	public static boolean isYyyy_MM_dd_HH(String dateStr){
		return PATTERN_YYYY_MM_DD_HH.matcher(dateStr).matches();
	}
	
	public static boolean isYyyy_MM_dd(String dateStr){
		return PATTERN_YYYY_MM_DD.matcher(dateStr).matches();
	}
	
	public static boolean isYyyy_MM(String dateStr){
		return PATTERN_YYYY_MM.matcher(dateStr).matches();
	}
	
	public static boolean isYyyy(String dateStr){
		return PATTERN_YYYY.matcher(dateStr).matches();
	}
	
	public static boolean isHH_mm(String dateStr){
		return PATTERN_HH_MM.matcher(dateStr).matches();
	}
	
	public static boolean isHH_mm_ss(String dateStr){
		return PATTERN_HH_MM_SS.matcher(dateStr).matches();
	}
	
	/****
	 * 简单匹配时间模式
	 * @author wayshall
	 * @param dateStr
	 * @return
	 */
	public static String matchPattern(String dateStr){
		if(isYyyy_MM_dd_HH_mm_ss(dateStr)){
			return DATE_TIME;
		} else if (isYyyy_MM_dd_HH_mm(dateStr)){
			return DATE_SHORT_TIME;
		} else if (isYyyy_MM_dd(dateStr)){
			return DATE_ONLY;
		} else if (isYyyy(dateStr)){
			return YEAR_ONLY;
		} else if (isYyyy_MM(dateStr)){
			return YEAR_MONTH;
		} else if (isYyyy_MM_dd_HH(dateStr)){
			return "yyyy-MM-dd HH";
		}else if (isHH_mm(dateStr)){
			return "HH:mm";
		} else if (isHH_mm_ss(dateStr)){
			return "HH:mm:ss";
		} else {
			return DATE_TIME_MILLS;
		}
	}
	

	public static void main(String[] args) {
//		Date date = null;
//		date = parse("2010-10-10", "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
//		System.out.println(date.toLocaleString());
//		date = parse("2010-12-10", "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
//		System.out.println(date.toLocaleString());
		
		
		String ad = getFirstDayOfMonth(2012,8);
		String ad2 = getLastDayOfMonth(2012,7);
		System.out.println(ad+"========="+ad2);
	}
}
