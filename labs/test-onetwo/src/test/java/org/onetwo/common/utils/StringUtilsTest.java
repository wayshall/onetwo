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
	
	
}
