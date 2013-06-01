package org.onetwo.common.spring.ejbcontext;

import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
import org.w3c.dom.Element;

public class ExtContextNamespaceParser extends ComponentScanBeanDefinitionParser {


	private static final String PARENT_ATTRIBUTE = "parent";

	protected ExtClassPathBeanScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters) {
		return new ExtClassPathBeanScanner(readerContext.getRegistry());
	}
	
	protected ClassPathBeanDefinitionScanner configureScanner(ParserContext parserContext, Element element) {
		ExtClassPathBeanScanner scanner = (ExtClassPathBeanScanner)super.configureScanner(parserContext, element);

		if (element.hasAttribute(PARENT_ATTRIBUTE)) {
			scanner.setParent(element.getAttribute(PARENT_ATTRIBUTE));
		}
		
		return scanner;
	}
	
	
}
