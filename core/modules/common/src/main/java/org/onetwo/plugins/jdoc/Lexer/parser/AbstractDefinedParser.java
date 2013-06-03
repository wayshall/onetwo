package org.onetwo.plugins.jdoc.Lexer.parser;

import java.util.List;

import org.onetwo.common.lexer.AbstractParser;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.plugins.jdoc.Lexer.JLexer;
import org.onetwo.plugins.jdoc.Lexer.JToken;

abstract public class AbstractDefinedParser extends AbstractParser<JToken> implements DefinedParser {
	
	protected JToken[] definedTokens;

	public AbstractDefinedParser(JLexer lexer, JToken... defineds) {
		super(lexer);
		this.definedTokens = defineds;
	}

	protected void ignoreBlock(){
		this.ignoreBlock(0);
	}

	protected void ignoreBlock(int count){
		this.ignoreBlock(count, JToken.LBRACE, JToken.RBRACE);
	}
	
	protected List<JToken> nextAllDefinedTokens(){
		return nextAllTheseTokens(definedTokens);
	}
	
	
	@Override
	public boolean isDefinedMatch(JToken token) {
		return ArrayUtils.contains(definedTokens, token);
	}
	//	protected boolean throwIfNextTokenIsNot(JToken token){
//		
//	}
//	
	
	
}
