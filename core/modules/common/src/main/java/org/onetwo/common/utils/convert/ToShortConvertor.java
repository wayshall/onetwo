package org.onetwo.common.utils.convert;

public class ToShortConvertor extends AbstractTypeConvert<Short> {

private ToLongConvertor longConvertor;
	
	public ToShortConvertor() {
		super((short)0);
		this.longConvertor = new ToLongConvertor();
	}
	
	@Override
	public Short doConvert(Object value, Class<?> componentType) {
		return longConvertor.convert(value, componentType).shortValue();

	}

}
