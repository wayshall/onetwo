package org.onetwo.common.spring.web.tag;

import javax.annotation.Resource;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.WebFilterAdapter;
import org.onetwo.common.web.view.ThemeSetting;

public class ThemeSettingWebFilter extends WebFilterAdapter {
	
	@Resource
	private ThemeSetting themeSetting;

	@Override
	public void onInit(FilterConfig config) {
//		ThemeSetting themeSetting = SpringApplication.getInstance().getBean(ThemeSetting.class);
		if(themeSetting==null){
			logger.info("no themeSetting found!");
		}else{
			config.getServletContext().setAttribute("themeSetting", themeSetting);
		}
	}

	@Override
	public void onFilter(HttpServletRequest request, HttpServletResponse response) {
		String jsuistr = request.getParameter(BaseSiteConfig.THEME_JSUI_KEY);
		if(StringUtils.isNotBlank(jsuistr)){
			boolean jsui = Types.convertValue(jsuistr, boolean.class);
			themeSetting.setJsui(jsui);
		}
		
	}
}
