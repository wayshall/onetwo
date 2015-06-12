package org.onetwo.common.spring.underline;

import org.onetwo.common.utils.ReflectUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

abstract public interface UnderlineInitBinder {

	@InitBinder
	default public void initBinder(WebDataBinder binder){
		ConversionService conversionService = binder.getConversionService();
		UnderlineBeanPropertyBindingResult binding = new UnderlineBeanPropertyBindingResult(binder.getTarget(), binder.getObjectName());
		if(conversionService!=null){
			binding.initConversion(conversionService);
		}
		ReflectUtils.setFieldValue("bindingResult", binder, binding);
	}
}
