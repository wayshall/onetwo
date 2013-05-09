package org.onetwo;

import java.math.BigDecimal;
import java.net.URL;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Test {

	private static final DateTimeFormatter dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter date = DateTimeFormat.forPattern("yyyy-MM-dd");

	public static void main(String[] args) throws Exception {
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Byte.MAX_VALUE);
		System.out.println("value:"+BigDecimal.valueOf(Character.MAX_VALUE));
	}

	public static void test(String... args) {
		System.out.println(args);
	}
}
