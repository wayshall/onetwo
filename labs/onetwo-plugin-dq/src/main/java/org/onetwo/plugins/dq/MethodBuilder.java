package org.onetwo.plugins.dq;

import org.onetwo.common.utils.SimpleBlock;
import org.onetwo.common.utils.StringUtils;

public class MethodBuilder {
	
	public static String typeAsString(Class<?> rawType, Class<?>...genTypes){
		if(genTypes.length>0){
			String genTypeStr = StringUtils.join(genTypes, ", ", new SimpleBlock<Object, String>() {

				@Override
				public String execute(Object object) {
					return ((Class<?>)object).getName();
				}

				
			});
			return rawType.getName() + "<"+genTypeStr+">";
		}
		return rawType.isArray()?rawType.getComponentType().getName()+"[]":rawType.getName();
	}
	
	
	public static MethodBuilder newPublicMethod(){
		return new MethodBuilder()._public();
	}
	private static enum ArgType{
		START,
		END
	}
	
	private StringBuilder methodBody = new StringBuilder();

	private MethodBuilder _public(){
		methodBody.append("public ");
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
	public MethodBuilder _return(Class<?> rawType, Class<?>...genTypes){
		methodBody.append(typeAsString(rawType, genTypes));
		return this;
	}
	public MethodBuilder name(String methodName){
		methodBody.append(methodName).append(" (");
		return this;
	}
	public MethodBuilder arg(String argName, Class<?> rawType, Class<?>...genTypes){
		methodBody.append(typeAsString(rawType, genTypes)).append(" ").append(argName).append(" ");
		return this;
	}
	public String end(String body){
		methodBody.append(")").append(body);
		return methodBody.toString();
	}
	public void _(){
		
	}

}
