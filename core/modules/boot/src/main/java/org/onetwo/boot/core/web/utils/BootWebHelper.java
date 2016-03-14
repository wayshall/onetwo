package org.onetwo.boot.core.web.utils;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.WebContextUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.onetwo.common.web.xss.XssUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.HtmlUtils;

public class BootWebHelper {

	public static final String WEB_HELPER_CLASS = "web.helper.class";
	public static final String WEB_HELPER_KEY = "helper";
	
//	private static final Class<?> ACTUAL_HELPERCLASS;
	
	/*static {
		ACTUAL_HELPERCLASS = BaseSiteConfig.getInstance().getClass(WEB_HELPER_CLASS, BootWebHelper.class);
	}*/
	
	public static BootWebHelper newHelper(HttpServletRequest request){
//		BootWebHelper helper = (BootWebHelper)ReflectUtils.newInstance(ACTUAL_HELPERCLASS);
		BootWebHelper helper = new BootWebHelper();
		helper.initHelper(request);
		return helper;
	}
	
	public static BootWebHelper newHelper(NativeWebRequest webRequest){
		BootWebHelper helper = null;
		if(webRequest.getNativeRequest() instanceof HttpServletRequest){
			helper = newHelper((HttpServletRequest)webRequest.getNativeRequest());
		}else{
			throw new BaseException("cann't create webHelper!");
		}
		return helper;
	}
	
	private HttpServletRequest request;
	private RequestContext requestContext;
	
//	private String requestURI;
	private String requestExtension;
	private HandlerMethod controllerHandler;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void initHelper(HttpServletRequest request) {
		this.request = request;
	}
	
	public boolean isAaXmlRequest(){
		return RequestUtils.isAaXmlRequest(request);
	}
	
	public String getRequestUrl(){
		return this.request.getRequestURL().toString();
	}
	
	public String getRequestMethod(){
		return request==null?"":request.getMethod().toLowerCase();
	}

    public boolean isAaRequest() {
//    	return AAUtils.isAjaxRequest(this.request);
    	return false;
    }

	public String getQueryString(){
		String qs = this.request.getQueryString();
		return qs;
	}
	
	/*public String getXlsFormatAction(){
		String action = TagUtils.getUriWithParamsFilterPageNo(request);
		action = TagUtils.appendParam(action, TagUtils.PARAM_FORMAT, JsonExcelView.URL_POSFIX);
//		action = TagUtils.appendParam(action, Page.PAGINATION_KEY, "false");
		return action;
	}*/
	
	public UserDetail getCurrentUserLogin(){
		return WebContextUtils.getUserDetail(request.getSession());
	}
	
	public UserDetail getCurrentLoginUser(){
		return WebContextUtils.getUserDetail(request.getSession());
	}
	
	public boolean isLogin(){
		return getCurrentUserLogin()!=null;
	}
	
	public UserDetail getCurrentUserLogin(String key){
		return WebContextUtils.getAttr(request.getSession(), key);
	}
	
	public String getRefererUrl(){
		String url = this.request.getHeader("Referer");
		url = StringUtils.emptyIfNull(url);
		return url;
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


	public String getRequestURI() {
		return request.getRequestURI();
	}

/*	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}*/


	public String getRequestExtension() {
		return requestExtension;
	}

	public void setRequestExtension(String requestExtension) {
		this.requestExtension = requestExtension;
	}
	
	public boolean isFormat(String ext){
		if(StringUtils.isBlank(ext))
			return false;
		return ext.equalsIgnoreCase(getRequestExtension());
	}

	public HandlerMethod getControllerHandler() {
		return controllerHandler;
	}

	public void setControllerHandler(HandlerMethod controllerHandler) {
		this.controllerHandler = controllerHandler;
	}

	
	/*public String web(String name){
		return ToolEl.escapeHtml(WebHolder.getValue(name).toString());
	}*/
	public String escapeHtml(String content){
		return HtmlUtils.htmlEscape(content);
	}
	
	public String firstNotblank(String val, String def1, String def2){
		return StringUtils.firstNotBlank(val, def1, def2);
	}
	
	public String web(String name){
		return WebHolder.getValue(name).toString();
	}
	public String escape(String content){
		return XssUtils.escape(content);
	}
}
