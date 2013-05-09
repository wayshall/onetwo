package org.onetwo.common.utils;

public class VProviderFactory {
	
	private VProviderFactory(){}
	
	public static ValueProvider createSimple(Object context){
		return new SimpleVariableProvider(context);
	}

}
