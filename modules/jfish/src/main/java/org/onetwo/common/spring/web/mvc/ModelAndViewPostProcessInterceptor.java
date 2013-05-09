package org.onetwo.common.spring.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginMeta;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

public class ModelAndViewPostProcessInterceptor extends WebInterceptorAdapter  {

	public static String PLUGIN_KEY = "pluginConfig";
	
	protected JFishPluginManager pluginManager;
	
	public ModelAndViewPostProcessInterceptor(){
	}
	
	public ModelAndViewPostProcessInterceptor(JFishPluginManager pluginManager) {
		super();
		this.pluginManager = pluginManager;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
		if(mv==null)
			return ;
		if(!HandlerMethod.class.isInstance(handler))
			return ;

		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		JFishPluginMeta meta = pluginManager.getJFishPluginMetaOf(handlerMethod.getBean().getClass());
		if(meta!=null){
			mv.getModel().put(PLUGIN_KEY, meta.getPluginConfig());
			/*if(JFishWebUtils.isRedirect(mv) && mv.isReference()){
				String newView = mv.getViewName();
				if(!newView.startsWith("/")){
					//TODO 
				}
				newView = meta.getPluginInfo().getContextPath() + newView;
				mv.setViewName(newView);
			}*/
		}

		this.doInModelAndView(handlerMethod, request, mv);
		
		//append url postfix
		if(BaseSiteConfig.getInstance().hasAppUrlPostfix() && mv.getViewName()!=null && JFishWebUtils.isRedirect(mv) && !mv.getViewName().contains("?")){
			if(mv.isReference()){
				mv.setViewName(BaseSiteConfig.getInstance().appendAppUrlPostfix(mv.getViewName()));
			}
		}
	}
	
	protected void doInModelAndView(HandlerMethod handlerMethod, HttpServletRequest request, ModelAndView mv){
		if(AbstractBaseController.class.isInstance(handlerMethod.getBean())){
			AbstractBaseController c = (AbstractBaseController)handlerMethod.getBean();
			//callback
			c.doInModelAndView(request, mv);
		}
	}

	@Override
	public int getOrder() {
		return InterceptorOrder.MODEL_AND_VIEW_POST_PROCESS;
	}

	public void setPluginManager(JFishPluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}
}
