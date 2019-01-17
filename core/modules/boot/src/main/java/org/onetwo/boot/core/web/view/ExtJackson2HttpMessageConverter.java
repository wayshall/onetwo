package org.onetwo.boot.core.web.view;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.web.utils.WebHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;

/**
 * @author wayshall
 * <br/>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExtJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter implements InitializingBean {
	private Set<String> jsonpParameterNames = new LinkedHashSet<String>(Arrays.asList("jsonp", "callback"));
	private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
	
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
//		outputMessage.getHeaders().setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
		Optional<HttpServletRequest> requestOpt = WebHolder.getRequest();
		if(requestOpt.isPresent()){
			object = filterAndWrapModel(object, outputMessage, requestOpt.get());
		}
		super.writeInternal(object, type, outputMessage);
	}

	public void setMapperBuilder(Jackson2ObjectMapperBuilder mapperBuilder) {
		this.mapperBuilder = mapperBuilder;
	}

	public void setObjectMapperProvider(ObjectMapperProvider objectMapperProvider) {
		this.objectMapperProvider = objectMapperProvider;
	}

	protected Object filterAndWrapModel(Object value, HttpOutputMessage outputMessage, HttpServletRequest request) {
		String jsonpParameterValue = getJsonpParameterValue(request);
		if (jsonpParameterValue != null) {
			if (value instanceof MappingJacksonValue) {
				((MappingJacksonValue) value).setJsonpFunction(jsonpParameterValue);
			}
			else {
				MappingJacksonValue container = new MappingJacksonValue(value);
				container.setJsonpFunction(jsonpParameterValue);
				value = container;
			}
			outputMessage.getHeaders().setContentType(MediaType.parseMediaType("application/javascript"));
		}
		return value;
	}
	
	public void setJsonpParameterNames(Set<String> jsonpParameterNames) {
		this.jsonpParameterNames = jsonpParameterNames;
	}

	private String getJsonpParameterValue(HttpServletRequest request) {
		if (this.jsonpParameterNames != null) {
			for (String name : this.jsonpParameterNames) {
				String value = request.getParameter(name);
				if (StringUtils.isEmpty(value) || !isValidJsonpQueryParam(value)) {
					continue;
				}
				return value;
			}
		}
		return null;
	}
	
	protected boolean isValidJsonpQueryParam(String value) {
		return CALLBACK_PARAM_PATTERN.matcher(value).matches();
	}
	
}
