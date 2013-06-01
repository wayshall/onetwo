package org.onetwo.plugins.jdoc.Lexer.parser;

import org.onetwo.plugins.jdoc.Lexer.JLexer;
import org.onetwo.plugins.jdoc.Lexer.JToken;
import org.onetwo.plugins.jdoc.Lexer.defined.JDefinedImpl;

abstract public class SemiParser extends AbstractDefinedParser{

	protected final JToken definedType;
	
	public SemiParser(JLexer lexer, JToken definedType) {
		super(lexer, definedType);
		this.definedType = definedType;
	}

	@Override
	public JDefinedImpl parse(JDefinedImpl parent) {
		StringBuilder name = new StringBuilder();
		while (this.lexer.nextToken()) {
			if (this.lexer.getToken() == JToken.SEMI)
				break;
			name.append(this.lexer.getStringValue());
		}
		return newJDefined(definedType, name.toString());
	}
	
	abstract protected JDefinedImpl newJDefined(JToken definedType, String str);

}
