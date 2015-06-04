package org.onetwo.common.utils.convert;

abstract public class AbstractWithConvertorTypeConvert<T> extends AbstractTypeConvert<T>{

	protected Convertor convertor;

	public AbstractWithConvertorTypeConvert(T defValue, Convertor convertor) {
		super(defValue);
		this.convertor = convertor;
	}

	public Convertor getConvertor() {
		return convertor;
	}
	
}
