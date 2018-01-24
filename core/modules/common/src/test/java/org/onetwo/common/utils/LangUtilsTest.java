package org.onetwo.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.convert.Types;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.md.Hashs;
import org.onetwo.common.utils.list.JFishList;

public class LangUtilsTest {
	
	@Test
	public void testFormatValue(){
		Assert.assertTrue(Double.valueOf("335.00")==335);
		long dec = LangUtils.hexToLong("BC092A11");
		Assert.assertEquals(3154717201L, dec);
		Object val = LangUtils.formatValue(0.755, "#0.00#");
		Assert.assertEquals("0.755", val);
		val = LangUtils.formatValue(1.9, "0.00");
		System.out.println("val: " + val);
		Assert.assertEquals("1.90", val);
		val = LangUtils.formatValue(0, "0.00");
		System.out.println("val: " + val);
		Assert.assertEquals("0.00", val);
		val = LangUtils.formatValue(0.755, "#.##");
		Assert.assertEquals("0.76", val);
		val = LangUtils.formatValue(1, "0.00");
		Assert.assertEquals("1.00", val);
		
		val = LangUtils.formatValue(0.755, "0.00");
		Assert.assertEquals("0.76", val);
		val = Double.parseDouble(String.format("%.2f",0.755));
		Assert.assertEquals("0.76", val.toString());
		
		val = LangUtils.formatValue(0.1, "0.00");
		Assert.assertEquals("0.10", val);
		val = LangUtils.formatValue(1253.1, "0.00");
		Assert.assertEquals("1253.10", val);
		val = LangUtils.formatValue(1253.1222222, "0.00");
		Assert.assertEquals("1253.12", val);
		val = LangUtils.formatValue(1253.1222222, "0");
		System.out.println("val1: " + val);
		Assert.assertEquals("1253", val);
		
		BigDecimal bd = new BigDecimal("1.44445");
		Assert.assertEquals("1.44", bd.setScale(2, RoundingMode.HALF_UP).toString());
		

		val = LangUtils.formatValue(-800.755, "0.00");
		Assert.assertEquals("-800.76", val);

		val = LangUtils.formatValue(-800.755, "#0.00元");
		Assert.assertEquals("-800.76元", val);
		
		
	}
	
	@Test
	public void testHash(){
		String str = "hellohello_helloh_ellohello";
		System.out.println("hash: " + str.hashCode());
		boolean voted = false;
		System.out.println("voted: " + (voted |=true));
		voted = false;
		System.out.println("voted: " + (voted |=false));
		voted = false;
		System.out.println("voted: " + (voted &=true));
		voted = false;
		System.out.println("voted: " + (voted &=true));
		str = "ntyg168@163.com";
		System.out.println("substring: " + str.length());
	}
	@Test
	public void testTimeUnit(){
		long i = TimeUnit.MINUTES.toHours(59);
		System.out.println("unit: " + i);
		
		List<String> list = LangUtils.newArrayList("aa", "bb", "cc");
		List<String> sublist = list.subList(0, 2);
		System.out.println("sublist: " + sublist);
		
	}
	
	@Test
	public void testPrint(){
		LangUtils.println("test the ${0} and ${1}.", "test1", 2);
		LangUtils.printMemory();
		LangUtils.printMemory("kb");
	}
	
	@Test
	public void testPrintArgs(){
		LangUtils.printlnNamedArgs("test the ${test1} and ${test2}.", "test1", 2, "test2", "3adfas");
		LangUtils.printMemory("mb");
	}
	
	@Test
	public void testExceptionString(){
		Exception e = LangUtils.asBaseException("test exception");
		LangUtils.println("exception : ${0}", e);
	}
	
	
	@Test
	public void testAsList(){
		Object array = new Object[]{"1111", 222, "333"};
		List rs = LangUtils.asList(array);
		Assert.assertEquals(3, rs.size());
	}
	
	@Test
	public void testGetFirst(){
		Object array = new Object[]{"1111", 222, "333"};
		List rs = LangUtils.asList(array);
		Assert.assertEquals("1111", LangUtils.getFirst(rs));
	}
	
	@Test
	public void testRemoveCollection(){
		Object array = new Object[]{"1111", 222, "333", "4444"};
		List rs = LangUtils.asList(array);
		LangUtils.remove(rs, 1, 3);
		Assert.assertEquals(2, rs.size());
		Assert.assertEquals("1111", LangUtils.getFirst(rs));
		Assert.assertEquals("4444", rs.get(rs.size()-1));
	}
	
	
	
