package org.onetwo.common.spring.web.mvc.log;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.slf4j.Logger;

public class LoggerInterceptor extends WebInterceptorAdapter  {

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
//	private JsonMapper jsonMapper = JsonMapper.mapper(Inclusion.NON_NULL, true);
	private ContextHolder contextHolder;
	private AccessLogger accessLogger;
	private final boolean logOperation;
	
	public LoggerInterceptor(){
		this.logOperation  = BaseSiteConfig.getInstance().isLogOperation();
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if(!isLogOperation())
			return ;
		
		try {
			log(request, response, handler, ex);
		} catch (Exception e) {
			logger.error("log error: {}", e.getMessage(), e);
		}
	}
	public void log(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		AuthenticationContext authen = AuthenticUtils.getContextFromRequest(request);
		if(authen==null)
			return ;
		
		OperatorLogInfo info = new OperatorLogInfo();
		String url = request.getMethod() + "|" + request.getRequestURL();
		info.setUrl(url);
		
		UserDetail userdetail = JFishWebUtils.getUserDetail();
		if(userdetail!=null){
			info.setOperatorId(userdetail.getUserId());
			info.setOperatorName(userdetail.getUserName());
		}
		if(ex!=null){
			info.setSuccess(false);
			info.setMessage(ex.getMessage());
		}
		info.setOperatorTime(new Date());
		info.setDatas(contextHolder.getDataChangedContext());
		
		accessLogger.logOperation(info);
	}

	@Override
	public int getOrder() {
		return InterceptorOrder.afater(InterceptorOrder.SECURITY);
	}

	public void setContextHolder(ContextHolder contextHolder) {
		this.contextHolder = contextHolder;
	}
	public void setAccessLogger(AccessLogger accessLogger) {
		this.accessLogger = accessLogger;
	}
	public boolean isLogOperation() {
		return logOperation;
	}
	
	
}
