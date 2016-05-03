package org.onetwo.common.excel.etemplate.directive;

import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;

public class IfDirectiveTest {
	
	@Test
	public void testDirectivePattern(){
		IfRowDirective ifDirective = new IfRowDirective();
		String directive = "[if index%2==0]";
		Matcher matcher = ifDirective.getStartTag().matcher(directive);
		Assert.assertTrue(matcher.matches());
		System.out.println("groupCount: " + matcher.groupCount());
		System.out.println("groupCount: " + matcher.group(1));
		String condition = matcher.group(1);
		Assert.assertEquals("index%2==0", condition);

		directive = "[if index+2==0  ]";
		matcher = ifDirective.getStartTag().matcher(directive);
		Assert.assertTrue(matcher.matches());
		Assert.assertTrue(matcher.groupCount()==1);
		System.out.println("condition: " + matcher.group(1));
		condition = matcher.group(1).trim();
		Assert.assertEquals("index+2==0", condition);
		
		directive = "[/if]";
		matcher = ifDirective.getEndTag().matcher(directive);
		Assert.assertTrue(matcher.matches());

		directive = "[/if ]";
		matcher = ifDirective.getEndTag().matcher(directive);
		Assert.assertTrue(matcher.matches());

		directive = "[/ if]";
		matcher = ifDirective.getEndTag().matcher(directive);
		Assert.assertFalse(matcher.matches());

		directive = "[if]";
		matcher = ifDirective.getEndTag().matcher(directive);
		Assert.assertFalse(matcher.matches());
	}
	
}
