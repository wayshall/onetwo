package org.onetwo.common.spring;

import java.util.Map;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.springframework.expression.Expression;
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
		elcontext.setVariables(map);
		elcontext.setRootObject(tb);
		Expression exp = parser.parseExpression("'bb{ccc}'");
		String val = (String)exp.getValue(elcontext, String.class);
		System.out.println("val: " + val);
	}

}
