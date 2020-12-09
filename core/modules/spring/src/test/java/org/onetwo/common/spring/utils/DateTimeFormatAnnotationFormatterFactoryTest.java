package org.onetwo.common.spring.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.onetwo.common.reflect.Intro;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;

/**
 * @author weishao zeng
 * <br/>
 */

public class DateTimeFormatAnnotationFormatterFactoryTest {
	
	DateTimeFormatAnnotationFormatterFactory format = new DateTimeFormatAnnotationFormatterFactory();
	
	@Test
	public void test() throws ParseException {
		DateFormatTest t = new DateFormatTest();
		Field field = Intro.wrap(DateFormatTest.class).getField("date");
		DateTimeFormat df = AnnotationUtils.findAnnotation(field, DateTimeFormat.class);
		DateFormatter date = (DateFormatter)format.getParser(df, field.getType());
		System.out.println("date: " + date.parse("2020-12-09", Locale.getDefault()).toLocaleString());
	}

	static public class DateFormatTest {
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		Date date;
	}
}
