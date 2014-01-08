package org.onetwo.common.utils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.common.utils.list.L;

public class LangUtilsTest {
	
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
	public void testBeanOrder(){
		List list = L.aslist(new User(), new Role());
		LangUtils.asc(list);
		System.out.println(list.get(0));
		Assert.assertEquals(Role.class, list.get(0).getClass());
		
		LangUtils.desc(list);
		System.out.println(list.get(0));
		Assert.assertEquals(User.class, list.get(0).getClass());
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
		MD5 md5 = new MD5();
		String token1 = md5.calcMD5("wayshall");
		String token2 = MDFactory.MD5.encrypt("wayshall");
		Assert.assertEquals(token1, token2);
		
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
}
