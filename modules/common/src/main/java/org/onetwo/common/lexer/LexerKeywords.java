package org.onetwo.common.lexer;

import java.util.Collection;


public interface LexerKeywords<T> {

	public T[] getKeyWordTokenAsArray();

	public Collection<T> getKeyWordTokens();
	
	public T getTokenByKeyWord(String kw);

	public boolean isKeyWord(T token);

}