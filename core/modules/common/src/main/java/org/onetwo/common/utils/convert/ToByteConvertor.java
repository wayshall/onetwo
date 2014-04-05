package org.onetwo.common.utils.convert;

public class ToByteConvertor extends AbstractTypeConvert<Byte> {

	private ToLongConvertor longDelegate;

	public ToByteConvertor(Byte defValue) {
		super(defValue);
		this.longDelegate = new ToLongConvertor(defValue==null?null:defValue.longValue());
	}
	
	@Override
	public Byte doConvert(Object value, Class<?> componentType) {
		return Byte.valueOf(longDelegate.convert(value, componentType).byteValue());

	}

}
