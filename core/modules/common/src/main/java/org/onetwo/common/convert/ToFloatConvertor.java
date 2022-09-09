package org.onetwo.common.convert;

public class ToFloatConvertor extends AbstractTypeConvert<Float> {

	private ToDoubleConvertor doubleDelegate;

	public ToFloatConvertor() {
		super(0.0f);
		doubleDelegate = new ToDoubleConvertor();
	}
	
	@Override
	public Float doConvert(Object value, Class<?> componentType) {
		Double val = doubleDelegate.convert(value, componentType);
		if (val==null) {
			return null;
		}
		return val.floatValue();

	}

}
