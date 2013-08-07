package org.example.utils;

import org.example.WebConfig;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.dozer.DozerFacotry;
import org.onetwo.common.spring.xml.JaxbMapperFactory;
import org.onetwo.common.utils.bean.BeanDuplicator;
import org.onetwo.common.utils.xml.jaxb.JaxbMapper;


abstract public class FastUtils {

	private final static BeanDuplicator BEAN_DUPLICATOR = DozerFacotry.createBeanDuplicator(FastUtils.class.getClassLoader(), WebConfig.class.getPackage().getName());
	private final static JaxbMapper JAXB_MAPPER = JaxbMapperFactory.createMapper(WebConfig.class.getPackage().getName());

	public static BeanDuplicator beanDuplicator() {
		return BEAN_DUPLICATOR;
	}
	
	public static JaxbMapper jaxbMapper(){
		return JAXB_MAPPER;
	}
	
	public static JsonMapper jsonMapper(){
		return JsonMapper.DEFAULT_MAPPER;
	}
	
	public static JsonMapper jsonMapperIgnoreNull(){
		return JsonMapper.IGNORE_NULL;
	}
	
	public static JsonMapper jsonMapperIgnoreEmpty(){
		return JsonMapper.IGNORE_EMPTY;
	}
	
	public static void main(String[] args){
		JaxbMapper jaxb = FastUtils.jaxbMapper();
		System.out.println(jaxb);
	}
}
