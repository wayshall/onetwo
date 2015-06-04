package org.onetwo.common.jackson;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class JsonMapper {
	
	public static String DEFAULT_JSONP_NAME = "callback";

	public static final JsonMapper DEFAULT_MAPPER = defaultMapper();
	/*****
	 * 忽略Null值
	 */
	public static final JsonMapper IGNORE_NULL = ignoreNull();
	/*****
	 * 忽略空值
	 */
	public static final JsonMapper IGNORE_EMPTY = ignoreEmpty();
	
	public static JsonMapper defaultMapper(){
		JsonMapper jsonm = new JsonMapper(Include.ALWAYS);
		return jsonm;
	}
	
	public static JsonMapper ignoreNull(){
		JsonMapper jsonm = new JsonMapper(Include.NON_NULL);
		return jsonm;
	}
	
	public static JsonMapper ignoreEmpty(){
		JsonMapper jsonm = new JsonMapper(Include.NON_EMPTY);
		return jsonm;
	}
	
	public static JsonMapper mapper(Include include, boolean fieldVisibility){
		JsonMapper jsonm = new JsonMapper(include, fieldVisibility);
		return jsonm;
	}
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	

	public JsonMapper(Include include){
		this(include, false);
	}
	public JsonMapper(Include include, boolean fieldVisibility){
		objectMapper.setSerializationInclusion(include);
//		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		setDateFormat(DateUtil.Date_Time);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if(fieldVisibility)
			objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		this.objectMapper.setFilters(filterProvider);
	}
	
	/*public JsonMapper addMixInAnnotations(Class<?> target, Class<?> mixinSource){
		this.objectMapper.getSerializationConfig().addMixInAnnotations(target, mixinSource);
		this.objectMapper.getDeserializationConfig().addMixInAnnotations(target, mixinSource);
		return this;
	}*/
	
	public JsonMapper defaultFiler(BeanPropertyFilter bpf){
		this.filterProvider.setDefaultFilter(bpf);
		return this;
	}
	
	public JsonMapper filter(String id, String...properties){
		this.filterProvider.addFilter(id, SimpleBeanPropertyFilter.serializeAllExcept(properties));
		return this;
	}
	
	public JsonMapper setDateFormat(String format){
		if(StringUtils.isBlank(format))
			return this;
		objectMapper.setDateFormat(new SimpleDateFormat(format));
//		objectMapper.getSerializationConfig().withDateFormat(df);
//		objectMapper.getDeserializationConfig().withDateFormat(df);
		return this;
	}

	public String toJson(Object object){
		return toJson(object, true);
	}
	
	public String toJson(Object object, boolean throwIfError){
		String json = "";
		try {
			json = this.objectMapper.writeValueAsString(object);
		} catch (Exception e) {
//			e.printStackTrace();
			if(throwIfError)
				LangUtils.throwBaseException("parse to json error : " + object, e);
			else
				logger.warn("parse to json error : " + object);
		}
		return json;
	}
	
	public JsonNode readTree(String content){
		try {
			JsonNode rootNode = objectMapper.readTree(content);
			return rootNode;
		} catch (Exception e) {
			throw new BaseException("parse to json error : " + e.getMessage(), e);
		}
	}
	
	public JsonNode readTree(InputStream in){
		try {
			JsonNode rootNode = objectMapper.readTree(in);
			return rootNode;
		} catch (Exception e) {
			throw new BaseException("parse to json error : " + e.getMessage(), e);
		}
	}
	
	public String toJsonPadding(String function, Object object){
		return toJson(new JSONPObject(function, object));
	}
	
	public String toJsonPadding(Object object){
		return toJson(new JSONPObject(DEFAULT_JSONP_NAME, object));
	}
	
	public <T> T fromJson(String json, Class<T> objClass){
		if(StringUtils.isBlank(json))
			return null;
		Assert.notNull(objClass);
		T obj = null;
		try {
			obj = this.objectMapper.readValue(json, objClass);
		} catch (Exception e) {
			LangUtils.throwBaseException("parse json to object error : " + objClass + " => " + json, e);
		}
		return obj;
	}
	
	public <T> T fromJson(InputStream in, Class<T> objClass){
		if(in==null)
			return null;
		Assert.notNull(objClass);
		T obj = null;
		try {
			obj = this.objectMapper.readValue(in, objClass);
		} catch (Exception e) {
			LangUtils.throwBaseException("parse json to object error : " + objClass + " => " + e.getMessage(), e);
		}
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> fromJsonAsList(String json, Class<T[]> objClass){
		if(StringUtils.isBlank(json))
			return null;
		Assert.notNull(objClass);
		if(!objClass.isArray())
			LangUtils.throwBaseException("mapped class must be a array class");
		List<T> obj = null;
		try {
			T[] array = this.objectMapper.readValue(json, objClass);
			obj = (List<T>)LangUtils.asList(array);
		} catch (Exception e) {
			LangUtils.throwBaseException("parse json to object error : " + objClass + " => " + json, e);
		}
		return obj;
	}
	
	public <T> T[] fromJsonAsArray(String json, Class<T[]> objClass){
		if(StringUtils.isBlank(json))
			return null;
		Assert.notNull(objClass);
		if(!objClass.isArray())
			LangUtils.throwBaseException("mapped class must be a array class");
		try {
			T[] array = this.objectMapper.readValue(json, objClass);
			return array;
		} catch (Exception e) {
			throw new BaseException("parse json to object error : " + objClass + " => " + json, e);
		}
	}
	
	public <T> List<T> fromJsonAsList(String json){
		if(StringUtils.isBlank(json))
			return null;
		List<T> obj = null;
		try {
			obj = this.objectMapper.readValue(json, new TypeReference<List<T>>(){});
		} catch (Exception e) {
			LangUtils.throwBaseException("parse json to List error : " + json, e);
		}
		return obj;
	}
	
	public <T> T update(String jsonString, T object) {
		T obj = null;
		try {
			obj = objectMapper.readerForUpdating(object).readValue(jsonString);
		}catch (Exception e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		return obj;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}


}
