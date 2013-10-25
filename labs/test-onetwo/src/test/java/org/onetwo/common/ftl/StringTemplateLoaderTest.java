package org.onetwo.common.ftl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.spring.ftl.FtlUtils;
import org.onetwo.common.spring.ftl.ForeachDirective;
import org.onetwo.common.utils.LangUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class StringTemplateLoaderTest {
	Configuration cfg = new Configuration();
	
	@Before
	public void before() throws Exception{
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		this.cfg.setObjectWrapper(FtlUtils.BEAN_WRAPPER);
		Map<String, Object> freemarkerVariables = LangUtils.newHashMap();
		freemarkerVariables.put(ForeachDirective.DIRECTIVE_NAME, new ForeachDirective());
		cfg.setAllSharedVariables(new SimpleHash(freemarkerVariables, cfg.getObjectWrapper()));
	}
	
	@Test
	public void testStringLoader() throws Exception{
		StringTemplateLoader st = new StringTemplateLoader();
		String tname = "hello";
		st.putTemplate(tname, "hello：${user} [#if user?has_content]test[/#if]");
		cfg.setTemplateLoader(st); 
		cfg.setDefaultEncoding("UTF-8"); 

		Template template = cfg.getTemplate(tname); 

		Map root = new HashMap(); 
		root.put("user", "lunzi"); 

		StringWriter writer = new StringWriter(); 
		template.process(root, writer); 
		System.out.println(writer.toString()); 
		Assert.assertEquals("hello：lunzi test", writer.toString());
	}

	
	@Test
	public void testJoin() throws Exception{
		StringTemplateLoader st = new StringTemplateLoader();
		String tname = "hello";
		String content = 
"hello：[@foreach list=datas separator='union '; val, index]" +
		"select from ${val} " +
		"[/@foreach]";
		st.putTemplate(tname, content);
		cfg.setTemplateLoader(st); 
		cfg.setDefaultEncoding("UTF-8"); 

		Template template = cfg.getTemplate(tname); 

		Map root = new HashMap(); 
		root.put("user", "lunzi"); 
		root.put("datas", LangUtils.newArrayList("aa", "bb")); 

		StringWriter writer = new StringWriter(); 
		template.process(root, writer); 
		System.out.println(writer.toString()); 
		Assert.assertEquals("hello：select from aa union select from bb ", writer.toString());
	}

}
