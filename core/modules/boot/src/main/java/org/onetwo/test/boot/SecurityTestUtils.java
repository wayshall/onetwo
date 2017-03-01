package org.onetwo.test.boot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.onetwo.common.data.AbstractDataResult;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

public abstract class SecurityTestUtils {

	public static LoginUserDetails mockLogin(MockMvc mockMvcs, String loginUrl, String userName, String password){
		MvcResult result = mockLogin(mockMvcs, post(loginUrl)
												.accept(MediaType.APPLICATION_JSON_UTF8)
												.param("username", userName)
												.param("password", password));
		return getLoginUserDetails(result);
	}
	public static MvcResult mockLogin(MockMvc mockMvcs, RequestBuilder request){
		MvcResult result;
		try {
			result = mockMvcs.perform(request)
										.andExpect(status().isOk())
										.andExpect(jsonPath("$.code").value(AbstractDataResult.SUCCESS))
										.andReturn();
		} catch (Exception e) {
			throw new RuntimeException("mock login error:"+e.getMessage(), e);
		}
		return result;
		
//		SecurityContextRepository repo = WebTestUtils.getSecurityContextRepository(result.getRequest());
	}
	
	public static SecurityContext getSecurityContext(MvcResult result){
		SecurityContext securityContext = (SecurityContext)result.getRequest().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		return securityContext;
	}
	
	public static LoginUserDetails getLoginUserDetails(MvcResult result){
		SecurityContext securityContext = getSecurityContext(result);
		if(securityContext==null){
			return null;
		}
		LoginUserDetails loginUserDetails = (LoginUserDetails)securityContext.getAuthentication().getPrincipal();
		return loginUserDetails;
	}
}
