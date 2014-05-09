package org.onetwo.common.spring.sql;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.utils.LangUtils;

public class ParserContext implements Map<Object, Object> {
	
	public static ParserContext create(Object...params){
		if(LangUtils.isEmpty(params))
			return new ParserContext();
		return new ParserContext(LangUtils.asMap(params));
	}

	private Map<Object, Object> context;
	
	public ParserContext(){
		context = LangUtils.newHashMap();
		context.put(ParserContextFunctionSet.CONTEXT_KEY, ParserContextFunctionSet.getInstance());
	}
	
	public ParserContext(Map<Object, Object> context) {
		super();
		this.context = context;
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
