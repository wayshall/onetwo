package org.onetwo.boot.core.web.controller;


import java.util.Date;

import org.onetwo.boot.bugfix.FixWebDataBinder;
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

	/***
	 * 漏洞见：
	 * http://blog.nsfocus.net/cve-2022-22965/
	 * https://paper.seebug.org/1877/
	 * @param binder
	 */
	@InitBinder
	default public void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(Date.class, new JFishDateEditor());
//		binder.registerCustomEditor(JsonNode.class, new JsonNodeEditor());
		// 设置不允许绑定以下属性，避免webshell漏洞
		FixWebDataBinder.fixWebShellVulnerability(binder);
	}
}
