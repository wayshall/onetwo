package org.onetwo.common.db.parser;


import static org.onetwo.common.lexer.JLexerUtils.isNumberChar;

import org.onetwo.common.lexer.AbstractLexer;
import org.onetwo.common.lexer.JLexerException;
import org.onetwo.common.lexer.JLexerUtils;
import org.onetwo.common.lexer.SourceReader;

public class SqlLexer extends AbstractLexer<SqlTokenKey> {

	private final SqlKeywords keywords = SqlKeywords.KEYWORDS;
	private final SqlKeywords operators = SqlKeywords.OPERATORS;
	
	public SqlLexer(SourceReader input) {
		super(input);
	}
	

	@Override
	public boolean nextToken() {
		clearCharBuf();
		while(true){

			if(isEOF()){
				setToken(SqlTokenKey.EOF);
				return false;
			}
			
			if(isWhiteSpace(ch)){
				scanNextChar();
				continue;
			}
			
			if(isFirstIdentifier(ch)){
				scanIdentifier();
				return true;
			}
			
			if(isNumberChar(ch)){
				scanNumber();
				return true;
			}
			
			if(ch=='('){
				setToken(SqlTokenKey.LPARENT);
			}else if(ch==')'){
				setToken(SqlTokenKey.RPARENT);
			}else if(ch=='<'){
				setToken(SqlTokenKey.LT);
				saveChar();
				scanNextChar();
				if(ch=='='){
					setToken(SqlTokenKey.LTEQ);
				}else if(ch=='>'){
					setToken(SqlTokenKey.LTGT);
				}else{
					return true;
				}
			}else if(ch=='>'){
				setToken(SqlTokenKey.GT);
				saveChar();
				scanNextChar();
				if(ch=='='){
					setToken(SqlTokenKey.GTEQ);
				}else{
					return true;
				}
			}else if(ch==';'){
				setToken(SqlTokenKey.SEMI);
			}else if(ch=='\''){
				setToken(SqlTokenKey.SINGLE_QUOTE);
				scanSqlString('\'');
				return true;
			}else if(ch==','){
				setToken(SqlTokenKey.COMMA);
			}else if(ch=='*'){
				setToken(SqlTokenKey.START);
			}else if(ch==':'){
//				token = SqlTokenKey.COLON;
				scanVarname();
				return true;
			}else if(ch=='='){
				setToken(SqlTokenKey.EQ);
				saveChar();
				scanNextChar();
				if(ch=='='){
					setToken(SqlTokenKey.EQEQ);
				}else{
					return true;
				}
			}else if(ch=='?'){
				setToken(SqlTokenKey.QUESTION);
			}else if(ch=='!'){
				scanNextChar();
				saveChar();
				if(ch=='='){
					setToken(SqlTokenKey.NEQ);
				}else{
					return true;
				}
			}else{
				setToken(SqlTokenKey.UNKNOW);
			}

			saveChar();
			scanNextChar();

			//TODO
			/*if(token==SqlTokenKey.UNKNOW){
				return this.nextToken();
			}*/
			return true;
		}
	}

	protected void scanSqlString(char endChar){
		if(ch!=endChar){
			throw new JLexerException("must a char end with " + ch +", actual is " + endChar);
		}
		saveChar();
		while(true){
			scanNextChar();
			saveChar();
			if(ch==endChar){
				break;
			}
		}
		setToken(SqlTokenKey.STRING);
		scanNextChar();
	}
	
	protected void scanNumber(){
		if(!isNumberChar(ch)){
			throw new JLexerException("not a number char : " + ch);
		}
		saveChar();
		while(true){
			scanNextChar();
			if((!isNumberChar(ch))){
				break;
			}
			saveChar();
		}
		setToken(SqlTokenKey.NUMBER);
	}
	
	protected boolean isNumberStart(char ch){
		return JLexerUtils.isNumberChar(ch);
	}
	
	protected boolean isWhiteSpace(char ch){
		return JLexerUtils.isWhiteSpace(ch);
	}
	
	protected boolean isFirstIdentifier(char ch){
		return JLexerUtils.isFirstIdentifier(ch);
	}

	
	public static boolean isIdentifier(char ch){
		return SqlLexerUtils.isSqlIdentifier(ch);
	}


	protected void scanVarname(){
		if(ch!=':'){
			throw new JLexerException();
		}
		saveChar();
		while(true){
			scanNextChar();
			if(!isIdentifier(ch)){
				break;
			}
			saveChar();
		}
		String id = getStringValue();
		SqlTokenKey t = keywords.getTokenByKeyWord(id);
		if(t!=null){
			throw new JLexerException("the named var can not be a keyword: " + t);
		}
		setToken(SqlTokenKey.VARNAME);
	}

	protected void scanIdentifier(){
		if(!isFirstIdentifier(ch)){
			throw new JLexerException("sql lexer error: " + ch);
		}
		saveChar();
		while(true){
			scanNextChar();
			if(!isIdentifier(ch)){
				break;
			}
			saveChar();
		}
		String id = getStringValue();
		SqlTokenKey t = keywords.getTokenByKeyWord(id);
		if(t!=null){
			setToken(t);
		}else{
			t = operators.getTokenByKeyWord(id);
			if(t!=null){
				setToken(t);
			}else{
				setToken(SqlTokenKey.IDENTIFIER);
			}
		}
	}


	@Override
	public SqlKeywords getKeyWords() {
		return keywords;
	}


	public SqlKeywords getOperators() {
		return operators;
	}

}
