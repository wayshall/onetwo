package org.onetwo.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Assert;
import org.junit.Test;

public class MathTest {

	@Test
	public void testMoney(){
		double price = 1.76;
		double rate = .88;
		double tickePrice = price/rate;
		System.out.println("ticketPrice: " + tickePrice);
		Assert.assertTrue(tickePrice==2.0);
		BigDecimal ticketPriceBd = BigDecimal.valueOf(price).divide(BigDecimal.valueOf(rate), 2, RoundingMode.HALF_UP);
		System.out.println("ticketPriceBd: " + ticketPriceBd);
		ticketPriceBd.setScale(2, RoundingMode.HALF_UP);
		System.out.println("ticketPriceBd: " + ticketPriceBd);
		Assert.assertTrue(ticketPriceBd.doubleValue()==2.0);

	}
}
