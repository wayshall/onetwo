package org.onetwo.common.date;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.onetwo.common.date.DateUtils.DateType;
import org.onetwo.common.date.NiceDateRange.QuarterDateRange;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;

public class NiceDate {

	public static NiceDate New(){
		return new NiceDate();
	}

	public static NiceDate Now(){
		return new NiceDate(DateUtils.now());
	}

	public static NiceDate New(LocalDateTime localDateTime){
		Assert.notNull(localDateTime);
		return new NiceDate(Dates.toDate(localDateTime));
	}

	public static NiceDate New(Date date){
		Assert.notNull(date);
		return new NiceDate(date);
	}

	public static NiceDate New(String dateStr){
		Date date = DateUtils.parse(dateStr);
		return New(date);
	}

	public static NiceDate New(String dateStr, String format){
		Date date = DateUtils.parseByPatterns(dateStr, format);
		return New(date);
	}
	
	public static enum DatePhase {
		morning(6, 12),
		noon(12, 14),
		afternoon(14, 18),
		night(18, 24),
		deepnight(0, 6);
		
		private int start;
		private int end;
		
		/*****
		 * 
		 * @param start include
		 * @param end exclude
		 */
		private DatePhase(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean inPhase(int hour){
			if(hour>=start && hour<end)
				return true;
			return false;
		}
	}
	
	private Calendar calendar = Calendar.getInstance();
	
	public NiceDate(){
	}
	
	public NiceDate(Date date){
		calendar.setTime(date);
	}
	
	public NiceDate(Calendar calendar) {
		super();
		this.calendar = calendar;
	}
	
	/***
	 * 当前季度，第一季度为0
	 * @author weishao zeng
	 * @return
	 */
	public QuarterDateRange getCurrentQuarter() {
		int quarter = getCurrentQuarterIndex();
		QuarterDateRange currentQuarter = getQuarter(quarter);
		return currentQuarter;
	}
	
	/***
	 * 获取当前所属季度
	 * @author weishao zeng
	 * @return
	 */
	public int getCurrentQuarterIndex() {
		int currentMonth = getMonth() + 1;

		int quarter = 0;
		if (currentMonth >= 1 && currentMonth <= 3) {
			quarter = 0;
		} else if (currentMonth >= 4 && currentMonth <= 6) {
			quarter = 1;
		} else if (currentMonth >= 7 && currentMonth <= 9) {
			quarter = 2;
		} else if (currentMonth >= 10 && currentMonth <= 12) {
			quarter = 3;
		} else {
			throw new BaseException("error month index: " + currentMonth);
		}
		return quarter;
	}

	public int getCurrentSeason() {
		return getCurrentQuarterIndex();
	}
	
	/***
	 * @author weishao zeng
	 * @param quarter 从0开始
	 * @return
	 */
	public QuarterDateRange getQuarter(int quarter) {
		QuarterDateRange currentQuarter = null;
		if (quarter==0) {
			currentQuarter = new QuarterDateRange(quarter, atMonth(0).preciseAtMonth().atTheBeginning(), 
													atMonth(2).preciseAtMonth().atTheEnd());
		} else if (quarter==1) {
			currentQuarter = new QuarterDateRange(quarter, atMonth(3).preciseAtMonth().atTheBeginning(), 
												atMonth(5).preciseAtMonth().atTheEnd());
		} else if (quarter==2) {
			currentQuarter = new QuarterDateRange(quarter, atMonth(6).preciseAtMonth().atTheBeginning(), 
					atMonth(8).preciseAtMonth().atTheEnd());
		} else if (quarter==3) {
			currentQuarter = new QuarterDateRange(quarter, atMonth(9).preciseAtMonth().atTheBeginning(), 
					atMonth(11).preciseAtMonth().atTheEnd());
		} else {
			throw new IllegalArgumentException("error quarter : " + quarter);
		}
		return currentQuarter;
	}
	
	public NiceDate atMonth(int month) {
		NiceDate d = clone();
		d.calendar.set(Calendar.MONTH, month);
		return d;
	}

	protected PreciseNiceDate precise(DateType dateType) {
		return new PreciseNiceDate(this, dateType);
//		this.dateType = dateType;
//		atTheBeginning();
	}
	
	public NiceDate setTimeByString(String timeString){
		String dateString = formatAsDate();
		String dateTimeString = dateString + " " + timeString;
		Date date = DateUtils.parse(dateTimeString);
		calendar.setTime(date);
		return this;
	}


	/*********
	 * 
	 * @return a new Date Object
	 */
	public Date getTime() {
		return calendar.getTime();
	}

	public long getTimeInMillis(){
		return calendar.getTimeInMillis();
	}

