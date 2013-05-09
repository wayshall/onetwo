package org.onetwo.common.utils.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class DefaultTypeConvertors implements Convertor {
	
	
	private Map<Class<?>, TypeConvert<?>> convertors = new HashMap<Class<?>, TypeConvert<?>>();
	
	public DefaultTypeConvertors(){
		register(new ToLongConvertor(), Long.class, Long.TYPE);
		register(new ToIntegerConvertor(), Integer.class, Integer.TYPE);
		register(new ToShortConvertor(), Short.class, Short.TYPE);
		register(new ToDoubleConvertor(), Double.class, Double.TYPE);
		register(new ToFloatConvertor(), Float.class, Float.TYPE);
		register(new ToBooleanConvertor(), Boolean.class, Boolean.TYPE);
		register(new ToByteConvertor(), Byte.class, Byte.TYPE);
		register(new ToCharConvertor(), Character.class, Character.TYPE);
		
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
		return (TypeConvert<T>)convertors.get(clazz);
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.convert.TypeConvertor#convert(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <T> T convert(Object value, Class<T> targetClass){
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
