package org.onetwo.common.utils;


@SuppressWarnings("unchecked")
public abstract class NumberUtils {
	
	public static Long getLong(Object val, Long def){
		if(val==null)
			return def;
		if(!(val instanceof Long)){
			try {
				return Long.parseLong(val.toString());
			} catch (Exception e) {
				return def;
			}
		}else{
			return (Long) val;
		}
	}
	
	public static Integer getInteger(Object val, Integer def){
		if(val==null)
			return def;
		if(!(val instanceof Integer)){
			try {
				return Integer.parseInt(val.toString());
			} catch (Exception e) {
				return def;
			}
		}else{
			return (Integer) val;
		}
	}
	
	
	public static void main(String[] args){
		System.out.println(getLong("1a", 0l));
	}
}
