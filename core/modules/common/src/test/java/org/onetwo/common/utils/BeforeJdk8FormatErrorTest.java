package org.onetwo.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Assert;
import org.junit.Test;

/**
 * bug详见：
 * https://stackoverflow.com/questions/30778927/roundingmode-half-down-issue-in-java8
 * https://www.oracle.com/technetwork/java/javase/8-compatibility-guide-2156366.html
 * 
 * @author wayshall
 * 
 * <br/>
 */
public class BeforeJdk8FormatErrorTest {
	@Test
	public void test() {
		Object val = null;
		
		val = formatValue(-800.755, "0.00");
		//这个断言在jdk8之前的版本是可以通过的，jdk8后无法通过，详见：https://stackoverflow.com/questions/30778927/roundingmode-half-down-issue-in-java8
		Assert.assertEquals("-800.76", val);
		 
		val = formatValue(BigDecimal.valueOf(-800.755), "0.00");
		Assert.assertEquals("-800.76", val);
	}

	public static Object formatValue(Object value, String dataFormat){
		NumberFormat nf = new DecimalFormat(dataFormat);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		Object actualValue = nf.format(value);
		return actualValue;
	}
}

