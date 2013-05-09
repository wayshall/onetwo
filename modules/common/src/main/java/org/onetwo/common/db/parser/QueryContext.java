package org.onetwo.common.db.parser;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.utils.LangUtils;

public class QueryContext {
	public static final String KEY_PREFIX = ":";
	public static final String ASC_KEY = "asc";
	public static final String DESC_KEY = "desc";
	public static final String RAW_QUERY_KEY = "rawQuery";
	
	private Map<String, Object> context = new LinkedHashMap<String, Object>(3);
	private DynamicQuery rawQuery;
	
	public QueryContext(DynamicQuery rawQuery){
		this.rawQuery = rawQuery;
	}
	
	public <T extends DynamicQuery> T getRawQuery(Class<T> type){
		return type.cast(rawQuery);
	}

	final public void putInContext(String key, Object value){
		context.put(trimPrefix(key), value);
	}
	final public <T> T getFromContext(String key, Class<T> type){
		return type.cast(context.get(trimPrefix(key)));
	}
	
	private String trimPrefix(String key){
		String newKey = key;
		if(key.startsWith(KEY_PREFIX)){
			newKey = newKey.substring(KEY_PREFIX.length());
		}
		return newKey;
	}

	public QueryContext asc(String...fields){
		putInContext(ASC_KEY, fields);
		return this;
	}
	@SuppressWarnings("unchecked")
	public List<QValue<String, String[]>> orderBy(){
		if(isEmpty())
			return Collections.EMPTY_LIST;
		
		List<QValue<String, String[]>> orderby = LangUtils.newArrayList(2);
		for(Entry<String, Object> entry : context.entrySet()){
			if(orderby.size()>=2)
				break;
			if(ASC_KEY.equals(entry.getKey())){
				orderby.add(new QValue<String, String[]>(entry.getKey(), (String[])entry.getValue()));
			}else if(DESC_KEY.equals(entry.getKey())){
				orderby.add(new QValue<String, String[]>(entry.getKey(), (String[])entry.getValue()));
			}
		}
		return orderby;
	}
	public QueryContext desc(String...fields){
		putInContext(DESC_KEY, fields);
		return this;
	}
	
	public boolean isEmpty() {
		return context.isEmpty();
	}

	public static class QValue<K, V> {
		public K key;
		public V value;
		public QValue(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}
		
	}

}
