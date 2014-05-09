package org.onetwo.common.utils.convert;

public class ToShortConvertor extends AbstractTypeConvert<Short> {

private ToLongConvertor longConvertor;
	
	public ToShortConvertor(Short defValue) {
		super(defValue);
		this.longConvertor = new ToLongConvertor(defValue==null?null:defValue.longValue());
	}
	
	@Override
	public Short doConvert(Object value, Class<?> componentType) {
		return longConvertor.convert(value, componentType).shortValue();

	}

}
