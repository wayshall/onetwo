package org.onetwo.common.utils.convert;

public class ToIntegerConvertor implements TypeConvert<Integer> {

	private ToLongConvertor longConvertor = new ToLongConvertor();
	
	@Override
	public Integer convert(Object value, Class<?> componentType) {
		return longConvertor.convert(value, componentType).intValue();

	}

}
