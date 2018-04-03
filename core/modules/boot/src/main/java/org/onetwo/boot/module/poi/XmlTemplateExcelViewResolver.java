package org.onetwo.boot.module.poi;

import java.util.Objects;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.ext.poi.excel.interfaces.XmlTemplateGeneratorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class XmlTemplateExcelViewResolver extends UrlBasedViewResolver implements InitializingBean {
	public static final String DEFAULT_BASE_TEMPLATE_DIR = "META-INF/resources/excel-view/";
	
	private XmlTemplateGeneratorFactory xmlTemplateGeneratorFactory;
	@Autowired
	private BootSiteConfig bootSiteConfig;
	@Autowired
	private PluginManager pluginManager;
	
	public XmlTemplateExcelViewResolver(){
		this.setViewClass(requiredViewClass());
		this.setPrefix(DEFAULT_BASE_TEMPLATE_DIR);
		this.setContentType("application/jfxls;charset=utf-8");
		this.setSuffix(".xml");
	}

	public String getSuffix() {
		return super.getSuffix();
	}

	public String getPrefix() {
		return super.getPrefix();
	}

	@Override
	protected Class<?> requiredViewClass() {
		return PoiExcelView.class;
	}
	

	public boolean isCache() {
		return bootSiteConfig.isProduct();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Objects.requireNonNull(xmlTemplateGeneratorFactory, "xmlTemplateGeneratorFactory can not be null!");
	}

	@Override
	protected PoiExcelView buildView(String viewName) throws Exception {
		String actualView = pluginManager.resovleAsCurrentPluginPath(viewName, plugin->plugin.getPluginMeta().getName());
		PoiExcelView exceView = (PoiExcelView)super.buildView(actualView);
//		exceView.setFileName(actualView);
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
