package org.onetwo.plugins.rest.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.fish.exception.JFishBusinessException;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.common.spring.web.mvc.SingleReturnWrapper;
import org.onetwo.common.spring.web.mvc.WebExceptionResolver;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.plugins.rest.ErrorCode;
import org.onetwo.plugins.rest.RestResult;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/************
 * 异常处理
 * @author wayshall
 *
 */
public class RestExceptionResolver extends WebExceptionResolver {

	
	public RestExceptionResolver(){
		super();
	}
	

	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
		if(handlerMethod==null)
			return null;
		
		String extension = JFishWebUtils.requestExtension();
		
		if("json".equals(extension)){
			RestResult<?> rest = new RestResult<Object>();
			ErrorMessage errorMessage = this.getRestErrorMessage(request, handlerMethod, ex);
			
			this.doLog(request, handlerMethod, ex, errorMessage.isDetail());
			
			rest.setError_code(errorMessage.getCode());
			rest.setRet_msg(errorMessage.getMesage());
			
			ModelAndView mv = new ModelAndView();
			mv.addObject(SingleReturnWrapper.wrap(rest));
			return mv;
		}else{
			return super.doResolveHandlerMethodException(request, response, handlerMethod, ex);
		}
	}
	
	protected ErrorMessage getRestErrorMessage(HttpServletRequest request, HandlerMethod handlerMethod, Exception ex){
		String errorCode = "";
		String errorMsg = "";
		boolean detail = true;
		if(BaseException.class.isInstance(ex)){
//			BaseException be = (BaseException) ex;
//			errorCode = be.getCode();
			errorCode = ErrorCode.SYSTEM_ERROR;
		}else if(BusinessException.class.isInstance(ex)){
			BusinessException be = (BusinessException) ex;
			errorCode = be.getCode();
			
		}else if(ValidationException.class.isInstance(ex)){
			ValidationException ve = (ValidationException) ex;
			errorCode = ErrorCode.COM_ER_VALIDATION;
			errorMsg = ve.getMessage();
			detail = false;
			
		}else if(BindException.class.isInstance(ex)){
			BindException bind = (BindException) ex;
			errorCode = ErrorCode.COM_ER_VALIDATION;
			BindingResult br = bind.getBindingResult();
			errorMsg = ValidatorUtils.asString(br);
			detail = false;
			
		}else if(JFishBusinessException.class.isInstance(ex)){
			JFishBusinessException be = (JFishBusinessException) ex;
			errorCode = be.getCode();
			
			if(StringUtils.isNotBlank(errorCode)){
				errorMsg = getMessage(errorCode, be.getArgs());
			}
			detail = false;
			
		}else if(TypeMismatchException.class.isInstance(ex)){
			errorCode = ErrorCode.COM_ER_VALIDATION;
			
		}else{
			errorCode = getUnknowError();
			errorMsg = getMessage(ErrorCode.UN_KNOWN, null, ErrorCode.UN_KNOWN, request.getLocale());
		}
		
//		this.doLog(request, handlerMethod, ex, detail);
		
		if(StringUtils.isBlank(errorMsg)){
			errorMsg = getMessage(errorCode, null, ex.getMessage(), request.getLocale());
		}
		
//		return new ErrorMessage(errorCode, errorMsg, detail);
		ErrorMessage error = new ErrorMessage(ex);
		error.setCode(errorCode);
		error.setMesage(errorMsg);
		error.setDetail(detail);
		return error;
	}
	
	protected String getUnknowError(){
		return ErrorCode.UN_KNOWN;
	}

}
