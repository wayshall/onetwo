package org.onetwo.common.utils;

import java.util.Map;
import java.util.Optional;

import org.onetwo.common.exception.BaseException;

public interface RegisterManager<K, V> {
	
	default public RegisterManager<K, V> register(K name, V value){
		Assert.notNull(name);
		Assert.notNull(value);
		getRegister().put(name, value);
		return this;
	}
	
	default public V getRegistered(K name){
		Map<K, V> register = getRegister();
		if(!register.containsKey(name)){
			throw new BaseException("can not find register name: " + name);
		}
		return register.get(name);
	}
	
	default public Optional<V> findRegistered(K name){
		return Optional.ofNullable(getRegistered(name));
	}
	
	/*default public V findRegistered(K name){
		return getRegister().get(name);
	}*/
	

	public Map<K, V> getRegister();

	
}
