package org.onetwo.common.spring.mcache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.cache.Cacheable;
import org.onetwo.common.cache.FlushCache;
import org.onetwo.common.expr.Expression;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.expr.VProviderFactory;
import org.onetwo.common.expr.ValueProvider;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;

public class CacheModelManager implements InitializingBean {
	
	protected Map<Serializable, CacheModel> cacheModels;
	
	protected CacheKeyGenerator keyGenerator;
	
	protected Expression expr = ExpressionFacotry.DOLOR;
	
	public CacheModelManager(){
		this.cacheModels = new HashMap<Serializable, CacheModel>();
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

	public CacheModel getCacheModel(Cacheable cacheable, MethodInvocation invocation){
//		String getCacheKey = invocation.getMethod().toGenericString()+":getCacheKey";
//		UtilTimerStack.push(getCacheKey);
		
		Serializable key = getCacheKey(cacheable, invocation);
		
//		UtilTimerStack.pop(getCacheKey);
		
		CacheModel cacheModel = this.cacheModels.get(key);
		if(cacheModel!=null)
			return cacheModel;
		
		String group = cacheable.group();
		cacheModel = new CacheModel(key, group, cacheable.expire());
		this.cacheModels.put(key, cacheModel);
		
		return cacheModel;
	}
	
	public CacheModel getCacheModel(String key){
		return cacheModels.get(key);
	}
	
	public List<CacheModel> getCacheModelsByGroup(String group){
		if(cacheModels.isEmpty())
			return null;
		
		List<CacheModel> models = null;
		for(Map.Entry<Serializable, CacheModel> m : cacheModels.entrySet()){
			if(!group.equals(m.getValue().getGroup()))
				continue;
			if(models==null)
				models = new ArrayList<CacheModel>();
			models.add(m.getValue());
		}
		
		return models;
	}
	
	@SuppressWarnings("rawtypes")
	public Serializable getCacheKey(Cacheable cacheable, MethodInvocation invocation){
		Serializable key = null;
		String keyStr = cacheable.key();
		if(StringUtils.isBlank(keyStr)){
			key = getKeyGenerator().generateKey(invocation);
		}else{
			ValueProvider context = createValueProvider(invocation.getArguments(), null);
			key = expr.parseByProvider(keyStr, context);
			if(cacheable.useKeyHashCode()){
				key = getKeyGenerator().generateKey(invocation.getMethod(), key);//generated with method for unique group
			}
		}
		return key;
	}
	
	public FlushCacheModel getFlushCacheModel(FlushCache flushCache, MethodInvocation invocation, Object returnValue){
		Serializable key = getFlushCacheKey(flushCache, invocation, returnValue);
		FlushCacheModel flushModel = FlushCacheModel.create(flushCache, key);
		return flushModel;
	}
	
	@SuppressWarnings("rawtypes")
	public Serializable getFlushCacheKey(FlushCache flushCache, MethodInvocation invocation, Object returnValue){
		Serializable key = null;
		String keyStr = flushCache.key();
		if(StringUtils.isBlank(keyStr)){
//			key = getKeyGenerator().generateKey(invocation);//flush all of group if no key 
		}else{
			ValueProvider context = createValueProvider(invocation.getArguments(), returnValue);
			key = expr.parseByProvider(keyStr, context);
			/*if(flushCache.useKeyHashCode()){
				key = getKeyGenerator().generateKey(invocation.getMethod(), key);
			}*/
		}
		return key;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ValueProvider createValueProvider(Object args, Object result){
		Map context = LangUtils.newHashMap();
		if(args!=null)
			context.put("args", args);
		if(result!=null)
			context.put("result", result);
		ValueProvider provider = VProviderFactory.createSimple(context);
		return provider;
	}
	
	public void remove(Serializable key){
		this.cacheModels.remove(key);
	}

	public Map<Serializable, CacheModel> getCacheModels() {
		return cacheModels;
	}

}
