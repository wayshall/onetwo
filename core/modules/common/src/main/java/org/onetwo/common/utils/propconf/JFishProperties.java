package org.onetwo.common.utils.propconf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.EnumUtils;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.SimpleBlock;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;

@SuppressWarnings({"unchecked", "rawtypes", "serial"})
public class JFishProperties extends Properties implements VariableSupporter {
	
	public static final JFishProperties wrap(Properties properties){
		return new JFishProperties(true, properties);
	}
	
	private static Comparator<String> NAME_LENGTH_COMPARATOR = new Comparator<String>() {

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	};

	private SimpleBlock<String, Map<String, String>> startWithBlock = new SimpleBlock<String, Map<String, String>>() {
		@Override
		public Map<String, String> execute(String keyStartWith) {
			Map<String, String> values = LangUtils.newHashMap();
			for(Enumeration<String> ekey = configNames(); ekey.hasMoreElements();){
				String key = ekey.nextElement();
				if(key.startsWith(keyStartWith)){
					String val = getVariable(key);
					key = key.substring(keyStartWith.length());
					values.put(key, val);
				}
			}
			return values;
		}
	};
	
	private SimpleBlock<String[], List<String>> splitBlock = new SimpleBlock<String[], List<String>>() {

		@Override
		public List<String> execute(String[] key) {
			List<String> listValue = LangUtils.newArrayList();
			String value = getVariable(key[0]);
			if(StringUtils.isBlank(value)){
				listValue = Collections.EMPTY_LIST;
			}else{
				listValue = new ArrayList<String>();
				String[] strs = StringUtils.split(value, key[1]);
				for(String str : strs){
					if(StringUtils.isBlank(str))
						continue;
					listValue.add(str.trim());
				}
			}
			return ImmutableList.copyOf(listValue);
		}
		
	};
	
	private SimpleBlock<String, List<Class>> classBlock = new SimpleBlock<String, List<Class>>() {

		@Override
		public List<Class> execute(String object) {
			List<Class> classes = new ArrayList<Class>();
			String strs = getVariable(object);
			for(String clsName : Splitter.on(',').omitEmptyStrings().trimResults().split(strs)){
				Class clazz = ReflectUtils.loadClass(clsName);
				if(!classes.contains(clazz))
					classes.add(clazz);
			}
			return classes;
		}
		
	};
	
	private static final Logger logger = LoggerFactory.getLogger(JFishProperties.class);
	
//	protected Properties config = new Properties();

	protected VariableExpositor expositor;
	
	protected Expression expression = Expression.AT;

//	private Map cache = new HashMap();
	private Cache<String, Object> cache = CacheBuilder.newBuilder().build();
	

	public JFishProperties(Properties... configs) {
		this(true, configs);
	}
	public JFishProperties(boolean cacheable, Properties... configs) {
//		Assert.notEmpty(configs);
		setConfigs(configs);
		this.expositor = new VariableExpositor(this, cacheable);
	}
	
	final public void setConfigs(Properties...configs){
		clear();
		for(Properties conf : configs){
//			this.config.putAll(conf);
			Enumeration<String> names = (Enumeration<String>)conf.propertyNames();
			String qname = null;
			while(names.hasMoreElements()){
				qname = names.nextElement();
				/*if(this.config.containsKey(qname)){
					throw new BaseException("query name ["+qname+"] has already exist!");
				}*/
				setProperty(qname, conf.getProperty(qname));
			}
		}
	}


	public boolean load(String srcpath){
		File file = null;
		boolean succeed = false;
		String filepath = srcpath;
		try {
			if (filepath.indexOf(".")==-1)
				filepath += PropUtils.CONFIG_POSTFIX;
			file = new File(filepath);
			if (!file.exists()) {
				if (filepath.indexOf(':') == -1) {
					filepath = FileUtils.getResourcePath(this.getClass().getClassLoader(), filepath);
					file = new File(filepath);
				}
			}
			if(file==null || !file.exists()){
				logger.info("1. file not exist : " + (file==null?"null":file.getPath()));
				InputStream fin = getInputStream(filepath);
				if(fin==null){
					throw new FileNotFoundException("no stream: " + filepath);
				}
				PropUtils.loadProperties(fin, this);
			}else{
				PropUtils.loadProperties(file, this);
			}
			succeed = true;
		} catch (Exception e) {
			InputStream in = null;
			try {
				if(logger.isInfoEnabled()){
					logger.error("load config error: " + e.getMessage());
					logger.info("try to load config by stream : " + filepath);
				}
				in = this.getClass().getClassLoader().getResourceAsStream(srcpath);
				load(in);
				succeed = true;
			} catch (Exception e1) {
				throw new BaseException("load config error: " + filepath, e);
			} finally{
				IOUtils.closeQuietly(in);
			}
		}finally{
			logger.info("load config finished : "+succeed);
		}
		return succeed;
	}
	
	protected InputStream getInputStream(String path) throws Exception{
		if(FileUtils.isJarURL(path)){
			URL url = new URL(path);
			URLConnection con = url.openConnection();
			try {
				return con.getInputStream();
			}
			catch (IOException ex) {
				if (con instanceof HttpURLConnection) {
					((HttpURLConnection) con).disconnect();
				}
				throw ex;
			}
		}else{
			return new FileInputStream(new File(path));
		}
	}

	public List<String> sortedKeys(){
		List<String> keys = new ArrayList<String>();
		Enumeration<String> keyNames = (Enumeration<String>)configNames();
		while(keyNames.hasMoreElements()){
			keys.add(keyNames.nextElement());
		}
		Collections.sort(keys, NAME_LENGTH_COMPARATOR);
		return keys;
	}
	
