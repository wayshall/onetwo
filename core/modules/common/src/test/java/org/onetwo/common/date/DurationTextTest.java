package org.onetwo.common.date;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.Assert;
import org.junit.Test;


public class DurationTextTest {

	@Test
	public void testNiceDateText(){
		DurationText dtext = DurationText.New()
										.map(Duration.ofMinutes(1), "刚刚")
										.map(Duration.ofMinutes(60), du->du.toMinutes()+"分钟前")
										.map(Duration.ofHours(24), du->du.toHours()+"小时前")
										.other(du->du.toDays()+"天前");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.now();
		LocalDateTime date2 = date.minusSeconds(40);
		System.out.println("date: " + date.format(formatter)+", date2: " + date2.format(formatter));
		
		Duration duration = Duration.ofSeconds(date2.until(date, ChronoUnit.SECONDS));
		String text = dtext.getText(duration);
		Assert.assertEquals("刚刚", text);
		
		date2 = date.minusMinutes(3);
		System.out.println("date: " + date.format(formatter)+", date2: " + date2.format(formatter));
		
		duration = Duration.ofSeconds(date2.until(date, ChronoUnit.SECONDS));
		text = dtext.getText(duration);
		Assert.assertEquals("3分钟前", text);
	}
}
