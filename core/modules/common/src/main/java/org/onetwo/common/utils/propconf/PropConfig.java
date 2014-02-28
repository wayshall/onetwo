package org.onetwo.common.utils.propconf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Intro;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PropConfig implements VariableSupporter {
	
	public static final String CONFIG_KEY = "config";
//	private static String DEBUG_KEY = "debug";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected VariableConfig config = new VariablePropConifg(this);

	protected List<String> files = new ArrayList<String>(5);
	
	protected String configName;
	
	protected VariableExpositor expositor;
	
	protected Expression expression = Expression.AT;
	
	private Map cache = new HashMap();
	

	/*public PropConfig(){
		expositor = new VariableExpositor(this, true);
	}
	public PropConfig(boolean cacheable){
		expositor = new VariableExpositor(this, cacheable);
	}*/
	
	public PropConfig(VariableConfig config, boolean cacheable){
		this.config = config;
		this.expositor = new VariableExpositor(this, cacheable);
	}

	protected PropConfig(String configName) {
		this(configName, true);//实例化时首先直接加载第一个配置文件
	}

	protected PropConfig(String configName, boolean cacheable) {
		expositor = new VariableExpositor(this, cacheable);
		this.configName = configName;
		addConfigFile(configName, false);
	}

	protected PropConfig(String configName, VariableExpositor expositor) {
		this.configName = configName;
		addConfigFile(configName, false);
		this.expositor = expositor;
	}

	protected PropConfig(String name, File configFile, boolean cacheable) {
		expositor = new VariableExpositor(this, cacheable);
		this.configName = name;
		addConfigFile(configFile.getPath());
	}

	protected void initAppConfig(){
		this.loadFirstConfig();
		this.loadSilent();
	}

	public void initAppConfig(boolean loadSient){
		if(loadSient){
			this.initAppConfig();
		}else{
			this.loadFirstConfig();
			this.load();
		}
	}

	protected void loadFirstConfig(){
		this.config.load(files.get(0));
	}
	
	protected void addConfigFile(String fileName){
		addConfigFile(fileName, false);
	}
	
	protected void addConfigFile(String fileName, boolean load){
//		if(fileName!=null && fileName.indexOf(".")!=-1){
		if(StringUtils.isNotBlank(fileName)){
			this.files.add(fileName);
			if(load)
				this.config.load(fileName);
		}
	}

	/*public void saveToFile(String filepath) {
		try {
			FileOutputStream fout = new FileOutputStream(filepath);
			config.store(fout, null);
			System.out.println("file : " + filepath);
			fout.close();
		} catch (Exception e) {
			throw new BaseException("save config error!", e);
		}
	}*/

	public void loadSilent(){
		/*try {
			load();
		} catch (Exception e) {
			logger.error("loadSilent config error", e);
		}*/
		if(this.files==null || this.files.isEmpty())
			return ;
		for(String f : this.files){
			if(StringUtils.isBlank(f) || f.equals(configName))
				continue;
			try {
				this.config.load(f);
			} catch (Exception e) {
				logger.error("loadSilent config error, ignore config file : " + f);
			}
		}
	}
	
	protected void load(){
		if(this.files==null || this.files.isEmpty())
			return ;
		for(String f : this.files){
			if(f.equals(configName))
				continue;
			this.config.load(f);
		}
	}

	public void reload(){
		config.clear();
		cache.clear();
		this.initAppConfig(true);
	}
	
	public void remove(String key){
		this.config.remove(key);
		this.cache.remove(key);
	}
	
	protected void putInCache(String key, Object value){
		this.cache.put(key, value);
	}
	
	protected Object getFromCache(String key){
		return cache.get(key);
	}

	public String getProperty(String key, String defaultValue) {
		return getProperty(key, defaultValue, true);
	}

	public List<String> getPropertyWithSplit(String key, String split) {
		List<String> listValue = (List<String>)getFromCache(key);
		if(listValue!=null)
			return listValue;
		
		String value = getProperty(key);
		if(StringUtils.isBlank(value)){
			listValue = Collections.EMPTY_LIST;
		}else{
			listValue = new ArrayList<String>();
			String[] strs = StringUtils.split(value, split);
			for(String str : strs){
				if(StringUtils.isBlank(str))
					continue;
				listValue.add(str.trim());
			}
		}
		putInCache(key, listValue);
		return listValue;
	}

	public String getProperty(String key, String defaultValue, boolean checkCache) {
		String value = this.getVariable(key, checkCache);
		return StringUtils.isBlank(value) ? defaultValue : value;
	}

	/*public Enumeration<String> keys() {
		return (Enumeration<String>) config.propertyNames();
	}*/

	public String getVariable(String key) {
		return getVariable(key, false);
	}

	public String getVariable(String key, boolean checkCache) {
		String value = config.getOriginalProperty(key);
		//
		if (StringUtils.isNotBlank(value) && expositor!=null) {
			value = this.expositor.explainVariable(value, checkCache);
		}
		return StringUtils.trimToEmpty(value);
	}
	
	public String formatVariable(String key, Object...values){
		String str = getVariable(key);
		if(this.expression.isExpresstion(str))
			str = this.expression.parse(str, values);
		return str;
	}

	public String getProperty(String key) {
		return this.getProperty(key, true);
	}

	public String getAndThrowIfEmpty(String key) {
		String val = getProperty(key);
		if(StringUtils.isBlank(val))
			LangUtils.throwBaseException("can find the value for key: " + key);
		return val;
	}

	public String getProperty(String key, boolean checkCache) {
		return this.getVariable(key, checkCache);
	}

	public Integer getInteger(String key, Integer def) {
		String value = this.getProperty(key);
		if (StringUtils.isBlank(value)) {
			return def;
		}
		Integer integer = null;
		try {
			integer = new Integer(value);
		} catch (Exception e) {
			integer = def;
		}
		return integer;
	}
	
	public int getInt(String key){
		return this.getInteger(key);
	}
	
	public int getInt(String key, int def){
		return this.getInteger(key, def);
	}

	public Integer getInteger(String key) {
		return getInteger(key, Integer.valueOf(0));
	}

	public Class getClass(String key, Class cls) {
		Collection<Class> clses =  this.getClasses(key, cls);
		if(clses!=null){
			Iterator<Class> it = clses.iterator();
			if(it.hasNext())
				return it.next();
		}
		return null;
	}

	public Collection<Class> getClasses(String key) {
		return this.getClasses(key, (Class[])null);
	}

	public Collection<Class> getClasses(String key, Class... defClasses) {
		//cache
		List<Class> classes = (List<Class>) getFromCache(key);
		if(classes!=null)
			return classes;
		classes = new ArrayList<Class>();
		String strs = this.getVariable(key);
		
		try {
			if(StringUtils.isBlank(strs))
				classes = MyUtils.asList(defClasses);
			else{
				String[] classNames = StringUtils.split(strs, ",");
				Class clazz = null;
				for(String clsName : classNames){
					clazz = ReflectUtils.loadClass(clsName.trim());
					if(!classes.contains(clazz))
						classes.add(clazz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.putInCache(key, classes);
		
		return classes;
	}

	public List<Class> getClassList(String key) {
		//cache
		List<Class> classes = (List<Class>) getFromCache(key);
		if(classes!=null)
			return classes;
		
		String value = this.getVariable(key);
		
		if(StringUtils.isBlank(value))
			return null;
		String[] valueses = value.split(",");
		if(valueses==null || valueses.length<1)
			return null;
		
		classes = new ArrayList<Class>();
		for(String v : valueses){
			if(StringUtils.isBlank(v))
				continue;
			classes.add(ReflectUtils.loadClass(v.trim()));
		}
		
		this.putInCache(key, classes);
		
		return classes; 
	}

	public Long getLong(String key, Long def) {
		String value = this.getProperty(key);
		if (StringUtils.isBlank(value)) {
			return def;
		}
		Long longValue = null;
		try {
			longValue = new Long(value);
		} catch (Exception e) {
			longValue = def;
		}
		return longValue;
	}
	

	public Boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public Boolean getBoolean(String key, boolean def) {
		String value = this.getProperty(key);
		if (StringUtils.isBlank(value))
			return def;
		Boolean booleanValue = false;
		try {
			booleanValue = Boolean.parseBoolean(value);
		} catch (Exception e) {
			booleanValue = def;
		}
		return booleanValue;
	}

	public Date getDate(String key, Date def) {
		String value = this.getProperty(key);
		Date date = DateUtil.parse(value);
		if(date==null)
			date = def;
		return date;
	}

	public VariableConfig getConfig() {
		return config;
	}
	
	public void setProperty(String key, Object value){
		config.setOriginalProperty(key, value);
	}

	public void clear() {
		if(config!=null)
			this.config.clear();
		if(expositor!=null)
			this.expositor.clear();
		if(cache!=null)
			this.cache.clear();
	}
	
	public Enumeration configNames() {
		return config.configNames();
	}

	public String getConfigName() {
		return configName;
	}
	
	public boolean containsKey(Object key){
		return this.config.containsKey(key);
	}
	
	public <T> T asObject(Class<T> clazz){
		Intro<T> jc = Intro.wrap(clazz);
		T bean = jc.newFrom((Map<String, ?>)this.getConfig());
		return bean;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("configName:").append(configName).append(", config:").append(config.toString());
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		PropConfig p = new PropConfig("siteConfig-base.properties");
		p.loadSilent();
		System.out.println(p.getProperty("app.environment"));
	}
}