	@Test
	public void testConvert(){
		Integer sint = 3;
		Boolean sbool = true;
		Long slong = Long.MAX_VALUE;
		Float sfloat = Float.MAX_VALUE;
		List<String> list = LangUtils.asList(new String[]{"aaa", "bbb", "ccc"});
		Assert.assertEquals(sint, LangUtils.strCastTo(sint+"", Integer.class));
		Assert.assertEquals(sbool, LangUtils.strCastTo(sbool+"", Boolean.class));
		Assert.assertEquals(slong, LangUtils.strCastTo(slong+"", Long.class));
		Assert.assertEquals(sfloat, LangUtils.strCastTo(sfloat+"", Float.class));
		Assert.assertEquals(list, LangUtils.strCastTo(StringUtils.join(list, ","), List.class));
	}
	
	@Test
	public void testGetCause(){
		RuntimeException f = new RuntimeException("runtime error");
		BaseException e = new BaseException("1 error", new BaseException("2 error", f));
		Throwable t = LangUtils.getFinalCauseException(e);
		Assert.assertFalse(e.equals(t));
		Assert.assertEquals(f, t);
	}
	
	@Test
	public void testStrCastTo(){
		String str = "2012-9-7";
		Date date = LangUtils.strCastTo(str, Date.class);
		LangUtils.println("date: ${0}", date);
		str = "22:22:22";
		date = LangUtils.strCastTo(str, Date.class);
		LangUtils.println("date: ${0}", date);
	}
	
	@Test
	public void testGenerateToken(){
		String token2 = Hashs.MD5.hash("wayshall");
		System.out.println("token:" + LangUtils.generateToken("wayshall"));
	}
	
	@Test
	public void testHex(){
		String str = "北";
		byte[] bytes = str.getBytes();
		char[] chars = new char[]{0xE5, 0x8C, 0x97};
		String hexstr = LangUtils.toHex(bytes);
		System.out.println("hexstr: " + hexstr);
		
		bytes = LangUtils.hex2Bytes(hexstr);
		System.out.println("hex2Bytes: " + new String(bytes));
	}

	@Test
	public void testReg(){
		Assert.assertTrue(LangUtils.isWord("word"));
		Assert.assertTrue(LangUtils.isWord("hello_word"));
		Assert.assertTrue(LangUtils.isWord("hello_word_2"));
		Assert.assertFalse(LangUtils.isWord("hello@word"));
		Assert.assertFalse(LangUtils.isWord("中文"));
	}

	@Test
	public void testGetCauseServiceException(){
		ServiceException se = new ServiceException("test service");
		Exception re = new RuntimeException(se);
		Throwable e = LangUtils.getCauseServiceException(re);
		Assert.assertTrue(ServiceException.class.isInstance(e));
	}

	@Test
	public void testFixedLengthString(){
		int length = 12;
		String str = "12";
		String rs = LangUtils.fixedLengthString(str, length, "0");
		Assert.assertEquals("000000000012", rs);
		str = "10000000000000012";
		rs = LangUtils.fixedLengthString(str, length, "0");
		Assert.assertEquals("000000000012", rs);
		str = null;
		rs = LangUtils.fixedLengthString(str, length, "0");
		Assert.assertEquals("000000000000", rs);
	}
	
	@Test
	public void testDataFormat(){
		double value = 2002.2456;
		String rs = (String)LangUtils.formatValue(value, "#0.0#");
		Assert.assertEquals(String.valueOf(2002.25), rs);

		value = 2002.2;
		rs = (String)LangUtils.formatValue(value, "#0.0#");
		Assert.assertEquals(String.valueOf(2002.20), rs);

		Integer intValue = 2002;
		rs = (String)LangUtils.formatValue(intValue, "#0.0#");
		Assert.assertEquals(String.valueOf(2002.00), rs);

		value = 0.525;
		rs = (String)LangUtils.formatValue(value, "#0.0#");
		Assert.assertEquals(String.valueOf(0.53), rs);
		
		value = 0.775;
		rs = (String)LangUtils.formatValue(value, "#0.0#");
		Assert.assertEquals(String.valueOf(0.78), rs);
		
		value = 0.001;
		rs = (String)LangUtils.formatValue(value, "#0.0#");
		Assert.assertEquals(String.valueOf(0.00), rs);
		
		value = 0.775;
		rs = (String)LangUtils.formatValue(value, "#0.00#");
		Assert.assertEquals(String.valueOf(0.775), rs);
	}
	
	@Test
	public void test10to16(){
		String cardNo10 = "6124895493223875970";
		System.out.println("cardno: " + cardNo10.length());
		Long max = Long.MAX_VALUE;
//		Assert.assertEquals(expected, actual);
		System.out.println("max: "+max+", length:" + max.toString().length());
		String cardNo16 = LangUtils.decToHexString(cardNo10);
		System.out.println("cardNo16: " + cardNo16);
	}
	
