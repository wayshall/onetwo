package org.onetwo.common.spring.web.mvc;

import java.io.Serializable;

/*****
 * @author wayshall
 * 
 * 对以json形式返回的对象包装，会在jsonview里进行特殊处理
 * 主要是针对不使用@ResponseBody注解，而返回ModelAndView的情况。
 *
 */
@SuppressWarnings("serial")
public class DataWrapper implements Serializable{
	
	public static interface LazyValue {
		Object get();
	}
	
	public static DataWrapper wrap(Object value){
		return new DataWrapper(value);
	}
	
	public static DataWrapper wrap(LazyValue value){
		return new DataWrapper(value);
	}
	
	private Object value;

	public DataWrapper(Object value) {
		this.value = value;
	}

	public Object getValue() {
		if(LazyValue.class.isInstance(value)){
			return ((LazyValue)value).get();
		}
		return value;
	}
	
}
