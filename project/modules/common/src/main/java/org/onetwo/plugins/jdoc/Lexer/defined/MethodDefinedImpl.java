package org.onetwo.plugins.jdoc.Lexer.defined;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.onetwo.common.lexer.JSyntaxException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.jdoc.Lexer.JLexerUtils;
import org.onetwo.plugins.jdoc.Lexer.JToken;

public class MethodDefinedImpl extends HasAnnotationDefinedImpl {
	
	public static class MethodParam extends HasAnnotationDefinedImpl {

		private MethodDefinedImpl method;
		private String declareType;
		
		public MethodParam(MethodDefinedImpl method, String name, JToken... tokens) {
			super(name, tokens);
			this.method = method;
		}

		public MethodDefinedImpl getMethod() {
			return method;
		}
		
		public MethodParam(String name) {
			super(name);
		}

		public String getDeclareType() {
			return declareType;
		}

		public void setDeclareType(String declareType) {
			this.declareType = declareType;
		}
		
		public String getDeclareFullType(){
			if(method==null)
				return declareType;
			String type = this.method.classDefined.getJavaClassDefined().getFullClassNameFromImport(declareType);
			if(StringUtils.isBlank(type))
				return declareType;
			return type;
		}
		
		
	}

	private ClassDefinedImpl classDefined;
	private final List<MethodParam> parameters = LangUtils.newArrayList();
	private String returnType;
	private final List<String> throwsExceptions = LangUtils.newArrayList();
	
	public MethodDefinedImpl(ClassDefinedImpl classDefined, String name, JToken... tokens) {
		super(name, tokens);
		this.classDefined = classDefined;
	}
	
	public boolean isConstructor(){
		return name.equals(classDefined.getName());
	}
	
	public boolean isPublic(){
		return isDefineBy(JToken.PUBLIC);
	}

	public String getReturnType() {
		return returnType;
	}

	public List<String> getReturnGenerateTypes() {
		return getGenerateType(returnType);
	}
	
	protected List<String> getGenerateType(String type){
		String[] types = JLexerUtils.getGenerateType(type);
		List<String> typeList = LangUtils.newArrayList();
		for(String t : types){
			String typeName = this.classDefined.getJavaClassDefined().getFullClassNameFromImport(t);
			if(typeName.indexOf('.')==-1 || typeName.startsWith("java.")){//RestResult<List<DataType>>
				continue;
			}
			typeList.add(typeName);
		}
		return typeList;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	public MethodParam getParameter(String name){
		for(MethodParam param : parameters){
			if(name.equals(param.getName()))
				return param;
		}
		return null;
	}
	
	public MethodDefinedImpl addParameter(String name, String type){
		if(getParameter(name)!=null){
			throw new JSyntaxException("double paremter : " + name);
		}
		MethodParam param = new MethodParam(name);
		this.parameters.add(param);
		return this;
	}

	public List<MethodParam> getParameters() {
		return parameters;
	}

	public List<String> getThrowsExceptions() {
		return throwsExceptions;
	}
	
	public String[] getParammeterTypes(){
		List<String> types = LangUtils.newArrayList();
		for(MethodParam p : this.parameters){
			types.add(p.getDeclareType());
		}
		return types.toArray(new String[types.size()]);
	}
	
	public boolean isMatch(String name, String...paramTypes){
		if(getParameters().size()!=paramTypes.length)
			return false;
		EqualsBuilder eq = new EqualsBuilder();
		eq.append(this.name, name);
		for (int i = 0; i < paramTypes.length; i++) {
			eq.append(this.parameters.get(i).getDeclareType(), paramTypes[i]);
		}
		return eq.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(name);
		for(MethodParam pm :this.parameters){
			hash.append(pm.getDeclareType());
		}
		return hash.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!MethodDefinedImpl.class.isInstance(obj))
			return false;
		MethodDefinedImpl o = (MethodDefinedImpl)obj;
		return isMatch(o.getName(), o.getParammeterTypes());
	}

}
