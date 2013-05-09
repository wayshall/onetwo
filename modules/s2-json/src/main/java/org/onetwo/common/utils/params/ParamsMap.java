package org.onetwo.common.utils.params;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.MyUtils;


@SuppressWarnings({"unchecked", "serial"})
public class ParamsMap extends LinkedHashMap {

	public static final String QUERY = "query";
	public static final String PARAMETERS_KEY_POSTFIX = "_PARAMETERS";
	
	protected boolean propertyParams;
	protected String name = "";
	protected Map<String, ParamsMap> typeParameters;

	public ParamsMap(){
		this(true, "");
	}
	
	public ParamsMap(boolean prefixParams, String name){
		this.propertyParams = prefixParams;
		this.name = name;
		if(this.propertyParams){
			typeParameters = new HashMap<String, ParamsMap>();
		}
	}
	
	public Object get(Object key){
		if(!propertyParams){
			return super.get(key);
		}
		return get(key, null);
	}
	
	protected Object getOriginal(Object key){
		return super.get(key);
	}
	
	protected Object putOriginal(Object key, Object value){
		return super.put(key, value);
	}
	
	public Object get(Object key, Object def){
		Object val = getOriginal(key);
		if(val==null)
			return def;
		return val;
	}
	
	@Override
	public Object put(Object key, Object value) {
		Object actualValue = null;
		if(value!=null && value.getClass().isArray() && Array.getLength(value)==1){
			actualValue = (String)Array.get(value, 0);
		}else{
			actualValue = value;
		}
			
		if(!propertyParams){
			return super.put(key, actualValue);
		}
		
		Object oval = null;
		Parameter parameter = parseParameter(key.toString(), actualValue);
		if(parameter!=null){
			putParameterValue(parameter);
			key = parameter.getName();
		}
		oval = super.put(key, actualValue);
		return oval;
	}
	
	public Parameter parseParameter(String name, Object value){
		return Parameter.parse(name, value);
	}
	
	
	@Override
	public void putAll(Map m) {
		for(Map.Entry entry : (Set<Map.Entry>)m.entrySet()){
			put(entry.getKey(), entry.getValue());
		}
	}

	protected void putParameterValue(Parameter parameter){
		for(ParameterProperty prop : parameter.getProperties()){
			getTypeParameters(prop.getName()).put(parameter.getName(), parameter);
			if(prop.isKeep())
				getTypeParameters(Parameter.KEEP).put(parameter.getName(), parameter);
		}
	}

	public String getString(Object key){
		return getString(key, null);
	}
	
	public String getString(Object key, String def){
		Object val = get(key, def);
		return convert(val, String.class, def);
	}
	
	
	public Long getLong(String key){
		return this.getLong(key, null);
	}
	
	public Long getLong(String key, Long def){
		Object val = get(key);
		return convert(val, Long.class, def);
	}
	
	public Double getDouble(String key, Double def){
		Object val = get(key);
		return convert(val, Double.class, def);
	}
	
	public Float getFloat(String key, Float def){
		Object val = get(key);
		return convert(val, Float.class, def);
	}
	
	protected <T> T convert(Object val, Class<T> toType, T def){
		return MyUtils.simpleConvert(val, toType, def);
	}
	
	public Integer getInteger(String key){
		return getInteger(key, null);
	}
	
	public Integer getInteger(String key, Integer def){
		Object val = get(key);
		return convert(val, Integer.class, def);
	}
	
	public List getList(String key){
		List list = null;
		Object val = get(key, null);
		if(val!=null)
			list = MyUtils.asList(val);
		return list;
	}
	
	
	public ParamsMap getQueryParameters(){
		return getTypeParameters(QUERY.toUpperCase());
	}
	
	public ParamsMap getKeepParameters(){
		return getTypeParameters(Parameter.KEEP.toUpperCase());
	}
	
	public String getKeepParameterString(){
		ParamsMap params = getKeepParameters();
		if(params.isEmpty())
			return "";
			
		StringBuilder sb = new StringBuilder();
		int index = 0;
		String keepStr = null;
		for(Map.Entry<String, Parameter> entry : (Set<Map.Entry<String, Parameter>>)params.entrySet()){
			keepStr = entry.getValue().getKeepParameterString();
			if(StringUtils.isBlank(keepStr))
				continue;
			if(index!=0)
				sb.append("&");
			sb.append(keepStr);
			index++;
		}
		return sb.toString();
	}
	
	protected ParamsMap getTypeParameters(String key){
		String attributekey = key.toUpperCase()+PARAMETERS_KEY_POSTFIX;
		ParamsMap params = typeParameters.get(attributekey);
		if(params==null){
			params = createParamsMap(key);
			typeParameters.put(attributekey, params);
		}
		return params;
	}
	
	protected ParamsMap createParamsMap(String key){
		return new ParamsMap(false, key);
	}
	
	public String getTypeParameterString(String type){
		if(isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		int index = 0;
		String typeStr = null;
		for(Map.Entry<String, Parameter> entry : (Set<Map.Entry<String, Parameter>>)entrySet()){
			typeStr = entry.getValue().getTypeName(type);
			if(StringUtils.isBlank(typeStr))
				continue;
			if(index!=0)
				sb.append("&");
			sb.append(typeStr);
			index++;
		}
		return sb.toString();
	}
	
	public String toParameterString(){
		String str = "";
		if(!propertyParams){
			str = getTypeParameterString(null);
		}else{
			str = super.toString();
		}
		return str;
	}
	
	public static void main(String[] args){
		ParamsMap params = new ParamsMap();
		String name = "testName[keep-query:create:keep-save]";
		params.put(name, "testName");
		params.put("testValue[query:keep-create]", "testValue2");
		System.out.println(params.get("testName"));
		System.out.println("q:"+params.getQueryParameters().toParameterString());
		System.out.println("keep:"+params.getKeepParameterString());
		System.out.println("conver-keep:"+params.getQueryParameters().getTypeParameterString("keep-query"));
		System.out.println(params);
	}
}
