package org.onetwo.common.ftl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.TestUtils;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.spring.ftl.DateRangeDirective;
import org.onetwo.common.spring.ftl.ForeachDirective;
import org.onetwo.common.spring.ftl.FtlUtils;
import org.onetwo.common.spring.sql.ParserContext;
import org.onetwo.common.spring.sql.SqlFunctionFactory;
import org.onetwo.common.utils.LangUtils;

import test.entity.UserEntity;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class FreemarkerStringTemplateLoaderTest {
	Configuration cfg = new Configuration();
	
	@Before
	public void before() throws Exception{
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		this.cfg.setObjectWrapper(FtlUtils.BEAN_WRAPPER);
		Map<String, Object> freemarkerVariables = LangUtils.newHashMap();
		freemarkerVariables.put(ForeachDirective.DIRECTIVE_NAME, new ForeachDirective());
		freemarkerVariables.put(DateRangeDirective.DIRECTIVE_NAME, new DateRangeDirective());
		cfg.setAllSharedVariables(new SimpleHash(freemarkerVariables, cfg.getObjectWrapper()));
	}
	
	@Test
	public void testStringLoader() throws Exception{
		StringTemplateLoader st = new StringTemplateLoader();
		String tname = "hello";
		st.putTemplate(tname, "hello：${user} [#if user?has_content]test[/#if]");
		cfg.setTemplateLoader(st); 
		cfg.setDefaultEncoding("UTF-8"); 
		

		Map share = new HashMap(); 
		share.put("shareProperty", "shareValue"); 
		UserEntity userShare = TestUtils.createUser("share User name", 11);
		share.put("shareUser", userShare); 
		cfg.setSharedVariable("share", share);

		Template template = cfg.getTemplate(tname); 

		Map root = new HashMap(); 
		root.put("user", "lunzi"); 

		StringWriter writer = new StringWriter(); 
		template.process(root, writer); 
		System.out.println(writer.toString()); 
		Assert.assertEquals("hello：lunzi test", writer.toString());

		
		st.putTemplate("template1", "hello：${user1.userName}, p:${share.shareProperty}, age:${share.shareUser.age}");
		template = cfg.getTemplate("template1"); 
		UserEntity user = TestUtils.createUser("way", 11);
		root.put("user1", user); 
		writer = new StringWriter(); 
		template.process(root, writer); 
		System.out.println(writer.toString()); 
		Assert.assertEquals("hello：way, p:shareValue, age:11", writer.toString());
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
	

	@Test
	public void testDateRange() throws Exception{
		StringTemplateLoader st = new StringTemplateLoader();
		String tname = "hello";
		String content = 
		"[@dateRange from='2014-10-01' to='2014-10-03' joiner='union ' ; val, index]" +
		"select from ${val?string('yyyyMMdd')} " +
		"[/@dateRange]";
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
		Assert.assertEquals("select from 20141001 union select from 20141002 ", writer.toString());
		
	}


	@Test
	public void testDateRange2() throws Exception{
		StringTemplateLoader st = new StringTemplateLoader();
		String tname = "hello";
		String content = 
		"[@dateRange from='2014-08-29' to='2014-10-03' includeEnd=true type='month' joiner='union ' format='yyyyMM'; val, index]" +
		"select from ${val?string('yyyyMM')} " +
		"[/@dateRange]";
		st.putTemplate(tname, content);
		cfg.setTemplateLoader(st); 
		cfg.setDefaultEncoding("UTF-8"); 

		Template template = cfg.getTemplate(tname); 

		Map root = new HashMap(); 
		StringWriter writer = new StringWriter(); 
		template.process(root, writer); 
		System.out.println(writer.toString()); 
		Assert.assertEquals("select from 201408 union select from 201409 union select from 201410 ", writer.toString());
		
	}

	@Test
	public void testComsterModel() throws Exception{
		StringTemplateLoader st = new StringTemplateLoader();
		String tname = "hello";
		st.putTemplate(tname, "hello：${_func.to_date(startDate, 'yyyy-MM-dd')}");
		cfg.setTemplateLoader(st); 
		cfg.setDefaultEncoding("UTF-8"); 

		Template template = cfg.getTemplate(tname); 

		ParserContext root = ParserContext.create();
		root.put(SqlFunctionFactory.CONTEXT_KEY, SqlFunctionFactory.getSqlFunctionDialet(DataBase.Oracle));
		root.put("startDate", "2014-01-19");
//		BeanModel m = new BeanModel(new OracleSqlFunctionDialet(), FtlUtils.BEAN_WRAPPER);
//		root.put("_func", new OracleSqlFunctionDialet()); 
		StringWriter writer = new StringWriter(); 
		template.process(root, writer); 
		System.out.println(writer.toString()); 
		Assert.assertEquals("hello：to_date('2014-01-19', 'yyyy-MM-dd')", writer.toString());
		

		root.put(SqlFunctionFactory.CONTEXT_KEY, SqlFunctionFactory.getSqlFunctionDialet(DataBase.Sqlserver));
		writer = new StringWriter(); 
		template.process(root, writer); 
		System.out.println(writer.toString()); 
		Assert.assertEquals("hello：convert(datetime, '2014-01-19')", writer.toString());

		
	}

}
