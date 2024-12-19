package org.onetwo.common.spring.ftl;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

public class StringTemplateConfigurerTest {
	StringTemplateConfigurer config = new StringTemplateConfigurer();
	
	@Test
	public void testString() {
		String template = "test ${a}";
		Map<String, Object> context = Maps.newHashMap();
		context.put("a", "aaaa");
		String res = config.parse(template, context);
		System.out.println("res: " + res);
	}

}
