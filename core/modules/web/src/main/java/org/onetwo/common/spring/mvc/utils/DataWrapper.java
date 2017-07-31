package org.onetwo.common.spring.mvc.utils;

import java.io.Serializable;

import org.onetwo.common.data.LazyValue;

/*****
 * @author wayshall
 * 
 * 对以json形式返回的对象包装，会在jsonview里进行特殊处理
 * 主要是针对不使用@ResponseBody注解，而返回ModelAndView的情况。
 * 
 * 扩展：可以根据不同的response view 指定不同的DataWrapper，
 * 如果找不到，且指定了默认名字（DEFAULT_NAME）的DataWrapper，则使用默认的DataWrapper
 *
 */
@SuppressWarnings("serial")
public class DataWrapper implements Serializable{
	public static final String DEFAULT_NAME = DataWrapper.class.getName();
	
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
