package org.onetwo.common.web.s2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.json.JSONUtils;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.utils.StrutsUtils;
import org.onetwo.common.web.utils.Tool;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * struts2 action的基类
 *              
 * @author weishao
 *
 */

@SuppressWarnings("unchecked")
abstract public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, Preparable {
 
	private static final long serialVersionUID = 5011713702690873698L;

	public static final String RESULT_DISPATCHER = "dispatcher";
	public static final String RESULT_REDIRECT = "redirect";
	public static final String RESULT_CHAIN = "chain";
	
	public static final String GLOBAL_RESULT = "global-result";
	public static final String GLOBAL_ERROR = "global-error";
	public static final String GLOBAL_MESSAGE = "global-message";
	public static final String GLOBAL_RESOURCES = "global-resources";

	public static final String DELETE_MESSAGE = "operation.delete.success";
	public static final String SAVE_MESSAGE = "operation.save.success";

	public static final String INDEX = "index";
	public static final String DELETE = "delete";
	public static final String LIST = "list";
	public static final String VIEW = "view";
	public static final String EDIT = "edit";
	public static final String TRANSLATE = "translate";
	public static final String DOWNLOAD = "download";
//	public static final String EXPORT_EXCEL = "export_excel";

	protected Logger log = Logger.getLogger(getClass());

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	public BaseAction(){
	}

	public void prepare() throws Exception {
	}

	public String execute(){
		return SUCCESS;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Locale getLocale(){
		return StrutsUtils.getCurrentSessionLocale();
	}

	public UserDetail getCurrentUserLogin(){
		UserDetail user = StrutsUtils.getCurrentLoginUser();
		/*if(user==null){
			DefaultUserDetail duser = new DefaultUserDetail();
			duser.setUserId(5000);
			user = duser;
		}*/
		return user;
	}

	public boolean isLogin(){
		UserDetail detail =  this.getCurrentUserLogin();
		return detail != null;
	}

	/*public boolean isAdmin(){
		return this.getCurrentUserLogin().isAdmin();
	}*/

	public void outJson(Object datas, String...fieldName){
		String text = "";
		if(!(datas instanceof String))
			text = getJsonString(datas, fieldName);
		else
			text = (String)datas;
		System.out.println(text);
		this.render(text, StrutsUtils.JSON_TYPE);
	}
	
	public void outJsonWith(Object datas, String...fieldName){
		String text = "";
		if(!(datas instanceof String))
			text = JSONUtils.getJsonStringWith(datas, fieldName);
		else
			text = (String)datas;
		System.out.println(text);
		this.render(text, "application/json; charset=UTF-8");
	}

	protected String getJsonString(Object datas, String...fieldName){
		return StrutsUtils.getJsonString(datas, fieldName);
	}

	protected String doAjax(AjaxAction ajax){
		Map rs = null;
		Exception actionExp = null;

		try{
			rs = ajax.execute();
		}catch(Exception e){
			e.printStackTrace();
			actionExp = e;
		}

		if(rs==null)
			rs = new HashMap();

		if(actionExp!=null){
			rs.put(AjaxAction.MESSAGE_KEY, "操作失败："+ actionExp.getMessage());
			rs.put(AjaxAction.MESSAGE_CODE_KEY, AjaxAction.RESULT_FAILED);
		}
		else{	
			if(rs.get(AjaxAction.MESSAGE_KEY)==null)
				rs.put(AjaxAction.MESSAGE_KEY, "操作成功！");
			if(rs.get(AjaxAction.MESSAGE_CODE_KEY)==null)
				rs.put(AjaxAction.MESSAGE_CODE_KEY, AjaxAction.RESULT_SUCCEED);
			
		}

		String cb = (String)rs.get(AjaxAction.CALLBACK_KEY);
		
		String[] fieldNames;
		boolean include;
		if(rs.containsKey(AjaxAction.INCLUDE_FIELDS)){
			fieldNames = (String[])rs.get(AjaxAction.INCLUDE_FIELDS);
			include = true;
		}else{
			fieldNames = (String[])rs.get(AjaxAction.EXCLUDE_FIELDS);
			include = false;
		}
		
		String jsonString = JSONUtils.getJsonString(rs, include, fieldNames);
		
		if(StringUtils.isNotBlank(cb)){
			StrutsUtils.renderJsonp(cb, jsonString);
		}
		else if(ajax instanceof JsonpAjaxAction){
			JsonpAjaxAction jsonp = (JsonpAjaxAction) ajax;
			StrutsUtils.renderJsonp(jsonp.getCallback(), jsonString);
		}else{
			StrutsUtils.renderJSON(jsonString);
		}

		return null;
	}

	protected String rd(String path){
		return "rd:"+path;
	}
	
	protected String fd(String path){
		return "fd:"+path;
	}
	
	protected void renderText(String text){
		render(text, "", true);
	}

	protected void render(String text, String contentType){
		render(text, contentType, true);
	}

	protected void renderHTML(String text){
		StrutsUtils.renderHTML(text);
	}

	protected void renderScriptMessage(String msg){
		StrutsUtils.renderHTML(MyUtils.append("<script>alert('", msg, "')</script>"));
	}

	protected void renderMessageAndLocation(String msg, String url){
		StrutsUtils.renderHTML(MyUtils.append("<script>alert('", msg, "');location.href='"+url+"'</script>"));
	}
	
	protected void renderScript(String msg){
		StrutsUtils.renderHTML(MyUtils.append("<script>", msg, "</script>"));
	}

	protected void render(String text, String contentType, boolean noCache){
		StrutsUtils.render(text, contentType, noCache);
	}

	protected String getBaseURL(){
		return SiteConfig.getInstance().getBaseURL();
	}

	public final String getSessionLanguage(){
		return StrutsUtils.getSessionLanguage();
	}

	public String getDataLanguage(){
		return StrutsUtils.getDataLanguage();
	}

	public String getText(String aTextName, List<Object> values){
		String msg = super.getText(aTextName, values);
		if(StringUtils.isBlank(msg))
			msg = LocalizedTextUtil.findText(getTexts(GLOBAL_RESOURCES), aTextName, getLocale(), aTextName, values.toArray());
		return msg;
	}

	public String getText(String aTextName, Object... values){
		String msg = null;
		if(values==null || values.length==0)
			msg = super.getText(aTextName);
		else{
			List list = new ArrayList();
			Collections.addAll(list, values);
			msg = this.getText(aTextName, list);
		}
		return msg;
	}

	public String getParameter(String name){
		return this.request.getParameter(name);
	}

	public Map getParameters(){
		return this.request.getParameterMap();
	}

	public Object getAttribute(String name){
		return this.request.getAttribute(name);
	}

	public void setAttribute(String name, Object value){
		this.request.setAttribute(name, value);
	}

	public String getActionName(){
		String an = ActionContext.getContext().getActionInvocation().getProxy().getActionName();
		return an;
	}

	public String getMethodName(){
		String mn = ActionContext.getContext().getActionInvocation().getProxy().getConfig().getMethodName();
		return mn;
	}
	
	public boolean isActionError(){
		return hasActionErrors();
	}
	
	public Tool getHelper(){
		return Tool.getInstance();
	}

}
