package org.onetwo.common.convert;

import org.onetwo.common.utils.Assert;

abstract public class AbstractTypeConvert<T> implements TypeConvert<T>{
	
	private T defValue;
	private boolean supportedNotNull = true;
	
	/*protected AbstractTypeConvert(){
		this.defValue = null;
	}*/
	

	protected AbstractTypeConvert(T defValue){
		this(true, defValue);
	}
	
	protected AbstractTypeConvert(boolean supportedNotNull, T defValue){
		this.defValue = defValue;
		this.supportedNotNull = supportedNotNull;
		if(supportedNotNull){
			Assert.notNull(defValue);
		}
	}
	
	/****
	 * 如果source为null，则返回配置的默认值
	 */
	public T convertNotNull(Object source, Class<?> componentType){
		if(source==null){
			if(!supportedNotNull)
				throw new IllegalArgumentException("source value is null, componentType: " + componentType);
			return getDefaultValue(componentType);
		}
		return doConvert(source, componentType);
	}
	
	/***
	 * 当目标类型为基本类型时，如果source为null，则返回配置的基本类型的默认值
	 * 否则，返回null
	 */
	public T convert(Object source, Class<?> componentType){
		if(source==null){
			if(componentType.isPrimitive())
				return getDefaultValue(componentType);
			return null;
		}
		return doConvert(source, componentType);
	}
	
	protected T getDefaultValue(Class<?> componentType){
		return defValue;
	}

	abstract protected T doConvert(Object source, Class<?> componentType);
	
//	final private T defaultValue;
//
//	public AbstractTypeConvert(T defaultValue) {
//		super();
//		this.defaultValue = defaultValue;
//	}
//
//	public T getDefaultValue() {
//		return defaultValue;
//	}
	

}
