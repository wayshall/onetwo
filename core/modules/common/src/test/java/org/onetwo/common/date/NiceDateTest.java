package org.onetwo.common.date;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.NiceDateRange.QuarterDateRange;



public class NiceDateTest {
	

	@Test
	public void testQuarter() throws ParseException{
		NiceDate now = NiceDate.New("2020-11-14");
		
		assertThat(NiceDate.New("2020-11-14").getQuarter(0).toString()).isEqualTo("2020Q1");
		
		String dateTime = now.getQuarter(0).getStart().format("MM-dd HH:mm:ss");
		System.out.println("dateTime: " + dateTime);
		assertThat(dateTime).isEqualTo("01-01 00:00:00");

		// 第三季度的下一季度，即第四季度
		QuarterDateRange q = now.getQuarter(2).nextQuarter(1);
		dateTime = q.getStart().format("MM-dd HH:mm:ss");
		assertThat(dateTime).isEqualTo("10-01 00:00:00");
		assertThat(q.getValue()).isEqualTo(3);
		
		q = now.getQuarter(3).nextQuarter(1);
		dateTime = q.getStart().format("MM-dd HH:mm:ss");
		assertThat(dateTime).isEqualTo("01-01 00:00:00");
		assertThat(q.getValue()).isEqualTo(0);
		
		// 第四季的前三季度，即第一季
		q = now.getQuarter(3).nextQuarter(-3);
		dateTime = q.getStart().format("MM-dd HH:mm:ss");
		assertThat(dateTime).isEqualTo("01-01 00:00:00");
		assertThat(q.getValue()).isEqualTo(0);
		assertThat(q.getStart().formatAsDate()).isEqualTo("2020-01-01");
		assertThat(q.getEnd().formatAsDate()).isEqualTo("2020-03-31");

		// 2020第四季的前7季度，即上一年2019第四季的前三季度，即上一年2019的第一季
		q = now.getQuarter(3).nextQuarter(-7);
		dateTime = q.getStart().format("MM-dd HH:mm:ss");
		assertThat(dateTime).isEqualTo("01-01 00:00:00");
		assertThat(q.getValue()).isEqualTo(0);
		assertThat(q.getStart().formatAsDate()).isEqualTo("2019-01-01");
		assertThat(q.getEnd().formatAsDate()).isEqualTo("2019-03-31");

		now = NiceDate.New("2021-01-19");
		// 2021-01的当前季度的上一季，即2020第四季
		q = now.getCurrentQuarter().nextQuarter(-1);
		assertThat(q.getStart().formatAsDate()).isEqualTo("2020-10-01");
		assertThat(q.getEnd().formatAsDate()).isEqualTo("2020-12-31");
		

		q = now.getCurrentQuarter();
		System.out.println("start: " + q.getStart().formatAsDate());
		System.out.println("end: " + q.getEnd().formatAsDate());
		
	}
		

