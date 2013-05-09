package org.onetwo.common.utils.convert;

public class ToByteConvertor implements TypeConvert<Byte> {

	private ToLongConvertor longDelegate = new ToLongConvertor();
	
	@Override
	public Byte convert(Object value, Class<?> componentType) {
		return Byte.valueOf(longDelegate.convert(value, componentType).byteValue());

	}

}
