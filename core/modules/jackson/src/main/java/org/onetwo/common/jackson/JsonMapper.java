package org.onetwo.common.jackson;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.jackson.exception.JsonException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.JSONPObject;


/********
 * 使用JsonFormat注解时，注意时区问题
 * 推荐 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
 * http://wiki.fasterxml.com/JacksonHowToCustomSerializers
 * 
 * @author way
 *
 */
public class JsonMapper {
	
	final public static String TIME_ZONE_CHINESE = "GMT+8";
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
		JsonMapper jsonm = new JsonMapper(new ObjectMapper(), include, fieldVisibility);
		return jsonm;
	}
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private ObjectMapper objectMapper;
	private SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	private TypeFactory typeFactory;
	

	public JsonMapper(Include include){
		this(new ObjectMapper(), include);
	}
	public JsonMapper(ObjectMapper objectMapper, Include include){
		this(objectMapper, include, false);
	}
	@SuppressWarnings("deprecation")
	public JsonMapper(ObjectMapper objectMapper, Include include, boolean fieldVisibility){
		objectMapper.setSerializationInclusion(include);
//		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
//		setDateFormat(DateUtils.DATE_TIME);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(Feature.ALLOW_COMMENTS, true);
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
//		objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if(fieldVisibility)
			objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.setFilters(filterProvider);
		this.objectMapper = objectMapper;
		this.typeFactory = this.objectMapper.getTypeFactory();
	}
	
	public JsonMapper prettyPrint() {
		this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		return this;
	}
	
	public JsonMapper singleQuotes() {
		this.objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
		this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return this;
	}
	
	public JsonMapper unquotedFieldNames() {
		this.objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
		this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return this;
	}
	
	public JsonMapper disable(SerializationFeature features){
//		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		this.objectMapper.disable(features);
		return this;
	}
	public JsonMapper disable(DeserializationFeature features){
		this.objectMapper.disable(features);
		return this;
	}
	public JsonMapper enableTyping(){
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		return this;
	}
	
	/*public JsonMapper addMixInAnnotations(Class<?> target, Class<?> mixinSource){
		this.objectMapper.getSerializationConfig().addMixInAnnotations(target, mixinSource);
		this.objectMapper.getDeserializationConfig().addMixInAnnotations(target, mixinSource);
		return this;
	}*/
	
	public JsonMapper defaultFiler(PropertyFilter bpf){
//		this.filterProvider.setDefaultFilter(bpf);
		this.filterProvider.setDefaultFilter(bpf);
		return this;
	}
	
	public JsonMapper filter(String id, String...properties){
		this.filterProvider.addFilter(id, SimpleBeanPropertyFilter.serializeAllExcept(properties));
		return this;
	}
	
	public JsonMapper setDateFormat(String format){
		return setDateFormat(format, TIME_ZONE_CHINESE);
	}
	
	public JsonMapper setDateFormat(String format, String timezone){
		if(StringUtils.isBlank(format))
			return this;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(timezone));
		objectMapper.setDateFormat(sdf);
