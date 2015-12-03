package org.onetwo.common.date;

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
}
