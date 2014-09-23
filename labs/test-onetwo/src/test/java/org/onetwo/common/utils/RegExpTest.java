package org.onetwo.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;

import org.junit.Test;

public class RegExpTest {
	
	private static final Pattern NESTED_PATH_PATTER = Pattern.compile("(\\[([a-z]+[\\w]+)\\])");
	private static final Pattern TRANS_TABLES = Pattern.compile("(in_transaction|out_transaction)_(20[0-9]{2}[01]{1}[0-9]{1})", Pattern.CASE_INSENSITIVE);
	
	
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
	
	@Test
	public void testTransTable(){
		String str = "out_transaction_norepeate";
		Matcher m = TRANS_TABLES.matcher(str);
		Assert.assertFalse(m.matches());
		
		str = "out_transaction_201401";
		m = TRANS_TABLES.matcher(str);
		Assert.assertTrue(m.matches());
		
		str = "IN_TRANSACTION_201401";
		m = TRANS_TABLES.matcher(str);
		Assert.assertTrue(m.matches());
		

		Matcher matcher = TRANS_TABLES.matcher(str);
		Assert.assertTrue(matcher.find());
		Assert.assertEquals("IN_TRANSACTION_201401", matcher.group(0));
		Assert.assertEquals("201401", matcher.group(2));
		
		str = "aain_transaction_201401";
		m = TRANS_TABLES.matcher(str);
		Assert.assertFalse(m.matches());

		str = "in_transaction_201431";
		m = TRANS_TABLES.matcher(str);
		Assert.assertFalse(m.matches());
	}

}
