package org.onetwo.common.db.sqlext;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.exception.ServiceException;

public class DefaultSQLFunctionManager implements SQLFunctionManager {
	
	private static SQLFunctionManager instance = new DefaultSQLFunctionManager();
	
	static {
		instance.register("lower", new SQLFunctionTemplate("lower(?0)"));
		instance.register("upper", new SQLFunctionTemplate("upper(?0)"));
		instance.register("substring", new SQLFunctionTemplate("substring(?0,?1,?2)"));
		instance.register("concat", new SQLFunctionTemplate("concat(?0,?1)"));
		instance.register("trim", new SQLFunctionTemplate("trim(?0)"));
		instance.register("length", new SQLFunctionTemplate("length(?0)"));
	}
	
	public static SQLFunctionManager get(){
		return instance;
	}
	
	private Map<String, SQLFunction> functions = new HashMap<String, SQLFunction>();
	
	private DefaultSQLFunctionManager(){
	}
	
	@Override
	public SQLFunctionManager register(String name, SQLFunction function){
		this.functions.put(name, function);
		return this;
	}
	
	@Override
	public boolean constains(String name){
		return functions.containsKey(name);
	}
	
	@Override
	public SQLFunction getFunction(String name){
		return getFunction(name, false);
	}
	
	public SQLFunction getFunction(String name, boolean throwIfNotfound){
		SQLFunction func = this.functions.get(name.toLowerCase());
		if(func==null && throwIfNotfound)
			throw new ServiceException("can not find the function : " + name);
		return func;
	}
	
	@Override
	public String exec(String name, Object...objects){
		SQLFunction func = getFunction(name, true);
		String result = func.render(objects);
		return result;
	}
}
