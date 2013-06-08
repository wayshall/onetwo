package org.onetwo.plugins.dq;

import java.lang.reflect.Type;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.SimpleBlock;
import org.onetwo.common.utils.StringUtils;

public class MethodBuilder {
	

	public static String typeAsString(Type type){
		/*if(type instanceof ParameterizedType){
			return type.toString();
		}*/
		Class<?> rawType = (Class<?>) type;
		return rawType.isArray()?rawType.getComponentType().getName()+"[]":rawType.getName();
	}
	
	public static String class2String(Class<?> rawType, Class<?>...genTypes){
		if(genTypes.length>0){
			return rawType.getName() + "<"+class2String(genTypes)+">";
		}
		return rawType.isArray()?rawType.getComponentType().getName()+"[]":rawType.getName();
	}
	
	public static String class2String(Class<?>[] genTypes){
		if(genTypes.length>0){
			String genTypeStr = StringUtils.join(genTypes, ", ", new SimpleBlock<Object, String>() {

				@Override
				public String execute(Object object) {
					return ((Class<?>)object).getName();
				}

				
			});
			return genTypeStr;
		}
		return LangUtils.EMPTY_STRING;
	}
	

	public static MethodBuilder newPublicMethod(){
		return new MethodBuilder()._public();
	}
	public static MethodBuilder newPrivateMethod(){
		return new MethodBuilder()._private();
	}
	public static MethodBuilder newProtectedMethod(){
		return new MethodBuilder()._protected();
	}
	private static enum ArgType{
		START,
		END
	}
	
	private StringBuilder methodBody = new StringBuilder();
	private ArgType argType = null;

	private MethodBuilder _public(){
		methodBody.append("public ");
		return this;
	}
	private MethodBuilder _private(){
		methodBody.append("private ");
		return this;
	}
	private MethodBuilder _protected(){
		methodBody.append("protected ");
		return this;
	}
	public MethodBuilder _final(){
		methodBody.append("final ");
		return this;
	}
	public MethodBuilder _static(){
		methodBody.append("static ");
		return this;
	}
	public MethodBuilder _return(Type rawType){
		if(rawType==null){
			methodBody.append("void ");
		}else{
			methodBody.append(typeAsString(rawType)).append(" ");
		}
		return this;
	}
	public MethodBuilder name(String methodName){
		methodBody.append(methodName).append(" ");
		return this;
	}
	public MethodBuilder arg(String argName, Class<?> rawType){
		if(argType==null){
			argType = ArgType.START;
			methodBody.append("(");
		}
		methodBody.append(typeAsString(rawType));
		methodBody.append(" ").append(argName).append(" ");
		return this;
	}
	public MethodBuilder _throws(Class<? extends Exception>... expTypes){
		methodBody.append(class2String(expTypes)).append(" ");
		return this;
	}
	public String body(String... body){
		if(argType==ArgType.START){
			methodBody.append(")");
			argType = ArgType.END;
		}
		methodBody.append("{").append(StringUtils.join(body, " ")).append("}");
		String str = methodBody.toString();
		return str;
	}
	
	public String toString(){
		return methodBody.toString();
	}

}
