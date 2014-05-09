package org.onetwo.plugins.jasper.web;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReslover extends UrlBasedViewResolver implements InitializingBean {
	public static final String REPORT_DATA_KEY = "reportData";
	public static final String DEFAULT_BASE_TEMPLATE_DIR = "/WEB-INF/jasper/";
	public static final Map<String, Class<?>> VIEW_CLASS_MAPPER;
	
	static {
		Map<String, Class<?>> temp = LangUtils.newHashMap(5);
		temp.put("xls", ExtJasperReportsXlsView.class);
		VIEW_CLASS_MAPPER = Collections.unmodifiableMap(temp);
	}
	

	private Boolean exposePathVariablesHolder;
	private DataSource dataSource;
	
	public JasperReslover(){
		this.setPrefix(DEFAULT_BASE_TEMPLATE_DIR);
//		this.setContentType("application/jreport");
		this.setViewClass(requiredViewClass());
		this.setSuffix(".jrxml");
		this.setCache(false);
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		this.dataSource = SpringUtils.getBean(getApplicationContext(), DataSource.class);
	}

	@Override
	protected Class<?> requiredViewClass() {
		return AbstractJasperReportsSingleFormatView.class;
	}

	public void setExposePathVariables(Boolean exposePathVariables) {
		this.exposePathVariablesHolder = exposePathVariables;
		super.setExposePathVariables(exposePathVariables);
	}
	
	
	
	public Boolean getExposePathVariablesHolder() {
		return exposePathVariablesHolder;
	}

	protected View loadView(String viewName, Locale locale) throws Exception {
		String extName = FileUtils.getExtendName(viewName);
		if(extName==null || !VIEW_CLASS_MAPPER.containsKey(extName.toLowerCase()))
			return null;
		return super.loadView(viewName, locale);
	}
	
	@Override
	protected AbstractJasperReportsSingleFormatView buildView(String viewName) throws Exception {
		String extName = FileUtils.getExtendName(viewName);
		Class<?> viewClass = getViewClassByExtName(extName);
		
		AbstractJasperReportsSingleFormatView view = (AbstractJasperReportsSingleFormatView) BeanUtils.instantiateClass(viewClass);
		String viewNameNoExt = StringUtils.substringBefore(viewName, "."+extName);
		view.setUrl(getPrefix() + viewNameNoExt + getSuffix());
		
//		view.setContentType("application/msexcel");
		
		view.setJdbcDataSource(dataSource);
		view.setReportDataKey(REPORT_DATA_KEY);
		view.setRequestContextAttribute(getRequestContextAttribute());
		view.setAttributesMap(getAttributesMap());
		if (this.getExposePathVariablesHolder() != null) {
			view.setExposePathVariables(this.getExposePathVariablesHolder());
		}
		
		return view;
	}
	
	protected Class<?> getViewClassByExtName(String extName){
		return VIEW_CLASS_MAPPER.get(extName.toLowerCase());
	}

}
