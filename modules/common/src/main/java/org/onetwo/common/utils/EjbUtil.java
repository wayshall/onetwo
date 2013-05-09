package org.onetwo.common.utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class EjbUtil {

	protected static Logger logger = Logger.getLogger(EjbUtil.class);

	public static Object remoteLookup(String mappedName, String JNDIName) {
		Context context;
		try {
			context = new InitialContext();
			Object object = context.lookup(mappedName + "#" + JNDIName);
			return object;
		} catch (NamingException e) {
			logger.error(e);
			return null;
		}
	}

}
