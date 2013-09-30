package org.onetwo.common.jackson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.TypeReference;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

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
		JsonMapper jsonm = new JsonMapper(Inclusion.ALWAYS);
		return jsonm;
	}
	
	public static JsonMapper ignoreNull(){
		JsonMapper jsonm = new JsonMapper(Inclusion.NON_NULL);
		return jsonm;
	}
	
	public static JsonMapper ignoreEmpty(){
		JsonMapper jsonm = new JsonMapper(Inclusion.NON_EMPTY);
		return jsonm;
	}
	
	public static JsonMapper mapper(Inclusion include){
		JsonMapper jsonm = new JsonMapper(include);
		return jsonm;
	}
	
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public JsonMapper(Inclusion include){
		objectMapper.setSerializationInclusion(include);
//		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		setDateFormat(DateUtil.Date_Time);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@SuppressWarnings("deprecation")
	public JsonMapper setDateFormat(String format){
		if(StringUtils.isBlank(format))
			return this;
		DateFormat df = new SimpleDateFormat(format);
		objectMapper.getSerializationConfig().setDateFormat(df);
		objectMapper.getDeserializationConfig().setDateFormat(df);
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
			if(throwIfError)
				LangUtils.throwBaseException("parse to json error : " + object, e);
			else
				logger.warn("parse to json error : " + object);
		}
		return json;
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
