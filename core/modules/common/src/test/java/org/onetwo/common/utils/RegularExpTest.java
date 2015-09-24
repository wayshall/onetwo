package org.onetwo.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.Assert;

public class RegularExpTest {

	@Test
	public void testGroup() {
		/****
		 * \\(	括号
		 * (\\w+)	单词
		 * (
		 * 		?: 不捕获此括号
		 * 		\\|{2} 匹配||
		 * 		(\\w+) 匹配单词
		 * )*	重复0或多个
		 * \\)	括号
		 */
		Pattern pattern = Pattern.compile("\\((\\w+)(\\|{2}(\\w+))*\\)");
		String input = "findUserByStatus(NORMAL||DELETE)";
		Matcher matcher = pattern.matcher(input);

		/*while(matcher.find()){
			System.out.println("group:"+matcher.group());
		}*/
		
		/*matcher.find();
		for (int i = 0; i <= matcher.groupCount(); i++) {
			String group = matcher.group(i);
			System.out.println("group"+i+": " + group);
		}*/
		
		boolean rs = matcher.find();
		Assert.assertTrue(rs);
		Assert.assertEquals("(NORMAL||DELETE)", matcher.group(0));
		Assert.assertEquals("NORMAL", matcher.group(1));
		Assert.assertEquals("||DELETE", matcher.group(2));
		Assert.assertEquals("DELETE", matcher.group(3));
		
		input = "findUserByStatus(NORMAL||DELETE||)";
		matcher = pattern.matcher(input);
		rs = matcher.find();
		Assert.assertFalse(rs);
		

		input = "findUserByStatus(NORMAL||DELETE||TEST)";
		matcher = pattern.matcher(input);
		rs = matcher.find();
		Assert.assertTrue(rs);

		for (int i = 0; i <= matcher.groupCount(); i++) {
			String group = matcher.group(i);
			System.out.println("group"+i+": " + group);
		}
		
		Assert.assertEquals("(NORMAL||DELETE||TEST)", matcher.group(0));
		Assert.assertEquals("NORMAL", matcher.group(1));
//		Assert.assertEquals("DELETE", matcher.group(2));
		Assert.assertEquals("TEST", matcher.group(3));
		

		input = "findUserByStatus(NORMAL||DELETE||TEST)";
		matcher = pattern.matcher(input);
		StringBuffer str = new StringBuffer();
		while(matcher.find()){
			matcher.appendReplacement(str, "");
			System.out.println("str: " + matcher.group());
		}
		matcher.appendTail(str);

		Assert.assertEquals("findUserByStatus", str.toString());
	}

}
