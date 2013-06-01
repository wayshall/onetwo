package org.onetwo.common.utils.params;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.PropUtils;

@SuppressWarnings({"serial"})
public class TagParamsMap extends ParamsMap {

	public static final String MAPPER_FILE = "var-mapper.properties";
	
	public static final String FIRST_KEY = "firstResult";
	public static final String MAX_RESULTS_KEY = "maxResults";
	public static final String DATASOURCE_KEY = "dataSource";
	public static final String TEMPLATE_KEY = "template";

	public static final String ORDERASC_KEY = "order asc";
	public static final String ORDERDESC_KEY = "order desc";
	
	private static PropConfig VAR_MAPPER;
	
	public static void loadVarKey(){
		VAR_MAPPER = PropUtils.loadPropConfig(MAPPER_FILE);
	}
	
	public static void reloadVarKey(){
		VAR_MAPPER.reload();
	}

	public TagParamsMap(){
		super(false, "");
	}
	
	protected boolean isVarMapperLoaded(){
		return VAR_MAPPER!=null;
	}
	
	public Object get(Object key, boolean constantValue, Object def){
		Object val = super.getOriginal(key);
		if(val==null)
			return def;
		if(constantValue)
			val = getConstantValue((String)val, def);
		return val;
	}
	
	public Object get(Object key, Object def){
		return get(key, false, def);
	}
	
	public Object get(Object key, boolean constantValue){
		return get(key, constantValue, null);
	}
	
	@Override
	public Object put(Object key, Object value) {
		Object oval = super.put(key, value);
		
		if(!isVarMapperLoaded())
			return oval;
		
		if(VAR_MAPPER.containsKey(key)){
			Object newKey = VAR_MAPPER.getProperty(key.toString());
			oval = super.put(newKey, value);
		}
		return oval;
	}

	
	public Object getConstantValue(String key, Object def){
		if(key==null || !isVarMapperLoaded() || !VAR_MAPPER.containsKey(key))
			return def;
		Object val = VAR_MAPPER.getProperty(key);
		if(val == null || StringUtils.isBlank(val.toString()))
			val = def;
		return val;
	}
	
	public Object getConstantValue(String key){
		return getConstantValue(key, null);
	}
	
	public Integer getFirstResult(){
		return getFirstResult(null);
	}
	
	public Integer getFirstResult(Integer def){
		Integer firstResult = (Integer)get(FIRST_KEY, def);
		return firstResult;
	}
	
	public void setFirstResult(Integer firstResult){
		put(FIRST_KEY, firstResult);
	}
	
	public void setMaxResults(Integer maxResults){
		put(MAX_RESULTS_KEY, maxResults);
	}
	
	public Integer getMaxResults(){
		return getMaxResults(null);
	}
	
	public Integer getMaxResults(Integer def){
		Integer maxResults = (Integer)get(MAX_RESULTS_KEY, def);
		return maxResults;
	}
	
	public String getDataSource(){
		return (String)get(DATASOURCE_KEY);
	}
	
	public void setDataSource(String datasource){
		put(DATASOURCE_KEY, datasource);
	}
	
	public String getTemplate(){
		return (String)get(TEMPLATE_KEY);
	}
	
	public void setTemplate(String template){
		put(TEMPLATE_KEY, template);
	}
	public void setOrderDesc(String orderDesc) {
		put(ORDERDESC_KEY, orderDesc);
	}
	
	public String getOrderDesc() {
		return (String)get(ORDERDESC_KEY, true);
	}

	public void setOrderAsc(String orderAsc) {
		put(ORDERASC_KEY, orderAsc);
	}

	public String getOrderAsc() {
		return (String)get(ORDERASC_KEY, true);
	}
	
	public boolean getBoolean(String key, boolean constantValue) {
		return this.convert(get(key, constantValue), Boolean.class, false);
	}
	
	public static void main(String[] args){
		TagParamsMap param = new TagParamsMap();
		param.put("aa", "aavalue");
		
		System.err.println(param.get("aa"));
	}
}
