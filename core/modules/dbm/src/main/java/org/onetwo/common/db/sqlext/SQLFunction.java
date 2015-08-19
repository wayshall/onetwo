package org.onetwo.common.db.sqlext;


public interface SQLFunction {

	public String render(Object...args);

	public boolean hasArguments();

	public boolean hasParenthesesIfNoArguments();

}