package org.onetwo.common.utils.convert;

abstract public class AbstractTypeConvert<T> implements TypeConvert<T>{

	protected Convertor convertor;

	public AbstractTypeConvert(Convertor convertor) {
		super();
		this.convertor = convertor;
	}

	public Convertor getConvertor() {
		return convertor;
	}
	
}
