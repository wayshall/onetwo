package org.onetwo.common.ejb.interceptor;

import javax.ejb.EJBException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.onetwo.common.ejb.exception.AppEJBException;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbExceptionInterceptor {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@AroundInvoke
	public Object handleEjbException(InvocationContext context) throws Exception{
		Object obj = null;
		String methodName = context.getMethod().getName();
		try {
			obj = context.proceed();
		} catch (Exception e) {
			String errorMsg = LangUtils.toString("methodName[${0}] occur error: ${1}" , methodName, e.getMessage());
			logger.error(errorMsg, e);
			if(e instanceof EJBException){
				throw (EJBException) e;
			}else{
				throw new AppEJBException(errorMsg, e);
			}
		}
		return obj;
	}

}
