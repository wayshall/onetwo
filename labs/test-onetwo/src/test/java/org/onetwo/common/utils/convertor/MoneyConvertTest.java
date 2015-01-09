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
		Assert.assertEquals("壹佰壹拾壹元整", str);
		
		money = new BigDecimal("2356855");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("贰佰叁拾伍万陆仟捌佰伍拾伍元整", str);
		
		money = new BigDecimal("300005");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("叁拾万零伍元整", str);
		
		money = new BigDecimal("3000050");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("叁佰万零伍拾元整", str);
		
		money = new BigDecimal("1300000050");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("壹拾叁亿零伍拾元整", str);
		
		money = new BigDecimal("10300000050");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("壹佰零叁亿零伍拾元整", str);
		
		money = new BigDecimal("10330000050");
		str = MoneyConvertUtils.convert(money);
		Assert.assertEquals("壹佰零叁亿叁仟万零伍拾元整", str);
	}
	
	@Test
	public void testConvertScale(){
		BigDecimal money = new BigDecimal("111.23");
		String str = MoneyConvertUtils.convert(money);
		System.out.println("str: " + str);
		Assert.assertEquals("壹佰壹拾壹元贰角叁分", str);
		
		money = new BigDecimal("2356855.56");
		str = MoneyConvertUtils.convert(money);
		System.out.println("str: " + str);
		Assert.assertEquals("贰佰叁拾伍万陆仟捌佰伍拾伍元伍角陆分", str);
		
		money = new BigDecimal("2356855.50");
		str = MoneyConvertUtils.convert(money);
		System.out.println("str: " + str);
		Assert.assertEquals("贰佰叁拾伍万陆仟捌佰伍拾伍元伍角整", str);
		
		money = new BigDecimal("2356855.00");
		str = MoneyConvertUtils.convert(money);
		System.out.println("str: " + str);
		Assert.assertEquals("贰佰叁拾伍万陆仟捌佰伍拾伍元整", str);
		
		money = new BigDecimal("2356855.001");
		try {
			str = MoneyConvertUtils.convert(money);
			Assert.fail("只支持精确到两位小数");
		} catch (Exception e) {
			Assert.assertNotNull(e);
		}
	}

}
