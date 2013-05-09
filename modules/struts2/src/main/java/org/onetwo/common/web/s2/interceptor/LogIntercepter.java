package org.onetwo.common.web.s2.interceptor;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/***
 * 日志记录拦截器
 * @author weishao
 *
 */
@SuppressWarnings("serial")
public class LogIntercepter extends AbstractInterceptor {

	protected Logger logger = Logger.getLogger(LogIntercepter.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		/*if(logger.isInfoEnabled()){
			String name = StrutsUtils.getCurrentLoginUser() != null ? StrutsUtils.getCurrentLoginUser().getUserName() : "匿名";
			StringBuilder sb = new StringBuilder("[");
			sb.append(StrutsUtils.getRequest().getRemoteHost()).append("|").append(name).append("] 的会话：sessionId[").append(StrutsUtils.getRequest().getSession(false).getId()).append("]").append("  访问地址：").append(StrutsUtils.getRequestURI());
			logger.info(sb.toString());
		}
*/
		return invocation.invoke();
	}

}
