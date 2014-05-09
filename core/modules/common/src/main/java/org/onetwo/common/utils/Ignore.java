package org.onetwo.common.utils;

import java.util.Map;

import org.onetwo.common.utils.map.M;

@SuppressWarnings("rawtypes")
public class Ignore {

	public static class IgnoreKeys {
		public static final String IGNORE = ":ignore";
		public static final String NULL = ":null";//map.put(":ignore", ":null")
		public static final String EMPTY = ":empty";//map.put(":ignore", ":empty")
	}
	
	/**********
	 * ignore rule
	 * @param objects
	 * @return
	 */
	public static Ignore create(Object...objects){
		return createBy(true, M.c(objects));
	}
	

	public static Ignore createBy(Map map){
		return createBy(true, map);
	}
	
	public static Ignore createBy(boolean removeFromMap, Map map){
		Ignore i = new Ignore();
		if(map.containsKey(IgnoreKeys.IGNORE)){
			String val = (String) map.get(IgnoreKeys.IGNORE);
			if(removeFromMap)
				map.remove(IgnoreKeys.IGNORE);
			if(IgnoreKeys.NULL.equals(val))
				i.setIgnoreNull(true);
			else if(IgnoreKeys.EMPTY.equals(val)){
				i.setIgnoreNull(true);
				i.setIgnoreEmptyIfString(true);
			}else
				throw new UnsupportedOperationException();
		}
		i.cause = map;
		return i;
	}

	private boolean ignoreNull;
	private boolean ignoreEmptyIfString;
	private Map cause;
	


	public boolean ignore(Object val){
		return ignore(null, val);
	}

	/****
	 * 是否忽略指定的名称和值
	 * @param name
	 * @param val
	 * @return
	 */
	public boolean ignore(String name, Object val){
		boolean ig;
		if(val==null){
			ig = isIgnoreNull();
		}else if(val instanceof String && StringUtils.isBlank(val.toString())){
			ig = isIgnoreEmptyIfString();
		}else{
			Object igVal = null;
			if(StringUtils.isNotBlank(name) && cause.containsKey(name)){
				igVal = cause.get(name);
			}else if(cause.containsKey(val.getClass())){
				igVal = cause.get(val.getClass());
			}
			ig = val.equals(igVal);
		}
		return ig;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T defaultVal(String name, T val){
		T def = val;
		if(val==null || LangUtils.hasNotElement(cause))
			return val;
		if(StringUtils.isNotBlank(name) && cause.containsKey(name)){
			def = (T)cause.get(name);
		}else if(cause.containsKey(val.getClass())){
			def = (T)cause.get(val.getClass());
		}
		return def;
	}
	public boolean isIgnoreNull() {
		return ignoreNull;
	}

	protected void setIgnoreNull(boolean ignoreNull) {
		this.ignoreNull = ignoreNull;
	}

	public boolean isIgnoreEmptyIfString() {
		return ignoreEmptyIfString;
	}

	protected void setIgnoreEmptyIfString(boolean ignoreEmptyIfString) {
		this.ignoreEmptyIfString = ignoreEmptyIfString;
	}

}
