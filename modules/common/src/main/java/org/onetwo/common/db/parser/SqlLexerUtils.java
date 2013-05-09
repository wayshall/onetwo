package org.onetwo.common.db.parser;

public class SqlLexerUtils {

	private static final boolean[] SQL_IDENTIFIERS = new boolean[256];
	static {
		for(char c=0; c<SQL_IDENTIFIERS.length; c++){
			if(c>='a' && c<='z'){
				SQL_IDENTIFIERS[c] = true;
			}else if(c>='A' && c<='Z'){
				SQL_IDENTIFIERS[c] = true;
			}else if(c>='0' && c<='9'){
				SQL_IDENTIFIERS[c]= true;
			}
		}
		SQL_IDENTIFIERS['_'] = true;
		SQL_IDENTIFIERS['.'] = true;
		SQL_IDENTIFIERS['*'] = true;
	}
	

	
	public static boolean isSqlIdentifier(char ch){
		return ch <SQL_IDENTIFIERS.length && SQL_IDENTIFIERS[ch];
	}
	
	private SqlLexerUtils(){
	}

}
