package org.onetwo.common.utils.propconf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PropertiesWraper implements VariableSupporter {
	
	public static final PropertiesWraper wrap(Properties properties){
		return new PropertiesWraper(properties);
	}
	
	private static Comparator<String> NAME_LENGTH_COMPARATOR = new Comparator<String>() {

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	};
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesWraper.class);
	
	protected Properties config = new Properties();

	protected VariableExpositor expositor;
	
	protected Expression expression = Expression.AT;

	private Map cache = new HashMap();
	
	

	public PropertiesWraper(Properties config) {
		super();
		this.config = config;
	}
	
	public PropertiesWraper(Properties... configs) {
		Assert.notEmpty(configs);
		for(Properties conf : configs){
//			this.config.putAll(conf);
			Enumeration<String> names = (Enumeration<String>)conf.propertyNames();
			String qname = null;
			while(names.hasMoreElements()){
				qname = names.nextElement();
				/*if(this.config.containsKey(qname)){
					throw new BaseException("query name ["+qname+"] has already exist!");
				}*/
				this.config.setProperty(qname, conf.getProperty(qname));
			}
		}
	}

	protected void putInCache(String key, Object value){
		this.cache.put(key, value);
	}
	
	protected Object getFromCache(String key){
		return cache.get(key);
	}

	public String getProperty(String key, String defaultValue) {
		String val = getVariable(key);
		if(StringUtils.isBlank(val))
			val = defaultValue;
		return val;
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
	
	public String getVariable(String key) {
		return getVariable(key, true);
	}

	public String getVariable(String key, boolean checkCache) {
		String value = config.getProperty(key);
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
		return this.getVariable(key, true);
	}

	public String getAndThrowIfEmpty(String key) {
		String val = getProperty(key);
		if(StringUtils.isBlank(val))
			LangUtils.throwBaseException("the value can not be empty: " + key);
		return val;
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
		return getInteger(key, new Integer(0));
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

	public int size() {
		return cache.size();
	}

	public Properties getConfig() {
		return config;
	}
	
	
	
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	public List<String> sortedKeys(){
		List<String> keys = new ArrayList<String>();
		Enumeration<String> keyNames = (Enumeration<String>)this.config.propertyNames();
		while(keyNames.hasMoreElements()){
			keys.add(keyNames.nextElement());
		}
		Collections.sort(keys, NAME_LENGTH_COMPARATOR);
		return keys;
	}

	public String toString() {
		return config.toString();
	}


}
