package org.onetwo.common.web.exception;

import javax.xml.rpc.ServiceException;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.BusinessException;

public class ExceptionUtilsTest {

	@Test
	public void testExceptionConfig(){
		AuthenticationException auth = new AuthenticationException();
		String viewName = ExceptionUtils.findInSiteConfig(auth);
		Assert.assertEquals("error_authentic", viewName);
		
		BusinessException be = new BusinessException("");
		viewName = ExceptionUtils.findInSiteConfig(be);
		Assert.assertEquals("500", viewName);
		
		ServiceException se = new ServiceException("");
		viewName = ExceptionUtils.findInSiteConfig(se);
		Assert.assertEquals("error_service", viewName);
		
		RuntimeException re = new RuntimeException("");
		viewName = ExceptionUtils.findInSiteConfig(re);
		Assert.assertEquals("500", viewName);
	}
}
