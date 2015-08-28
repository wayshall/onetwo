package org.onetwo.common.spring.web.mvc.args;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.annotation.ListParameter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

public class ListParameterArgumentResolver implements HandlerMethodArgumentResolver {
	
//	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

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
		final String attrName;
		if(StringUtils.isBlank(listParameterAnnotation.value()))
			attrName = parameter.getParameterName();
		else
			attrName = listParameterAnnotation.value();
		
		Class<?> etype = ReflectUtils.getGenricType(parameter.getGenericParameterType(), 0);
		Object list = null;
		
		if(MultipartFile.class.isAssignableFrom(etype)){
			MultipartRequest mrequest = webRequest.getNativeRequest(MultipartRequest.class);
			list = mrequest.getFiles(attrName);
			
		}else{
			list = ReflectUtils.newList(listParameterAnnotation.type());
			Map<String, Object> listWrapper = LangUtils.newHashMap();
			listWrapper.put(attrName, list);
			BeanWrapper bw = SpringUtils.newBeanMapWrapper(listWrapper, attrName, etype);
			Iterator<String> pnames = webRequest.getParameterNames();
			while(pnames.hasNext()){
				String pname = pnames.next();
				if(pname.startsWith(attrName) && bw.isWritableProperty(pname)){
					bw.setPropertyValue(pname, webRequest.getParameter(pname));
				}
			}

			if(webRequest.getNativeRequest() instanceof MultipartRequest){
				MultipartRequest mrequest = webRequest.getNativeRequest(MultipartRequest.class);
				mrequest.getFileNames().forEachRemaining(fn->{
					if(fn.startsWith(attrName) && bw.isWritableProperty(fn)){
						bw.setPropertyValue(fn, mrequest.getFile(fn));
					}
				});
			}
		}
		
		
		return list;
	}

}
