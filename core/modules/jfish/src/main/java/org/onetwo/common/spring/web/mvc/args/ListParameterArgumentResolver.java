package org.onetwo.common.spring.web.mvc.args;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.annotation.ListParameter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ListParameterArgumentResolver implements HandlerMethodArgumentResolver {
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ListParameter.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		ListParameter listParameterAnnotation = parameter.getParameterAnnotation(ListParameter.class);
		if(!List.class.isAssignableFrom(parameter.getParameterType())){
			throw new BaseException("the parameter type must be a List: " + parameter.getParameterType());
		}
		String attrName = listParameterAnnotation.value();
		if(StringUtils.isBlank(attrName))
			attrName = parameter.getParameterName();

		Class<?> etype = ReflectUtils.getGenricType(parameter.getGenericParameterType(), 0);
		Object list = ReflectUtils.newList(listParameterAnnotation.type());
		
		Map<String, Object> listWrapper = LangUtils.newHashMap();
		listWrapper.put(attrName, list);
		BeanWrapper bw = SpringUtils.newBeanWrapper(listWrapper, attrName, etype);
		Iterator<String> pnames = webRequest.getParameterNames();
		while(pnames.hasNext()){
			String pname = pnames.next();
			if(pname.startsWith(attrName)){
				bw.setPropertyValue(pname, webRequest.getParameter(pname));
			}
		}
		
		return list;
	}

}
