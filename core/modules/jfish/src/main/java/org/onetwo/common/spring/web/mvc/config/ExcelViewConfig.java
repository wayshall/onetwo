package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.excel.ExcelTemplateExcelViewResolver;
import org.onetwo.common.excel.XmlTemplateExcelViewResolver;
import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;
import org.onetwo.common.spring.web.mvc.view.JFishExcelTemplateView;
import org.onetwo.common.spring.web.mvc.view.JsonExcelView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelViewConfig {

	@Bean
	public XmlTemplateExcelViewResolver xmlTemplateExcelViewResolver(){
		XmlTemplateExcelViewResolver resolver = new XmlTemplateExcelViewResolver();
		resolver.setViewClass(JsonExcelView.class);
		return resolver;
	}
	
	@Bean
	public ExcelTemplateExcelViewResolver excelTemplateResolver(){
		ExcelTemplateExcelViewResolver resolver = new ExcelTemplateExcelViewResolver();
		resolver.setViewClass(JFishExcelTemplateView.class);
		return resolver;
	}

	
	@Bean
	public XmlTemplateGeneratorFactory xmlTemplateGeneratorFactory(){
		/*String className = "org.onetwo.common.excel.POIExcelGeneratorImpl";
		DefaultXmlTemplateExcelFacotory factory = null;
		if(ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader())){
			factory = new DefaultXmlTemplateExcelFacotory();
//			factory.setCacheTemplate(true);
		}else{
			logger.warn("there is not bean implements [" + className + "]");
		}
		return factory;*/
		return xmlTemplateExcelViewResolver().getXmlTemplateGeneratorFactory();
	}
}
