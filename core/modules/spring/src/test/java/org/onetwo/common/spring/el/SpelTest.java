package org.onetwo.common.spring.el;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelTest {
	
	private SpelExpressionParser parser = new SpelExpressionParser();
	
	public static class TestBean {
		private String aaa;

		public String getAaa() {
			return aaa;
		}

		public void setAaa(String aaa) {
			this.aaa = aaa;
		}
		
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
		Map map = LangUtils.asMap("ccc", "dddd", "userName", "testUser");
//		map.put("tb", tb);
		map.put("map", LangUtils.asMap("ccc", "dddd", "tb", tb));
		elcontext.setRootObject(map);
		Expression exp = parser.parseExpression("['ccc']");
		Object val = (String)exp.getValue(elcontext, String.class);
		System.out.println("val: " + val);
		Assert.assertEquals("dddd", val);

		elcontext.setVariables(map);
		elcontext.setRootObject(null);
		exp = parser.parseExpression("I am ${#userName}", PARSER_CONTEXT);
		val = (String)exp.getValue(elcontext, String.class);
		System.out.println("val: " + val);
		Assert.assertEquals("I am testUser", val);
	}
	
	private static final ParserContext PARSER_CONTEXT = new ParserContext() {

		@Override
		public String getExpressionPrefix() {
			return "${";
		}

		@Override
		public String getExpressionSuffix() {
			return "}";
		}

		@Override
		public boolean isTemplate() {
			return true;
		}
	};

}
