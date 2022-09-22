package org.onetwo.boot.core.web.controller;


import java.util.Date;

import org.onetwo.common.spring.copier.UnderlineBeanPropertyBindingResult;
import org.onetwo.common.spring.propeditor.JFishDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/***
 * @see UnderlineBeanPropertyBindingResult
 * @author way
 *
 */
public interface DateInitBinder {

	@InitBinder
	default public void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(Date.class, new JFishDateEditor());
//		binder.registerCustomEditor(JsonNode.class, new JsonNodeEditor());
	}
}
