package org.onetwo.common.utils;

import java.util.Calendar;
import java.util.Date;

import org.onetwo.common.utils.DateUtil.DateType;
import org.onetwo.common.utils.list.It;
import org.onetwo.common.utils.list.JFishList;

/**********
 * 表示时间段
 * @author wayshall
 *
 */
public class DateInterval {
	
	public static DateInterval in(String date1, String date2){
		return new DateInterval(DateUtil.parse(date1), DateUtil.parse(date2));
	}
	
	public static DateInterval in(Date date1, Date date2){
		return new DateInterval(date1, date2);
	}

	private final long startMillis;
	private final long endMillis;
	

	private DateInterval(Date start, Date end) {
		this(start.getTime(), end.getTime());
	}
	
	private DateInterval(long startMillis, long endMillis) {
		super();
		this.startMillis = startMillis;
		this.endMillis = endMillis;
		this.checkInterval();
	}
	
	private void checkInterval(){
		if(startMillis>endMillis)
			throw new IllegalArgumentException("the start can not greater than end");
	}
	

	public int eachDate(It<Date> it){
		return eachDate(1, false, it);
	}
	
	/********
	 * 则表示从开始时间开始，每次递增intevalNumb天，直到结束时间。
	 * @param intevalNumb
	 * @param includeEnd 是否包含结束时间在内
	 * @param it
	 * @return
	 */
	public int eachDate(int intevalNumb, boolean includeEnd, It<Date> it){
		return each(DateType.date, intevalNumb, includeEnd, it);
	}

	/****
	 * 根据DateType类型逐渐增加intevalNumb。
	 * 比如如果DateType是DateType#date，intervalNumb是1，则表示从开始时间开始，每次递增1天，直到结束时间。
	 * @param dt
	 * @param intevalNumb
	 * @param includeEnd 是否包含结束时间在内
	 * @param it
	 * @return
	 */
	public int each(DateType dt, int intevalNumb, boolean includeEnd, It<Date> it){
		Calendar start = getStartCalendar();
		Calendar end = getEndCalendar();

		int index = 0;
		while((!includeEnd && DateUtil.compareAccurateAt(end, start, dt)>0) ||
				(includeEnd && DateUtil.compareAccurateAt(end, start, dt)>=0)
				){
			if(it.doIt(start.getTime(), index)){
				DateUtil.addByDateType(start, dt, intevalNumb);
				index++;
			}else{
				break;
			}
		}
		return index;
	}

	public JFishList<Date> getIntervalByDate(int intevalNumb, boolean includeEnd){
		return getInterval(DateType.date, intevalNumb, includeEnd);
	}



	public JFishList<Date> getInterval(DateType dt){
		return getInterval(dt, 1, false);
	}
	public JFishList<Date> getInterval(DateType dt, int intevalNumb, boolean includeEnd){
		Calendar start = getStartCalendar();
		Calendar end = getEndCalendar();

		JFishList<Date> dates = JFishList.create();
		while((!includeEnd && DateUtil.compareAccurateAt(end, start, dt)>0) ||
				(includeEnd && DateUtil.compareAccurateAt(end, start, dt)>=0)
				){
//			System.out.println("stat:" + start.getTime().toLocaleString());
			dates.add(start.getTime());
			DateUtil.addByDateType(start, dt, intevalNumb);
		}
		return dates;
	}
	public Date getStartDate() {
		return new Date(startMillis);
	}

	public Date getEndDate() {
		return new Date(endMillis);
	}

	public Calendar getStartCalendar() {
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startMillis);
		return start;
	}

	public Calendar getEndCalendar() {
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(endMillis);
		return end;
	}

	public long getStartMillis() {
		return startMillis;
	}

	public long getEndMillis() {
		return endMillis;
	}
	
	public long getMillis(){
		return endMillis - startMillis;
	}

	public long getStandardDays() {
        return getMillis() / DateUtil.MILLIS_PER_DAY;
    }

    public long getStandardHours() {
        return getMillis() / DateUtil.MILLIS_PER_HOUR;
    }
    
    public long getStandardMinutes() {
        return getMillis() / DateUtil.MILLIS_PER_MINUTE;
    }
    
    public long getStandardSeconds() {
        return getMillis() / DateUtil.MILLIS_PER_SECOND;
    }
}
