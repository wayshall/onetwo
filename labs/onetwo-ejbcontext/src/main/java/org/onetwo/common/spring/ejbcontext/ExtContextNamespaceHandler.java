package org.onetwo.common.spring.ejbcontext;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class ExtContextNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("component-scan", new ExtContextNamespaceParser());
	}

}
