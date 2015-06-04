package org.onetwo.common.utils.convert;

public class ToFloatConvertor extends AbstractTypeConvert<Float> {

	private ToDoubleConvertor doubleDelegate;

	public ToFloatConvertor() {
		super(0.0f);
		doubleDelegate = new ToDoubleConvertor();
	}
	
	@Override
	public Float doConvert(Object value, Class<?> componentType) {
		return doubleDelegate.convert(value, componentType).floatValue();

	}

}
