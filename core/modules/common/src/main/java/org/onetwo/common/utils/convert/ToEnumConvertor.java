package org.onetwo.common.utils.convert;

import org.onetwo.common.utils.StringUtils;


@SuppressWarnings("rawtypes")
public class ToEnumConvertor extends AbstractTypeConvert<Enum<?>> {

	protected ToEnumConvertor() {
		super(false, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enum<?> doConvert(Object value, Class<?> componentType) {
		if(Integer.class.isInstance(value)){
			Enum<?>[] values = (Enum<?>[]) componentType.getEnumConstants();
			int ordinal = (Integer)value;
			return ordinal>values.length?null:values[ordinal];
		}else{
	        String name = value.toString();
	        if(StringUtils.isBlank(name))
	        	return null;
	        return Enum.valueOf((Class)componentType, name);
		}
	}

}
