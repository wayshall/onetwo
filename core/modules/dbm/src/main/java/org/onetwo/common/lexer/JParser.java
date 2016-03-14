package org.onetwo.common.lexer;


public interface JParser<T> {

	public T parse(T parent);

}