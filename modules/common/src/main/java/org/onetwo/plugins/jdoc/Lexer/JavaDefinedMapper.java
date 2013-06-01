package org.onetwo.plugins.jdoc.Lexer;

import org.onetwo.plugins.jdoc.Lexer.defined.JavaClassDefineImpl;

public interface JavaDefinedMapper<T> {

	public T map(JavaClassDefineImpl classDefined);
}
