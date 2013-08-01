package org.onetwo.plugins.fmtagext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter;
import org.onetwo.plugins.fmtagext.ui.SimplePageUI;
import org.springframework.web.servlet.ModelAndView;

public class FmUIModelAndViewHandler extends WebInterceptorAdapter {

	private static final String JFISHUI_PAGE = FmtagextPlugin.PLUGIN_PATH+"/lib/jfishui-page";

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
		if(mv==null)
			return ;
		
		SimplePageUI page = null;
		for(Object model : mv.getModel().values()){
			if(SimplePageUI.class.isInstance(model)){
				page = (SimplePageUI) model;
				break;
			}
		}
		if(page!=null){
			mv.addObject("__page__", page);
			mv.setViewName(JFISHUI_PAGE);
		}
	}
	
	@Override
	public int getOrder() {
		return InterceptorOrder.before(InterceptorOrder.MODEL_AND_VIEW_POST_PROCESS);
	}

}
