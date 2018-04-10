package org.onetwo.boot.module.oauth2.authorize;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.onetwo.boot.module.oauth2.authorize.ClientDetailRequest.DefaultClientDetailRequest;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;

/**
 * @author wayshall
 * <br/>
 */
public class ClientDetailFilterInterceptor implements MethodInterceptor {

	private final BeanToMapConvertor mapConverter = BeanToMapBuilder.newBuilder()
																			.enableFieldNameAnnotation()
																			.enableUnderLineStyle()
																			.build();
//	@Autowired
	private ExtJackson2HttpMessageConverter httpMessageConverter = new ExtJackson2HttpMessageConverter();
	private Class<? extends ClientDetailRequest> bodyType = DefaultClientDetailRequest.class;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if("doFilter".equals(invocation.getMethod().getName())){
			HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[0];
			ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
			MediaType mediaType = getMediaType(inputMessage);
			ClientDetailRequest messageBody = null;
			if(httpMessageConverter.canRead(bodyType, mediaType)){
				messageBody = (ClientDetailRequest)httpMessageConverter.read(bodyType, inputMessage);
				Map<String, Object> params = mapConverter.toFlatMap(messageBody);
				HttpServletRequest requestWrapper = new HttpServletRequestWrapper(request){
					@Override
					public String getParameter(String name) {
						Object val = params.get(name);
						if(val!=null){
							return val.toString();
						}
						return super.getParameter(name);
					}
				};
				invocation.getArguments()[0] = requestWrapper;
			}
		}
		return ReflectionUtils.invokeMethod(invocation.getMethod(), invocation.getThis(), invocation.getArguments());
	}

	
	protected MediaType getMediaType(ServletServerHttpRequest inputMessage){
		MediaType contentType;
		try {
			contentType = inputMessage.getHeaders().getContentType();
		}
		catch (InvalidMediaTypeException ex) {
			throw new BaseException(ex.getMessage());
		}
		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}
		return contentType;
	}

}
