package org.onetwo.common.utils.convert;

import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class ToListConvertor extends AbstractTypeConvert<List<?>>{

	private ToArrayConvertor arrayDelegate;
	public ToListConvertor(Convertor convertor) {
		super(convertor);
		this.arrayDelegate = new ToArrayConvertor(convertor);
	}

	@Override
	public List<?> convert(Object source, Class<?> componentType) {
		Object array = this.arrayDelegate.convert(source, componentType);
		return LangUtils.asList(array);
	}

}
