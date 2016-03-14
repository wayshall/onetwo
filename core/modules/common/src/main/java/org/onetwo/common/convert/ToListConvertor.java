package org.onetwo.common.convert;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class ToListConvertor extends AbstractWithConvertorTypeConvert<List<?>>{

	private ToArrayConvertor arrayDelegate;
	public ToListConvertor(Convertor convertor) {
		super(Collections.EMPTY_LIST, convertor);
		this.arrayDelegate = new ToArrayConvertor(convertor);
	}

	@Override
	public List<?> doConvert(Object source, Class<?> componentType) {
		Object array = this.arrayDelegate.convert(source, componentType);
		return LangUtils.asList(array);
	}

}
