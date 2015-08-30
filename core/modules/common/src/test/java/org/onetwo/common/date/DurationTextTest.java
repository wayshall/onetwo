package org.onetwo.common.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;


public class DurationTextTest {

	@Test
	public void testNiceDateText(){
		DurationText dtext = DurationText.createSimpleChineseDurationText();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.now();
		LocalDateTime date2 = date.minusSeconds(40);
		System.out.println("date: " + date.format(formatter)+", date2: " + date2.format(formatter));
		
//		Duration duration = Duration.ofSeconds(date2.until(date, ChronoUnit.SECONDS));
		String text = dtext.getText(date2, date);
		Assert.assertEquals("刚刚", text);
		
		date2 = date.minusMinutes(3);
		System.out.println("date: " + date.format(formatter)+", date2: " + date2.format(formatter));
		text = dtext.getText(date2, date);
		Assert.assertEquals("3分钟前", text);

		date2 = date.minusHours(4);
		text = dtext.getText(date2, date);
		Assert.assertEquals("4小时前", text);

		date2 = date.minusDays(32);
		text = dtext.getText(date2, date);
		Assert.assertEquals("32天前", text);
	}
}
