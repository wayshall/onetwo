package org.onetwo.common.db.parser;

import java.util.List;
import java.util.Map;

import org.onetwo.common.lexer.LexerKeywords;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;

public class SqlKeywords implements LexerKeywords<SqlTokenKey> {
	
	public static enum SqlType {
		SELECT,
		UPDATE,
		INSERT,
		DELETE,
		OTHER;
	}

	public final static SqlKeywords KEYWORDS;
	public final static SqlKeywords SYMBOLS;
	public final static SqlKeywords OPERATORS;
	public final static SqlKeywords ALL;
	static {
		KEYWORDS = new SqlKeywords();
		KEYWORDS.addKeyWord(SqlTokenKey.DELETE)
				.addKeyWord(SqlTokenKey.INSERT)
				.addKeyWord(SqlTokenKey.SELECT)
				.addKeyWord(SqlTokenKey.VALUES)
				.addKeyWord(SqlTokenKey.FROM)
				.addKeyWord(SqlTokenKey.LEFT)
				.addKeyWord(SqlTokenKey.JOIN)
				.addKeyWord(SqlTokenKey.AS)
				.addKeyWord(SqlTokenKey.ON)
				.addKeyWord(SqlTokenKey.ORDER)
				.addKeyWord(SqlTokenKey.CASE)
				.addKeyWord(SqlTokenKey.WHEN)
				.addKeyWord(SqlTokenKey.THEN)
				.addKeyWord(SqlTokenKey.ELSE)
				.addKeyWord(SqlTokenKey.GROUP)
				.addKeyWord(SqlTokenKey.IF)
				.addKeyWord(SqlTokenKey.END)
				.addKeyWord(SqlTokenKey.HAVING)
				.addKeyWord(SqlTokenKey.NOT)
				.addKeyWord(SqlTokenKey.DISTINCT)
				.addKeyWord(SqlTokenKey.INTO)
				.addKeyWord(SqlTokenKey.NULL)
				.addKeyWord(SqlTokenKey.ASC)
				.addKeyWord(SqlTokenKey.DESC)
				.addKeyWord(SqlTokenKey.LIMIT)
				.addKeyWord(SqlTokenKey.WITH)
				.addKeyWord(SqlTokenKey.CONNECT)
				.addKeyWord(SqlTokenKey.BY)
				.addKeyWord(SqlTokenKey.WHERE)
				.addKeyWord(SqlTokenKey.IN)
				.addKeyWord(SqlTokenKey.IS)
//				.addKeyWord(SqlTokenKey.BETWEEN)
				.addKeyWord(SqlTokenKey.LIKE)
				.addKeyWord(SqlTokenKey.OR)
				.addKeyWord(SqlTokenKey.AND);
		
		OPERATORS = new SqlKeywords();
		OPERATORS.addKeyWord(SqlTokenKey.EQ)
				.addKeyWord(SqlTokenKey.GT)
				.addKeyWord(SqlTokenKey.LT)
				.addKeyWord(SqlTokenKey.EQEQ)
				.addKeyWord(SqlTokenKey.LTEQ)
				.addKeyWord(SqlTokenKey.NEQ)
				.addKeyWord(SqlTokenKey.LTGT)
				.addKeyWord(SqlTokenKey.GTEQ)
				.addKeyWord(SqlTokenKey.IN)
				.addKeyWord(SqlTokenKey.IS)
//				.addKeyWord(SqlTokenKey.BETWEEN)
				.addKeyWord(SqlTokenKey.LIKE);
		
		SYMBOLS = new SqlKeywords();
		SYMBOLS.addKeyWord(SqlTokenKey.SEMI)
				.addKeyWord(SqlTokenKey.COMMA)
				.addKeyWord(SqlTokenKey.START)
//				.addKeyWord(SqlTokenKey.QUESTION)
//				.addKeyWord(SqlTokenKey.VARNAME)
				.addKeyWord(SqlTokenKey.LPARENT)
				.addKeyWord(SqlTokenKey.RPARENT);

		ALL = new SqlKeywords().addAll(KEYWORDS).addAll(OPERATORS).addAll(SYMBOLS);
	}
	

	private final Map<String, SqlTokenKey> keyWords;

	private SqlKeywords(){ 
		keyWords = LangUtils.newMap();
	}
	private SqlKeywords(Map<String, SqlTokenKey> kws){
		this.keyWords = kws;
	}
	
	private SqlKeywords addKeyWord(SqlTokenKey token){
		this.keyWords.put(token.getName(), token);
		return this;
	}
	
	private SqlKeywords addAll(SqlKeywords kws){
		this.keyWords.putAll(kws.keyWords);
		return this;
	}

	@Override
	public SqlTokenKey[] getKeyWordTokenAsArray(){
		return this.keyWords.values().toArray(new SqlTokenKey[this.keyWords.size()]);
	}
	@Override
	public List<SqlTokenKey> getKeyWordTokens(){
		return JFishList.wrap(this.keyWords.values());
	}
	
	@Override
	public SqlTokenKey getTokenByKeyWord(String kw){
		return this.keyWords.get(kw.toLowerCase());
	}
	
	@Override
	public boolean isKeyWord(SqlTokenKey token){
		return this.keyWords.containsKey(token.getName());
	}
	
	@Override
	public boolean isKeyWord(String tokenString){
		return this.keyWords.containsKey(tokenString);
	}
}
