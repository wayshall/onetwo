package org.onetwo.common.convert;

public class ToCharConvertor extends AbstractTypeConvert<Character> {

	private ToLongConvertor longDelegate;

	public ToCharConvertor() {
		super('\u0000');
		this.longDelegate = new ToLongConvertor();
	}
	
	@Override
	public Character doConvert(Object value, Class<?> componentType) {
		return Character.valueOf((char)longDelegate.convert(value, componentType).shortValue());

	}

}
