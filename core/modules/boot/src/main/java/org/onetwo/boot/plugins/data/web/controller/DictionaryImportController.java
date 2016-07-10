package org.onetwo.boot.plugins.data.web.controller;


import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.plugin.web.PluginBaseController;
import org.onetwo.boot.plugins.data.service.DictionaryService;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/plugins/data/dictionary/import")
@Controller
public class DictionaryImportController extends PluginBaseController {
	 
	@Autowired
	private DictionaryService dictionaryService;
	private String dataXmlPath = "/plugins/data/dict.xml";
	
	public DictionaryImportController() {
		super();
	}

	public DictionaryImportController(String dataXmlPath) {
		super();
		this.dataXmlPath = dataXmlPath;
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request){	
		Resource dictResource = SpringUtils.newClassPathResource("/data/dict.xml");
		return pluginMv("/data/dictionary-import", "dictResource", dictResource);
	}

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView importDatas(HttpServletRequest request){
		Assert.hasText(dataXmlPath);
		int count = this.dictionaryService.importDatas(dataXmlPath);
		return messageMv("已同步"+count+"条字典数据！");
	}

	
}