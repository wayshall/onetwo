package org.onetwo.common.utils;

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
	
}
