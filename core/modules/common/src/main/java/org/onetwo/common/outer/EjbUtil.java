package org.onetwo.common.outer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

public class EjbUtil {

	protected static Logger logger = JFishLoggerFactory.getLogger(EjbUtil.class);

	public static Object remoteLookup(String mappedName, String JNDIName) {
		Context context;
		try {
			context = new InitialContext();
			Object object = context.lookup(mappedName + "#" + JNDIName);
			return object;
		} catch (NamingException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
