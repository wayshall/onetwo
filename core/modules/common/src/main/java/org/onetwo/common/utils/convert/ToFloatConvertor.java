package org.onetwo.common.utils.convert;

public class ToFloatConvertor extends AbstractTypeConvert<Float> {

	private ToDoubleConvertor doubleDelegate;

	public ToFloatConvertor(Float defValue) {
		super(defValue);
		doubleDelegate = new ToDoubleConvertor(defValue==null?null:defValue.doubleValue());
	}
	
	@Override
	public Float doConvert(Object value, Class<?> componentType) {
		return doubleDelegate.convert(value, componentType).floatValue();

	}

}
