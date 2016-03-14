package org.onetwo.common.convert;

public class ToByteConvertor extends AbstractTypeConvert<Byte> {

	private ToLongConvertor longDelegate;

	public ToByteConvertor() {
		super((byte)0);
		this.longDelegate = new ToLongConvertor();
	}
	
	@Override
	public Byte doConvert(Object value, Class<?> componentType) {
		return Byte.valueOf(longDelegate.convert(value, componentType).byteValue());

	}

}
