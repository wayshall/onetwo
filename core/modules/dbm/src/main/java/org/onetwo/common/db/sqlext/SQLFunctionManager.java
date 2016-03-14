package org.onetwo.common.db.sqlext;

public interface SQLFunctionManager {

	public SQLFunctionManager register(String name, SQLFunction function);

	public boolean constains(String name);

	public SQLFunction getFunction(String name);

	public String exec(String name, Object... objects);
	
	public SQLFunction getFunction(String name, boolean throwIfNotfound);

}