package org.onetwo.boot.core.web.view;

import java.io.File;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.web.utils.UserAgentUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class JspResourceViewResolver extends InternalResourceViewResolver {
	
	private String pcPostfix = ".pc";
	private String mobilePostfix = ".wap";

	public View resolveViewName(String viewName, Locale locale) throws Exception {
		String themeViewName = viewName;
		if(themeViewName.endsWith(pcPostfix) || themeViewName.endsWith(mobilePostfix)){
			themeViewName = viewName;
		}else{
			themeViewName = getThemeViewName(viewName);
		}
		return super.resolveViewName(themeViewName, locale);
	}
	
	protected String getThemeViewName(final String viewName){
		HttpServletRequest request = getRequest();
		String newView = viewName;
		boolean isPcView = true;
		if(UserAgentUtils.isMobileRequest(request)){
			newView = viewName + mobilePostfix;
			isPcView = false;
		}else{
			newView = viewName + pcPostfix;
		}
		if(!tryToCheckJspResource(request, newView)){
			newView = viewName;
			if(!tryToCheckJspResource(request, newView)){
				//反转
				newView = isPcView?(viewName + mobilePostfix):(viewName + pcPostfix);
			}
		}
		return newView;
	}
	
	protected boolean tryToCheckJspResource(HttpServletRequest request, String viewName){
		ServletContext sc = request.getServletContext();
		String jsp = getPrefix() + viewName + getSuffix();
		ServletContextResource scr = new ServletContextResource(sc, jsp);
		if(scr.exists()){
			return true;
		}
		String path = sc.getRealPath(jsp);
		if(StringUtils.isBlank(path)){
			return false;
		}
		File jspFile = new File(path);
		return jspFile.exists();
	}

	public static HttpServletRequest getRequest(){
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs==null?null:attrs.getRequest();
	}

	public void setPcPostfix(String pcPostfix) {
		this.pcPostfix = pcPostfix;
	}

	public void setMobilePostfix(String mobilePostfix) {
		this.mobilePostfix = mobilePostfix;
	}
	
}
