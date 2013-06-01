package org.onetwo.common.ioc;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.onetwo.common.exception.BaseException;

abstract public class JndiUtils {
	public static final String JAVA_ENV = "java:comp/env/";

	public static Object lookup(String jndiName){
		Object bean = null;
		try {
			if (!jndiName.startsWith(JAVA_ENV))
				jndiName = JAVA_ENV + jndiName;
			InitialContext context = new InitialContext();
			bean = context.lookup(jndiName);
		} catch (NamingException e) {
			throw new BaseException("lookup error. jndiName : " + jndiName , e);
		}
		return bean;
	}
}
