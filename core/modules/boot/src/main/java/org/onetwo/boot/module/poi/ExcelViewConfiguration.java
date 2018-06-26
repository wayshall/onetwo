package org.onetwo.boot.module.poi;

import org.onetwo.ext.poi.excel.interfaces.XmlTemplateGeneratorFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value=PoiProperties.EXPORT_VIEW_ENABLE_KEY, matchIfMissing=true)
@ConditionalOnClass(XmlTemplateGeneratorFactory.class)
@EnableConfigurationProperties(value=PoiProperties.class)
public class ExcelViewConfiguration {

	@Bean
	public XmlTemplateExcelViewResolver xmlTemplateExcelViewResolver(){
		XmlTemplateExcelViewResolver resolver = new XmlTemplateExcelViewResolver();
		resolver.setViewClass(PoiExcelView.class);
		resolver.setXmlTemplateGeneratorFactory(xmlTemplateGeneratorFactory());
		return resolver;
	}
	
	/*@Bean
	public ExcelTemplateExcelViewResolver excelTemplateResolver(){
		ExcelTemplateExcelViewResolver resolver = new ExcelTemplateExcelViewResolver();
		resolver.setViewClass(JFishExcelTemplateView.class);
		return resolver;
	}*/

	@Bean
	@ConditionalOnMissingBean(XmlTemplateGeneratorFactory.class)
	public XmlTemplateGeneratorFactory xmlTemplateGeneratorFactory(){
		return new DefaultXmlTemplateExcelFacotory();
	}
}
