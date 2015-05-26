package org.onetwo.common.spring.web.mvc.config;

import javax.annotation.Resource;

import org.onetwo.common.fish.spring.config.JFishAppConfigrator;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.JFishJaxb2Marshaller;
import org.onetwo.common.spring.web.mvc.MvcSetting;
import org.onetwo.common.spring.web.mvc.view.JsonView;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.xml.MarshallingView;

@Configuration
public class JsonXmlViewConfig {

	@Resource
	private JFishMvcApplicationContext applicationContext;
	
	@Autowired
	private JFishAppConfigrator jfishAppConfigurator;

	@Resource
	private MvcSetting mvcSetting;
	
	@Bean
	public View jsonView() {
		JsonView jview = SpringUtils.getHighestOrder(applicationContext, JsonView.class);
		if(jview==null){
			jview = new JsonView();
		}
		return jview;
	}

	@Bean
	public View xmlView() {
		MarshallingView view = new MarshallingView();
		view.setMarshaller(jaxb2Marshaller());
		return view;
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		JFishJaxb2Marshaller marshaller = new JFishJaxb2Marshaller();
		
		if(jfishAppConfigurator!=null && !LangUtils.isEmpty(jfishAppConfigurator.getXmlBasePackages())){
			marshaller.setClassesToBeBoundByBasePackages(jfishAppConfigurator.getXmlBasePackages());
		}else{
			String xmlBasePackage = this.mvcSetting.getMvcSetting().getProperty("xml.base.packages");
			Assert.hasText(xmlBasePackage, "xmlBasePackage in mvc.properties must has text");
			marshaller.setXmlBasePackage(xmlBasePackage);
		}
		return marshaller;
	}
}
