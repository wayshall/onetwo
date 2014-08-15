package org.onetwo.common.spring.sql;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.db.QueryConfigData;
import org.onetwo.common.utils.LangUtils;

public class ParserContext implements Map<Object, Object> {
	
	public static ParserContext create(Object...params){
		if(LangUtils.isEmpty(params))
			return new ParserContext();
		return new ParserContext(LangUtils.asMap(params));
	}


	public static final String CONTEXT_KEY = ParserContextFunctionSet.CONTEXT_KEY;//helper
	public static final String QUERY_CONFIG = "_queryConfig";
	private static final QueryConfigData EMPTY_CONFIG = new QueryConfigData(){

		public void setLikeQueryFields(List<String> likeQueryFields) {
			throw new UnsupportedOperationException();
		}
	};
	
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
		this.context.put(QUERY_CONFIG, config);
	}
	
	public QueryConfigData getQueryConfig(){
		return context.containsKey(QUERY_CONFIG)?(QueryConfigData)context.get(QUERY_CONFIG):EMPTY_CONFIG;
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
