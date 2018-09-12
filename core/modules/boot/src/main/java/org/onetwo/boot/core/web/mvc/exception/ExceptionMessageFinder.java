package org.onetwo.boot.core.web.mvc.exception;

import java.io.Serializable;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.core.web.utils.RemoteClientUtils;
import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.HeaderableException;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.onetwo.dbm.exception.DbmException;
import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;

/****
 * TODO: 这里可以修改为非ExceptionCodeMark异常（即没有异常代码）可以根据异常获取映射的错误代码或ErrorType，
 * 根据代码获取异常信息，或者直接用ErrorType的message显示异常信息
 * SystemErrorCode定义的错误代码可以放弃了
 * @author wayshall
 *
 */
public interface ExceptionMessageFinder {
	public String ERROR_RESPONSE_HEADER = "X-RESPONSE-JFISH-ERROR";
	//TODO: 必要时加上serviceName头，一边追踪，待实现
	public String ERROR_JSERVICE_HEADER = "X-Response-JService";
	

	default ErrorMessage getErrorMessage(Exception throwable, boolean alwaysLogErrorDetail){
		String errorCode = "";
		String errorMsg = "";
		Object[] errorArgs = null;
		
//		String defaultViewName = ExceptionView.UNDEFINE;
		boolean detail = true;
//		boolean authentic = false;
		ErrorMessage error = new ErrorMessage(throwable);
		
		boolean findMsgByCode = true;

		Exception ex = throwable;
		//内部调用失败
		if(isInternalError(ex)){
			ex = LangUtils.getCauseException(ex, ServiceException.class);
		}
		
		/*if(ex instanceof MaxUploadSizeExceededException){
			defaultViewName = ExceptionView.UNDEFINE;
			errorCode = MAX_UPLOAD_SIZE_ERROR;//MvcError.MAX_UPLOAD_SIZE_ERROR;
//			errorArgs = new Object[]{this.mvcSetting.getMaxUploadSize()};
		}else */
		if(ex instanceof ExceptionCodeMark){//serviceException && businessException
			ExceptionCodeMark codeMark = (ExceptionCodeMark) ex;
			errorCode = codeMark.getCode();
			errorArgs = codeMark.getArgs();
			Optional<Integer> statusCode = codeMark.getStatusCode();
			if(statusCode.isPresent()){
				error.setHttpStatus(HttpStatus.valueOf(statusCode.get()));
			}else if(ex instanceof AuthenticationException){
				detail = false;
				error.setHttpStatus(HttpStatus.UNAUTHORIZED);
			}else if(ex instanceof ServiceException){
				detail = ((ServiceException)ex).getCause()!=null;
				//ServiceException 一般为业务异常，属于预期错误，直接返回正常状态
				error.setHttpStatus(HttpStatus.OK);
			}else{
				//其它实现了ExceptionCodeMark的异常也可以视为预期错误，直接返回正常状态
				error.setHttpStatus(HttpStatus.OK);
			}
			findMsgByCode = StringUtils.isNotBlank(errorCode);// && !codeMark.isDefaultErrorCode();
			
		}else if(BootUtils.isDmbPresent() && DbmException.class.isInstance(ex)){
//			defaultViewName = ExceptionView.UNDEFINE;
//			errorCode = JFishErrorCode.ORM_ERROR;//find message from resouce
			error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			
//			Throwable t = LangUtils.getFirstNotJFishThrowable(ex);
		}else if(ex instanceof BaseException){
//			defaultViewName = ExceptionView.UNDEFINE;
//			errorCode = SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;//find message from resouce
			error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			
//			Throwable t = LangUtils.getFirstNotJFishThrowable(ex);
		}else if(TypeMismatchException.class.isInstance(ex)){
//			errorCode = SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
			errorMsg = "parameter convert error!";
			errorCode = SystemErrorCode.ERR_PARAMETER_CONVERT;
			error.setHttpStatus(HttpStatus.BAD_REQUEST);
		}else if(ex instanceof ConstraintViolationException){
			ConstraintViolationException cex = (ConstraintViolationException) ex;
			Set<ConstraintViolation<?>> constrants = cex.getConstraintViolations();
			errorMsg = ValidatorUtils.toMessages(constrants);
			findMsgByCode = false;
			error.setHttpStatus(HttpStatus.BAD_REQUEST);
		}else if(ex instanceof BindException){
			//处理 ModelAttributeMethodProcessor#resolveArgument的BindException异常, 避免在没有定义Errors（BindingResult）参数的情况下直接显示BindException异常
			BindingResult br = ((BindException)ex).getBindingResult();
			errorMsg = ValidatorUtils.asString(br);
			findMsgByCode = false;
			detail = false;
			errorCode = SystemErrorCode.ERR_PARAMETER_VALIDATE;
			error.setHttpStatus(HttpStatus.BAD_REQUEST);
		}/*else if(ex instanceof ObjectOptimisticLockingFailureException){
			errorCode = ObjectOptimisticLockingFailureException.class.getSimpleName();
		}*//*else if(BeanCreationException.class.isInstance(ex)){
			
		}*/else if(ex instanceof MethodArgumentNotValidException){
			MethodArgumentNotValidException mve = (MethodArgumentNotValidException) ex;
			BindingResult br = mve.getBindingResult();
			errorMsg = ValidatorUtils.asString(br);
			findMsgByCode = false;
			detail = false;
			errorCode = SystemErrorCode.ERR_PARAMETER_VALIDATE;
			error.setHttpStatus(HttpStatus.BAD_REQUEST);
		}else{
			errorCode = SystemErrorCode.UNKNOWN;
			error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		detail = alwaysLogErrorDetail?true:detail;
//		error.setMesage(errorMsg);
		error.setDetail(detail);
		
		if(StringUtils.isBlank(errorCode)){
			errorCode = SystemErrorCode.UNKNOWN;//ex.getClass().getName();
		}
		//设置code，findMessage需要用到
		error.setCode(errorCode);

		if(findMsgByCode){
			errorMsg = findMessage(findMsgByCode, error, errorArgs);
		}
		if(StringUtils.isBlank(errorMsg)){
			errorMsg = LangUtils.getCauseServiceException(ex).getMessage();
		}
		
		//防止远程调用时，方法返回null，且异常定义的httpstatus也为200时，尽管在responsebody里返回error相关数据，
		//但feign客户端判断200且返回类型为null时，不解释response boyd，从而忽略了错误
		//即使不是feign调用，而是普通请求，如果不返回任何数据，http status又是200，实际上调用方（浏览器）也无法判断这个调用是否成功，除非它总是解释response body
		if(error.getHttpStatus()==HttpStatus.OK && RemoteClientUtils.isFeign()){
			HandlerMethod hm = BootWebUtils.currentHandlerMethod();
			if(hm!=null && hm.isVoid()){
				error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//for web
		if(ex instanceof HeaderableException){
			Optional<HttpServletResponse> reponse = WebHolder.getResponse();
			HeaderableException he = (HeaderableException)ex;
			if(reponse.isPresent() && he.getHeaders().isPresent()){
				he.getHeaders().get().forEach((name, value)->{
					reponse.get().setHeader(name, value.toString());
				});
			}
		}
		WebHolder.getResponse().ifPresent(response->{
			response.setHeader(ERROR_RESPONSE_HEADER, error.getCode());
		});
		WebHolder.getRequest().ifPresent(request->{
			BootWebUtils.webHelper(request).setErrorMessage(error);
		});

		error.setCode(errorCode);
//		detail = product?detail:true;
		error.setMesage(errorMsg);
//		error.setDetail(detail);
//		error.setViewName(viewName);
//		error.setAuthentic(authentic);
		return error;
	}
	
	default String findMessage(boolean findMsgByCode, ErrorMessage error, Object[] errorArgs){
		String errorMsg = null;
		String errorCode = error.getCode();
		Exception ex = error.getException();
//			errorMsg = getMessage(errorCode, errorArgs, "", getLocale());
		if(isInternalError(ex)){
			//内部调用失败
			errorMsg = findMessageByThrowable(ex, errorArgs)+" "+LangUtils.getCauseServiceException(ex).getMessage();
		}else if(SystemErrorCode.UNKNOWN.equals(errorCode)){
			errorMsg = findMessageByThrowable(ex, errorArgs);
		}else{
			errorMsg = findMessageByErrorCode(errorCode, errorArgs);
		}
//			defaultViewName = ExceptionView.CODE_EXCEPTON;
//			defaultViewName = ExceptionView.UNDEFINE;
		return errorMsg;
	}
	
	default boolean isInternalError(Exception ex){
		if(BootUtils.isHystrixErrorPresent()){
			String name = ex.getClass().getName();
			return name.endsWith("HystrixRuntimeException") || name.endsWith("HystrixBadRequestException");
		}
		return false;
	}
	
	default Locale getLocale(){
		return BootUtils.getDefaultLocale();
	}

	default String findMessageByErrorCode(String errorCode, Object...errorArgs){
		String errorMsg = getMessage(errorCode, errorArgs, "", getLocale());
		return errorMsg;
	}
	
	default String findMessageByThrowable(Throwable e, Object...errorArgs){
		String errorMsg = findMessageByErrorCode(e.getClass().getName(), errorArgs);
		if(StringUtils.isBlank(errorMsg)){
			errorMsg = findMessageByErrorCode(e.getClass().getSimpleName(), errorArgs);
		}
		return errorMsg;
	}
	
	default String getMessage(String code, Object[] args, String defaultMessage, Locale locale){
		ExceptionMessageAccessor exceptionMessageAccessor = getExceptionMessageAccessor();
		if(exceptionMessageAccessor==null)
			return "";
		try {
//			return exceptionMessage.getMessage(code, args, defaultMessage, locale);
			return exceptionMessageAccessor.getMessage(code, args, defaultMessage, locale);
		} catch (Exception e) {
			hanldeFindMessageError(e);
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}


	default String getMessage(String code, Object[] args){
		ExceptionMessageAccessor exceptionMessageAccessor = getExceptionMessageAccessor();
		if(exceptionMessageAccessor==null)
			return "";
		try {
			return exceptionMessageAccessor.getMessage(code, args);
		} catch (Exception e) {
			hanldeFindMessageError(e);
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	
	default void hanldeFindMessageError(Exception e) {
		JFishLoggerFactory.getCommonLogger().error("getMessage error :" + e.getMessage());
	}
	
	ExceptionMessageAccessor getExceptionMessageAccessor();
	
	

	@SuppressWarnings("serial")
	public static class ErrorMessage implements Serializable {
		private String code;
		private String mesage;
		boolean detail;
		private HttpStatus httpStatus;
//		private boolean authentic = false;
		private String viewName;
		
		final private Exception exception;
		
		
		public ErrorMessage(Exception throwable) {
			super();
			this.exception = throwable;
		}
		
		public void logErrorContext(Logger logger){
			Map<String, Object> ctx = getErrorContext();
			if(!ctx.isEmpty()){
				logger.error("error context: {}", ctx);
			}
		}
		
		public Map<String, Object> getErrorContext(){
			Map<String, Object> ctx = null;
			if(exception instanceof SystemErrorCode){
				ctx = ((SystemErrorCode)exception).getErrorContext();
			}else{
				ctx = Collections.emptyMap();
			}
			return ctx;
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
		
		public Exception getException() {
			return exception;
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
			return NotLoginException.class.isInstance(exception);
		}
		public boolean isNoPermissionException() {
			return NoAuthorizationException.class.isInstance(exception);
		}
		public HttpStatus getHttpStatus() {
			return httpStatus;
		}
		public void setHttpStatus(HttpStatus httpStatus) {
			this.httpStatus = httpStatus;
		}
		/*
		public void setAuthentic(boolean authentic) {
			this.authentic = authentic;
		}*/
		public String toString(){
			return ReflectionToStringBuilder.toString(this);
		}
		
	}
}
