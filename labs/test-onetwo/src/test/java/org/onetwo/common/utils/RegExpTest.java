package org.onetwo.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;

import org.junit.Test;

public class RegExpTest {
	
	private static final Pattern NESTED_PATH_PATTER = Pattern.compile("(\\[([a-z]+[\\w]+)\\])");
	
	@Test
	public void testName(){
		Pattern p = Pattern.compile(":\\)|:D");
		Matcher m = p.matcher("atestb:)asdf");
		if(m.find()){
			System.out.println("true");
		}else{
			System.out.println("false");
		}
	}
	
	@Test
	public void testList(){
		String str = "aa[ccc].bb";
		Matcher m = NESTED_PATH_PATTER.matcher(str);
		boolean rs = m.find();
		if (rs) {
			String newPath = m.replaceAll(".$2");
			System.out.println("newpath: " + newPath);
			Assert.assertTrue(newPath.equals("aa.ccc.bb"));
		}
	}

}
