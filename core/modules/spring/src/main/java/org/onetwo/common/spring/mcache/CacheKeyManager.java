package org.onetwo.common.spring.mcache;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.onetwo.common.cache.Cacheable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


@Component
public class CacheKeyManager implements InitializingBean {
	
	public static final int DEFAULT_EXPIRE = 60*30;
	
	protected CacheKeyGenerator keyGenerator;
	
	public CacheKeyManager(){
	}
	
	public CacheKeyGenerator getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyGenerator(CacheKeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (keyGenerator == null)
			setKeyGenerator(defaultKeyGenerator());
	}
	
	protected CacheKeyGenerator defaultKeyGenerator() {
		return new HashCodeCacheKeyGenerator(true);
	}
	
	public Serializable getCacheKey(Cacheable cacheable, MethodInvocation invocation){
		Serializable key = cacheable.key();
		if(key==null || StringUtils.isBlank(key.toString())){
			key = getKeyGenerator().generateKey(invocation);
		}
		return key;
	}

}
