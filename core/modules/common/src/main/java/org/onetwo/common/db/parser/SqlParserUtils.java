package org.onetwo.common.db.parser;

import org.onetwo.common.db.parser.SqlKeywords.SqlType;
import org.onetwo.common.lexer.AbstractParser.JTokenValue;
import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;

public final class SqlParserUtils {

//	public static final String JDBC_HOLDER = "?";
	
	/*public static String toFragmentSql(JTokenValueCollection<SqlTokenKey> tokens){
		StringBuilder value = new StringBuilder();
//		int index = 0;
		SqlTokenKey preToken = null;
		for(JTokenValue<SqlTokenKey> tv : tokens.getTokenValues()){
			if(preToken!=null && !SqlKeywords.SYMBOLS.isKeyWord(preToken) && tv.getToken()==SqlTokenKey.RPARENT)
				value.append(" ");
			value.append(tv.value);
			if(SqlKeywords.SYMBOLS.isKeyWord(tv.getToken())){
				value.append(" ");
			}
//			index++;
			preToken = tv.getToken();
		}
		return value.toString();
	}*/
	
	/*public static boolean isNotLeftParent(SqlTokenKey token){
		return SqlTokenKey.LPARENT!=token;
	}*/

	public static String getActualPlaceHolder(int count, boolean hasParent) {
		if(count<=1)
			return SqlTokenKey.QUESTION.getName();
		StringBuilder str = new StringBuilder();
		if(hasParent)
			str.append("(");
		for (int i = 0; i < count; i++) {
			if(i!=0)
				str.append(", ");
			str.append(SqlTokenKey.QUESTION.getName());
		}
		if(hasParent)
			str.append(")");
		return str.toString();
	}

	public static String toFragmentSql(JTokenValueCollection<SqlTokenKey> tokens){
		return toSqlWithReplace(tokens, null, 1);
	}
	
	public static String toSqlWithReplace(JTokenValueCollection<SqlTokenKey> tokenValues, SqlTokenKey token, int holderCount){
		StringBuilder value = new StringBuilder();
//		int index = 0;
		SqlTokenKey preToken = null;
		for(JTokenValue<SqlTokenKey> tv : tokenValues.getTokenValues()){

			//为了输出格式，加空白
			if(tv.getToken()==SqlTokenKey.IDENTIFIER && preToken!=null && preToken==SqlTokenKey.IDENTIFIER){
				appendBlank(value);
			}else if(SqlTokenKey.RPARENT==tv.getToken()){
				appendBlank(value);
			}else if(SqlKeywords.KEYWORDS.isKeyWord(tv.getToken())){
				appendBlank(value);
			}
			
			if(token!=null && tv.getToken()==token){
				boolean hasParent = !SqlTokenKey.LPARENT.equals(preToken) && holderCount>1;
				value.append(getActualPlaceHolder(holderCount, hasParent));
			}else{
				value.append(tv.getValue());
			}
			
			//为了输出格式，加空白
			if(SqlKeywords.SYMBOLS.isKeyWord(tv.getToken())){
				appendBlank(value);
			}else if(SqlKeywords.KEYWORDS.isKeyWord(tv.getToken())){
				appendBlank(value);
			}
//			index++;
			preToken = tv.getToken();
		}
		if(value.length()>0 && value.charAt(value.length()-1)!=' ')
			value.append(" ");
		
		return value.toString();
	}
	
	public static void appendBlank(StringBuilder value){
		if(value.length()==0){
			value.append(' ');
			return ;
		}
		if(value.charAt(value.length()-1)!=' ')
			value.append(' ');
	}
	
	public static SqlType getSqlType(SqlObject sqlobj){
		if(!SqlKeywordObject.class.isInstance(sqlobj))
			return SqlType.OTHER;
		SqlKeywordObject key = (SqlKeywordObject) sqlobj;
		SqlTokenKey token = key.getToken();
		SqlType type = null;
		switch (token) {
			case SELECT:
				type = SqlType.SELECT;
				break;
				
			case INSERT:
				type = SqlType.INSERT;
				break;
			
			case UPDATE:
				type = SqlType.UPDATE;
				break;
			
			case DELETE:
				type = SqlType.DELETE;
				break;
	
			default:
				type = SqlType.OTHER;
				break;
		}
		return type;
	}
	
	private SqlParserUtils(){
	}

}
