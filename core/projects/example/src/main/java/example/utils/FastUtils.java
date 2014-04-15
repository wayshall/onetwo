package example.utils;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.xml.JaxbMapperFactory;
import org.onetwo.common.utils.xml.jaxb.JaxbMapper;



abstract public class FastUtils {

	private final static JaxbMapper JAXB_MAPPER = JaxbMapperFactory.createMapper(WebConfig.class.getPackage().getName());

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
	
}
