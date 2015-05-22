package org.onetwo.common.jfishdbm.dialet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.utils.RegisterManager;

public class DefaultDatabaseDialetManager implements RegisterManager<String, DBDialect>{
	
	final private Map<String, DBDialect> dialetRegister = new ConcurrentHashMap<>();
	
	public DefaultDatabaseDialetManager(){
	}

	public Map<String, DBDialect> getRegister() {
		return dialetRegister;
	}

	
}
