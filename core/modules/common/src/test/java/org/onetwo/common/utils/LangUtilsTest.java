package org.onetwo.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
	public void testIsChinese() {
		String str = "test";
		boolean res = LangUtils.isChinese(str);
		assertThat(res).isFalse();
		res = LangUtils.isChinese("中国人");
		assertThat(res).isTrue();
		res = LangUtils.isChinese("中国ren");
		assertThat(res).isFalse();
		res = LangUtils.isChinese("1个中国人");
		assertThat(res).isFalse();
//		System.out.println("res: " + "中国人".substring(0, 1));
	}

	@Test
	public void testRadixString() {
		String res = LangUtils.toRadixString(60, 60);
		assertThat(res).isEqualTo("10");
		res = LangUtils.toRadixString(121, 60);
		assertThat(res).isEqualTo("21");
		res = LangUtils.toRadixString(198263763892923L, 60);
		System.out.println("res: " + res);
		assertThat(res).isEqualTo("1aNsNTM23");
		res = LangUtils.toRadixString(198263763892928L, 60);
		System.out.println("res2: " + res);
		assertThat(res).isEqualTo("1aNsNTM28");
		res = LangUtils.toRadixString(Long.MAX_VALUE, 60);
		System.out.println("res3: " + res);
		assertThat(res).isEqualTo("ffdywvTkfu7");
		res = Long.toString(Long.MAX_VALUE, 36);
		System.out.println("res: " + res);
		assertThat(res).isEqualTo("1y2p0ij32e8e7");
	}

	@Test
	public void testJoinMap() {
		Map<String, String> map = CUtils.asLinkedMap("test1", "1", "test2", 2);
		String res = StringUtils.join(map, ":", "-");
		System.out.println("res: " + res);
		assertThat(res).isEqualTo("test1:1-test2:2");
		System.out.println("res: " + System.currentTimeMillis());
	}

	@Test
	public void testEllipsis() {
		String str = "test";
		String res = LangUtils.ellipsis(str, 10);
		assertThat(res).isEqualTo(str);
		
		str = null;
		res = LangUtils.ellipsis(str, 10);
		assertThat(res).isEqualTo(str);
		
		str = "12345676910";
		res = LangUtils.ellipsis(str, 10);
		assertThat(res).isEqualTo("1234567...");
		
		str = "一二三四五六七八九十十一";
		res = LangUtils.ellipsis(str, 10);
		assertThat(res).isEqualTo("一二三四五六七...");
	}
	
	@Test
	public void testReplace() {
		int val = 127;
		int val2 = 0xFF;
		System.out.println("val:" +val+", " + Integer.toHexString(val)+", " + Integer.toBinaryString(val));
		System.out.println("val2:" +val2);
		String str = "waypc.mshome.net|8888";
		String[] strs = str.split("\\|");
		System.out.println("str: " + LangUtils.toString(strs));
		str = "127.0.0.1|8888";
		strs = str.split("|");
		System.out.println("str: " + LangUtils.toString(strs));
		
		String url = "aaa\\bb?cc*dd|ee\"ff";
		System.out.println("source: " + url);
		url = url.replaceAll("\\\\|:|\\*|\\?|\"|<|>|\\|", "");
		System.out.println("url: " + url);
		assertThat(url).isEqualTo("aaabbccddeeff");
	}
	
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
		//这个断言在jdk8之前的版本是可以通过的，jdk8后无法通过，详见：https://stackoverflow.com/questions/30778927/roundingmode-half-down-issue-in-java8
		// 实际上和小数无法精确表示有关，-800.755的ieee二进制表示是一个近似值-800.75499999999之类，即第三位之后其实少于5，所以HALF_UP后，应该是900.755
//		Assert.assertEquals("-800.76", val);
		Assert.assertEquals("-800.75", val);

		val = LangUtils.formatValue(-800.755, "#0.00元");
		//这个断言在jdk8之前的版本是可以通过的，jdk8后无法通过，详见：https://stackoverflow.com/questions/30778927/roundingmode-half-down-issue-in-java8
