package org.onetwo.common.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author wayshall
 * <br/>
 */
public class RegexTest {
	
	@Test
	public void test(){
		Pattern navPathPattern = Pattern.compile("^/([\\w]+)/nav/.+");
		Matcher matcher = navPathPattern.matcher("/fudan/nav/swagger-ui.html");
		if(matcher.matches()){
			int count = matcher.groupCount();
			for (int i = 0; i <= count; i++) {
				String val = matcher.group(i);
				System.out.println("val_"+i+": "+val);
			}
		}
	}

}