	public Object remove(Object key){
//		this.cache.remove(key);
		this.cache.invalidate(key);
		return super.remove(key);
	}
	
	protected void putInCache(String key, Object value){
		this.cache.put(key, value);
	}
	
	protected Object getFromCache(String key){
		return cache.getIfPresent(key);
	}

	public List<String> getStringList(String key, String split) {
		return getPropertyWithSplit(key, split);
	}

	
	public Map<String, String> getPropertiesStartWith(String keyStartWith) {
		return getFromCache(keyStartWith, startWithBlock, keyStartWith);
	}

	protected <K, T> T getFromCache(String key, final SimpleBlock<K, T> block, final K k) {
		try {
			return (T)this.cache.get(key, new Callable<T>() {

				@Override
				public T call() throws Exception {
					return block.execute(k);
				}
				
			});
		} catch (ExecutionException e) {
			throw new BaseException("get cache error.", e);
		}
		
		/*T cacheValue = (T)getFromCache(key);
		
		cacheValue = block.execute(k);
		
		putInCache(key, cacheValue);
		return cacheValue;*/
		
	}

	public List<String> getPropertyWithSplit(String key, String split) {
		return getFromCache(key, splitBlock, new String[]{key, split});
		/*List<String> listValue = (List<String>)getFromCache(key);
		if(listValue!=null)
			return listValue;
		
//		String value = getProperty(key);
		String value = getVariable(key);
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
		return listValue;*/
	}
	

	public List<? extends Enum<?>> getEnums(String key, Class<? extends Enum<?>> clazz){
		List<String> strs = getPropertyWithSplit(key, ",");
		List<? extends Enum<?>> enumObj = (List<? extends Enum<?>>)EnumUtils.asEnumList(clazz, strs.toArray(new String[0]));
		return enumObj;
	}

	public String getProperty(String key, String defaultValue) {
		String value = this.getProperty(key);
		return StringUtils.isBlank(value) ? defaultValue : value;
	}

	public String getPath(String key, String def){
		String path = getProperty(key, def);
		path = FileUtils.replaceBackSlashToSlash(path);
		return path;
	}

	public String getDir(String key, String def){
		String path = getPath(key, def);
		path = FileUtils.convertDir(path);
		return path;
	}
	/*public Enumeration<String> keys() {
		return (Enumeration<String>) config.propertyNames();
	}*/

	public String getVariable(String key) {
		return getVariable(key, false);
	}

	public String getVariable(String key, boolean checkCache) {
		String value = super.getProperty(key);
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

	/*public String getProperty(String key) {
		return super.getProperty(key);
	}*/

	public String getAndThrowIfEmpty(String key) {
		String val = getProperty(key);
		if(StringUtils.isBlank(val))
			LangUtils.throwBaseException("can find the value for key: " + key);
		return val;
	}

    public String getProperty(String key) {
    	return StringUtils.trimToEmpty(super.getProperty(key));
    }
	/*public String getProperty(String key, boolean checkCache) {
		return this.getVariable(key, checkCache);
	}*/

	public Integer getInteger(String key, Integer def) {
		if (!containsKey(key)) {
			return def;
		}
		Integer integer = null;
		try {
			integer = new Integer(getVariable(key));
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

	public Collection<Class> getClasses(final String key, Class... defClasses) {
		if(!this.containsKey(key))
			return Arrays.asList(defClasses);
		
		return this.getFromCache(key, classBlock, key);
		/*//cache
		List<Class> classes = (List<Class>) getFromCache(key);
		if(classes!=null)
			return classes;
		
		classes = new ArrayList<Class>();
		this.putInCache(key, classes);

		String strs = this.getVariable(key);
		if(StringUtils.isBlank(strs)){
			return MyUtils.asList(defClasses);
		}
		
		String[] classNames = StringUtils.split(strs, ",");
		Class clazz = null;
		for(String clsName : classNames){
			clazz = ReflectUtils.loadClass(clsName.trim());
			if(!classes.contains(clazz))
				classes.add(clazz);
		}
		for(String clsName : Splitter.on(',').omitEmptyStrings().trimResults().split(strs)){
			Class clazz = ReflectUtils.loadClass(clsName);
			if(!classes.contains(clazz))
				classes.add(clazz);
		}
		
//		this.putInCache(key, classes);
		
		return classes;*/
	}

	public List<Class> getClassList(String key) {
		if(!this.containsKey(key))
			return Collections.EMPTY_LIST;
		
		return this.getFromCache(key, classBlock, key);
	}

	public Long getLong(String key, Long def) {
		if (!containsKey(key)) {
			return def;
		}
		Long longValue = null;
		try {
			longValue = new Long(getVariable(key));
		} catch (Exception e) {
			longValue = def;
		}
		return longValue;
	}

	public Double getDouble(String key, Double def) {
		if (!containsKey(key)) {
			return def;
		}
		try {
			return new Double(getVariable(key));
		} catch (Exception e) {
			return def;
		}
	}
	

	public Boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public Boolean getBoolean(String key, boolean def) {
		if (!containsKey(key)) {
			return def;
		}
		Boolean booleanValue = false;
		try {
			booleanValue = Boolean.parseBoolean(getVariable(key));
		} catch (Exception e) {
			booleanValue = def;
		}
		return booleanValue;
	}

	public Date getDate(String key, Date def) {
		if (!containsKey(key)) {
			return def;
		}
		Date date = DateUtil.parse(getVariable(key));
		if(date==null)
			date = def;
		return date;
	}

	public synchronized void clear() {
		super.clear();
		if(expositor!=null)
			this.expositor.clear();
		if(cache!=null)
			this.cache.cleanUp();
	}
	
	public Enumeration configNames() {
		return keys();
	}
}
