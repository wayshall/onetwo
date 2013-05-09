package org.onetwo.common.web.filter;

import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.utils.CookieUtil;
import org.onetwo.common.web.utils.WebLocaleUtils;
import org.onetwo.common.web.utils.RequestUtils;

public class PathLocaleSystemInitFilter extends SystemInitFilter {
	
	private static Pattern INDEX_PATTERN = Pattern.compile("^/([^/]+)\\.[\\w]+$");
	
	protected void processLocale(HttpServletRequest request, HttpServletResponse response){
		processLocaleByPath(request, response);
//		processDataLocale(request); 
	}

	protected void processLocaleByPath(HttpServletRequest request, HttpServletResponse response){

		if(!SiteConfig.inst().isStrutsAutoLanguage()){
			return ;
		}
		
		String uri = RequestUtils.getServletPath(request);
		//是否首页
		boolean isIndex = uri.equals("") || INDEX_PATTERN.matcher(uri).matches();

		String localeStr = "";
		int index = uri.indexOf('/', 1);
		if (index != -1)
			localeStr = uri.substring(1, index);

		Locale currentLocale = (Locale) request.getSession().getAttribute(WebLocaleUtils.ATTRIBUTE_KEY);
		Locale closestLocale = null;
		
		if(!isIndex){
			if (WebLocaleUtils.isSupport(localeStr)) { 
				closestLocale = WebLocaleUtils.getClosestLocale(localeStr);
			}
		}else{
			String cookieLanguge = CookieUtil.getCookieLanguage(request);
			if(StringUtils.isNotBlank(cookieLanguge))
				closestLocale = WebLocaleUtils.getClosestLocale(cookieLanguge);
		}
		
		if(closestLocale == null)
			closestLocale = getDefaultLocale(request);

		if(CookieUtil.getCookieCurrentLanguage(request)==null)
			CookieUtil.setCookieCurrentLanguage(response, closestLocale.toString());
		
		
		if (closestLocale != null && !WebLocaleUtils.isSameLocale(currentLocale, closestLocale)){
			request.getSession().setAttribute(WebLocaleUtils.ATTRIBUTE_KEY, closestLocale);
			CookieUtil.setCookieCurrentLanguage(response, closestLocale.toString());
		}
		
		String cookie = request.getParameter("remember_language");
		if("true".equals(cookie)){
			CookieUtil.setCookieLanguage(response, closestLocale.toString());
		}
		
	}
	
	protected Locale getDefaultLocale(HttpServletRequest request){
//		return request.getLocale();
		return WebLocaleUtils.getDefault();
	}

	public void destroy() {
	}
	
	public static void main(String[] args){
		String str = "/as/dfas.jsp";
		boolean l = INDEX_PATTERN.matcher(str).matches();
		System.out.println(l);
	}
}
