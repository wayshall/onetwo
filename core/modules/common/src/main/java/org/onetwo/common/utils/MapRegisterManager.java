package org.onetwo.common.utils;

import java.util.HashMap;
import java.util.Map;

public class MapRegisterManager<K, V> implements RegisterManager<K, V>{
	
	private Map<K, V> register;

	
	public MapRegisterManager() {
		this(new HashMap<K, V>());
	}

	public MapRegisterManager(Map<K, V> register) {
		super();
		this.register = register;
	}

	public Map<K, V> getRegister() {
		return register;
	}

	
}
