package org.onetwo.common.expr;


public class VProviderFactory {
	
	private VProviderFactory(){}
	
	public static ValueProvider createSimple(Object context){
		return new SimpleVariableProvider(context);
	}

}
