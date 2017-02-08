package org.onetwo.common.spring.mvc.utils;

import java.io.Serializable;

import org.onetwo.common.data.LazyValue;

/*****
 * @author wayshall
 * 
 * 对以json形式返回的对象包装，会在jsonview里进行特殊处理
 * 主要是针对不使用@ResponseBody注解，而返回ModelAndView的情况。
 *
 */
@SuppressWarnings("serial")
public class DataWrapper implements Serializable{
	
	public static DataWrapper wrap(Object value){
		return new DataWrapper(value);
	}
	
	public static DataWrapper lazy(LazyValue value){
		return new DataWrapper(value);
	}
	
	private Object value;

	public DataWrapper(Object value) {
		this.value = value;
	}

	public Object getValue() {
		if(LazyValue.class.isInstance(value)){
			return ((LazyValue)value).lazyGet();
		}
		return value;
	}
	
}
