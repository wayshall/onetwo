package org.onetwo.boot.module.oauth2.authorize;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.onetwo.boot.module.oauth2.authorize.ClientDetailRequest.DefaultClientDetailRequest;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.ReflectionUtils;

/**
 * 因为ClientCredentialsTokenEndpointFilter只支持键值参数里读取clientId和clientSecret；
 * 有时候需要通过json从body传进来时，filter会无法读取，此拦截器可以拦截request，并把request.getParameter("client_id")方法转换到读取json body
 * @author wayshall
 * <br/>
 */
@Slf4j
public class DefaultClientDetailFilterInterceptor implements TokenEndpointFilterInterceptor {

	private final BeanToMapConvertor mapConverter = BeanToMapBuilder.newBuilder()
																			.enableFieldNameAnnotation()
																			.enableUnderLineStyle()
																			.build();
	private final Method doFilterMethod = ReflectionUtils.findMethod(AbstractAuthenticationProcessingFilter.class, 
																	"doFilter", 
																	ServletRequest.class,
																	ServletResponse.class,
																	FilterChain.class
																	);
//	@Autowired
	private HttpMessageConverter<Object> httpMessageConverter = new ExtJackson2HttpMessageConverter();
	private Class<? extends ClientDetailRequest> bodyType = DefaultClientDetailRequest.class;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if(!isInterceptMethod(invocation)){
			return invokeTarget(invocation);
		}
		HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[0];
		ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
		MediaType mediaType = getMediaType(inputMessage);
		ClientDetailRequest messageBody = null;
		if(httpMessageConverter.canRead(bodyType, mediaType)){
			messageBody = (ClientDetailRequest)httpMessageConverter.read(bodyType, inputMessage);
			HttpServletRequest requestWrapper = createHttpServletRequestWrapper(request, messageBody);
			invocation.getArguments()[0] = requestWrapper;
			
			Authentication authen = SecurityContextHolder.getContext().getAuthentication();
			if(authen==null){
				UsernamePasswordAuthenticationToken userPass = new UsernamePasswordAuthenticationToken(messageBody.getClientId(), messageBody.getClientSecret());
				userPass.setDetails(messageBody);
				userPass.setAuthenticated(false);
				SecurityContextHolder.getContext().setAuthentication(userPass);
			}else if(authen instanceof UsernamePasswordAuthenticationToken){
				UsernamePasswordAuthenticationToken userPass = (UsernamePasswordAuthenticationToken) authen;
				//标记未验证
				userPass.setAuthenticated(false);
				userPass.setDetails(messageBody);
			}else{
				if(log.isInfoEnabled()){
					log.info("can not set messageBody to Authentication: {}", authen.getClass());
				}
			}
		}
		return invokeTarget(invocation);
	}
	
	protected boolean isInterceptMethod(MethodInvocation invocation){
		return doFilterMethod.equals(invocation.getMethod());
	}
	
	protected Object invokeTarget(MethodInvocation invocation){
		return ReflectionUtils.invokeMethod(invocation.getMethod(), invocation.getThis(), invocation.getArguments());
	}
	
	protected HttpServletRequest createHttpServletRequestWrapper(HttpServletRequest request, ClientDetailRequest messageBody){
		Map<String, Object> params = mapConverter.toFlatMap(messageBody);
		/*Map<String, String[]> paramMap = params.entrySet()
												.stream()
												.collect(Collectors.toMap(
															entry->entry.getKey(), 
															entry->new String[]{entry.getValue().toString()}
															)
												);*/
		return new HttpServletRequestWrapper(request){
			@Override
			public String getParameter(String name) {
				Object val = params.get(name);
				if(val!=null){
					return val.toString();
				}
				return super.getParameter(name);
			}

			@Override
			public Map<String, String[]> getParameterMap() {
				return super.getParameterMap();
			}
			
		};
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


	public void setHttpMessageConverter(HttpMessageConverter<Object> httpMessageConverter) {
		this.httpMessageConverter = httpMessageConverter;
	}

	public void setBodyType(Class<? extends ClientDetailRequest> bodyType) {
		this.bodyType = bodyType;
	}

}
