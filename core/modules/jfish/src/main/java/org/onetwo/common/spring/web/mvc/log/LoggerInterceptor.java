package org.onetwo.common.spring.web.mvc.log;

import java.util.Date;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.utils.JFishMathcer;
import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter;
import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter.InterceptorOrder;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.WebContextUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.HandlerMethod;

public class LoggerInterceptor extends WebInterceptorAdapter implements InitializingBean {

	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	private JsonMapper jsonMapper = JsonMapper.mapper(Inclusion.NON_NULL, true);
	private ContextHolder contextHolder;
	private AccessLogger accessLogger;
	private final boolean logOperation;
	private JFishMathcer matcher ;
	private String[] excludes;
	
	public LoggerInterceptor(){
		this.logOperation  = BaseSiteConfig.getInstance().isLogOperation();
	}
	
	public void afterPropertiesSet() throws Exception{
		if(LangUtils.isEmpty(excludes)){
			this.excludes = new String[]{"*password*"};
		}
		this.matcher = JFishMathcer.excludes(false, excludes);
		if(isLogOperation() && accessLogger==null){
			accessLogger = new DefaultAccessLogger();
		}
		if(accessLogger!=null){
			accessLogger.initLogger();
		}
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		request.setAttribute(START_TIME_KEY, System.currentTimeMillis());;
		return true;
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
		/*AuthenticationContext authen = AuthenticUtils.getContextFromRequest(request);
		if(authen==null)
			return ;*/

		if(accessLogger==null)
			return ;
		
		if(handler==null || !HandlerMethod.class.isInstance(handler))
			return ;
		
		long start = WebContextUtils.requestInfo(request).getStartTime();
		OperatorLogInfo info = new OperatorLogInfo(start, System.currentTimeMillis());
		
		String url = request.getMethod() + "|" + request.getRequestURL();
		info.setUrl(url);
		info.setRemoteAddr(RequestUtils.getRemoteAddr(request));
//		info.setParameters(request.getParameterMap());
		for(Entry<String, String[]> entry : request.getParameterMap().entrySet()){
			/*try {
				if(matcher.match(entry.getKey())){
					info.addParameter(entry.getKey(), entry.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			if(matcher.match(entry.getKey())){
				info.addParameter(entry.getKey(), entry.getValue());
			}else{
				info.addParameter(entry.getKey(), "******");
			}
		}
		
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
		HandlerMethod webHandler = (HandlerMethod)handler;
		info.setWebHandler(webHandler.getBeanType().getCanonicalName()+"."+webHandler.getMethod().getName());
		
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
