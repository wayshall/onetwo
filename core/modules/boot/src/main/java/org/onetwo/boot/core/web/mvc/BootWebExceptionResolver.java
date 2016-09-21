package org.onetwo.boot.core.web.mvc;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.exception.SystemErrorCode.JFishErrorCode;
import org.onetwo.common.jfishdbm.exception.DbmException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/************
 * 异常处理
 * instead of BasicErrorController
 * @author wayshall
 *
 */
public class BootWebExceptionResolver extends SimpleMappingExceptionResolver implements InitializingBean {
//	public static final String MAX_UPLOAD_SIZE_ERROR = "MVC_MAX_UPLOAD_SIZE_ERROR";

	private static final String EXCEPTION_STATCK_KEY = "__exceptionStack__";
	private static final String ERROR_CODE_KEY = "__errorCode__";
	private static final String PRE_URL = "preurl";
	private static final String AJAX_RESULT_PLACEHOLDER = "result";
	
	public static final int RESOLVER_ORDER = -9999;

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
//	private Map<String, WhenExceptionMap> whenExceptionCaches = new WeakHashMap<String, WhenExceptionMap>();
	protected final Logger mailLogger = JFishLoggerFactory.findLogger("mailLogger");
	
//	resouce: exception-messages
	/*@Qualifier(BootContextConfig.BEAN_EXCEPTION_MESSAGE)
	@Autowired
	private MessageSource exceptionMessage;*/
	
	private List<String> notifyThrowables;// = BaseSiteConfig.getInstance().getErrorNotifyThrowabbles();
	
	@Autowired
	private BootSiteConfig bootSiteConfig;

	@Autowired(required=false)
	private ExceptionMessageAccessor exceptionMessageAccessor;
	
//	protected String defaultRedirect;
	
	public BootWebExceptionResolver(){
		setOrder(RESOLVER_ORDER);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initResolver();
	}
	
	protected void initResolver(){
//		defaultRedirect = BaseSiteConfig.getInstance().getLoginUrl();
	}
	
	private boolean isAjaxRequest(HttpServletRequest request){
		return BootWebUtils.isAjaxRequest(request);
	}
	
	protected void processAfterLog(HttpServletRequest request, HttpServletResponse response, Object handlerMethod, Exception ex) {
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handlerMethod, Exception ex) {
		ModelMap model = new ModelMap();
		ErrorMessage errorMessage = this.getErrorMessage(request, handlerMethod, model, ex);
		this.doLog(request, handlerMethod, ex, errorMessage.isDetail());
		
		this.processAfterLog(request, response, handlerMethod, ex);
		
//		Object req = RequestContextHolder.getRequestAttributes().getAttribute(WebHelper.WEB_HELPER_KEY, RequestAttributes.SCOPE_REQUEST);
		
		if(isAjaxRequest(request)){
			SimpleDataResult<?> result = WebResultCreator.creator()
							.error("操作失败，"+ errorMessage.getMesage())
							.buildResult();
			model.put(AJAX_RESULT_PLACEHOLDER, result);
//			return createModelAndView(null, model, request, response, ex);
			ModelAndView mv = new ModelAndView("", model);
			return mv;
		}
		
		String msg = errorMessage.getMesage();
		if(!model.containsKey(AbstractBaseController.ERROR)){
			model.put(AbstractBaseController.ERROR, msg);
			model.put(AbstractBaseController.MESSAGE_TYPE, AbstractBaseController.MESSAGE_TYPE_ERROR);
		}
		if(BootWebUtils.isRedirect(errorMessage.getViewName())){
			return this.createModelAndView(errorMessage.getViewName(), model, request, response, ex);
		}

		String eInfo = "";
		if(!bootSiteConfig.isProduct() && errorMessage.isDetail()){
			eInfo = LangUtils.toString(ex, true);
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY, eInfo);
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY2, eInfo);
		}else{
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY, "");
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY2, "");
		}
