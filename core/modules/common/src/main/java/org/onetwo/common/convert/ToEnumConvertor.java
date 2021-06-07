package org.onetwo.common.convert;

import org.onetwo.common.utils.LangUtils;
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
			return ordinalToEnum(value, componentType);
		}else{
	        String name = value.toString();
	        if(StringUtils.isBlank(name)) {
	        	return null;
	        }
//	        return Enum.valueOf((Class)componentType, name);
	        
	        if (LangUtils.isNumeric(name)) {
	        	return ordinalToEnum(Integer.parseInt(name), componentType);
	        }
	        
	        // 如果实现了EnumerableTextLabel接口，首先尝试通过text匹配
	        if (EnumerableTextLabel.class.isAssignableFrom(componentType)) {
				Enum<?>[] values = (Enum<?>[]) componentType.getEnumConstants();
				for (Enum<?> ev : values) {
					EnumerableTextLabel label = (EnumerableTextLabel) ev;
					if (label.getLabel().equals(name)) {
						return ev;
					}
				}
	        } 

	        try {
				return Enum.valueOf((Class)componentType, name.trim());
			} catch (IllegalArgumentException e) {
				return Enum.valueOf((Class)componentType, name.trim().toUpperCase());
			}
		}
	}
	
	private Enum<?> ordinalToEnum(Object value, Class<?> componentType) {
		Enum<?>[] values = (Enum<?>[]) componentType.getEnumConstants();
		int ordinal = (Integer)value;
		return ordinal>values.length?null:values[ordinal];
	}

}
