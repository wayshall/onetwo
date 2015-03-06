package org.onetwo.common.excel.etemplate;

import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;

public class RowForeachDirectiveTest {
	
	@Test
	public void testDirectivePattern(){
		String directive = "[row:list #dataList as data]";
		Matcher matcher = RowForeachDirective.PATTERN_START.matcher(directive);
		Assert.assertTrue(matcher.matches());
		String ds = matcher.group(1);
		Assert.assertEquals("#dataList", ds);
		String var = matcher.group(2);
		Assert.assertEquals("data", var);

		directive = "[row:list #data-List as data]";
		matcher = RowForeachDirective.PATTERN_START.matcher(directive);
		Assert.assertFalse(matcher.matches());
		
		directive = "[row:list data#List as data]";
		matcher = RowForeachDirective.PATTERN_START.matcher(directive);
		Assert.assertFalse(matcher.matches());
		
		directive = "[row:list dataList as data, index  ]";
		matcher = RowForeachDirective.PATTERN_START.matcher(directive);
		Assert.assertTrue(matcher.matches());
		Assert.assertTrue(matcher.groupCount()==4);
		System.out.println("var: " + matcher.group(4));
		
		directive = "[/row:list]";
		matcher = RowForeachDirective.PATTERN_END.matcher(directive);
		Assert.assertTrue(matcher.matches());

		directive = "[/row:list ]";
		matcher = RowForeachDirective.PATTERN_END.matcher(directive);
		Assert.assertTrue(matcher.matches());

		directive = "[/ row:list]";
		matcher = RowForeachDirective.PATTERN_END.matcher(directive);
		Assert.assertFalse(matcher.matches());

		directive = "[row:list]";
		matcher = RowForeachDirective.PATTERN_END.matcher(directive);
		Assert.assertFalse(matcher.matches());
	}
	
	@Test
	public void testDirectiveModel(){
		String directive = "[row:list #dataList as data]";
		RowForeachDirective d = new RowForeachDirective();
		RowForeachDirectiveModel model = d.matchStartDirectiveText(directive);
		Assert.assertNotNull(model);
		Assert.assertEquals("#dataList", model.getDataSource());
		Assert.assertEquals("data", model.getItemVar());
	}

}
