package org.onetwo.plugins.jdoc.Lexer;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.lexer.AbstractParser;
import org.onetwo.common.lexer.JParser;
import org.onetwo.plugins.jdoc.Lexer.defined.JDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.parser.DefinedParser;

abstract public class JSourceParser extends AbstractParser<JToken> implements JParser<JDefinedImpl>{

	public JSourceParser(JLexer lexer) {
		super(lexer);
	}

	private List<DefinedParser> defineParsers = new ArrayList<DefinedParser>();
	
	final public JSourceParser addDefineParser(DefinedParser parser){
		defineParsers.add(parser);
		return this;
	}
	
	final public JParser<JDefinedImpl> matchDefineParser(JToken token){
		for(DefinedParser p : defineParsers){
			if(p.isDefinedMatch(token))
				return p;
		}
		return null;
	}
	
	public JDefinedImpl reparse(JDefinedImpl parent){
		getLexer().reset();
		return parse(parent);
	}

	public JLexer getLexer() {
		return (JLexer)lexer;
	}
}
