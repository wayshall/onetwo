package org.onetwo.common.expr;

import org.onetwo.common.expr.SimpleExpression.Context;

public interface Expression {

	public static final Expression AT = ExpressionFacotry.AT;
	public static final Expression PERCENT = ExpressionFacotry.PERCENT;
	public static final Expression DOLOR = ExpressionFacotry.DOLOR;
	public static final Expression WELL = ExpressionFacotry.WELL;
	public static final Expression BRACE = ExpressionFacotry.BRACE;
	
	public boolean isExpresstion(String text);

//	public boolean isExpresstion();

	public String parse(String text);

	public String parse(String text, Object... objects);
//	public void setThrowIfVarNotfound(boolean throwIfVarNotfound);

//	public String parse(ValueProvider provider);

	public String parseByProvider(String text, Object provider);

//	public Context parseWithContext(String text, Object provider);
	public Context parseWithContext(String text, Object... objects);
//	public Integer getVarIndex(String var);

}