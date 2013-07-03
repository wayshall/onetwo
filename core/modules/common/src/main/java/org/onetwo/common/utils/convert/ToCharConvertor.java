package org.onetwo.common.utils.convert;

public class ToCharConvertor implements TypeConvert<Character> {

	private ToLongConvertor longdelegate = new ToLongConvertor();
	
	@Override
	public Character convert(Object value, Class<?> componentType) {
		return Character.valueOf((char)longdelegate.convert(value, componentType).shortValue());

	}

}
