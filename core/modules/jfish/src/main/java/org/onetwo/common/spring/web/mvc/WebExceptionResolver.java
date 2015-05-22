package org.onetwo.common.spring.web.mvc;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.fish.exception.JFishErrorCode.MvcError;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.exception.ExceptionUtils;
import org.onetwo.common.web.exception.ExceptionUtils.ExceptionView;
import org.onetwo.common.web.utils.RequestTypeUtils;
import org.onetwo.common.web.utils.RequestTypeUtils.AjaxKeys;
import org.onetwo.common.web.utils.RequestTypeUtils.RequestType;
import org.onetwo.common.web.utils.RequestUtils;
import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

/************
 * 异常处理
 * @author wayshall
 *
 */
public class WebExceptionResolver extends AbstractHandlerMethodExceptionResolver implements InitializingBean {

	public static final String EXCEPTION_STATCK_KEY2 = "__exception__stack__";
	public static final String EXCEPTION_STATCK_KEY = "__exceptionStack__";
	public static final String ERROR_CODE_KEY = "__errorCode__";
	public static final String PRE_URL = "preurl";
	public static final String AJAX_RESULT_KEY = "result";
	
	public static final int RESOLVER_ORDER = -9999;

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
//	private Map<String, WhenExceptionMap> whenExceptionCaches = new WeakHashMap<String, WhenExceptionMap>();
	protected final Logger mailLogger = JFishLoggerFactory.findLogger("mailLogger");
	
	private MvcSetting mvcSetting;
	private MessageSource exceptionMessage;
	
	private List<String> notifyThrowables = BaseSiteConfig.getInstance().getErrorNotifyThrowabbles();

//	protected String defaultRedirect;
	
	public WebExceptionResolver(){
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
		String extension = JFishWebUtils.requestExtension();
		String reqeustKey = request.getHeader(RequestTypeUtils.HEADER_KEY);
		RequestType requestType = RequestTypeUtils.getRequestType(reqeustKey);
		
		return "json".equals(extension) || RequestType.Ajax.equals(requestType) || RequestType.Flash.equals(requestType) || "true".equalsIgnoreCase(request.getParameter("ajaxRequest"));
	}

	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
		ModelMap model = new ModelMap();
		ErrorMessage errorMessage = this.getErrorMessage(request, handlerMethod, model, ex);
		this.doLog(request, handlerMethod, ex, errorMessage.isDetail());
		
//		Object req = RequestContextHolder.getRequestAttributes().getAttribute(WebHelper.WEB_HELPER_KEY, RequestAttributes.SCOPE_REQUEST);
		if(isAjaxRequest(request)){
//			model.put(AjaxKeys.MESSAGE_KEY, "操作失败："+ ex.getMessage());
//			model.put(AjaxKeys.MESSAGE_CODE_KEY, AjaxKeys.RESULT_FAILED);
			DataResult result = new DataResult();
			result.setCode(AjaxKeys.RESULT_FAILED);
			result.setMessage("操作失败，"+ errorMessage.getMesage());
//			model.put(AJAX_RESULT_KEY, SingleReturnWrapper.wrap(result));
			model.put(AJAX_RESULT_KEY, result);
			return createModelAndView(null, model, request);
		}
		
		String msg = errorMessage.getMesage();
		if(!model.containsKey(AbstractBaseController.MESSAGE)){
			model.put(AbstractBaseController.MESSAGE, msg);
			model.put(AbstractBaseController.MESSAGE_TYPE, AbstractBaseController.MESSAGE_TYPE_ERROR);
		}
		if(JFishWebUtils.isRedirect(errorMessage.getViewName())){
			return this.createModelAndView(errorMessage.getViewName(), model, request);
		}

		String eInfo = "";
		if(!BaseSiteConfig.getInstance().isProduct() && errorMessage.isDetail()){
			eInfo = LangUtils.toString(ex, true);
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY, eInfo);
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY2, eInfo);
		}else{
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY, "");
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY2, "");
		}