	@Test
	public void test() throws ParseException{
		String weekformat = NiceDate.New("2020-10-26").format("E HH:mm", Locale.ENGLISH);
		System.out.println("weekformat: " + weekformat);
		assertThat(weekformat).isEqualTo("Mon 00:00");
		weekformat = NiceDate.New("2020-10-31").format("E HH:mm", Locale.ENGLISH);
		System.out.println("weekformat: " + weekformat);
		assertThat(weekformat).isEqualTo("Sat 00:00");
		weekformat = NiceDate.New("2020-10-31").format("yyyy-w'(week)' HH:mm", Locale.ENGLISH);
		System.out.println("weekformat: " + weekformat);
		assertThat(weekformat).isEqualTo("2020-44(week) 00:00");
		weekformat = NiceDate.New("2020-11-11 00:00:33").format("E HH:mm", Locale.ENGLISH);
		System.out.println("weekformat2: " + weekformat);
		
		NiceDate start = NiceDate.Now().nextMinute(-4);
		System.out.println(start.formatAsDateTime());
		System.out.println(start.nextMinute(2).formatAsDateTime());
		
		LocalDateTime date = LocalDateTime.of(2015, 11, 11, 11, 11, 11, 510000000);
		String format = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
//		System.out.println("format: " + format);
		Assert.assertEquals("2015-11-11 11:11:11 510", format);
		
		String str = "Tue, 24 Nov 2015 06:33:31 GMT";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
//		System.out.println("f:"+sdf.format(new Date()));
		Date d = sdf.parse(str);
		System.out.println("parse:"+d.toLocaleString());
		
		str = NiceDate.New(date).preciseAtMonth().atTheEnd().formatAsDateTime();
		System.out.println("preciseAtMonth: " + str);
		Assert.assertEquals("2015-11-30 23:59:59", str);
		
		str = NiceDate.New(date).preciseAtHour().atTheEnd().formatAsDateTime();
		System.out.println("preciseAtHour: " + str);
		Assert.assertEquals("2015-11-11 11:59:59", str);
		
		str = NiceDate.New(date).preciseAtDate().atTheBeginning().formatAsDateTime();
		System.out.println("preciseAtDate: " + str);
		Assert.assertEquals("2015-11-11 00:00:00", str);

		str = NiceDate.New(date).preciseAtHour().atTheBeginning().formatAsDateTime();
		System.out.println("preciseAtHour: " + str);
		Assert.assertEquals("2015-11-11 11:00:00", str);
		
		NiceDate monthDate = NiceDate.New("11月19日", "MM月dd日");
		assertThat(monthDate.formatAsDateTime()).isEqualTo("1970-11-19 00:00:00");
		monthDate = NiceDate.currentYearWithDate(2020, "11月19日", "MM月dd日");
		System.out.println("monthDate: " + monthDate.formatAsDateTime());
		assertThat(monthDate.formatAsDateTime()).isEqualTo("2020-11-19 00:00:00");
	}

	@Test
	public void testThis() throws ParseException{
		String res = NiceDate.New("2019-12-12 10:38:38").preciseAtDate().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 00:00:00");
		
		res = NiceDate.New("2019-12-12 10:38:38").preciseAtHour().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 10:00:00");
		
		res = NiceDate.New("2019-12-12 10:38:38").preciseAtMin().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 10:38:00");
		
		res = NiceDate.New("2019-12-12 10:38:38").preciseAtSec().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 10:38:38");
		
		
		
		res = NiceDate.New("2019-12-12 10:38:38").atLastMomentOfDay().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 23:59:59");
		res = NiceDate.New("2019-12-12 10:38:38").atEarliestMomentOfDay().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 00:00:00");
		
		res = NiceDate.New("2019-12-12 10:38:38").preciseAtHour().atTheEnd().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 10:59:59");
		
		res = NiceDate.New("2019-12-12 10:38:38").preciseAtMin().atTheEnd().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 10:38:59");
		
		res = NiceDate.New("2019-12-12 10:38:38.100").preciseAtSec().atTheEnd().formatDateTimeMillis();
		assertThat(res).isEqualTo("2019-12-12 10:38:38.999");
		
		/*res = NiceDate.New("2019-12-12 10:38:38.100").preciseAtMisec().formatDateTimeMillis();
		assertThat(res).isEqualTo("2019-12-12 10:38:38.100");
		res = NiceDate.New("2019-12-12 10:38:38.100").preciseAtMisec().atTheEnd().formatDateTimeMillis();
		assertThat(res).isEqualTo("2019-12-12 10:38:38.100");*/
		
		res = NiceDate.New().preciseAtDate().atTheEnd().formatDateTimeMillis();
		System.out.println("res: " + res);
		

		Date date = NiceDate.New("2019-12-26 10:38:38").preciseAtDate().atTheEnd().clearMillis().getTime();
		assertThat(date).isEqualTo(DateUtils.parse("2019-12-26 23:59:59"));

		date = NiceDate.New("2019-12-26 10:38:38").preciseAtDate().clearMillis().clearSeconds().clearMinute().clearHour().getTime();
		assertThat(date).isEqualTo(DateUtils.parse("2019-12-26"));
		
		date = NiceDate.New("2019-12-26 10:38:38").preciseAtDate().clearHour().getTime();
		assertThat(date).isEqualTo(DateUtils.parse("2019-12-26"));

	}
}
