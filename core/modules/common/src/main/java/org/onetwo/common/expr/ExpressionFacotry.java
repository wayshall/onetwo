package org.onetwo.common.expr;

import java.util.Map;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.text.StrSubstitutor;

public class ExpressionFacotry {

	public static final Expression AT = newExpression("@{", "}");
	public static final Expression PERCENT = newExpression("%{", "}");
	public static final Expression DOLOR = newExpression("${", "}");
	public static final Expression WELL = newExpression("#{", "}");
	public static final Expression BRACE = newExpression("{", "}");

	public static Expression newExpression(String start, String end){
		return new SimpleExpression(start, end);
	}

	public static Expression newExpression(String start, String end, Object valueProvider){
		return new SimpleExpression(start, end, valueProvider);
	}
	public static Expression newExpression(String start, String end, String nullValue){
		return new SimpleExpression(start, end, null, nullValue);
	}
	/***
	 * apache StrSubstitutor
	 * @author wayshall
	 * @param start
	 * @param end
	 * @return
	 */
	public static <V> StrSubstitutor newStrSubstitutor(String start, String end, final Map<String, V> valueMap){
		return newStrSubstitutor(start, end, start.charAt(0), valueMap);
	}
	public static <V> StrSubstitutor newStrSubstitutor(String start, String end, char escape, final Map<String, V> valueMap){
		StrSubstitutor substitutor = new StrSubstitutor(StrLookup.mapLookup(valueMap), StrMatcher.stringMatcher(start), StrMatcher.stringMatcher(end), escape);
		return substitutor;
	}
	
}
