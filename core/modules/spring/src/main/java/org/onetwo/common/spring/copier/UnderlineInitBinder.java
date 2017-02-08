package org.onetwo.common.spring.copier;

import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

abstract public interface UnderlineInitBinder {
	
	static public void resetDataBinder(WebDataBinder binder){
		ConversionService conversionService = binder.getConversionService();
		UnderlineBeanPropertyBindingResult binding = new UnderlineBeanPropertyBindingResult(binder.getTarget(), binder.getObjectName());
		if(conversionService!=null){
			binding.initConversion(conversionService);
		}
//		Reflection.setField(binder, "bindingResult", binding);
		ReflectUtils.setBeanFieldValue("bindingResult", binder, binding);
	}
	

	@InitBinder
	default public void initBinder(WebDataBinder binder){
		resetDataBinder(binder);
	}

}
