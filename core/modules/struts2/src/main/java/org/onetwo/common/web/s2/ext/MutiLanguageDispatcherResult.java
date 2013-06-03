package org.onetwo.common.web.s2.ext;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.config.TemplatePathMapper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.I18nInterceptor;

@SuppressWarnings("serial")
public class MutiLanguageDispatcherResult extends ServletDispatcherResult{
	

	private boolean autoLanguage;
	private String resultPath = "";
	protected TemplatePathMapper templatePathMapper;
	protected Container container;
	
	public MutiLanguageDispatcherResult() {
		super();
		this.autoLanguage = SiteConfig.inst().isStrutsAutoLanguage();
	}

	public MutiLanguageDispatcherResult(String location) {
		super(location);
		this.autoLanguage = SiteConfig.inst().isStrutsAutoLanguage();
	}

	@Inject("struts.convention.result.path")
	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}
	
	@Inject
	public void setContainer(Container container) {
		this.container = container;
		this.templatePathMapper = container.getInstance(TemplatePathMapper.class, container.getInstance(String.class, ExtConstant.EXT_TEMPLATE_PATH_MAPER));
	}
	
	protected boolean isTemplateEnable(){
		return this.templatePathMapper!=null && this.templatePathMapper.isEnable();
	}

	@Override
	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {

		Locale locale = (Locale)invocation.getInvocationContext().getSession().get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
		
		if(locale==null){
			locale = LocaleUtils.getDefault();
			invocation.getInvocationContext().getSession().put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, locale);
		}

		if(autoLanguage){
			if(this.resultPath.length()>1 && finalLocation.startsWith(this.resultPath))
				finalLocation = finalLocation.substring(this.resultPath.length());
			
			int index = finalLocation.indexOf("/", 1);
			String langPath = null;
			if(index!=-1){ 
				langPath = finalLocation.substring(1, index);
				if(!LocaleUtils.isSupport(langPath))
					langPath = LocaleUtils.getDefault().getLanguage();
			}
			
			if(langPath==null){
				langPath = "/" + locale.toString().toLowerCase();
				finalLocation = langPath + finalLocation;
			}
			else if(!langPath.equals(locale.getLanguage())){//replace if diff from session locale
				langPath = "/" + locale.toString().toLowerCase();
				finalLocation = langPath + finalLocation.substring(index);
			}
			if(!this.resultPath.equals("/"))
				finalLocation = this.resultPath + finalLocation;
		}
		
		if(isTemplateEnable())
			finalLocation = this.templatePathMapper.parse(finalLocation, true);
		
		if(StringUtils.isBlank(finalLocation))
			throw new ServiceException("can not find resource path: " + finalLocation);
		
		super.doExecute(finalLocation, invocation);
	}

}
