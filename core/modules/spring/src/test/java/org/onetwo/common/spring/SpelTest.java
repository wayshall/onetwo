package org.onetwo.common.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import junit.framework.Assert;
import lombok.Data;

import org.junit.Test;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelTest {
	
	private SpelExpressionParser parser = new SpelExpressionParser();
	
	@Data
	public static class TestBean {
		private String aaa;
		private String IDCardNo;
		private String IDCardType;
		private String Name;
//		private String name;

	}
	
	@Test
	public void test1(){
		Intro<TestBean> intro = Intro.wrap(TestBean.class);
		intro.getProperties().forEach(p->{
			System.out.println("p:"+p);
		});
		TestBean test = new TestBean();
		BeanWrapper bw = SpringUtils.newBeanWrapper(test);
		bw.setPropertyValue("Name", "testName");
		assertThat(test.getName()).isEqualTo("testName");
		
		intro.setPropertyValue(test, "name", "testName2");
		assertThat(test.getName()).isEqualTo("testName2");
	}

	@Test
	public void test(){
		StandardEvaluationContext elcontext = new StandardEvaluationContext();
		TestBean tb = new TestBean();
		tb.aaa = "cccc";
		Map map = LangUtils.asMap("ccc", "dddd");
//		map.put("tb", tb);
		map.put("map", LangUtils.asMap("ccc", "dddd", "tb", tb));
		elcontext.setVariables(map);
		elcontext.setRootObject(tb);
		Expression exp = parser.parseExpression("'bb{ccc}'");
		Object val = (String)exp.getValue(elcontext, String.class);
		Assert.assertEquals("bb{ccc}", val);
		
		exp = parser.parseExpression("#ccc");
		val = (String)exp.getValue(elcontext, String.class);
		Assert.assertEquals("dddd", val);
		
		exp = parser.parseExpression("#map['tb'].aaa");
		val = (String)exp.getValue(elcontext, String.class);
		Assert.assertEquals("cccc", val);
	}

	@Test
	public void test2(){
		StandardEvaluationContext elcontext = new StandardEvaluationContext();
		TestBean tb = new TestBean();
		tb.aaa = "cccc";
		Map map = LangUtils.asMap("ccc", "dddd");
//		map.put("tb", tb);
		map.put("map", LangUtils.asMap("ccc", "dddd", "tb", tb));
		elcontext.setRootObject(map);
		Expression exp = parser.parseExpression("['ccc']");
		Object val = (String)exp.getValue(elcontext, String.class);
		System.out.println("val: " + val);
		Assert.assertEquals("dddd", val);
	}

}
