package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {
	
	@Test
	public void testEllipsis(){
		String str = "我是测试字符串～我是测试字符串！";
		String newStr = StringUtils.ellipsis(str, 10, "……");
		Assert.assertEquals("我是测试字符串～我是……", newStr);
		
		str = "I am a test String~I am a test String!";
		newStr = StringUtils.ellipsis(str, 10, "……");
		Assert.assertEquals("I am a tes……", newStr);
		
		str = "I String!";
		newStr = StringUtils.ellipsis(str, 10, "……");
		Assert.assertEquals(str, newStr);
	}

	@Test
	public void testStr(){
		String str = "new,update|list";
		String[] strs = StringUtils.split(str, ",|");
		Assert.assertEquals(3, strs.length);
		Assert.assertEquals("new", strs[0]);
		Assert.assertEquals("update", strs[1]);
		Assert.assertEquals("list", strs[2]);
		
		str = "_update|list";
		strs = StringUtils.split(str, "_|");
		Assert.assertEquals(2, strs.length);
		Assert.assertEquals("update", strs[0]);
		Assert.assertEquals("list", strs[1]);
	}

	@Test
	public void testGetFirstWord(){
		String str = "saveUser";
		String rs = StringUtils.getFirstWord(str);
		Assert.assertEquals("save", rs);
		
		str = "updateUser";
		rs = StringUtils.getFirstWord(str);
		Assert.assertEquals("update", rs);
		
		str = " ";
		rs = StringUtils.getFirstWord(str);
		Assert.assertEquals("", rs);
	}

	
	@Test
	public void testReplace(){
		String str = "441827000";
		String rs = StringUtils.replaceEach(str, new String[]{"0"}, new String[]{""});
		System.out.println("str: " + rs);
		Assert.assertEquals("441827", rs);
		
		rs = StringUtils.replaceEach(str, new String[]{"0", "44"}, new String[]{"", "22"});
		System.out.println("str: " + rs);
		Assert.assertEquals("221827", rs);
	}

	
	@Test
	public void testTrimRight(){
		String str = "441827000";
		String rs = StringUtils.trimRight(str, "0");
		System.out.println("trimRight str: " + rs);
		Assert.assertEquals("441827", rs);
		
		str = "4418247000";
		rs = StringUtils.trimLeft(str, "4");
		System.out.println("trimLeft str: " + rs);
		Assert.assertEquals("18247000", rs);
		
		str = "0004418247000";
		rs = StringUtils.trim(str, "4");
		Assert.assertEquals("0004418247000", rs);

		rs = StringUtils.trim(str, "00");
		Assert.assertEquals("044182470", rs);
		
		rs = StringUtils.trim(str, "0");
		System.out.println("trim str: " + rs);
		Assert.assertEquals("4418247", rs);
		
	}
	
	@Test
	public void testSubString(){
		String str = "5A3A10000844 1000000844A6";
		String rs = StringUtils.substring(str, 4, 12);
		Assert.assertEquals("10000844", rs);
		
		str = "5A3A100008";
		rs = StringUtils.substring(str, 4, 12);
		Assert.assertEquals("100008", rs);
		
		str = "5A3";
		rs = StringUtils.substring(str, 4, 12);
		Assert.assertEquals("", rs);
	}

	
	@Test
	public void testBlank(){
		String str = "";
		Assert.assertTrue(StringUtils.isBlank(str));
		Assert.assertTrue(org.apache.commons.lang3.StringUtils.isBlank(str));
		
		str = "   ";
		Assert.assertTrue(StringUtils.isBlank(str));
		Assert.assertTrue(org.apache.commons.lang3.StringUtils.isBlank(str));
		
		str = "  \r ";
		Assert.assertTrue(StringUtils.isBlank(str));
		Assert.assertTrue(org.apache.commons.lang3.StringUtils.isBlank(str));
		
		str = "  \t ";
		Assert.assertTrue(StringUtils.isBlank(str));
		Assert.assertTrue(org.apache.commons.lang3.StringUtils.isBlank(str));
		
		str = "  \u005Cn ";
		Assert.assertTrue(StringUtils.isBlank(str));
		Assert.assertTrue(org.apache.commons.lang3.StringUtils.isBlank(str));
	}
}
