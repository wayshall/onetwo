package org.onetwo.common.excel;

import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;
import org.onetwo.common.spring.web.mvc.view.JFishExcelView;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class XmlTemplateExcelViewResolver extends UrlBasedViewResolver implements InitializingBean {
	public static final String DEFAULT_BASE_TEMPLATE_DIR = "/WEB-INF/excel/";
	
	private XmlTemplateGeneratorFactory xmlTemplateGeneratorFactory;
	
	public XmlTemplateExcelViewResolver(){
		this.setViewClass(requiredViewClass());
		this.setPrefix(DEFAULT_BASE_TEMPLATE_DIR);
		this.setContentType("application/jfxls;charset=utf-8");
		this.setSuffix(".xml");
	}

	@Override
	protected Class<?> requiredViewClass() {
		return JFishExcelView.class;
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.xmlTemplateGeneratorFactory==null){
			DefaultXmlTemplateExcelFacotory xmlf = new DefaultXmlTemplateExcelFacotory();
			xmlf.afterPropertiesSet();
			this.xmlTemplateGeneratorFactory = xmlf;
		}
	}

	@Override
	protected JFishExcelView buildView(String viewName) throws Exception {
		JFishExcelView exceView = (JFishExcelView)super.buildView(viewName);
		exceView.setFileName(viewName);
		exceView.setXmlTemplateExcelFactory(xmlTemplateGeneratorFactory);
		return exceView;
	}

	public XmlTemplateGeneratorFactory getXmlTemplateGeneratorFactory() {
		return xmlTemplateGeneratorFactory;
	}

	public void setXmlTemplateGeneratorFactory(XmlTemplateGeneratorFactory xmlTemplateGeneratorFactory) {
		this.xmlTemplateGeneratorFactory = xmlTemplateGeneratorFactory;
	}
	
}
