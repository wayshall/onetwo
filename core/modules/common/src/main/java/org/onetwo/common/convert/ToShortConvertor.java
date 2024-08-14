package org.onetwo.common.convert;

public class ToShortConvertor extends AbstractTypeConvert<Short> {

private ToLongConvertor longConvertor;
	
	public ToShortConvertor() {
		super((short)0);
		this.longConvertor = new ToLongConvertor();
	}
	
	@Override
	public Short doConvert(Object value, Class<?> componentType) {
		Long val = longConvertor.convert(value, componentType);
		if (val==null) {
			return null;
		}
		return val.shortValue();

	}

}
