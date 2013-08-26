package org.onetwo.common.utils.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("unchecked")
public class DefaultTypeConvertors implements Convertor {
	
	
	private Map<Class<?>, TypeConvert<?>> convertors = new HashMap<Class<?>, TypeConvert<?>>();
	
	public DefaultTypeConvertors(){
		register(new ToStringConvertor(), String.class);
		register(new ToLongConvertor(), Long.class, Long.TYPE);
		register(new ToIntegerConvertor(), Integer.class, Integer.TYPE);
		register(new ToShortConvertor(), Short.class, Short.TYPE);
		register(new ToDoubleConvertor(), Double.class, Double.TYPE);
		register(new ToFloatConvertor(), Float.class, Float.TYPE);
		register(new ToBooleanConvertor(), Boolean.class, Boolean.TYPE);
		register(new ToByteConvertor(), Byte.class, Byte.TYPE);
		register(new ToCharConvertor(), Character.class, Character.TYPE);
		register(new ToDateConvertor(), Date.class);
		
		register(new ToBigDecemalConvertor(), BigDecimal.class);
		register(new ToBigIntegerConvertor(), BigInteger.class);
		register(new ToEnumConvertor(), Enum.class);
		register(new ToArrayConvertor(this), Object[].class);
		register(new ToListConvertor(this), List.class);
	}
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.convert.TypeConvertor#register(org.onetwo.common.utils.convert.TypeConvert, java.lang.Class)
	 */
	@Override
	public final Convertor register(TypeConvert<?> convertor, Class<?>... classes){
		for(Class<?> cls : classes){
			convertors.put(cls, convertor);
		}
		return this;
	}
	
	public <T> TypeConvert<T> getTypeConvertor(Class<T> clazz){
		TypeConvert<T> convert = (TypeConvert<T>)convertors.get(clazz);
		if(convert==null)
			throw new BaseException("type convertor not found : " + clazz);
		return convert;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.convert.TypeConvertor#convert(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <T> T convert(Object value, Class<T> targetClass){
		if(targetClass==null)
			throw new BaseException("targetClass can not be null, value: " + value);
		if(targetClass.isEnum()){
			return (T)getTypeConvertor(targetClass.getSuperclass()).convert(value, targetClass);
		}else{
			return getTypeConvertor(targetClass).convert(value, targetClass);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.convert.TypeConvertor#convert(java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public <T> T convert(Object value, Class<?> targetClass, Class<?> componentType){
		return (T)getTypeConvertor(targetClass).convert(value, componentType);
	}
}
