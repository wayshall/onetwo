package org.onetwo.common.spring.ftl;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

public class FtlTest {
	
	TemplateParser templateParser = new DefaultTemplateParser(NameIsTemplateConfigurer.INSTANCE);
	
	@Test
	public void test() {
		String template = "test ${a}";
		Map<String, Object> context = Maps.newHashMap();
		context.put("a", "aaaa");
		String res = templateParser.parse(template, context);
		System.out.println("res: " + res);
	}

}
