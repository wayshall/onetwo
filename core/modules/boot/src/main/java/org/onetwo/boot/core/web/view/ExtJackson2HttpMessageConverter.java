package org.onetwo.boot.core.web.view;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.onetwo.common.jackson.JsonMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author wayshall
 * <br/>
 */
public class ExtJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter implements InitializingBean {
	
	private Jackson2ObjectMapperBuilder mapperBuilder;
	@Autowired(required=false)
	private ObjectMapperProvider objectMapperProvider;

	public ExtJackson2HttpMessageConverter() {
		super(JsonMapper.ignoreNull().getObjectMapper());
		setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, 
				MediaType.APPLICATION_JSON_UTF8
//				MediaType.TEXT_PLAIN
				));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(mapperBuilder!=null){
			setObjectMapper(mapperBuilder.build());
		}
		if(objectMapperProvider!=null){
			setObjectMapper(objectMapperProvider.createObjectMapper());
		}
	}
	

	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		outputMessage.getHeaders().setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
		super.writeInternal(object, type, outputMessage);
	}

	public void setMapperBuilder(Jackson2ObjectMapperBuilder mapperBuilder) {
		this.mapperBuilder = mapperBuilder;
	}

	public void setObjectMapperProvider(ObjectMapperProvider objectMapperProvider) {
		this.objectMapperProvider = objectMapperProvider;
	}
	
}