	@Test
	public void test16to10(){
		String cardNo16 = "5500000000000582";
		System.out.println("cardNo16: " + cardNo16.length());
		Long max = Long.MAX_VALUE;
		System.out.println("max: "+max+", length:" + max.toString().length());
		String cardNo10 = Long.toOctalString(Types.convertValue(cardNo16, Long.class));
		System.out.println("cardNo10: " + cardNo10);
	}
	
	@Test
	public void testEncodeUrl() throws Exception{
		String url = "aa=#user";
		String encodeUrl = LangUtils.encodeUrl(url);
		Assert.assertEquals("aa%3D%23user", encodeUrl);
	}
	
	@Test
	public void testEquals() throws Exception{
		Assert.assertTrue(LangUtils.equals(1, 1));
		Assert.assertTrue(LangUtils.equalsIgnoreCase("aabbb", "AabBb"));
	}
	


	@Test
	public void testDigit(){
		String str = "123";
		Assert.assertTrue(LangUtils.isDigitString(str));
		str = "0";
		Assert.assertTrue(LangUtils.isDigitString(str));
		str = "3";
		Assert.assertTrue(LangUtils.isDigitString(str));
		str = "003";
		Assert.assertTrue(LangUtils.isDigitString(str));
		str = "a3";
		Assert.assertFalse(LangUtils.isDigitString(str));
		str = "3ssd";
		Assert.assertFalse(LangUtils.isDigitString(str));
		str = "34 33";
		Assert.assertFalse(LangUtils.isDigitString(str));
	}
	@Test
	public void testHasElement(){
		Assert.assertTrue(LangUtils.isEmpty(Collections.EMPTY_MAP));
		Assert.assertTrue(LangUtils.isEmpty(Collections.EMPTY_LIST));
		Assert.assertTrue(LangUtils.isEmpty(Collections.EMPTY_SET));
		
		Assert.assertFalse(LangUtils.hasElement(Collections.EMPTY_MAP));
		Assert.assertFalse(LangUtils.hasElement(Collections.EMPTY_LIST));
		Assert.assertFalse(LangUtils.hasElement(Collections.EMPTY_SET));
		
		int[] ints = new int[]{};
		Assert.assertTrue(LangUtils.isEmpty(ints));
		Assert.assertFalse(LangUtils.hasElement(ints));
		ints = new int[]{0};
		Assert.assertTrue(LangUtils.hasElement(ints));
		
		Object obj = new Object[]{"aaa", Long.valueOf(1L)};
		Assert.assertFalse(LangUtils.isEmpty(obj));
		Assert.assertTrue(LangUtils.hasElement(obj));
		obj = new Object[]{null, null};
		Assert.assertFalse(LangUtils.isEmpty(obj));
		Assert.assertTrue(LangUtils.hasNotElement(obj));
	}
	
	@Test
	public void testBlank(){
		Assert.assertFalse(Iterable.class.isInstance(new Object[]{1, ""}));

		Assert.assertFalse(LangUtils.isBlank(new Object[]{null, "", null}));
		Assert.assertFalse(LangUtils.isBlank(new Object[]{null, 1, "", null}));
		Assert.assertTrue(LangUtils.isBlank(new Object[]{null, null}));

		Assert.assertTrue(LangUtils.isBlank(LangUtils.newArrayList()));
		Assert.assertFalse(LangUtils.isBlank(LangUtils.newArrayList("")));
		Assert.assertFalse(LangUtils.isBlank(LangUtils.newArrayList("111")));
		List<?> list = JFishList.create();
		list.add(null);
		Assert.assertTrue(LangUtils.isBlank(list));
	}
	
	@Test
	public void testPad(){
		String str = "test";
		String res = LangUtils.padLeft(str, 7, "1");
		Assert.assertEquals("111test", res);
		
		res = LangUtils.padRight(str, 7, "2");
		Assert.assertEquals("test222", res);
	}
	public String padRight(String s, int n) {
	     return String.format("%1$-20s", s);  
	}

	public String padLeft(String s, int n) {
	    return String.format("%1$20s", s);  
	}

	@Test
	public void testPad2() {
	 System.out.println(padRight("Howto", 20) + "*");
	 System.out.println(padLeft("Howto", 20) + "*");
	}
	
	@Test
	public void testFormat(){
		String label = "订单满%s元立减%s元";
		String res = String.format(label, 100, 50, 30);
		System.out.println("res:"+res);
	}
	
	@Test
	public void testRandomString(){
		String key = RandomStringUtils.randomAscii(128);
		System.out.println("key:" + key);
		key = RandomStringUtils.randomAlphanumeric(32);
		System.out.println("key:" + key);
	}
}

