package org.onetwo.plugins.rest.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.spring.web.mvc.SingleReturnWrapper;
import org.onetwo.common.spring.web.mvc.WebExceptionResolver;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.plugins.rest.RestResult;
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
		
		if("json".equalsIgnoreCase(extension)){
			RestResult<?> rest = new RestResult<Object>();
			ErrorMessage errorMessage = this.getErrorMessage(request, handlerMethod, null, ex);
			
			this.doLog(request, handlerMethod, ex, errorMessage.isDetail());
			
			rest.setCode(errorMessage.getCode());
			rest.setMessage(errorMessage.getMesage());
			
			ModelAndView mv = new ModelAndView();
			mv.addObject(SingleReturnWrapper.wrap(rest));
			return mv;
		}else{
			return super.doResolveHandlerMethodException(request, response, handlerMethod, ex);
		}
	}

}
