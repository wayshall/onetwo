package org.onetwo.common.utils.json;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.beanutils.ConvertUtils;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.DateConverter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.annotation.IgnoreJson;


@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class JSONUtils { 

	public static JsonConfig JSON_CONFIG = new JsonConfig();
	public static JsonConfig TO_BEAN_CONFIG = new JsonConfig();
	public static final JSONArray JSONArrayEmpty = new JSONArray();

	static{
		JSON_CONFIG.setExcludes(new String[]{"handler","hibernateLazyInitializer"});
		JSON_CONFIG.setAllowNonStringKeys(true);
//		JSON_CONFIG.setIgnoreJPATransient(true);
		JSON_CONFIG.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		JSON_CONFIG.addIgnoreFieldAnnotation(IgnoreJson.class);

		ConvertUtils.register(new DateConverter(), Date.class);
		ConvertUtils.register(new DateConverter(), Timestamp.class);
		ConvertUtils.register(new DateConverter(), String.class);
		
		Set formats = CUtils.asSet("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss", "yyyy-MM-dd HH:mm");
		net.sf.json.util.JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((String[])formats.toArray(new String[formats.size()])));
	}
	
	public static boolean isJsonString(String str){
		if(StringUtils.isBlank(str))
			return false;
		str = str.trim();
		return (str.startsWith("[") && str.endsWith("]")) || (str.startsWith("{") && str.endsWith("}"));
	}
	

	public static String getJsonString(Object datas, String...fieldNames){
		return getJsonString(datas, getSafeJsonConfig(), fieldNames);
	}
	
	public static JSONObject getJsonData(String jsonString){
		JSONObject jsonObj = null;
		if(StringUtils.isNotBlank(jsonString)){
			jsonString = jsonString.trim();
			if(!jsonString.startsWith("{"))
				jsonString = "{" + jsonString;
			if(!jsonString.endsWith("}"))
				jsonString = jsonString + "}";
			jsonObj = JSONObject.fromObject(jsonString, JSON_CONFIG);
		}
		return jsonObj;
	}

	public static JSONObject getJSONObject(String json){
		JSONObject result = getJsonData(json);
		return result;
	}

	public static JSONArray getJSONArray(String json){
		if(StringUtils.isBlank(json))
			return JSONArrayEmpty;
		JSONArray result = JSONArray.fromObject(json, JSON_CONFIG);
		return result;
	}
	
	public static Map getJsonMap(String jsonString){
		JSONObject jsonObj = getJsonData(jsonString);
		Map map = null;
		if(jsonObj!=null)
			map = (Map) JSONObject.toBean(jsonObj, HashMap.class);
		return map;
	}
	
	public static <T> T getObject(String jsonString, Class<T> clazz){
		JSONObject jsonObj = getJsonData(jsonString);
		T obj = null;
		if(jsonObj!=null)
			obj = (T) JSONObject.toBean(jsonObj, clazz);
		return obj;
	}
	
	public static String getJsonString(Object datas, boolean include, String...fieldNames){
		return getJSON(datas, include, fieldNames).toString();
	}

	public static JSON getJSON(Object datas, boolean include, String...fieldNames){
		if(include){
			return getJSONWith(datas, fieldNames);
		}else{
			return getJSONExclude(datas, fieldNames);
		}
	}
	
	public static String getJsonString(Object datas, JsonConfig jsonconfig){
		jsonconfig.setExcludes(new String[]{"handler","hibernateLazyInitializer"});
		jsonconfig.setAllowNonStringKeys(true);
		jsonconfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		String text = getJsonString(datas, jsonconfig, "");
		return text;
	}

	public static String getJsonString(Object datas, JsonConfig jsonconfig, String...fieldNames){
		return getJSONExclude(datas, jsonconfig, fieldNames).toString();
	}

	public static JSON getJSONExclude(Object datas, String...fieldNames){
		return getJSONExclude(datas, getSafeJsonConfig(), fieldNames);
	}
	
	public static JSON getJSONExclude(Object datas, JsonConfig jsonconfig, String...fieldNames){
		jsonconfig.setExcludes(fieldNames);
		JSON json;
		if(datas instanceof Collection)
			json = JSONArray.fromObject(datas, jsonconfig);
		else
			json = JSONObject.fromObject(datas, jsonconfig);
		return json;
	}
	

	public static String getJsonStringWith(Object datas, final String...fieldNames){
		return getJsonString(datas, true, fieldNames);
	}

	public static JSON getJSONWith(Object datas, final String...fieldNames){
		JsonConfig jsonconfig = getSafeJsonConfig();
		if(fieldNames!=null && fieldNames.length>0){
			jsonconfig.setJsonPropertyFilter(new PropertyFilter(){
	
				@Override
				public boolean apply( Object source, String name, Object value ){
					LangUtils.println("source:${0}, name:${1}, value:${2}", source, name, value);
					return !ArrayUtils.contains(fieldNames, name);
				}
				
			});
		}
		JSON json;
		if(datas instanceof Collection)
			json = JSONArray.fromObject(datas, jsonconfig);
		else
			json = JSONObject.fromObject(datas, jsonconfig);
		return json;
	}
	
	public static JsonConfig getSafeJsonConfig(){
		return JSON_CONFIG.copy();
	}
}
