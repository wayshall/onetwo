package org.onetwo.plugins.jdoc.Lexer;

import org.apache.commons.lang.StringUtils;

public class JLexerUtils {
	

	public static String[] getGenerateType(String type){
		if(StringUtils.isBlank(type))
			return new String[0];
		int index = type.indexOf('<');
		if(index!=-1){
			type = StringUtils.replaceEach(type, new String[]{"<", ">", ","}, new String[]{" ", " ", " "});
		}
		String[] strs = StringUtils.split(type, " ");
		return strs;
	}
	
	private JLexerUtils(){
	}

}
