package org.onetwo.ext.poi.excel.etemplate.directive;

import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.ext.poi.excel.etemplate.directive.ForeachRowDirective;
import org.onetwo.ext.poi.excel.etemplate.directive.ForeachRowDirectiveModel;

public class RowForeachDirectiveTest {
	
	@Test
	public void testDirectivePattern(){
		ForeachRowDirective foreach = new ForeachRowDirective();
		String directive = "[list #dataList as data]";
		Matcher matcher = foreach.getStartTag().matcher(directive);
		Assert.assertTrue(matcher.matches());
		String ds = matcher.group(1);
		Assert.assertEquals("#dataList", ds);
		String var = matcher.group(2);
		Assert.assertEquals("data", var);

		directive = "[list #data-List as data]";
		matcher = foreach.getStartTag().matcher(directive);
		Assert.assertFalse(matcher.matches());
		
		directive = "[list data#List as data]";
		matcher = foreach.getStartTag().matcher(directive);
		Assert.assertFalse(matcher.matches());
		
		directive = "[list dataList as data, index  ]";
		matcher = foreach.getStartTag().matcher(directive);
		Assert.assertTrue(matcher.matches());
		Assert.assertTrue(matcher.groupCount()==4);
		System.out.println("var: " + matcher.group(4));
		
		directive = "[/list]";
		matcher = foreach.getEndTag().matcher(directive);
		Assert.assertTrue(matcher.matches());

		directive = "[/list ]";
		matcher = foreach.getEndTag().matcher(directive);
		Assert.assertTrue(matcher.matches());

		directive = "[/ list]";
		matcher = foreach.getEndTag().matcher(directive);
		Assert.assertFalse(matcher.matches());

		directive = "[list]";
		matcher = foreach.getEndTag().matcher(directive);
		Assert.assertFalse(matcher.matches());
	}
	
	@Test
	public void testDirectiveModel(){
		String directive = "[list #dataList as data]";
		ForeachRowDirective d = new ForeachRowDirective();
		ForeachRowDirectiveModel model = d.createModel(directive);
		Assert.assertNotNull(model);
		Assert.assertEquals("#dataList", model.getDataSource());
		Assert.assertEquals("data", model.getItemVar());
	}

}