//		Assert.assertEquals("-800.76元", val);
		Assert.assertEquals("-800.75元", val);
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
		assertThat(i).isEqualTo(0L);
		
		i = TimeUnit.MINUTES.toHours(119);
		System.out.println("unit: " + i);
		assertThat(i).isEqualTo(1L);
		
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
	public void test10to2(){
		short numb = 2605;
		System.out.println("numb: " + Integer.toBinaryString(numb));
//		byte h8 = (byte)((numb & 0xFF00) >> 8);
		byte h8 = LangUtils.high8(numb);
		System.out.println("numb1: " + Integer.toBinaryString(numb & 0xFF00));
		System.out.println("numb2: " + Integer.toBinaryString((numb & 0xFF00) >> 8));
//		byte l8 = (byte)(numb & 0x00FF);
		byte l8 = LangUtils.low8(numb);
		System.out.println("h8: " + h8);
		System.out.println("l8: " + l8);
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
	public void testVersion(){
		String str = "123";
		Assert.assertTrue(LangUtils.isVersionString(str));
		str = "0.1.1";
		Assert.assertTrue(LangUtils.isVersionString(str));
		str = "3.0.1";
		Assert.assertTrue(LangUtils.isVersionString(str));
		str = "0.03";
		Assert.assertTrue(LangUtils.isVersionString(str));
		str = "2.1.0";
		Assert.assertTrue(LangUtils.isVersionString(str));
		str = "3ssd";
		Assert.assertFalse(LangUtils.isVersionString(str));
		str = "34 33";
		Assert.assertFalse(LangUtils.isVersionString(str));
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
	public void testSensitive() {
	 String name = "李建国";
	 
	 String unsensitive = LangUtils.sensitive(name, 1);
	 assertThat(unsensitive).isEqualTo("李**");
	 unsensitive = LangUtils.sensitive(name, 4);
	 assertThat(unsensitive).isEqualTo("李建国");
	 
	 unsensitive = LangUtils.sensitive(name, -1);
	 assertThat(unsensitive).isEqualTo("**国");
	 unsensitive = LangUtils.sensitive(name, 4);
	 assertThat(unsensitive).isEqualTo("李建国");
	 
	 name = "13666676666";
	 unsensitive = LangUtils.sensitive(name, 7);
	 assertThat(unsensitive).isEqualTo("1366667****");
	 
	 unsensitive = LangUtils.sensitive(name, -4);
	 assertThat(unsensitive).isEqualTo("*******6666");
	}
	
	@Test
	public void testRandomString(){
		String key = "key"+RandomStringUtils.randomAscii(128).replaceAll("\\s|#|:|\\*|-|!|&|%|\"", "");
		System.out.println("key:" + key);
		key = "sport-"+RandomStringUtils.randomAlphanumeric(128);
		System.out.println("key:" + key);
		key = RandomStringUtils.randomAlphanumeric(32);
		System.out.println("key:" + key);
	}
	
	@Test
	public void testGetCRC32() {
		String data = "测试一下阿斯顿发了水电费开始的";
		long value = LangUtils.getCrc32(data);
		System.out.println(Long.toString(value, 36));
		assertThat(value).isEqualTo(4286944383L);
	}
	
	@Test
	public void testGetNumber() {
		String data = "测试一下阿123斯顿6798发了水电费0432开始的";
		List<String> dataList = LangUtils.getNumbersFromString(data);
		System.out.println("dataList: " + dataList);
		assertThat(dataList).size().isEqualTo(3);
		
		Integer value = LangUtils.getNumberFromString(data);
		System.out.println("val: " + value);
		assertThat(value).isEqualTo(123);
		value = LangUtils.getNumberFromString(data, -1);
		System.out.println("val: " + value);
		assertThat(value).isEqualTo(432);
	}
	
}

