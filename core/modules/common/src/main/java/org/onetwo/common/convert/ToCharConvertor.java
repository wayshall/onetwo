package org.onetwo.common.convert;

import org.onetwo.common.exception.BaseException;

public class ToCharConvertor extends AbstractTypeConvert<Character> {

	private ToLongConvertor longDelegate;

	public ToCharConvertor() {
		super('\u0000');
		this.longDelegate = new ToLongConvertor();
	}
	
	@Override
	public Character doConvert(Object value, Class<?> componentType) {
		if (value instanceof Character) {
			return (Character) value;
		} else if (value instanceof String) {
			char[] chars = value.toString().toCharArray();
			if (chars.length!=1) {
				throw new BaseException("[" + value + "] can not convert to " + Character.class);
			}
			return chars[0];
		}
		return Character.valueOf((char)longDelegate.convert(value, componentType).shortValue());

	}

}
