package org.onetwo.common.utils.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.NiceDate;

@SuppressWarnings("unchecked")
public class DefaultTypeConvertors implements Convertor {
	
	
	private Map<Class<?>, TypeConvert<?>> convertors = new HashMap<Class<?>, TypeConvert<?>>();
	
	public DefaultTypeConvertors(){
		register(new ToStringConvertor(), String.class);
		register(new ToLongConvertor(), Long.class);
		register(new ToLongConvertor(), Long.TYPE);
		register(new ToIntegerConvertor(), Integer.class);
		register(new ToIntegerConvertor(), Integer.TYPE);
		register(new ToShortConvertor(), Short.class);
		register(new ToShortConvertor(), Short.TYPE);
		register(new ToDoubleConvertor(), Double.class);
		register(new ToDoubleConvertor(), Double.TYPE);
		register(new ToFloatConvertor(), Float.class);
		register(new ToFloatConvertor(), Float.TYPE);
		register(new ToBooleanConvertor(), Boolean.class);
		register(new ToBooleanConvertor(), Boolean.TYPE);
		register(new ToByteConvertor(), Byte.class);
		register(new ToByteConvertor(), Byte.TYPE);
		register(new ToCharConvertor(), Character.class);
		register(new ToCharConvertor(), Character.TYPE);
		register(new ToDateConvertor(), Date.class);
		
		register(new ToBigDecemalConvertor(), BigDecimal.class);
		register(new ToBigIntegerConvertor(), BigInteger.class);
		register(new ToEnumConvertor(), Enum.class);
		register(new ToArrayConvertor(this), Object[].class);
		register(new ToListConvertor(this), List.class);
		register(new ToNiceDateConvertor(), NiceDate.class);
	}
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.convert.TypeConvertor#register(org.onetwo.common.utils.convert.TypeConvert, java.lang.Class)
	 */
	@Override
	public final Convertor register(TypeConvert<?> convertor, Class<?> clazz){
//		for(Class<?> cls : classes){
//			convertors.put(cls, convertor);
//		}
		convertors.put(clazz, convertor);
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
		if(targetClass.isInstance(value))
			return (T) value;
		T val = null;
		if(targetClass.isEnum()){
			val = (T)getTypeConvertor(targetClass.getSuperclass()).convert(value, targetClass);
		}else{
			val = getTypeConvertor(targetClass).convert(value, targetClass);
		}
		return val;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.convert.TypeConvertor#convert(java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public <T> T convert(Object value, Class<?> targetClass, Class<?> componentType){
		return (T)getTypeConvertor(targetClass).convert(value, componentType);
	}
}
