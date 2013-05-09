package org.onetwo.plugins.jdoc.Lexer.defined;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.jdoc.Lexer.JToken;

public class AnnotationDefinedImpl extends JDefinedImpl {

	private Map<String, String> attributes = new HashMap<String, String>();
	
	public AnnotationDefinedImpl(String name) {
		super(name, JToken.AT);
		setAttribute("value", "");
	}

	public String getAttribute(String name){
		return attributes.get(name);
	}
	
	public void setAttribute(String name, String value){
		this.attributes.put(name, value);
	}
	
	public String toString(){
		return LangUtils.append(name, attributes);
	}
}
