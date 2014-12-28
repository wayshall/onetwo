package org.onetwo.common.utils.convertor;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.convert.MoneyConvertUtils;

public class MoneyConvertTest {
	
	@Test
	public void test(){
		String moneyStr = MoneyConvertUtils.convert(12.11);
		
		BigDecimal money = new BigDecimal("111.222");
		String str = money.toPlainString();
		String scale = money.toString().substring(str.length()-money.scale());
		String intStr = money.toString().substring(0, str.length()-money.scale()-1);
		Assert.assertEquals("222", scale);;
	}
	
	@Test
	public void testConvertInt(){
		BigDecimal money = new BigDecimal("111");
		String str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("壹佰壹拾壹元", str);
		
		money = new BigDecimal("2356855");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("贰佰弎拾伍万陆仟捌佰伍拾伍元", str);
		
		money = new BigDecimal("300005");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("弎拾万零伍元", str);
		
		money = new BigDecimal("3000050");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("弎佰万零伍拾元", str);
		
		money = new BigDecimal("1300000050");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("壹拾弎亿零伍拾元", str);
		
		money = new BigDecimal("10300000050");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("壹佰零弎亿零伍拾元", str);
		
		money = new BigDecimal("10330000050");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("壹佰零弎亿弎仟万零伍拾元", str);
	}
	
	@Test
	public void testConvertScale(){
		BigDecimal money = new BigDecimal("111.23");
		String str = MoneyConvertUtils.convert(money);
		System.out.println("str: " + str);
		Assert.assertEquals("壹佰壹拾壹元贰毫弎分", str);
		
		money = new BigDecimal("2356855.56");
		str = MoneyConvertUtils.convert(money);
		System.out.println("str: " + str);
		Assert.assertEquals("贰佰弎拾伍万陆仟捌佰伍拾伍元伍毫陆分", str);
	}

}
