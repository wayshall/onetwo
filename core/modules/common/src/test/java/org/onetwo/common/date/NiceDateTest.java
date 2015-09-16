package org.onetwo.common.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;



public class NiceDateTest {

	@Test
	public void test(){
		LocalDateTime date = LocalDateTime.of(2015, 11, 11, 11, 11, 11, 510000000);
		String format = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
		System.out.println("format: " + format);
		Assert.assertEquals("2015-11-11 11:11:11 510", format);
	}
}
