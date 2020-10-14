package org.onetwo.common.date;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

import org.onetwo.common.date.DateUtils.DateType;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.list.It;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;

/**********
 * 表示时间段
 * @author wayshall
 *
 */
public class DateInterval {
	
	public static DateInterval in(String date1, String date2){
		Assert.hasText(date1);
		Assert.hasText(date2);
		return new DateInterval(DateUtils.parse(date1), DateUtils.parse(date2));
	}
	
	public static DateInterval in(Date date1, Date date2){
		Assert.notNull(date1);
		Assert.notNull(date2);
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
			throw new IllegalArgumentException("the start date can not greater than end date");
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
		while((!includeEnd && DateUtils.compareAccurateAt(end, start, dt)>0) ||
				(includeEnd && DateUtils.compareAccurateAt(end, start, dt)>=0)
				){
			if(it.doIt(start.getTime(), index)){
				DateUtils.addByDateType(start, dt, intevalNumb);
				index++;
			}else{
				break;
			}
		}
		return index;
	}

	public DateIntervalList getIntervalByDate(int intevalNumb, boolean includeEnd){
		return getInterval(DateType.date, intevalNumb, includeEnd);
	}


	public DateIntervalList splitMonth(){
		return getInterval(DateType.month, 1, true);
	}
	public DateIntervalList splitDate(){
		return getInterval(DateType.date, 1, true);
	}
	public DateIntervalList split(DateType dt, boolean includeEnd){
		return getInterval(dt, 1, includeEnd);
	}

	public DateIntervalList getInterval(DateType dt){
		return getInterval(dt, 1, false);
	}
	
	/***
	 * 按照DateType的精度，返回时间段内的每个date对象
	 * @param dt 间隔单位
	 * @param intevalNumb 间隔数量
	 * @param includeEnd 
	 * @return
	 */
	public DateIntervalList getInterval(DateType dt, int intevalNumb, boolean includeEnd){
		DateIntervalList dates = new DateIntervalList();
		generateIntervalDates(dt, intevalNumb, includeEnd, date -> {
			dates.add(date);
		});
		return dates;
//		Calendar start = getStartCalendar();
//		Calendar end = getEndCalendar();
//
//		DateIntervalList dates = new DateIntervalList();
//		while((!includeEnd && DateUtils.compareAccurateAt(end, start, dt)>0) ||
//				(includeEnd && DateUtils.compareAccurateAt(end, start, dt)>=0)
//				){
////			System.out.println("stat:" + start.getTime().toLocaleString());
//			dates.add(start.getTime());
//			DateUtils.addByDateType(start, dt, intevalNumb);
//		}
//		return dates;
	}

	public NiceDateIntervalList getIntervalNiceDates(DateType dt, int intevalNumb, boolean includeEnd){
		NiceDateIntervalList dates = new NiceDateIntervalList();
		generateIntervalDates(dt, intevalNumb, includeEnd, date -> {
			dates.add(NiceDate.New(date));
		});
		return dates;
	}
	
	public void generateIntervalDates(DateType dt, int intevalNumb, boolean includeEnd, Consumer<Date> consumer){
		Calendar start = getStartCalendar();
		Calendar end = getEndCalendar();

		while((!includeEnd && DateUtils.compareAccurateAt(end, start, dt)>0) ||
				(includeEnd && DateUtils.compareAccurateAt(end, start, dt)>=0)
				){
//			dates.add(wrapper.apply(start.getTime()));
			consumer.accept(start.getTime());
			DateUtils.addByDateType(start, dt, intevalNumb);
		}
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
        return getMillis() / DateUtils.MILLIS_PER_DAY;
    }

    public long getStandardHours() {
        return getMillis() / DateUtils.MILLIS_PER_HOUR;
    }
    
    public long getStandardMinutes() {
        return getMillis() / DateUtils.MILLIS_PER_MINUTE;
    }
    
    public long getStandardSeconds() {
        return getMillis() / DateUtils.MILLIS_PER_SECOND;
    }
    
    @SuppressWarnings("serial")
	static public class DateIntervalList extends JFishList<Date> {
    	private DateIntervalList(){
    	}
    	
    	private DateIntervalList(Collection<Date> dates){
    		super(dates);
    	}
    	
    	public JFishList<String> format(final String pattern){
    		final JFishList<String> list = JFishList.create();
    		each(new NoIndexIt<Date>() {

				@Override
				protected void doIt(Date date) throws Exception {
					String rs = DateUtils.formatDateByPattern(date, pattern);
					list.add(rs);
				}
    			
			});
    		return list;
    	}
    }
    
    @SuppressWarnings("serial")
	static public class NiceDateIntervalList extends JFishList<NiceDate> {
    	private NiceDateIntervalList(){
    	}
    	private NiceDateIntervalList(Collection<NiceDate> dates){
    		super(dates);
    	}
    	
    	public JFishList<String> format(final String pattern){
    		final JFishList<String> list = JFishList.create();
    		each(new NoIndexIt<NiceDate>() {

				@Override
				protected void doIt(NiceDate date) throws Exception {
					String rs = date.format(pattern);
					list.add(rs);
				}
    			
			});
    		return list;
    	}
    }
}
