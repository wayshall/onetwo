package org.onetwo.common.date;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.onetwo.common.date.DateUtils.DateType;
import org.onetwo.common.utils.Assert;

public class NiceDate {

	public static NiceDate New(){
		return new NiceDate();
	}

	public static NiceDate Now(){
		return new NiceDate(DateUtils.now());
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
	
	private DateType dateType = DateType.date;
	
	public NiceDate(){
	}
	
	public NiceDate(Date date){
		calendar.setTime(date);
	}
	
	protected void precise(DateType dateType) {
		this.dateType = dateType;
		atTheBeginning();
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
	
	public NiceDate today(){
		calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		return this;
	}
	
	public NiceDate yesterday(){
		return nextDay(-1);
	}
	
	public NiceDate nextDay(int number){
		DateUtils.increaseDay(calendar, number);
		return this;
	}
	
	public NiceDate nextHour(int amount){
		DateUtils.addHours(calendar, amount);
		return this;
	}
	
	public NiceDate nextMonth(int amount){
		DateUtils.addMonth(calendar, amount);
		return this;
	}
	
	public NiceDate nextYear(int amount){
		DateUtils.addYear(calendar, amount);
		return this;
	}
	
	public NiceDate nextMinute(int amount){
		calendar.add(Calendar.MINUTE, amount);
		return this;
	}
	
	public NiceDate nextSecond(int amount){
		calendar.add(Calendar.SECOND, amount);
		return this;
	}
	
	/*public NiceDate increaseDay(int numb){
		DateUtil.increaseDay(calendar, numb);
		return this;
	}*/
	
	public NiceDate tomorrow(){
		return nextDay(1);
	}
	
	public NiceDate preciseAtYear(){
		precise(DateType.year);
		return this;
	}
	
	public NiceDate preciseAtMonth(){
		precise(DateType.month);
		return this;
	}
	
	public NiceDate preciseAtHour(){
		precise(DateType.hour);
		return this;
	}
	
	/***
	 * 精确到日，不要时分秒
	 * @see #preciseDate
	 * @return
	 */
	public NiceDate preciseAtDate(){
		precise(DateType.date);
		return this;
	}
	
	
	public NiceDate preciseAtMin(){
		precise(DateType.min);
		return this;
	}
	
	public NiceDate preciseAtSec(){
		precise(DateType.sec);
		return this;
	}
	
	/***
	 * 精确到毫秒
	 * @author weishao zeng
	 * @return
	
	public NiceDate preciseAtMisec(){
		precise(DateType.misec);
		return this;
	} */
	

	public NiceDate atTheBeginning(){
		DateUtils.accurateToBeginningAt(calendar, dateType);
		return this;
	}
	
	public NiceDate atTheEnd(){
		DateUtils.accurateToEndAt(calendar, dateType);
		return this;
	}
	
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
		return DateUtils.format(format, calendar.getTime());
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
	
	public static void main(String[] args){
		NiceDate date = new NiceDate();
		System.out.println(date.getDatePhase());
	}

}
