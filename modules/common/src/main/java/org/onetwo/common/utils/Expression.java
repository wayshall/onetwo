package org.onetwo.common.utils;

import org.onetwo.common.utils.SimpleExpression.Context;

public interface Expression {

	public static final Expression AT = new SimpleExpression("@{", "}");
	public static final Expression PERCENT = new SimpleExpression("%{", "}");
	public static final Expression DOLOR = new SimpleExpression("${", "}");
	public static final Expression WELL = new SimpleExpression("#{", "}");
	public static final Expression BRACE = new SimpleExpression("{", "}");
	
	public boolean isExpresstion(String text);

//	public boolean isExpresstion();

	public String parse(String text);

	public String parse(String text, Object... objects);
	public void setThrowIfVarNotfound(boolean throwIfVarNotfound);

//	public String parse(ValueProvider provider);

	public String parseByProvider(String text, Object provider);

	public Context parseWithContext(String text, Object provider);
	public Context parseWithContext(String text, Object... objects);
//	public Integer getVarIndex(String var);

}