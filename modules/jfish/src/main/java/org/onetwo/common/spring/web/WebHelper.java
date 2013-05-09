package org.onetwo.common.spring.web;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.PluginConfig;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.utils.WebContextUtils;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.RequestContext;

@SuppressWarnings("unchecked")
public class WebHelper {

	public static final String WEB_HELPER_CLASS = "web.helper.class";
	public static final String WEB_HELPER_KEY = "helper";
	
	private static final Class<?> ACTUAL_HELPERCLASS;
	
	static {
		ACTUAL_HELPERCLASS = BaseSiteConfig.getInstance().getClass(WEB_HELPER_CLASS, WebHelper.class);
	}
	
	public static WebHelper newHelper(HttpServletRequest request){
		WebHelper helper = (WebHelper)ReflectUtils.newInstance(ACTUAL_HELPERCLASS);
		helper.initHelper(request);
		return helper;
	}
	
	public static WebHelper newHelper(NativeWebRequest webRequest){
		WebHelper helper = null;
		if(webRequest.getNativeRequest() instanceof HttpServletRequest){
			helper = newHelper((HttpServletRequest)webRequest.getNativeRequest());
		}else{
			throw new JFishException("cann't create webHelper!");
		}
		return helper;
	}
	
	private HttpServletRequest request;
	private Map<String, String> pathParameters = Collections.EMPTY_MAP;
	private Map<String, Object> webRequestContext;
	private RequestContext requestContext;
	private StandardEvaluationContext elcontext;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void initHelper(HttpServletRequest request) {
		this.request = request;
		Object params = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if(params!=null){
			this.pathParameters = (Map<String, String>)params;
		}
	}
	
	public String getRequestUrl(){
		return this.request.getRequestURL().toString();
	}
	
	public String getRequestURI(){
		return this.request.getRequestURI().toString();
	}
	
	public String getQueryString(){
		String qs = this.request.getQueryString();
		return qs;
	}
	
	public UserDetail getCurrentUserLogin(){
		return WebContextUtils.getUserDetail(request.getSession());
	}
	
	public boolean isLogin(){
		return getCurrentUserLogin()!=null;
	}
	
	public UserDetail getCurrentUserLogin(String key){
		return WebContextUtils.getAttr(request.getSession(), key);
	}
	
	public PluginConfig getPluginConfig(String name){
		JFishPluginManager jpm = (JFishPluginManager)this.request.getSession().getServletContext().getAttribute(JFishPluginManager.JFISH_PLUGIN_MANAGER_KEY);
		return jpm.getJFishPluginMeta(name).getPluginConfig();
	}

	public Map<String, String> getPathParameters() {
		return pathParameters;
	}

	public Map<String, Object> getWebRequestContext() {
		if(webRequestContext==null){
			webRequestContext = LangUtils.newHashMap();
			webRequestContext.putAll(getPathParameters());
			for(Entry<String, String[]> entry : request.getParameterMap().entrySet()){
				if(LangUtils.isEmpty(entry.getValue()))
					continue;
				if(entry.getValue().length==1){
					webRequestContext.put(entry.getKey(), entry.getValue()[0]);
				}else{
					webRequestContext.put(entry.getKey(), entry.getValue());
				}
			}
		}
		if(!webRequestContext.containsKey("backUrl"))
			webRequestContext.put("backUrl", getRefererUrl(""));
		return webRequestContext;
	}
	
	public String getRefererUrl(String def){
		String url = this.request.getHeader("Referer");
		if(StringUtils.isBlank(url))
			url = def;
		return url;
	}

	public RequestContext getRequestContext() {
		if(requestContext==null){
			requestContext = new RequestContext(request);
		}
		return requestContext;
	}

	public StandardEvaluationContext getElcontext() {
		if(elcontext==null){
			this.elcontext = new StandardEvaluationContext();
			this.elcontext.setVariables(getWebRequestContext());
		}
		return elcontext;
	}

}
