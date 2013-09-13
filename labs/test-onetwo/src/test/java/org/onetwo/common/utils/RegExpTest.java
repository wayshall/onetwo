package org.onetwo.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegExpTest {
	
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

}
