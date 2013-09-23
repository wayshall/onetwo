package org.onetwo.common.spring.ftl;

import freemarker.ext.beans.BeansWrapper;

final public class FtlUtils {

	public static final BeansWrapper BEAN_WRAPPER = new BeansWrapper();

	static {
		BEAN_WRAPPER.setSimpleMapWrapper(true);
	}
	
	private FtlUtils(){
	}

}
