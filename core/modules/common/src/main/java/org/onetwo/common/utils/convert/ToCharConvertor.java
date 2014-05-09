package org.onetwo.common.utils.convert;

public class ToCharConvertor extends AbstractTypeConvert<Character> {

	private ToLongConvertor longDelegate;

	public ToCharConvertor(Character defValue) {
		super(defValue);
		this.longDelegate = new ToLongConvertor(null);
	}
	
	@Override
	public Character doConvert(Object value, Class<?> componentType) {
		return Character.valueOf((char)longDelegate.convert(value, componentType).shortValue());

	}

}
