package org.onetwo.common.convert;

public class ToIntegerConvertor extends AbstractTypeConvert<Integer> {

	private ToLongConvertor longConvertor;
	
	public ToIntegerConvertor() {
		super(0);
		this.longConvertor = new ToLongConvertor();
	}



	@Override
	public Integer doConvert(Object value, Class<?> componentType) {
		Long val = longConvertor.convert(value, componentType);
		if (val==null) {
			return null;
		}
		return val.intValue();
	}

}
