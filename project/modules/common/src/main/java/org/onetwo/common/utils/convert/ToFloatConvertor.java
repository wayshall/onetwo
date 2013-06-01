package org.onetwo.common.utils.convert;

public class ToFloatConvertor implements TypeConvert<Float> {

	private ToDoubleConvertor doubleDelegate = new ToDoubleConvertor();
	
	@Override
	public Float convert(Object value, Class<?> componentType) {
		return doubleDelegate.convert(value, componentType).floatValue();

	}

}
