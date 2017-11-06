package org.onetwo.boot.core.web.mvc.exception;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.exception.DbmException;
import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

/****
 * TODO: 这里可以修改为非ExceptionCodeMark异常（即没有异常代码）可以根据异常获取映射的错误代码或ErrorType，
 * 根据代码获取异常信息，或者直接用ErrorType的message显示异常信息
 * SystemErrorCode定义的错误代码可以放弃了
 * @author wayshall
 *
 */
public interface ExceptionMessageFinder {

	default ErrorMessage getErrorMessage(Exception ex, boolean product){
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
			}else{
				error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			findMsgByCode = StringUtils.isNotBlank(errorCode);// && !codeMark.isDefaultErrorCode();
			detail = !product;
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
			error.setHttpStatus(HttpStatus.BAD_REQUEST);
		}else{
			errorCode = SystemErrorCode.UNKNOWN;
			error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		/*if(StringUtils.isBlank(errorCode)){
			errorCode = ex.getClass().getName();
		}*/

		if(findMsgByCode){
//			errorMsg = getMessage(errorCode, errorArgs, "", getLocale());
			if(StringUtils.isBlank(errorCode) || SystemErrorCode.UNKNOWN.equals(errorCode)){
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
		
		detail = product?detail:true;
		error.setCode(errorCode);
		error.setMesage(errorMsg);
		error.setDetail(detail);
//		error.setViewName(viewName);
//		error.setAuthentic(authentic);
		return error;
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
		System.err.println("getMessage error :" + e.getMessage());
	}
	
	ExceptionMessageAccessor getExceptionMessageAccessor();
	
	

	public static class ErrorMessage {
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
