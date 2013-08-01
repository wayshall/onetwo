package org.onetwo.common.web.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.onetwo.common.utils.SiteInfo;
import org.onetwo.common.utils.UserActivityCheckable;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.DefaultTemplatePathMapper;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.s2.tag.webtag.TemplateTagManagerFactory;


abstract public class SessionUtils {

	protected static Logger logger = Logger.getLogger(SessionUtils.class);

	public static void updateLastActivityTime(Date date) {
		try {
			UserDetail user = getUserDetail();
			if (user == null)
				return;
			if (date == null)
				date = new Date();
			if(UserActivityCheckable.class.isInstance(user))
				((UserActivityCheckable)user).setLastActivityTime(date);
		} catch (Exception e) {
			logger.error("update LastActivityTime error. ", e);
		}
	}
	
	public static void setAttribute(String name, Object value){
		setAttribute(getSession(), name, value);
	}

	public static Object getAttribute(String name){
		return getAttribute(getSession(), name);
	}
	
	public static void setAttribute(HttpSession session, String name, Object value){
		if(session==null){
			logger.info("can not set attribute["+name+","+value+"] to session, because no session found!");
			return ;
		}
		session.setAttribute(name, value);
	}
	
	public static Object getAttribute(HttpSession session, String name){
		if(session!=null)
			return session.getAttribute(name);
		else{
			logger.info("can not get attribute["+name+"] to session, because no session found!");
			return null;
		}
	}

	public static Object remove(String name) {
		return remove(getSession(), name);
	}

	public static Object remove(HttpSession session, String name) {
		if(session==null){
			logger.info("can not remove attribute["+name+"] to session, because no session found!");
			return null;
		}
		Object value = session.getAttribute(name);
		if(value!=null)
			session.removeAttribute(name);
		return value;
	}
	
	public static HttpSession getSession(){
		HttpServletRequest request = StrutsUtils.getRequest();
		if(request!=null)
			return request.getSession(false);
		else
			return null;
	}
	
	public static void setUserDetail(UserDetail userDetail) {
		setUserDetail(getSession(), userDetail);
	}
	
	public static void setUserDetail(HttpSession session, UserDetail userDetail) {
		String key = UserKeyManager.getCurrentUserKeyManager().getCurrentUserKey();
		setAttribute(session, key, userDetail);
	}
	
	public static UserDetail getUserDetail() {
		return getUserDetail(getSession());
	}
	
	public static UserDetail getUserDetail(HttpSession session) {
		String key = UserKeyManager.getCurrentUserKeyManager().getCurrentUserKey();
		UserDetail userDetail = (UserDetail) getAttribute(session, key);
		return userDetail;
	}
	
	public static UserDetail getUserDetail(HttpSession session, String key) {
		UserDetail userDetail = (UserDetail) getAttribute(session, key);
		return userDetail;
	}
	
	public static void removeUserDetail() {
		removeUserDetail(getSession());
	}
	
	public static void removeUserDetail(HttpSession session) {
		String key = UserKeyManager.getCurrentUserKeyManager().getCurrentUserKey();
		remove(session, key);
	}
	
	public static void setSiteInfo(WebSiteInfo siteInfo){
		setAttribute(SiteInfo.KEY, siteInfo);
	}
	
	public static WebSiteInfo getSiteInfo(){
		return (WebSiteInfo)getAttribute(SiteInfo.KEY);
	}
	
	public static void setTestSiteInfo(HttpServletRequest request){
		String tagval = request.getParameter("tag-reload");
		if("true".equals(tagval)){
			TemplateTagManagerFactory.reload();
		}
		
		String val = request.getParameter("templateConfig-reload");
		if("true".equals(val))
			DefaultTemplatePathMapper.getInstance().reload();
		if("false".equals(val)){
			SessionUtils.remove(request.getSession(), SiteInfo.KEY);
			return ;
		}
		if(!DefaultTemplatePathMapper.getInstance().isEnable())
			return ;
		if(SessionUtils.getSiteInfo()!=null)
			return ;
		SiteInfo siteInfo = new WebSiteInfo();
		siteInfo.setTemplate(SiteConfig.getInstance().getTemplateName());
		setAttribute(request.getSession(), SiteInfo.KEY, siteInfo);
	}

}
