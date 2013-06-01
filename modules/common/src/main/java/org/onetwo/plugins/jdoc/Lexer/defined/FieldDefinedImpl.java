package org.onetwo.plugins.jdoc.Lexer.defined;

import org.onetwo.plugins.jdoc.Lexer.JToken;

public class FieldDefinedImpl extends HasAnnotationDefinedImpl {
	
	public static final String SERIALVERSIONUID_NAME = "serialVersionUID";

	private String declareType;
	public FieldDefinedImpl(String name, JToken[] tokens) {
		super(name, tokens);
	}
	public String getDeclareType() {
		return declareType;
	}
	public void setDeclareType(String declareType) {
		this.declareType = declareType;
	}
	
	public boolean isRequired(){
		return getAnnotationDefined("NotNull")!=null || getAnnotationDefined("NotBlank")!=null;
	}
	
	public boolean isSerialVersionUID(){
		return SERIALVERSIONUID_NAME.equals(getName());
	}

}
