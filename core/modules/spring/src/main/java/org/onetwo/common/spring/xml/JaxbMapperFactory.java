package org.onetwo.common.spring.xml;

import java.util.Collection;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.JaxbClassFilter;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.xml.jaxb.JaxbMapper;

final public class JaxbMapperFactory {
	
	private final static ResourcesScanner scaner = new JFishResourcesScanner();
	
	public static JaxbMapper createMapper(String...basePackages){
		Collection<Class<?>> xmlClasses = scaner.scan(JaxbClassFilter.Instance, basePackages);
		/*if(LangUtils.isEmpty(xmlClasses))
			throw new JFishException("can not find any xml class.");*/
		try {
			JaxbMapper mapper = JaxbMapper.createMapper(xmlClasses.toArray(new Class[xmlClasses.size()]));
			return mapper;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BaseException("create jaxb mapper error: " + e.getMessage(), e);
		}
	}

}
