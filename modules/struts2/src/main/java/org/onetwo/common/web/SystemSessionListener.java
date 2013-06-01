package org.onetwo.common.web;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTargetAdaptor;
import org.onetwo.common.web.utils.SessionUtils;
import org.onetwo.common.web.utils.UserKeyManager;


public class SystemSessionListener implements HttpSessionListener {
	
	protected Logger logger = Logger.getLogger(SystemSessionListener.class);

	public void sessionCreated(HttpSessionEvent event) {
//		Date now = new Date();
//		logger.warn(" session  创建 : " + event.getSession().getId() + "   time: " + DateUtil.formatDateTime(now));
	} 

	public void sessionDestroyed(HttpSessionEvent event) {
		UserDetail userinfo = null;
		for(UserKeyManager ukey : UserKeyManager.getUserKeys()){
			userinfo = SessionUtils.getUserDetail(event.getSession(), ukey.getCurrentUserKey());
			if(userinfo==null)
				continue;
			upadteUserLoginInfoToDB(userinfo, event.getSession());
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void upadteUserLoginInfoToDB(final UserDetail userinfo, HttpSession session){
		StringBuilder sb = new StringBuilder();
		sb.append("session 销毁 : ").append(session.getId()).append("   time: ").append(new Date().toLocaleString());
		if (userinfo != null) {
			try{
				sb.append("   token:").append(userinfo.getToken());
				SSOService ssoService = SpringApplication.getInstance().getBean(SSOService.class);
				if(ssoService!=null){
//					ssoService.checkTimeout(userinfo, null, false);
					ssoService.checkTimeout(new SecurityTargetAdaptor() {

						@Override
						public UserDetail getAuthoritable() {
							return userinfo;
						}
						
					}, false);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		logger.warn(sb.toString());
	}
	
}
