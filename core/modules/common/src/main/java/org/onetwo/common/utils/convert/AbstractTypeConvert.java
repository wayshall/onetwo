package org.onetwo.common.utils.convert;

abstract public class AbstractTypeConvert<T> implements TypeConvert<T>{
	
	private T defValue;
	
	protected AbstractTypeConvert(){
		this.defValue = null;
	}
	
	protected AbstractTypeConvert(T defValue){
		this.defValue = defValue;
	}
	

	public T convert(Object source, Class<?> componentType){
		if(source==null)
			return getDefaultValue();
		return doConvert(source, componentType);
	}
	
	protected T getDefaultValue(){
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
