package org.onetwo.common.utils.convert;

public class ToIntegerConvertor extends AbstractTypeConvert<Integer> {

	private ToLongConvertor longConvertor;
	
	public ToIntegerConvertor() {
		super(0);
		this.longConvertor = new ToLongConvertor();
	}



	@Override
	public Integer doConvert(Object value, Class<?> componentType) {
		return longConvertor.convert(value, componentType).intValue();
	}

}
