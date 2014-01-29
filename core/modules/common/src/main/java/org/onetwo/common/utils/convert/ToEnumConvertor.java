package org.onetwo.common.utils.convert;


@SuppressWarnings("rawtypes")
public class ToEnumConvertor implements TypeConvert<Enum<?>> {

	@SuppressWarnings("unchecked")
	@Override
	public Enum<?> convert(Object value, Class<?> componentType) {
		if (value == null)
            return null;
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
