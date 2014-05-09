package org.onetwo.common.spring.sql;

import java.util.HashMap;

import org.onetwo.common.spring.sql.func.SQLFunctionGenerator;
import org.onetwo.common.spring.sql.func.SqlFunctionDialet;

abstract public class AbstractSqlFunctionDialet extends HashMap<String, SQLFunctionGenerator> implements SqlFunctionDialet {
	
//	private Map<String, SQLFunctionGenerator> funcMaps = LangUtils.newHashMap();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1088735384768069511L;

	final public AbstractSqlFunctionDialet register(String name, SQLFunctionGenerator func){
		put(name, func);
		return this;
	}

	@Override
	public SQLFunctionGenerator get(String key) {
		return super.get(key);
	}

	
	/*@Override
	public SQLFunctionGenerator getSqlFunction(String name) {
		return get(name);
	}*/

}
