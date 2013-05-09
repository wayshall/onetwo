package org.onetwo.common.db.parser;

import java.util.List;
import java.util.Map;

import org.onetwo.common.lexer.LexerKeywords;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;

public class SqlKeywords implements LexerKeywords<SqlTokenKey> {

	public final static SqlKeywords KEYWORDS;
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
				.addKeyWord(SqlTokenKey.LIKE)
				.addKeyWord(SqlTokenKey.OR)
				.addKeyWord(SqlTokenKey.AND);
	}
	public final static SqlKeywords OPERATORS;
	static {
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
				.addKeyWord(SqlTokenKey.LIKE);
	}

	public final static SqlKeywords SYMBOLS;
	static {
		SYMBOLS = new SqlKeywords();
		SYMBOLS.addKeyWord(SqlTokenKey.SEMI)
				.addKeyWord(SqlTokenKey.COMMA)
				.addKeyWord(SqlTokenKey.START)
//				.addKeyWord(SqlTokenKey.QUESTION)
//				.addKeyWord(SqlTokenKey.VARNAME)
				.addKeyWord(SqlTokenKey.LPARENT)
				.addKeyWord(SqlTokenKey.RPARENT);
	}
	
	private Map<String, SqlTokenKey> keyWords = LangUtils.newMap();

	private SqlKeywords(){}
	private SqlKeywords(Map<String, SqlTokenKey> kws){
		this.keyWords = kws;
	}
	
	private SqlKeywords addKeyWord(SqlTokenKey token){
		this.keyWords.put(token.getName(), token);
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
}
