package org.onetwo.plugins.rest.log;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.plugins.rest.RestResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

public class RestRequestLogInterceptor extends WebInterceptorAdapter {

	protected final Logger logger = RestLogFactory.getLogger();

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		RestLogInfo info = new RestLogInfo();
		info.setUrl(JFishWebUtils.requestUri());
		info.setParams(request.getParameterMap());
		
		for(Map.Entry<String, Object> entry : modelAndView.getModel().entrySet()){
			if(RestResult.class.isInstance(entry.getValue())){
				info.setResult(entry.getValue());
				break;
			}
		}
		if(info.getResult()==null){
			Map<String, Object> data = new HashMap<String, Object>();
			for(Map.Entry<String, Object> entry : modelAndView.getModel().entrySet()){
				if(BindingResult.class.isInstance(entry.getValue())){
					continue;
				}else{
					data.put(entry.getKey(), entry.getValue());
				}
			}
			info.setResult(data);
		}
		logger.info(JsonMapper.IGNORE_NULL.toJson(info));
	}
	
	@Override
	public int getOrder() {
		return InterceptorOrder.afater(InterceptorOrder.MODEL_AND_VIEW_POST_PROCESS);
	}



}
