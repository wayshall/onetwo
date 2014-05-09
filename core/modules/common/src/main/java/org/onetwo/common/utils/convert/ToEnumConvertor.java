package org.onetwo.common.utils.convert;


@SuppressWarnings("rawtypes")
public class ToEnumConvertor extends AbstractTypeConvert<Enum<?>> {

	@SuppressWarnings("unchecked")
	@Override
	public Enum<?> doConvert(Object value, Class<?> componentType) {
		if(Integer.class.isInstance(value)){
			Enum<?>[] values = (Enum<?>[]) componentType.getEnumConstants();
			int ordinal = (Integer)value;
			return ordinal>values.length?null:values[ordinal];
		}else{
	        String name = value.toString();
	        return Enum.valueOf((Class)componentType, name);
		}
	}

}
