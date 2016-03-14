package appweb.admin.web.controller;

import org.onetwo.common.excel.XmlTemplateExcelViewResolver;
import org.onetwo.common.spring.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController extends WebBaseController{
	

	@RequestMapping("/index")
	public ModelAndView index(){
		XmlTemplateExcelViewResolver resolver = SpringApplication.getInstance().getBean(XmlTemplateExcelViewResolver.class);
		return mv("index");
	}


}
