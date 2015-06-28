package org.onetwo.common.web.sso;

import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.sso.SecurityService;
import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.slf4j.Logger;


abstract public class AbstractSSOServiceImpl implements SecurityService {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	/****
	 * 更新最后登录时间，这个和{@link org.onetwo.common.web.s2.security.config.annotation.Authentic authentic}注解的checkTimeout属性有关
	 * 如果不需要直接返回null
	 * @return
	 */
	abstract public UserActivityTimeHandler getUserActivityTimeHandler();

	/********
	 * 
	 * 创建新的UserDetail对象，这个和{@link org.onetwo.common.web.s2.security.config.annotation.Authentic authentic}注解的checkTimeout属性有关
	 * 但检测到登录超时，用这个方法创建的UserDetail作为参数传递给{@link org.onetwo.common.sso.UserLoginService#logout ssoService#logout}方法，回调注销操作
	 * @param target
	 * @return
	 */
//	abstract protected UserDetail createUserDetail(SecurityTarget target);
	public boolean checkTimeout(SecurityTarget target, boolean updateLastLogTime){
		return target.getAuthoritable()==null;
	}
	/***
	 * 验证登陆是否超时
	 * 逻辑比较复杂
	 * @param authoritable
	 * @param cookietoken
	 * @param updateLastLogTime
		public boolean checkTimeout(UserDetail authoritable, String cookietoken, boolean updateLastLogTime){
	/*public boolean checkTimeout(SecurityTarget target, boolean updateLastLogTime){
		if(!UserActivityCheckable.class.isInstance(target.getAuthoritable())){
			return false;
		}
		
		Date lastLog = null;
		Date now = new Date();
		SSOLastActivityStatus dbStatus = null;
		String token = target.getCookieToken();
		UserActivityCheckable authoritable = (UserActivityCheckable)target.getAuthoritable();
		if(authoritable!=null){
			lastLog = authoritable.getLastActivityTime();
			//是否需要同步db的时间
			if(!isTimeoutCheckTime(authoritable.getLastSynchronizedTime(), now))
				return false;
			else{
				token = authoritable.getToken();
				dbStatus = this.getUserActivityTimeHandler().getUserLastActivityStatus(token);
			}
		}
		else if(StringUtils.isNotBlank(token)){
			dbStatus = this.getUserActivityTimeHandler().getUserLastActivityStatus(token);
		}
		else{
			return false;
		}
		
		if(dbStatus==null || !dbStatus.isLogin()){
//			throw new NotLoginException("该账户在别处登陆，你已被踢下线，注意不能多人同时使用一个账户。");
//			StrutsUtils.removeCurrentLoginUser();
//			CookieUtil.removeAllCookies();
			return true;
		}

		if(lastLog==null || dbStatus.getLastActivityTime().getTime()>lastLog.getTime()){
			lastLog = dbStatus.getLastActivityTime();
		}
		
		//主要针对a机太久没活动，活动的b机可能超过了checkouttime的情况
		if(!isTimeoutCheckTime(lastLog, now))
			return false;
		
		if(isTimeout(lastLog, now)){//超时
			UserDetail user = (UserDetail) authoritable;
			if(user==null){
				user = this.createUserDetail(target);
			}
			logout((UserDetail)user, false);
			
//			StrutsUtils.removeCurrentLoginUser();
//			CookieUtil.removeAllCookies();
			return true;
		} 
		
		if(updateLastLogTime)
			this.getUserActivityTimeHandler().updateUserLastActivityTime(token, now);
		return false;
	}*/
	
	/*protected boolean isTimeoutCheckTime(Date lastLog, Date now){
		Date checkTimeout = DateUtil.addMinutes(lastLog, BaseSiteConfig.getInstance().getTokenTimeoutChecktime());
		return checkTimeout.getTime()<now.getTime();
	}
	
	protected boolean isTimeout(Date lastLog, Date now){
		Date timeout = DateUtil.addMinutes(lastLog, BaseSiteConfig.getInstance().getTokenTimeout());
		return timeout.getTime()<now.getTime();
	}*/

	public UserDetail checkLogin(SecurityTarget target) {
//		target.removeCurrentLoginUser();
		UserDetail authoritable = target.getAuthoritable();
		try {
			String cookietoken = target.getCookieToken();
			
			if(StringUtils.isNotBlank(cookietoken)){
				if(authoritable!=null){
					if(!cookietoken.equals(authoritable.getToken())){
						// 如果不相等，以cookietoken为准
						// 清空sesssion
						target.removeCurrentLoginUser();
						authoritable = getCurrentLoginUser(target);
						// 把用户信息放入session
						if (authoritable != null) {
							target.setCurrentLoginUser(authoritable);
						}
					}
				}else{
					// 如果session为空，cookietoken不为空
					authoritable = getCurrentLoginUser(target);
					// 把用户信息放入session
					if (authoritable != null) {
						target.setCurrentLoginUser(authoritable);
					} else {
						target.removeCookieToken();
					}
				}
			}else{
				if(authoritable!=null){
					// 注销退出
					target.removeCurrentLoginUser();
					authoritable = null;
				}else{
					// session和cookie都为空 ，没有登录
				}
			}
			
		} catch (Exception e) {
			handleLoginException(e, "sso login error by token: "+target.getCookieToken());
		}

		return authoritable;
	}
	
	public void handleLoginException(Exception e, String msg){
		logger.error("sso login error : " + e.getMessage());
		if(e instanceof LoginException){
			throw (LoginException)e;
		}
		Exception cause = (Exception)MyUtils.getCauseServiceException(e);
		if(cause instanceof ServiceException){
			ServiceException se = (ServiceException) cause;
			throw se;
		}
		throw new LoginException("登陆出错："+e.getMessage() , e);
	}

	/*********
	 * 这个方法在下面两种情况下调用：
	 * 1、session里user对象的token和cookie里的token不同
	 * 2、session里user对象为null，而cookie里有token
	 * 
	 * 如果登录token不是放在cookie里，而是主要存放在session里，这个方法直接返回null即可
	 * @param target
	 * @return
	 */
	public UserDetail getCurrentLoginUser(SecurityTarget target){
		UserDetail user = null;
		try{
			user = this.getCurrentLoginUserByCookieToken(target.getCookieToken());
//			user = getSSOUserService().getCurrentLoginUserByToken(target.getCookieToken());
		}catch(Exception e){
			handleLoginException(e, "get current login user error by token : " + target.getCookieToken());
		}
		return user;
	}

	/********
	 * 
	 * 如果登录token不是放在cookie里，而是主要存放在session里，这个方法直接返回null即可
	 * @param token
	 * @return
	 */
//	abstract public SSOUserService getSSOUserService();
	abstract protected UserDetail getCurrentLoginUserByCookieToken(String token);
}
