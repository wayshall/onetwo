package org.onetwo.dbm.dialet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.RegisterManager;

public class DefaultDatabaseDialetManager implements RegisterManager<String, DBDialect>{
	
	final private Map<String, DBDialect> dialetRegister = new ConcurrentHashMap<>();
	
	public DefaultDatabaseDialetManager(){
	}
	
	public DefaultDatabaseDialetManager register(DBDialect dialet){
		Assert.notNull(dialet);
		getRegister().put(dialet.getDbmeta().getDbName(), dialet);
		return this;
	}

	public Map<String, DBDialect> getRegister() {
		return dialetRegister;
	}
	
	public DBDialect getRegistered(String name){
		Map<String, DBDialect> register = getRegister();
		if(!register.containsKey(name)){
			throw new BaseException("can not find register name: " + name);
		}
		return register.get(name);
	}
	
}
