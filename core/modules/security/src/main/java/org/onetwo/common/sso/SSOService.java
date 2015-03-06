package org.onetwo.common.sso;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTarget;

public interface SSOService {

	/******
	 * 
	 * 如果{@link org.onetwo.common.web.s2.security.config.annotation.Authentic authentic}的checkTimeout为true，
	 * 并且target的authoriable对象实现了{@link org.onetwo.common.utils.UserActivityCheckable UserActivityCheckable}接口，
	 * 将会在所有验证和授权逻辑前调用此方法
	 * 此方法是之前基于数据库的登录超时检查而定义的，如果不需要，直接返回false即可，表示不通过程序来控制超时。
	 * 
	 * 没有超时返回false，超时返回true，安全验证其会据此抛出超时的异常
	 * 设置Authentic 的checkTimeout属性默认为true，设置为false，可以禁止此方法的调用
	 * @param target
	 * @param updateLastLogTime
	 * @return
	 */
	public boolean checkTimeout(SecurityTarget target, boolean updateLastLogTime);

	/********
	 * 如果{@link org.onetwo.common.web.s2.security.config.annotation.Authentic authentic}的checkLogin为true，将会在所有验证和授权逻辑前（checkTimeout之后）调用此方法
	 * 此方法是基于cookie token的单点登录的一个验证回调，如果不是cookie token的单点登录，可以直接返回session里的登录用户对象
	 * 
	 * 设置Authentic 的checkLogin属性默认为true，设置为false，可以禁止此方法的调用
	 * @param target
	 * @return
	 */
	public UserDetail checkLogin(SecurityTarget target);
}
