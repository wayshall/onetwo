package org.onetwo.common.spring.web.mvc;

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
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.fish.exception.ExceptionMessageArgs;
import org.onetwo.common.fish.exception.JFishErrorCode.MvcError;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.exception.ExceptionUtils;
import org.onetwo.common.web.exception.ExceptionUtils.ExceptionView;
import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.onetwo.common.web.utils.RequestTypeUtils;
import org.onetwo.common.web.utils.RequestTypeUtils.AjaxKeys;
import org.onetwo.common.web.utils.RequestTypeUtils.RequestType;
import org.onetwo.common.web.utils.RequestUtils;
import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
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
	
	public static final int RESOLVER_ORDER = -9999;

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
//	private Map<String, WhenExceptionMap> whenExceptionCaches = new WeakHashMap<String, WhenExceptionMap>();
	
	private MvcSetting mvcSetting;
	private MessageSource exceptionMessage;
	
	public WebExceptionResolver(){
		setOrder(RESOLVER_ORDER);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {

		ModelMap model = new ModelMap();
		ErrorMessage errorMessage = this.getErrorMessage(request, handlerMethod, model, ex);
		this.doLog(request, handlerMethod, ex, errorMessage.isDetail());
		

		String msg = errorMessage.getMesage();
		if(!model.containsKey(AbstractBaseController.MESSAGE)){
			model.put(AbstractBaseController.MESSAGE, msg);
			model.put(AbstractBaseController.MESSAGE_TYPE, AbstractBaseController.MESSAGE_TYPE_ERROR);
		}
		if(JFishWebUtils.isRedirect(errorMessage.getViewName())){
			return this.createModelAndView(errorMessage.getViewName(), model, request);
		}
		

		String extension = JFishWebUtils.requestExtension();
		
		String reqeustKey = request.getHeader(RequestTypeUtils.HEADER_KEY);
		RequestType requestType = RequestTypeUtils.getRequestType(reqeustKey);
		if("json".equals(extension) || RequestType.Ajax.equals(requestType)){
			model.put(AjaxKeys.MESSAGE_KEY, "操作失败："+ ex.getMessage());
			model.put(AjaxKeys.MESSAGE_CODE_KEY, AjaxKeys.RESULT_FAILED);
			return createModelAndView(null, model, request);
		}else if(RequestType.Flash.equals(requestType)){
			model.put(AjaxKeys.MESSAGE_KEY, "error request: "+ex.getMessage());
			model.put(AjaxKeys.MESSAGE_CODE_KEY, AjaxKeys.RESULT_FAILED);
			return createModelAndView(null, model, request);
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
		boolean authentic = false;
		
		boolean findMsgByCode = true;
		if(ex instanceof MaxUploadSizeExceededException){
			defaultViewName = ExceptionView.UNDEFINE;
			errorCode = MvcError.MAX_UPLOAD_SIZE_ERROR;
			errorArgs = new Object[]{this.mvcSetting.getMaxUploadSize()};
		}else if(ex instanceof AuthenticationException){
			defaultViewName = ExceptionView.AUTHENTIC;
			detail = false;
			authentic = true;
		}else if(ex instanceof LoginException){
			defaultViewName = ExceptionView.AUTHENTIC;
			detail = false;
			authentic = true;
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
		}*/else if(ex instanceof BaseException){
			defaultViewName = ExceptionView.SYS_BASE;
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
		}
		
		

		if(ExceptionCodeMark.class.isInstance(ex)){
			ExceptionCodeMark codeMark = (ExceptionCodeMark) ex;
			errorCode = codeMark.getCode();
			findMsgByCode = StringUtils.isNotBlank(errorCode);
		}else if(StringUtils.isBlank(errorCode)){
			errorCode = ex.getClass().getName();
		}
		
		if(ExceptionMessageArgs.class.isInstance(ex)){
			errorArgs = ((ExceptionMessageArgs)ex).getArgs();
		}

		if(findMsgByCode){
			errorMsg = getMessage(errorCode, errorArgs, "", getLocale());
			defaultViewName = ExceptionView.CODE_EXCEPTON;
		}
		
		if(StringUtils.isBlank(errorMsg)){
			detail = true;
			errorMsg = LangUtils.getCauseServiceException(ex).getMessage();
		}else{
			detail = false;
		}
		
		String viewName = null;
		if(authentic){
			AuthenticationContext context = AuthenticUtils.getContextFromRequest(request);
			viewName = context!=null?context.getConfig().getRedirect():"";
			model.addAttribute(PRE_URL, JFishWebUtils.requestUri());
//			if(JFishWebUtils.isRedirect(viewName))
//				viewName = appendPreurlForAuthentic(viewName);
		}else {
			viewName = findInSiteConfig(ex); 
		}
		if(StringUtils.isBlank(viewName)){
			viewName = defaultViewName;
		}
		
		ErrorMessage error = new ErrorMessage(errorCode, errorMsg, detail);
		error.setViewName(viewName);
		
		return error;
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
			msg += " ["+handlerMethod+"] error: ";
			logger.error(msg, ex);
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
		return JFishWebUtils.DEFAULT_LOCAL;
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


	protected static class ErrorMessage {
		final private String code;
		final private String mesage;
		final boolean detail;
		private String viewName;
		
		public ErrorMessage(String code, String mesage, boolean detail) {
			super();
			this.code = code;
			this.mesage = mesage;
			this.detail = detail;
		}
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
		
		public String toString(){
			return LangUtils.append(code, ":", mesage);
		}
		
	}
}
