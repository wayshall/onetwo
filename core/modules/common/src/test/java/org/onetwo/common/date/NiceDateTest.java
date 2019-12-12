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



public class NiceDateTest {

	@Test
	public void test() throws ParseException{
		LocalDateTime date = LocalDateTime.of(2015, 11, 11, 11, 11, 11, 510000000);
		String format = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
//		System.out.println("format: " + format);
		Assert.assertEquals("2015-11-11 11:11:11 510", format);
		
		String str = "Tue, 24 Nov 2015 06:33:31 GMT";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
//		System.out.println("f:"+sdf.format(new Date()));
		Date d = sdf.parse(str);
		System.out.println("parse:"+d.toLocaleString());
		
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
		
		
		
		res = NiceDate.New("2019-12-12 10:38:38").preciseAtDate().atTheEnd().formatAsDateTime();
		assertThat(res).isEqualTo("2019-12-12 23:59:59");
		
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
		
	}
}
