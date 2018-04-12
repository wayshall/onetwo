package org.onetwo.boot.module.oauth2.authorize;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.onetwo.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @author wayshall
 * <br/>
 */
@Deprecated
public class CustomClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {

	private boolean allowOnlyPost = false;
	@Autowired
	private ExtJackson2HttpMessageConverter httpMessageConverter;
	private Class<? extends ClientDetailRequest> bodyType;
	
	public CustomClientCredentialsTokenEndpointFilter(String path) {
		super(path);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		if (allowOnlyPost && !"POST".equalsIgnoreCase(request.getMethod())) {
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" });
		}

		ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
		MediaType mediaType = getMediaType(inputMessage);
		ClientDetailRequest messageBody = null;
		if(httpMessageConverter.canRead(bodyType, mediaType)){
			messageBody = (ClientDetailRequest)httpMessageConverter.read(bodyType, inputMessage);
		}else{
			return super.attemptAuthentication(request, response);
		}

		String clientId = messageBody.getClientId();
		String clientSecret = messageBody.getClientSecret();

		// If the request is already authenticated we can assume that this
		// filter is not needed
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication;
		}

		if (clientId == null) {
			throw new BadCredentialsException("No client credentials presented");
		}

		if (clientSecret == null) {
			clientSecret = "";
		}

		clientId = clientId.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId,
				clientSecret);
		authRequest.setDetails(messageBody);

		return this.getAuthenticationManager().authenticate(authRequest);

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

	public void setAllowOnlyPost(boolean allowOnlyPost) {
		this.allowOnlyPost = allowOnlyPost;
		super.setAllowOnlyPost(allowOnlyPost);
	}
}
