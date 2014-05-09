package org.onetwo.common.spring.web.mvc.args;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.WebHelper;
import org.onetwo.common.spring.web.mvc.annotation.WebAttribute;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.UserDetail;
import org.slf4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserDetailArgumentResolver implements HandlerMethodArgumentResolver {
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

//	private SessionStorer sessionStorer;
	
	public UserDetailArgumentResolver(){
//		SessionStorer ss = SpringApplication.getInstance().getSpringHighestOrder(SessionStorer.class);
//		this.sessionStorer = ss;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return (UserDetail.class.isAssignableFrom(parameter.getParameterType()) && !parameter.hasParameterAnnotation(WebAttribute.class))
				|| WebHelper.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object result = null;
		if(UserDetail.class.isAssignableFrom(parameter.getParameterType())){
//			WebAttribute wb = parameter.getParameterAnnotation(WebAttribute.class);
			 if(webRequest.getSessionMutex() instanceof HttpSession){
				 HttpSession session = (HttpSession)webRequest.getSessionMutex();
				 Enumeration<String> attrNames = session.getAttributeNames();
				 String name = null;
				 Object value = null;
				 while(attrNames.hasMoreElements()){
					 name = attrNames.nextElement();
					 value = session.getAttribute(name);
					 if(parameter.getParameterType().isInstance(value)){
//						 if(StringUtils.isBlank(wb.value())){
//							 result = value;
//							 break;
//						 }else if(wb.value().equals(name)){
//							 result = value;
//							 break;
//						 }
						 result = value;
						 break;
					 }
				 }
			 }
		}else if(WebHelper.class.isAssignableFrom(parameter.getParameterType())){
			result = JFishWebUtils.webHelper();
			if(result==null){
				try {
					result = WebHelper.newHelper(webRequest);
				} catch (Exception e) {
					logger.error("can not instance web helper : " + webRequest.getNativeRequest());
				}
			}
		}
		return result;
	}

}