//		objectMapper.getSerializationConfig().withDateFormat(df);
//		objectMapper.getDeserializationConfig().withDateFormat(df);
		return this;
	}

	public String toJson(Object object){
		return toJson(object, true);
	}
	
	public String toJson(Object object, boolean throwIfError){
		if(object==null){
			return null;
		}
		String json = "";
		try {
			json = this.objectMapper.writeValueAsString(object);
		} catch (Exception e) {
//			e.printStackTrace();
			if(throwIfError)
				throw new JsonException("parse to json error : " + object, e);
			else
				logger.warn("parse to json error : " + object);
		}
		return json;
	}
	

	public byte[] toJsonBytes(Object object){
		return toJsonBytes(object, true);
	}
	
	public byte[] toJsonBytes(Object object, boolean throwIfError){
		byte[] json = null;
		try {
			json = this.objectMapper.writeValueAsBytes(object);
		} catch (Exception e) {
			if(throwIfError)
				throw new RuntimeException("parse to json error : " + object, e);
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
			throw new JsonException("parse to json error : " + e.getMessage(), e);
		}
	}
	
	public JsonNode readTree(InputStream in){
		try {
			JsonNode rootNode = objectMapper.readTree(in);
			return rootNode;
		} catch (Exception e) {
			throw new JsonException("parse to json error : " + e.getMessage(), e);
		}
	}
	
	public String toJsonPadding(String function, Object object){
		return toJson(new JSONPObject(function, object));
	}
	
	public String toJsonPadding(Object object){
		return toJson(new JSONPObject(DEFAULT_JSONP_NAME, object));
	}
	
	/*public static interface ReadValuePolicy {
		Object readValue(Class<?> objClass);
	}
	private Map<Class<?>, ReadValuePolicy> policies = new HashMap<Class<?>, ReadValuePolicy>(){
		{
			policies.put(key, value)
		}
	};*/
	/*****
	 * 
	 * @param json
	 * @param type
	 * @param params propertyName, propertyType
	 * @return
	 */
	public <T> T fromJsonWith(String json, Class<?> type, Class<?>...parameterTypes){
		if(StringUtils.isBlank(json))
			return null;
		Assert.notNull(type);
		try {
			if(LangUtils.isEmpty(parameterTypes)){
				return objectMapper.readValue(json, typeFactory.constructType(type));
			}else{
				return objectMapper.readValue(json, typeFactory.constructParametricType(type, parameterTypes));
			}
			/*Map<String, Class<?>> attrsMap = CUtils.asMap(params);
			attrsMap.forEach((k, v)->bindings..addBinding(k, typeFactory.constructType(v)));*/
		} catch (Exception e) {
			throw new JsonException("parse json to "+type+" error : " + json, e);
		}
	}

	public <T> T fromJson(final Object json, Type objType){
		return fromJson(json, objType, true);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T fromJson(final Object json, Type objType, boolean parseAsStringIfError){
		if(json==null)
			return null;
		Assert.notNull(objType);
		Object obj = null;
		try {
			if(json instanceof InputStream){
				obj = this.objectMapper.readValue((InputStream)json, (Class<?>)objType);
			}else if(json instanceof File){
				obj = this.objectMapper.readValue((File)json, (Class<?>)objType);
			}else if(json.getClass().isArray() && json.getClass().getComponentType()==byte.class){
				obj = this.objectMapper.readValue((byte[])json, (Class<?>)objType);
			}else{
				String jsonstr = json.toString();
				if(StringUtils.isBlank(jsonstr)){
					return null;
				}
				obj = this.objectMapper.readValue(jsonstr, constructJavaType(objType));
			}
		} catch (Exception e) {
			if (parseAsStringIfError) {
				String jsonstr = fromJson(json, String.class, false);
				throw new JsonException("parse json to ["+objType+"] error, json: " + jsonstr, e);
			} else {
				throw new JsonException("parse json to ["+objType+"] error, json: " + json, e);
			}
		}
		return (T)obj;
	}
	
	/****
	 * 多级泛型可通过TypeReference传入，比如：new TypeReference<List<User<Integer>>(){}
	 * @param json
	 * @param valueTypeRef
	 * @return
	 */
	public <T> T fromJson(final Object json, TypeReference<T> valueTypeRef){
		if(json==null)
			return null;
		Assert.notNull(valueTypeRef);
		T obj = null;
		try {
			if(json instanceof InputStream){
				obj = this.objectMapper.readValue((InputStream)json, valueTypeRef);
			}else if(json instanceof File){
				obj = this.objectMapper.readValue((File)json, valueTypeRef);
			}else if(json.getClass().isArray() && json.getClass().getComponentType()==byte.class){
				obj = this.objectMapper.readValue((byte[])json, valueTypeRef);
			}else{
				String jsonstr = json.toString();
				obj = this.objectMapper.readValue(jsonstr, valueTypeRef);
			}
		} catch (Exception e) {
			throw new JsonException("parse json to "+valueTypeRef+" error : " + json, e);
		}
		return obj;
	}
	
	public JavaType constructJavaType(Type objType){
		JavaType javaType = null;
		if(objType instanceof ParameterizedType){
			ParameterizedType ptype = (ParameterizedType) objType;
			Class<?> objClass = ReflectUtils.loadClass(ptype.getRawType().getTypeName());
			List<Class<?>> classes = Stream.of(ptype.getActualTypeArguments()).map(type->(Class<?>)type).collect(Collectors.toList());
			javaType = typeFactory.constructParametrizedType(objClass, objClass, classes.toArray(new Class[classes.size()]));
			
		}else{
			Class<?> objClass = (Class<?>) objType;
			if(objClass.isArray()){
				javaType = typeFactory.constructArrayType(objClass.getComponentType());
				
			}else {
				javaType = typeFactory.constructType(objType);
			}
		}
		return javaType;
	}
	
	public <T> T fromJson(InputStream in, Type objType){
		if(in==null)
			return null;
		Assert.notNull(objType);
		T obj = null;
		try {
			obj = this.objectMapper.readValue(in, constructJavaType(objType));
		} catch (Exception e) {
			throw new JsonException("parse json to object error : " + objType + " => " + e.getMessage(), e);
		}
		return obj;
	}
	
	/*public <T> List<T> fromJsonAsList(String json, Class<T> objClass){
		Assert.notNull(objClass);
		if(StringUtils.isBlank(json))
			return null;
		try {
			return this.objectMapper.readValue(json, typeFactory.constructParametricType(List.class, objClass));
		} catch (Exception e) {
			throw new JsonException("parse json to object error : " + objClass + " => " + json, e);
		}
	}*/
	
	/*public <T> T[] fromJsonAsArray(String json, Class<T[]> objClass){
		Assert.notNull(objClass);
		if(StringUtils.isBlank(json))
			return null;
		if(!objClass.isArray())
			throw new JsonException("mapped class must be a array class");
		try {
			return this.objectMapper.readValue(json, objClass);
		} catch (Exception e) {
			throw new JsonException("parse json to object error : " + objClass + " => " + json, e);
		}
	}*/
	
	public <T> T[] fromJsonAsArray(String json, Class<T> objClass){
		Assert.notNull(objClass);
		if(StringUtils.isBlank(json))
			return null;
		try {
			return this.objectMapper.readValue(json, typeFactory.constructArrayType(objClass));
		} catch (Exception e) {
			throw new JsonException("parse json to object error : " + objClass + " => " + json, e);
		}
	}
	
	@SuppressWarnings("unchecked")
    public <T> List<T> fromJsonAsList(String json){
		if(StringUtils.isBlank(json))
			return Collections.EMPTY_LIST;
		return fromJson(json, new TypeReference<List<T>>(){});
	}
	
	public <T> T update(String jsonString, T object) {
		T obj = null;
		try {
			obj = objectMapper.readerForUpdating(object).readValue(jsonString);
		}catch (Exception e) {
			throw new JsonException("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		return obj;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}


}
