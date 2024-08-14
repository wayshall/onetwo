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
		
		Pattern navPathPattern = Pattern.compile("^/apidoc/(.+)$");
		Matcher matcher = navPathPattern.matcher("/apidoc/campus-main/api/common/home/getIndexResource");
		if(matcher.matches()){
			int count = matcher.groupCount();
			for (int i = 0; i <= count; i++) {
				String val = matcher.group(i);
				System.out.println("val_"+i+": "+val);
			}
		}
		
		System.out.println("---------swagger");
		navPathPattern = Pattern.compile("^/([\\w\\-]+)/swagger.+$");
		matcher = navPathPattern.matcher("/iotgateway-bff/swagger-ui/swagger-ui.css.map");
		if(matcher.matches()){
			int count = matcher.groupCount();
			for (int i = 0; i <= count; i++) {
				String val = matcher.group(i);
				System.out.println("val_"+i+": "+val);
			}
		} else {
			System.out.println("swagger no match");
		}
	}

}