	public long getMillis(){
		return calendar.getTimeInMillis();
	}

	public NiceDate setMillis(long millis){
		calendar.setTimeInMillis(millis);
		return this;
	}

	public NiceDate setTimeInSeconds(long seconds){
		return setMillis(TimeUnit.SECONDS.toMillis(seconds));
	}

	public void setTime(Date time) {
		calendar.setTime(time);
	}
	
//	public NiceDate today(){
//		calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//		return this;
//	}
	
	public NiceDate yesterday(){
		return nextDay(-1);
	}
	
	public NiceDate nextDay(int number){
		NiceDate d = clone();
		DateUtils.increaseDay(d.calendar, number);
		return d;
//		DateUtils.increaseDay(calendar, number);
//		return this;
	}
	
	public NiceDate nextHour(int amount){
		NiceDate d = clone();
		DateUtils.addHours(d.calendar, amount);
		return d;
//		DateUtils.addHours(calendar, amount);
//		return this;
	}
	
	public NiceDate nextMonth(int amount){
		NiceDate d = clone();
		DateUtils.addMonth(d.calendar, amount);
		return d;
//		DateUtils.addMonth(calendar, amount);
//		return this;
	}
	
	public NiceDate nextYear(int amount){
		NiceDate d = clone();
		DateUtils.addYear(d.calendar, amount);
		return d;
//		DateUtils.addYear(calendar, amount);
//		return this;
	}
	
	public NiceDate nextMinute(int amount){
		NiceDate d = clone();
		d.calendar.add(Calendar.MINUTE, amount);
		return d;
//		calendar.add(Calendar.MINUTE, amount);
//		return this;
	}
	
	public NiceDate nextWeek(int amount){
		NiceDate d = clone();
		d.calendar.add(Calendar.WEEK_OF_MONTH, amount);
		return d;
//		calendar.add(Calendar.MINUTE, amount);
//		return this;
	}
	
	public NiceDate nextSecond(int amount){
		NiceDate d = clone();
		d.calendar.add(Calendar.SECOND, amount);
		return d;
//		calendar.add(Calendar.SECOND, amount);
//		return this;
	}
	
	/*public NiceDate increaseDay(int numb){
		DateUtil.increaseDay(calendar, numb);
		return this;
	}*/
	
	public NiceDate tomorrow(){
		return nextDay(1);
	}
	
	public NiceDate atLastMomentOfDay() {
		// 时间精确到天，的最后一刻
		return preciseAtDate().atTheEnd();
	}
	
	public NiceDate atEarliestMomentOfDay() {
		// 时间精确到天，的最后一刻
		return preciseAtDate().atTheBeginning();
//		return this;
	}
	
	public PreciseNiceDate preciseAtYear(){
		return precise(DateType.year);
//		return this;
	}
	
	public PreciseNiceDate preciseAtMonth(){
		return precise(DateType.month);
//		return this;
	}
	
	public PreciseNiceDate preciseAtHour(){
		return precise(DateType.hour);
//		return this;
	}
	
	/***
	 * 精确到日，不要时分秒
	 * @see #preciseDate
	 * @return
	 */
	public PreciseNiceDate preciseAtDate(){
		return precise(DateType.date);
//		precise(DateType.date);
//		return this;
	}
	
	
	public PreciseNiceDate preciseAtMin(){
		return precise(DateType.min);
//		return this;
	}
	
	public PreciseNiceDate preciseAtSec(){
		return precise(DateType.sec);
//		return this;
	}
	
	/***
	 * 精确到毫秒
	 * @author weishao zeng
	 * @return
	
	public NiceDate preciseAtMisec(){
		precise(DateType.misec);
		return this;
	} */
	
	
	public NiceDate clearMillis(){
		this.calendar.clear(Calendar.MILLISECOND);
		return this;
	}
	
	public NiceDate clearSeconds(){
		this.calendar.clear(Calendar.SECOND);
		return this;
	}
	
	public NiceDate clearMinute(){
		this.calendar.clear(Calendar.MINUTE);
		return this;
	}
	
	/*public NiceDate clearHourOfDay(){
		this.calendar.clear(Calendar.HOUR_OF_DAY);
		return this;
	}*/
	
	public NiceDate clearHour(){
		this.calendar.clear(Calendar.HOUR);
		return this;
	}
	
	
	public boolean isBetween(Date start, Date end){
		long t = this.calendar.getTime().getTime();
		return start.getTime() <= t && t < end.getTime();
	}
	
	public DatePhase getDatePhase(){
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		for(DatePhase p : DatePhase.values()){
			if(p.inPhase(hour))
				return p;
		}
		throw new UnsupportedOperationException("error hour: " +  hour);
	}
	
