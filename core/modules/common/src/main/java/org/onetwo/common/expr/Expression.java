package org.onetwo.common.expr;

import org.onetwo.common.expr.SimpleExpression.Context;

public interface Expression {

	/*public static final Expression AT = ExpressionFacotry.AT;
	public static final Expression PERCENT = ExpressionFacotry.PERCENT;
	public static final Expression DOLOR = ExpressionFacotry.DOLOR;
	public static final Expression WELL = ExpressionFacotry.WELL;
	public static final Expression BRACE = ExpressionFacotry.BRACE;*/
	
	/****
	 * 是否是表达式：由属性和非属性字符串组成
	 * 如：aaaa{property}
	 */
	boolean isExpresstion(String text);
	
	/****
	 * 是否是属性：即整个字符串刚好由start字符和end字符包住，如：{property}
	 * @author weishao zeng
	 * @param text
	 * @return
	 */
	boolean isProperty(String text);

//	public boolean isExpresstion();

	public String parse(String text);

	public String parse(String text, Object... objects);
//	public void setThrowIfVarNotfound(boolean throwIfVarNotfound);

	public String parse(String text, ValueProvider provider);

	public String parseByProvider(String text, Object provider);

//	public Context parseWithContext(String text, Object provider);
	public Context parseWithContext(String text, Object... objects);
//	public Integer getVarIndex(String var);

}