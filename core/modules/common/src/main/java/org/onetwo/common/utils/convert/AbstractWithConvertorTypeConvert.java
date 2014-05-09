package org.onetwo.common.utils.convert;

abstract public class AbstractWithConvertorTypeConvert<T> extends AbstractTypeConvert<T>{

	protected Convertor convertor;

	public AbstractWithConvertorTypeConvert(Convertor convertor) {
		super();
		this.convertor = convertor;
	}

	public Convertor getConvertor() {
		return convertor;
	}
	
}
