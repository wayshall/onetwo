package org.onetwo.plugins.jdoc.Lexer.defined;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.jdoc.Lexer.JToken;

public class ClassDefinedImpl extends HasAnnotationDefinedImpl {

	private final Map<String, FieldDefinedImpl> fields = LangUtils.newHashMap();
	private final List<MethodDefinedImpl> methods = LangUtils.newArrayList();
	private JavaClassDefineImpl javaClassDefined;
	
	public ClassDefinedImpl(JavaClassDefineImpl javaClassDefined, String name, JToken...tokens) {
		super(name, tokens);
		this.javaClassDefined = javaClassDefined;
	}
	
	public FieldDefinedImpl getField(String name){
		return fields.get(name);
	}
	
	public MethodDefinedImpl getMethod(String name, String... paramTypes){
		for(MethodDefinedImpl m : methods){
			if(m.isMatch(name, paramTypes))
				return m;
		}
		return null;
	}
	
	public void addField(FieldDefinedImpl field){
		this.fields.put(field.getName(), field);
	}
	
	public void addMethod(MethodDefinedImpl method){
		this.methods.add(method);
	}

	public List<MethodDefinedImpl> getMethods() {
		return methods;
	}

	public Map<String, FieldDefinedImpl> getFields() {
		return fields;
	}

	public JavaClassDefineImpl getJavaClassDefined() {
		return javaClassDefined;
	}
	

}