//		eInfo = errorMessage.toString()+"  "+ eInfo;
		model.put(EXCEPTION_STATCK_KEY, eInfo);
		model.put(EXCEPTION_STATCK_KEY2, eInfo);
		model.put(ERROR_CODE_KEY, errorMessage.getCode());
		
		
//		WebContextUtils.attr(request, ERROR_CODE_KEY, ecode);
		
		
		return createModelAndView(errorMessage.getViewName(), model, request);
	}

	protected String getUnknowError(){
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	protected ErrorMessage getErrorMessage(HttpServletRequest request, HandlerMethod handlerMethod, ModelMap model, Exception ex){
		String errorCode = "";
		String errorMsg = "";
		Object[] errorArgs = null;
		
		String defaultViewName = ExceptionView.UNDEFINE;
		boolean detail = true;
//		boolean authentic = false;
		ErrorMessage error = new ErrorMessage(ex);
		
		boolean findMsgByCode = true;
		if(ex instanceof MaxUploadSizeExceededException){
			defaultViewName = ExceptionView.UNDEFINE;
			errorCode = MvcError.MAX_UPLOAD_SIZE_ERROR;
			errorArgs = new Object[]{this.mvcSetting.getMaxUploadSize()};
		}else if(ex instanceof LoginException){
			defaultViewName = ExceptionView.AUTHENTIC;
			detail = false;
		}else if(ex instanceof AuthenticationException){
			defaultViewName = ExceptionView.AUTHENTIC;
			detail = false;
		}/*else if(ex instanceof BusinessException){
			defaultViewName = ExceptionView.BUSINESS;
			detail = false;
		}else if(ex instanceof JFishBusinessException){
			defaultViewName = ExceptionView.SERVICE;
			detail = false;
		}else if(ex instanceof JFishServiceException){
			defaultViewName = ExceptionView.SERVICE;
			detail = false;
		}else if(ex instanceof ServiceException){
			defaultViewName = ExceptionView.SERVICE;
		}*/else if(ex instanceof ExceptionCodeMark){//serviceException && businessException
			ExceptionCodeMark codeMark = (ExceptionCodeMark) ex;
			errorCode = codeMark.getCode();
			errorArgs = codeMark.getArgs();
			errorMsg = ex.getMessage();
			
			findMsgByCode = StringUtils.isNotBlank(errorCode) && !codeMark.isDefaultErrorCode();
			detail = !BaseSiteConfig.getInstance().isProduct();
		}else if(ex instanceof BaseException){
			defaultViewName = ExceptionView.SYS_BASE;
			errorCode = SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
			
//			Throwable t = LangUtils.getFirstNotJFishThrowable(ex);
		}else if(TypeMismatchException.class.isInstance(ex)){
			defaultViewName = ExceptionView.UNDEFINE;
			//errorMsg = "parameter convert error!";
		}else if(ex instanceof ConstraintViolationException){
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
		}else if(ex instanceof ObjectOptimisticLockingFailureException){
			errorCode = ObjectOptimisticLockingFailureException.class.getSimpleName();
		}/*else if(BeanCreationException.class.isInstance(ex)){
			
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
			defaultViewName = ExceptionView.CODE_EXCEPTON;
		}
		
		if(StringUtils.isBlank(errorMsg)){
			errorMsg = LangUtils.getCauseServiceException(ex).getMessage();
		}
		
		String viewName = null;
		if(error.isNotLoginException()){
			viewName = getLoginView(request, model);
//			model.addAttribute(PRE_URL, getPreurl(request));
//			if(JFishWebUtils.isRedirect(viewName))
//				viewName = appendPreurlForAuthentic(viewName);
		}else if(error.isNoPermissionException()){
			viewName = getNoPermissionView(request, model, error);
		}
		
		if(StringUtils.isBlank(viewName)){
			viewName = findInSiteConfig(ex); 
			viewName = StringUtils.firstNotBlank(viewName, defaultViewName);
		}
		
		detail = BaseSiteConfig.getInstance().isProduct()?detail:true;
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
	public String getNoPermissionView(HttpServletRequest request, ModelMap model, ErrorMessage error){
		if(model!=null){
			model.addAttribute("noPermissionPath", request.getRequestURI());
		}
		return BaseSiteConfig.getInstance().getSecurityNopermissionView();
	}
	protected String getPreurl(HttpServletRequest request){
		String preurl = StringUtils.isBlank(request.getParameter(PRE_URL))?JFishWebUtils.requestUri():request.getParameter(PRE_URL);
//		return encode(preurl);
		return preurl;
	}
	
	protected String getLoginView(HttpServletRequest request, ModelMap model){
		if(model!=null){
			model.addAttribute(PRE_URL, getPreurl(request));
		}
		/*if(StringUtils.isBlank(defaultRedirect))
			return JFishWebUtils.redirect(defaultRedirect);*/
		return JFishWebUtils.redirect("/login");
		/*AuthenticationContext context = AuthenticUtils.getContextFromRequest(request);
		String view = context!=null?context.getConfig().getRedirect():"";
		return view;*/
	}
	
	protected ModelAndView createModelAndView(String viewName, ModelMap model, HttpServletRequest request){
		if(JFishWebUtils.isRedirect(viewName)){
//			RequestContextUtils.getOutputFlashMap(request).putAll(model);
			if(BaseSiteConfig.getInstance().hasAppUrlPostfix()){
				viewName = BaseSiteConfig.getInstance().appendAppUrlPostfix(viewName);
			}
			return new ModelAndView(viewName, model);
		}else{
			return new ModelAndView(viewName, model);
		}
	}
	
	protected void doLog(HttpServletRequest request, HandlerMethod handlerMethod, Exception ex, boolean detail){
		String msg = RequestUtils.getServletPath(request);
		if(detail){
			msg += " ["+handlerMethod+"] error: " + ex.getMessage();
			logger.error(msg, ex);
			JFishLoggerFactory.mailLog(notifyThrowables, ex, msg);
		}else{
			logger.error(msg + " code[{}], message[{}]", LangUtils.getBaseExceptonCode(ex), ex.getMessage());
		}
	}
	
//	protected String findWhenExceptionView(Class<?> clazz, Method method, Exception ex){
//		String key = method.toGenericString();
//		WhenExceptionMap map = this.whenExceptionCaches.get(key);
//		if(map!=null && map.match(ex))
//			return map.getPage();
//		
//		WhenExceptionMap wm = ExceptionUtils.findWhenException(clazz, method);
//		if(wm!=null){
//			this.whenExceptionCaches.put(key, wm);
//			if(wm.match(ex))
//				return wm.getPage();
//		}
//		return null;
//	}
	
	public static String findInSiteConfig(Exception ex){
		return ExceptionUtils.findInSiteConfig(ex);
	}

	protected String getMessage(String code, Object[] args, String defaultMessage, Locale locale){
		if(this.exceptionMessage==null)
			return "";
		try {
			return this.exceptionMessage.getMessage(code, args, defaultMessage, locale);
		} catch (Exception e) {
			logger.error("getMessage error :" + e.getMessage(), e);
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	
	protected Locale getLocale(){
//		return JFishWebUtils.getLocale();
		return JFishUtils.getDefaultLocale();
	}

	protected String getMessage(String code, Object[] args) {
		if(this.exceptionMessage==null)
			return "";
		try {
			return this.exceptionMessage.getMessage(code, args, getLocale());
		} catch (Exception e) {
			logger.error("getMessage error :" + e.getMessage(), e);
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}

	protected MessageSource getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(MessageSource exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	

	public void setMvcSetting(MvcSetting mvcSetting) {
		this.mvcSetting = mvcSetting;
	}

	/*protected static enum ErrorType {
		LOGIN,
		NOPERMISSION,
		OTHER
	}
*/
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
