package org.onetwo.plugins.jdoc.Lexer;

import org.onetwo.common.utils.StringUtils;


public enum JToken {

	IDENTIFIER("string"),
	
	//keywork
	PACKAGE("package"),
	IMPORT("import"),
	PUBLIC("public"),
	CLASS("class"),
	PRIVATE("private"),
	PROTECTED("protected"),
	
	FINAL("final"),
	STATIC("static"),
	ABSTRACT("abstract"),
	VOID("void"),
	THROWS("throws"),
	
	
	//tag
//	START("*"),
	JAVADOC("/** */"),
	LBRACE("{"),
	RBRACE("}"),
	LPARENT("("),
	RPARENT(")"),
	LBRACKET("["),
	RBRACKET("]"),
	LANGLE("<"),
	RANGLE(">"),
	
	DOT("."),
	COMMA(","),
	SEMI(";"),
//	DQUOTE("\""),
	
	ASSIGN("="),
	AT("@"),
	NULL("null"),
	
//	DIGIT,
	NUMBER("number"),
	JString,
	EOF,
	
//	EOF,
	MUTI_COMMENTS,
	COMMENT,
	UNKNOW;
	
	private String name;
	JToken(){
	}
	JToken(String name){
		this.name = name;
	}
	
	public String getName(){
		if(StringUtils.isBlank(name)){
			this.name = toString().toLowerCase();
		}
		return this.name;
	}
	
//	public String toString(){
//		if(name!=null)
//			return name;
//		else
//			return super.toString();
//	}
}