//		eInfo = errorMessage.toString()+"  "+ eInfo;
		model.put(EXCEPTION_STATCK_KEY, eInfo);
		model.put(ERROR_CODE_KEY, errorMessage.getCode());
		
		return createModelAndView(errorMessage.getViewName(), model, request, response, ex);
	}

	protected String getUnknowError(){
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	protected ErrorMessage getErrorMessage(HttpServletRequest request, Object handlerMethod, ModelMap model, Exception ex){
		String errorCode = "";
		String errorMsg = "";
		Object[] errorArgs = null;
		
//		String defaultViewName = ExceptionView.UNDEFINE;
		boolean detail = true;
//		boolean authentic = false;
		ErrorMessage error = new ErrorMessage(ex);
		
		boolean findMsgByCode = true;
		/*if(ex instanceof MaxUploadSizeExceededException){
			defaultViewName = ExceptionView.UNDEFINE;
			errorCode = MAX_UPLOAD_SIZE_ERROR;//MvcError.MAX_UPLOAD_SIZE_ERROR;
//			errorArgs = new Object[]{this.mvcSetting.getMaxUploadSize()};
		}else */
		if(ex instanceof LoginException){
//			defaultViewName = ExceptionView.AUTHENTIC;
			detail = false;
		}else if(ex instanceof AuthenticationException){
//			defaultViewName = ExceptionView.AUTHENTIC;
			detail = false;
		}else if(ex instanceof ExceptionCodeMark){//serviceException && businessException
			ExceptionCodeMark codeMark = (ExceptionCodeMark) ex;
			errorCode = codeMark.getCode();
			errorArgs = codeMark.getArgs();
//			errorMsg = ex.getMessage();
			
			findMsgByCode = StringUtils.isNotBlank(errorCode);// && !codeMark.isDefaultErrorCode();
			detail = !bootSiteConfig.isProduct();
		}else if(DbmException.class.isInstance(ex)){
//			defaultViewName = ExceptionView.UNDEFINE;
			errorCode = JFishErrorCode.ORM_ERROR;
			
//			Throwable t = LangUtils.getFirstNotJFishThrowable(ex);
		}else if(ex instanceof BaseException){
//			defaultViewName = ExceptionView.UNDEFINE;
			errorCode = SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
			
//			Throwable t = LangUtils.getFirstNotJFishThrowable(ex);
		}/*else if(TypeMismatchException.class.isInstance(ex)){
			defaultViewName = ExceptionView.UNDEFINE;
			//errorMsg = "parameter convert error!";
		}*/else if(ex instanceof ConstraintViolationException){
			ConstraintViolationException cex = (ConstraintViolationException) ex;
			Set<ConstraintViolation<?>> constrants = cex.getConstraintViolations();
			int i = 0;
			for(ConstraintViolation<?> cv : constrants){
				if(i!=0)
					errorMsg+=", ";
				errorMsg += cv.getMessage();
				i++;
			}
			findMsgByCode = false;
		}/*else if(ex instanceof ObjectOptimisticLockingFailureException){
			errorCode = ObjectOptimisticLockingFailureException.class.getSimpleName();
		}*//*else if(BeanCreationException.class.isInstance(ex)){
			
		}*/
		else{
			errorCode = SystemErrorCode.UNKNOWN;
		}
		
		/*if(StringUtils.isBlank(errorCode)){
			errorCode = ex.getClass().getName();
		}*/

		if(findMsgByCode){
//			errorMsg = getMessage(errorCode, errorArgs, "", getLocale());
			if(SystemErrorCode.UNKNOWN.equals(errorCode)){
				errorMsg = findMessageByThrowable(ex, errorArgs);
			}else{
				errorMsg = findMessageByErrorCode(errorCode, errorArgs);
			}
//			defaultViewName = ExceptionView.CODE_EXCEPTON;
//			defaultViewName = ExceptionView.UNDEFINE;
		}
		
		if(StringUtils.isBlank(errorMsg)){
			errorMsg = LangUtils.getCauseServiceException(ex).getMessage();
		}
		
		/*String viewName = null;
		
		if(StringUtils.isBlank(viewName)){
			viewName = findInSiteConfig(ex); 
			viewName = StringUtils.firstNotBlank(viewName, defaultViewName);
		}*/
		String viewName = determineViewName(ex, request);
		
		//always true if not production
		detail = bootSiteConfig.isProduct()?detail:true;
		error.setCode(errorCode);
		error.setMesage(errorMsg);
		error.setDetail(detail);
		error.setViewName(viewName);
//		error.setAuthentic(authentic);
		return error;
	}
	
	public String findMessageByErrorCode(String errorCode, Object...errorArgs){
		String errorMsg = getMessage(errorCode, errorArgs, "", getLocale());
		return errorMsg;
	}
	
	public String findMessageByThrowable(Throwable e, Object...errorArgs){
		String errorMsg = findMessageByErrorCode(e.getClass().getName(), errorArgs);
		if(StringUtils.isBlank(errorMsg)){
			errorMsg = findMessageByErrorCode(e.getClass().getSimpleName(), errorArgs);
		}
		return errorMsg;
	}
	protected String getPreurl(HttpServletRequest request){
		String preurl = StringUtils.isBlank(request.getParameter(PRE_URL))?BootWebUtils.requestUri():request.getParameter(PRE_URL);
//		return encode(preurl);
		return preurl;
	}
	
	protected ModelAndView createModelAndView(String viewName, ModelMap model, HttpServletRequest request, HttpServletResponse response, Exception ex){
//		return new ModelAndView(viewName, model);
		if (viewName != null) {
			// Apply HTTP status code for error views, if specified.
			// Only apply it if we're processing a top-level request.
			Integer statusCode = determineStatusCode(request, viewName);
			if (statusCode != null) {
				applyStatusCodeIfPossible(request, response, statusCode);
			}
			return getModelAndView(viewName, ex, request);
		}
		return null;
	}
	
	protected void doLog(HttpServletRequest request, Object handlerMethod, Exception ex, boolean detail){
		String msg = RequestUtils.getServletPath(request);
		if(detail){
			msg += " ["+handlerMethod+"] error: " + ex.getMessage();
			logger.error(msg, ex);
			JFishLoggerFactory.mailLog(notifyThrowables, ex, msg);
		}else{
			logger.error(msg + " code[{}], message[{}]", LangUtils.getBaseExceptonCode(ex), ex.getMessage());
		}
	}
	
	@Deprecated
	private String findInSiteConfig(Exception ex){
		Class<?> eclass = ex.getClass();
		String viewName = null;
		while(eclass!=null && Throwable.class.isAssignableFrom(eclass)){
			viewName = bootSiteConfig.getConfig(eclass.getName(), "");
			if(StringUtils.isNotBlank(viewName))
				return viewName;
			eclass = eclass.getSuperclass();
		} 
		return viewName;
	}

	protected String getMessage(String code, Object[] args, String defaultMessage, Locale locale){
		if(this.exceptionMessageAccessor==null)
			return "";
		try {
//			return this.exceptionMessage.getMessage(code, args, defaultMessage, locale);
			return this.exceptionMessageAccessor.getMessage(code, args, defaultMessage, locale);
		} catch (Exception e) {
			logger.error("getMessage error :" + e.getMessage(), e);
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	
	protected Locale getLocale(){
//		return JFishWebUtils.getLocale();
		return BootUtils.getDefaultLocale();
//		return LocaleContextHolder.getLocale();
	}

	protected String getMessage(String code, Object[] args) {
		if(this.exceptionMessageAccessor==null)
			return "";
		try {
//			return this.exceptionMessage.getMessage(code, args, getLocale());
			return this.exceptionMessageAccessor.getMessage(code, args);
		} catch (Exception e) {
			logger.error("getMessage error :" + e.getMessage(), e);
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
		
	}

	protected static class ErrorMessage {
		private String code;
		private String mesage;
		boolean detail;
//		private boolean authentic = false;
		private String viewName;
		
		final private Throwable throwable;
		
		
		public ErrorMessage(Throwable throwable) {
			super();
			this.throwable = throwable;
		}
		/*public ErrorMessage(String code, String mesage, boolean detail) {
			super();
			this.code = code;
			this.mesage = mesage;
			this.detail = detail;
		}*/
		public String getCode() {
			return code;
		}
		public String getMesage() {
			return mesage;
		}
		public boolean isDetail() {
			return detail;
		}
		public String getViewName() {
			return viewName;
		}
		public void setViewName(String viewName) {
			this.viewName = viewName;
		}
		
		public Throwable getThrowable() {
			return throwable;
		}
		
		public void setCode(String code) {
			this.code = code;
		}
		public void setMesage(String mesage) {
			this.mesage = mesage;
		}
		public void setDetail(boolean detail) {
			this.detail = detail;
		}
		public boolean isNotLoginException() {
			return NotLoginException.class.isInstance(throwable);
		}
		public boolean isNoPermissionException() {
			return NoAuthorizationException.class.isInstance(throwable);
		}
		/*
		public void setAuthentic(boolean authentic) {
			this.authentic = authentic;
		}*/
		public String toString(){
			return LangUtils.append(code, ":", mesage);
		}
		
	}
}
