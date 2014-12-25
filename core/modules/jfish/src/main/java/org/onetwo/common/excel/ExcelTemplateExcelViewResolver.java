package org.onetwo.common.excel;

import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;
import org.onetwo.common.spring.web.mvc.view.AbstractJFishExcelView;
import org.onetwo.common.spring.web.mvc.view.JFishExcelTemplateView;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class ExcelTemplateExcelViewResolver extends UrlBasedViewResolver implements InitializingBean {
	public static final String DEFAULT_BASE_TEMPLATE_DIR = "/WEB-INF/excel/";
	
	private XmlTemplateGeneratorFactory xmlTemplateGeneratorFactory;
	
	public ExcelTemplateExcelViewResolver(){
		this.setViewClass(requiredViewClass());
		this.setPrefix(DEFAULT_BASE_TEMPLATE_DIR);
		this.setContentType("application/jfxls;charset=utf-8");
		this.setSuffix(".xlsx");
	}

	public String getSuffix() {
		return super.getSuffix();
	}

	public String getPrefix() {
		return super.getPrefix();
	}

	@Override
	protected Class<?> requiredViewClass() {
		return AbstractJFishExcelView.class;
	}
	

	public boolean isCache() {
		return BaseSiteConfig.getInstance().isProduct();
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
	protected JFishExcelTemplateView buildView(String viewName) throws Exception {
		JFishExcelTemplateView exceView = (JFishExcelTemplateView)super.buildView(viewName);
		exceView.setFileName(viewName);
		return exceView;
	}

	public XmlTemplateGeneratorFactory getXmlTemplateGeneratorFactory() {
		return xmlTemplateGeneratorFactory;
	}

	public void setXmlTemplateGeneratorFactory(XmlTemplateGeneratorFactory xmlTemplateGeneratorFactory) {
		this.xmlTemplateGeneratorFactory = xmlTemplateGeneratorFactory;
	}
	
}
