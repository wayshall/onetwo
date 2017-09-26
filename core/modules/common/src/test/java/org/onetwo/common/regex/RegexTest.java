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
		Pattern navPathPattern = Pattern.compile("^/(19|dongbei)/cms/v2/api-docs$");
		Matcher matcher = navPathPattern.matcher("/dongbei/cms/v2/api-docs");
		if(matcher.matches()){
			int count = matcher.groupCount();
			for (int i = 0; i <= count; i++) {
				String val = matcher.group(i);
				System.out.println("val_"+i+": "+val);
			}
		}
	}

}
