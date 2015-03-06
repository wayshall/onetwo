package org.onetwo.plugins.admin.web.controller.data;


import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.plugins.admin.DataModule.ImportData;
import org.onetwo.plugins.admin.model.data.service.DictionaryService;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/data/dictionary/import")
@Controller
public class DictionaryImportController extends PluginSupportedController {
	 
	@Autowired
	private DictionaryService dictionaryService;


	@ByMenuClass(codeClass=ImportData.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request){
		Resource dictResource = SpringUtils.newClassPathResource("/data/dict.xml");
		return pluginMv("/data/dictionary-import", "dictResource", dictResource);
	}

	@ByFunctionClass(codeClass=ImportData.class)
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView importDatas(HttpServletRequest request){
		int count = this.dictionaryService.importDatas();
		return messageMv("已同步"+count+"条字典数据！");
	}

	
}