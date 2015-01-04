package org.onetwo.jodatime

import org.joda.time.DateTimeConstants
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.junit.Assert
import org.junit.Test
import org.onetwo.common.utils.DateRange
import org.onetwo.common.utils.DateUtil


class WeekTest {

	@Test
	public void testWeek(){
		def start = new LocalDate(Date.parse("yyyy-MM-dd", "2015-01-13"));
		def end = new LocalDate(Date.parse("yyyy-MM-dd", "2015-01-26"));
		
		Set<LocalDate> dates = new LinkedHashSet<LocalDate>();
		dates.add(start);
		LocalDate weekDate = start.withDayOfWeek(DateTimeConstants.MONDAY).plusWeeks(1);
		while(weekDate.isBefore(end) || weekDate.isEqual(end)){
			dates.add(weekDate)
			println weekDate.getDayOfWeek()
			weekDate = weekDate.plusWeeks(1);
		}
		
		println dates
		Assert.assertEquals("[2015-01-13, 2015-01-19, 2015-01-26]", dates.toString())
		
		start = new LocalDate(Date.parse("yyyy-MM-dd", "2015-01-11"));
		end = new LocalDate(Date.parse("yyyy-MM-dd", "2015-01-31"));
		
		Collection<DateRange> inters = DateUtil.splitAsWeekInterval(start, end);
		List<Interval> interList = new ArrayList(inters);
		Assert.assertEquals("2015-01-11", interList[0].getStartNiceDate().formatAsDate())
		Assert.assertEquals("2015-01-11", interList[0].getEndNiceDate().formatAsDate())
		Assert.assertEquals("2015-01-12", interList[1].getStartNiceDate().formatAsDate())
		Assert.assertEquals("2015-01-18", interList[1].getEndNiceDate().formatAsDate())
	}
}
