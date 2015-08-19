package org.onetwo.common.db.parser;

import org.onetwo.common.utils.StringUtils;

public enum SqlTokenKey {

	DELETE("delete"),
	UPDATE("update"),
	INSERT("insert"),
	SELECT("select"),
	VALUES("values"),
	FROM("from"),
	LEFT("left"),
	JOIN("join"),
	ON("on"),
	AS("as"),
	ORDER("order"),
	GROUP("group"),
	CASE("case"),
	WHEN("when"),
	THEN("then"),
	ELSE("else"),
    IF("if"), 
	END("end"),
	HAVING("having"),
	NOT("not"),
	DISTINCT("distinct"),
	INTO("into"),
    NULL("null"), 
    ASC("asc"), 
    DESC("desc"), 
    LIMIT("limit"),
    WITH("with"),
    CONNECT("connect"),
    BY("by"),

	WHERE("where"),
	AND("and"),
	OR("or"),
	
	IN("in"),
	LIKE("like"),

	IS("is"),
	EQ("="), 
    GT(">"), 
    LT("<"), 
    QUESTION("?"), 
    EQEQ("=="), 
    LTEQ("<="), 
    NEQ("!="), 
    LTGT("<>"), 
    GTEQ(">="), 
    BETWEEN("between"), 

//    COLON(":"),
	SEMI(";"), 
    COMMA(","), 
    START("*"), 
    SINGLE_QUOTE("'"), 

	LPARENT("("),
	RPARENT(")"),
	
	IDENTIFIER("identifier"),
	STRING("string"),
	NUMBER("number"),
	VARNAME("varname"),
	EOF,
	UNKNOW;
	
	private String name;
	SqlTokenKey(){
	}
	SqlTokenKey(String name){
		this.name = name;
	}
	
	public String getName(){
		if(StringUtils.isBlank(name)){
			this.name = toString().toLowerCase();
		}
		return this.name;
	}
}
