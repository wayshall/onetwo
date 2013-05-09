package org.onetwo.plugins.jdoc.Lexer.defined;

import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.plugins.jdoc.Lexer.DocumentInfo;
import org.onetwo.plugins.jdoc.Lexer.JToken;

public class JDefinedImpl {

	private JToken[] definedTokens;
	protected String name;
//	private String sourceType;
	private DocumentInfo document;
	
	public JDefinedImpl(String name, JToken... tokens) {
		super();
		this.definedTokens = tokens;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefineBy(JToken token){
		return ArrayUtils.contains(definedTokens, token);
	}
	
	public boolean isDefineByAll(JToken... tokens){
		for(JToken token : tokens){
			if(!ArrayUtils.contains(tokens, token)){
				return false;
			}
		}
		return true;
	}

//	public String getSourceType() {
//		return sourceType;
//	}
//
//	public void setSourceType(String sourceType) {
//		this.sourceType = sourceType;
//	}
//

	public JToken[] getDefinedTokens() {
		return definedTokens;
	}

	public DocumentInfo getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = DocumentInfo.create(document);
	}
	
	protected void parseDocumentDirective(String doc){
		
	}
	
	
}
