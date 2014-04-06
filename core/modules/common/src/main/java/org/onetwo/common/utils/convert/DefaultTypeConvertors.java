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
		register(new ToLongConvertor(null), Long.class);
		register(new ToLongConvertor(0L), Long.TYPE);
		register(new ToIntegerConvertor(null), Integer.class);
		register(new ToIntegerConvertor(0), Integer.TYPE);
		register(new ToShortConvertor(null), Short.class);
		register(new ToShortConvertor((short)0), Short.TYPE);
		register(new ToDoubleConvertor(), Double.class);
		register(new ToDoubleConvertor(0.0D), Double.TYPE);
		register(new ToFloatConvertor(null), Float.class);
		register(new ToFloatConvertor(0.0f), Float.TYPE);
		register(new ToBooleanConvertor(null), Boolean.class);
		register(new ToBooleanConvertor(false), Boolean.TYPE);
		register(new ToByteConvertor(null), Byte.class);
		register(new ToByteConvertor((byte)0), Byte.TYPE);
		register(new ToCharConvertor(null), Character.class);
		register(new ToCharConvertor('\u0000'), Character.TYPE);
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
