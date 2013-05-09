package org.onetwo.common.utils.convert;

public class ToShortConvertor implements TypeConvert<Short> {

	private ToLongConvertor longConvertor = new ToLongConvertor();
	
	@Override
	public Short convert(Object value, Class<?> componentType) {
		return longConvertor.convert(value, componentType).shortValue();

	}

}
