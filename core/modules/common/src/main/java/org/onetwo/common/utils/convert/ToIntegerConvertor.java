package org.onetwo.common.utils.convert;

public class ToIntegerConvertor extends AbstractTypeConvert<Integer> {

	private ToLongConvertor longConvertor;
	
	public ToIntegerConvertor(Integer defValue) {
		super(defValue);
		this.longConvertor = new ToLongConvertor(defValue==null?null:defValue.longValue());
	}



	@Override
	public Integer doConvert(Object value, Class<?> componentType) {
		return longConvertor.convert(value, componentType).intValue();
	}

}
