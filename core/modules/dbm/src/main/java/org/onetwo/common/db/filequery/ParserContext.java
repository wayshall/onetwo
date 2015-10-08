package org.onetwo.common.db.filequery;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.db.QueryConfigData;
import org.onetwo.common.db.QueryContextVariable;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.springframework.util.Assert;

public class ParserContext implements Map<Object, Object> {
	
	public static ParserContext create(Object...params){
		if(LangUtils.isEmpty(params))
			return new ParserContext();
		return new ParserContext(CUtils.asMap(params));
	}


	public static final String CONTEXT_KEY = ParserContextFunctionSet.CONTEXT_KEY;//helper
	public static final String QUERY_CONFIG = "_queryConfig";
	public static final String QUERY_CONFIG_FUNC = "_queryfunc";
	
	private Map<Object, Object> context;
	
	public ParserContext(){
		context = LangUtils.newHashMap();
		context.put(CONTEXT_KEY, ParserContextFunctionSet.getInstance());
	}
	
	public ParserContext(Map<Object, Object> context) {
		super();
		this.context = context;
	}

	public void setQueryConfig(QueryConfigData config){
		Assert.notNull(config);
		QueryConfigData oldConfig = (QueryConfigData)this.context.put(QUERY_CONFIG, config);
		if(oldConfig!=null){
			//remove old
			/*Stream.of(oldConfig.getVariables()).forEach(e -> 
														context.remove(e.varName()));*/
			//兼容java8以下
			JFishList.wrap(oldConfig.getVariables()).each(new NoIndexIt<QueryContextVariable>() {
				@Override
				protected void doIt(QueryContextVariable e) throws Exception {
					context.remove(e.varName());
				}
			});
		}
		if(config.getVariables()!=null){
			/*Stream.of(config.getVariables()).forEach(e -> 
														context.put(e.varName(), e));*/
			JFishList.wrap(config.getVariables()).each(new NoIndexIt<QueryContextVariable>() {
				@Override
				protected void doIt(QueryContextVariable e) throws Exception {
					context.put(e.varName(), e);
				}
			});
		}
	}
	
	public QueryConfigData getQueryConfig(){
		QueryConfigData config = (QueryConfigData)context.get(QUERY_CONFIG);
		return config==null?ParsedSqlUtils.EMPTY_CONFIG:config;
	}
	public int size() {
		return context.size();
	}

	public boolean isEmpty() {
		return context.isEmpty();
	}

	public boolean containsKey(Object key) {
		return context.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return context.containsValue(value);
	}

	public Object get(Object key) {
		return context.get(key);
	}

	public Object put(Object key, Object value) {
		return context.put(key, value);
	}

	public Object remove(Object key) {
		return context.remove(key);
	}

	public void putAll(Map<? extends Object, ? extends Object> m) {
		context.putAll(m);
	}

	public void clear() {
		context.clear();
	}

	public Set<Object> keySet() {
		return context.keySet();
	}

	public Collection<Object> values() {
		return context.values();
	}

	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		return context.entrySet();
	}

	public boolean equals(Object o) {
		return context.equals(o);
	}

	public int hashCode() {
		return context.hashCode();
	}
	
}
