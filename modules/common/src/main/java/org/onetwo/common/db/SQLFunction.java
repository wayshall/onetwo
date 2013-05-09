package org.onetwo.common.db;


public interface SQLFunction {

	public String render(Object...args);

	public boolean hasArguments();

	public boolean hasParenthesesIfNoArguments();

}