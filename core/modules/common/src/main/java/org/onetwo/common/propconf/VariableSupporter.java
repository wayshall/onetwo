package org.onetwo.common.propconf;

public interface VariableSupporter {

	public String getVariable(String key);
	
	public String getVariable(String key, boolean checkCache);
	
}
