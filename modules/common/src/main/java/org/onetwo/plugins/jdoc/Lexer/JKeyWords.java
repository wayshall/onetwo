package org.onetwo.plugins.jdoc.Lexer;

import java.util.List;
import java.util.Map;

import org.onetwo.common.lexer.LexerKeywords;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;

public class JKeyWords implements LexerKeywords<JToken>{
	
	public final static JKeyWords INSTANCE;
	static {
		INSTANCE = new JKeyWords();
		INSTANCE.addKeyWord(JToken.PACKAGE)
				.addKeyWord(JToken.CLASS)
				.addKeyWord(JToken.IMPORT)
				.addKeyWord(JToken.PUBLIC)
				.addKeyWord(JToken.PRIVATE)
				.addKeyWord(JToken.PROTECTED)
				.addKeyWord(JToken.STATIC)
				.addKeyWord(JToken.NULL)
				.addKeyWord(JToken.ABSTRACT)
				.addKeyWord(JToken.NULL)
				.addKeyWord(JToken.VOID)
				.addKeyWord(JToken.THROWS)
				.addKeyWord(JToken.FINAL);
	}
	
	private Map<String, JToken> keyWords = LangUtils.newMap();

	private JKeyWords(){}
	private JKeyWords(Map<String, JToken> kws){
		this.keyWords = kws;
	}
	
	private JKeyWords addKeyWord(JToken token){
		this.keyWords.put(token.getName(), token);
		return this;
	}
	
	public JToken getTokenByKeyWord(String kw){
		return this.keyWords.get(kw);
	}
	
	public boolean isKeyWord(JToken token){
		return this.keyWords.containsKey(token.getName());
	}


	@Override
	public JToken[] getKeyWordTokenAsArray(){
		return this.keyWords.values().toArray(new JToken[this.keyWords.size()]);
	}
	@Override
	public List<JToken> getKeyWordTokens(){
		return JFishList.wrap(this.keyWords.values());
	}
	
}
