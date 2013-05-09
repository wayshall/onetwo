package org.onetwo.plugins.jdoc.Lexer.parser;

import org.onetwo.common.lexer.JParser;
import org.onetwo.plugins.jdoc.Lexer.JToken;
import org.onetwo.plugins.jdoc.Lexer.defined.JDefinedImpl;

public interface DefinedParser extends JParser<JDefinedImpl> {
	
	public boolean isDefinedMatch(JToken token);

}
