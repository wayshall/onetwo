package org.onetwo.common.jackson;

import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author weishao zeng
 * <br/>
 */
public class JacksonXmlMapper {

	private final static JacksonXmlMapper IGNORE_EMPTY = ignoreEmpty();
	private final static JacksonXmlMapper IGNORE_NULL = ignoreNull();
	private final static JacksonXmlMapper DEFAULT_MAPPER = defaultMapper();
	

	public static JacksonXmlMapper getIgnoreEmpty() {
		return IGNORE_EMPTY;
	}

	public static JacksonXmlMapper getIgnoreNull() {
		return IGNORE_NULL;
	}

	public static JacksonXmlMapper getDefault() {
		return DEFAULT_MAPPER;
	}

	public static JacksonXmlMapper defaultMapper(){
		JacksonXmlMapper jsonm = new JacksonXmlMapper(Include.ALWAYS);
		return jsonm;
	}
	
	public static JacksonXmlMapper ignoreNull(){
		JacksonXmlMapper jsonm = new JacksonXmlMapper(Include.NON_NULL);
		return jsonm;
	}
	
	public static JacksonXmlMapper ignoreEmpty(){
		JacksonXmlMapper jsonm = new JacksonXmlMapper(Include.NON_EMPTY);
		return jsonm;
	}
	
	private JsonMapper jsonMapper;
	

	public JacksonXmlMapper(Include include){
		this(new XmlMapper(), include, false);
	}

	public JacksonXmlMapper(XmlMapper objectMapper, Include include, boolean fieldVisibility){
		this.jsonMapper = new JsonMapper(objectMapper, include, fieldVisibility);
	}


	public String toXml(Object object){
		return toXml(object, true);
	}
	
	public String toXml(Object object, boolean throwIfError){
		return jsonMapper.toJson(object, throwIfError);
	}
	

	public byte[] toXmlBytes(Object object){
		return toXmlBytes(object, true);
	}
	
	public byte[] toXmlBytes(Object object, boolean throwIfError){
		return jsonMapper.toJsonBytes(object, throwIfError);
	}
	

	public <T> T fromXml(final Object json, Type objType){
		return fromXml(json, objType, false);
	}
	
	public <T> T fromXml(final Object json, Type objType, boolean parseAsStringIfError){
		return jsonMapper.fromJson(json, objType, parseAsStringIfError);
	}
	public <T> T fromXml(final Object json, TypeReference<T> valueTypeRef){
		return jsonMapper.fromJson(json, valueTypeRef);
	}

	public ObjectMapper getObjectMapper() {
		return this.jsonMapper.getObjectMapper();
	}

	public JacksonXmlMapper enableTyping(){
		this.jsonMapper.enableTyping();
		return this;
	}

	public JsonMapper getJsonMapper() {
		return jsonMapper;
	}
}

