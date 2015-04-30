package org.onetwo.plugins.admin.web.controller.data;

import javax.annotation.Resource;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.web.csrf.CsrfValid;
import org.onetwo.plugins.admin.model.data.service.impl.DataInitServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/data/")
@Controller
public class DataInitController extends PluginSupportedController {
	
	@Resource
	private DataInitServiceImpl dataInitServiceImpl;

	@RequestMapping(value="/init", method=RequestMethod.GET)
	public ModelAndView init(){
		return pluginMv("/data/init-index");
	}
	
	@CsrfValid(false)
	@RequestMapping(value="/init", method=RequestMethod.POST)
	public ModelAndView initData(){
		this.dataInitServiceImpl.initDbdata();
		return pluginMv("/data/init-index", MESSAGE, "初始化成功！");
	}
	
}