	public boolean isMorning(){
		return DatePhase.morning == getDatePhase();
	}
	
	public boolean isNoon(){
		return DatePhase.noon == getDatePhase();
	}
	
	public boolean isAfternoon(){
		return DatePhase.afternoon == getDatePhase();
	}
	
	public boolean isNight(){
		return DatePhase.night == getDatePhase();
	}
	
	public boolean isDeepnight(){
		return DatePhase.deepnight == getDatePhase();
	}
	
	public String toString(){
		return formatDateTimeMillis();
	}

	public String format(String format){
//		return DateUtils.format(format, calendar.getTime());
		return format(format, null);
	}

	public String format(String format, Locale locale){
		DateTimeFormatter formatter = locale==null?DateTimeFormatter.ofPattern(format):DateTimeFormatter.ofPattern(format, locale);
		LocalDateTime ldt = Dates.toLocalDateTime(calendar.getTime());
		return ldt.format(formatter);
	}
	
	public String formatAsDate(){
		return DateUtils.formatDate(calendar.getTime());
	}
	public String formatAsTime(){
		return DateUtils.formatTime(calendar.getTime());
	}
	public String formatAsDateTime(){
		return DateUtils.formatDateTime(calendar.getTime());
	}
	public String formatDateTimeMillis(){
		return DateUtils.formatDateTimeMillis(calendar.getTime());
	}
	
	public int getYear(){
		return calendar.get(Calendar.YEAR);
	}
	
	public int getMonth(){
		return calendar.get(Calendar.MONTH);
	}
	
	public int getDayOfMonth(){
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public int getHourOfDay(){
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public boolean isBeforeOrEquals(NiceDate date){
		return isBefore(date) || isEquals(date);
	}

	public boolean isAfterOrEquals(NiceDate date){
		return isAfter(date) || isEquals(date);
	}

	public boolean isBefore(NiceDate date){
		return getTimeInMillis()<date.getTimeInMillis();
	}
	public boolean isAfter(NiceDate date){
		return getTimeInMillis()>date.getTimeInMillis();
	}
	public boolean isEquals(NiceDate date){
		return getTimeInMillis()==date.getTimeInMillis();
	}
	
	public Duration awayFrom(NiceDate date){
		long distance = date.getTimeInMillis() - this.getTimeInMillis();
		Duration duration = Duration.ofMillis(Math.abs(distance));
		return duration;
	}
	
	public Calendar getCalendar() {
		return calendar;
	}

	public NiceDate clone(){
		NiceDate date = new NiceDate(this.calendar.getTime());
		return date;
	}
	
	public static void main(String[] args){
		NiceDate date = new NiceDate();
		System.out.println(date.getDatePhase());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calendar == null) ? 0 : calendar.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NiceDate other = (NiceDate) obj;
		if (calendar == null) {
			if (other.calendar != null)
				return false;
		} else if (!calendar.equals(other.calendar))
			return false;
		return true;
	}


	/***
	 * 设置日期对象精确到某年或某月或某天，获取某年或某月或某天的开始和结束时间
	 * @author way
	 *
	 */
	final static public class PreciseNiceDate extends NiceDate {
		private DateType dateType = DateType.date;

		public PreciseNiceDate(NiceDate niceDate, DateType dateType) {
			super(niceDate.getTime());
			this.dateType = dateType;
			DateUtils.accurateToBeginningAt(getCalendar(), dateType);
		}

		/***
		 * 根据被精确到的DateType类型，设置对应的开始时间
		 * 如
		 * DateType为DateType.date，则开始时间类似：2020-10-26 00:00:00
		 * DateType为DateType.hour，则开始时间类似：2020-10-26 11:00:00
		 * DateType为DateType.min，则开始时间类似：2020-10-26 11:11:00
		 * @author weishao zeng
		 * @return
		 */
		final public NiceDate atTheBeginning(){
			NiceDate d = super.clone();
			DateUtils.accurateToBeginningAt(d.getCalendar(), dateType);
			return d;
		}
		
		/****
		 * 根据被精确到的DateType类型，设置对应的开始时间
		 * 如
		 * DateType为DateType.month，则开始时间类似：2020-10-31 23:59:59
		 * DateType为DateType.date，则开始时间类似：2020-10-26 23:59:59
		 * DateType为DateType.hour，则开始时间类似：2020-10-26 11:59:59
		 * @author weishao zeng
		 * @return
		 */
		final public NiceDate atTheEnd(){
			NiceDate d = super.clone();
			DateUtils.accurateToEndAt(d.getCalendar(), dateType);
			return d;
		}

		final public PreciseNiceDate clone(){
			PreciseNiceDate date = new PreciseNiceDate(this, dateType);
			return date;
		}
	}
}
