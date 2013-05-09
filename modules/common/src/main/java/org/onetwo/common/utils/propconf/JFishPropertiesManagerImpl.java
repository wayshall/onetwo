package org.onetwo.common.utils.propconf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.watch.FileChangeListener;
import org.onetwo.common.utils.watch.FileMonitor;
import org.slf4j.Logger;


public class JFishPropertiesManagerImpl<T extends JFishNameValuePair> implements JFishPropertiesManager<T>{
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	public static class JFishPropertyConf {
		private String dir;
		private String overrideDir;
		private ClassLoader classLoader = FileUtils.getClassLoader();
		private Class<?> propertyBeanClass;
		private boolean watchSqlFile;
		private String postfix;
		
		public String getDir() {
			return dir;
		}
		public void setDir(String dir) {
			Assert.hasText(dir);
			this.dir = dir;
		}
		public String getOverrideDir() {
			return overrideDir;
		}
		public void setOverrideDir(String overrideDir) {
			Assert.hasText(overrideDir);
			this.overrideDir = overrideDir;
		}
		public ClassLoader getClassLoader() {
			return classLoader;
		}
		public void setClassLoader(ClassLoader classLoader) {
			if(classLoader==null){
				classLoader = FileUtils.getClassLoader();
			}
			this.classLoader = classLoader;
		}
		public Class<?> getPropertyBeanClass() {
			return propertyBeanClass;
		}
		public void setPropertyBeanClass(Class<?> propertyBeanClass) {
			Assert.notNull(propertyBeanClass);
			this.propertyBeanClass = propertyBeanClass;
		}
		public boolean isWatchSqlFile() {
			return watchSqlFile;
		}
		public void setWatchSqlFile(boolean watchSqlFile) {
			this.watchSqlFile = watchSqlFile;
		}
		public String getPostfix() {
			return postfix;
		}
		public void setPostfix(String postfix) {
			Assert.hasText(postfix);
			this.postfix = postfix;
		}
		
	}

	public static final String NOTE = "--";
	public static final String NAME_PREFIX = "@";
	public static final String EQUALS_MARK = "=";
	public static final String IGNORE_NULL_KEY = "ignore.null";
	public static final String JFISH_SQL_POSTFIX = ".jfish";
	
	private List<Properties> sqlfiles;
//	private PropertiesWraper wrapper;
	private Map<String, T> namedQueryCache;
	private FileMonitor fileMonitor;
	private long period = 1;

	private JFishPropertyConf conf;
	
	
	public JFishPropertiesManagerImpl(JFishPropertyConf conf) {
		super();
		this.conf = conf;
		if(conf.getPropertyBeanClass()==null){
			Class<T> clz = ReflectUtils.getSuperClassGenricType(this.getClass(), JFishPropertiesManagerImpl.class);
			conf.setPropertyBeanClass(clz);
		}
	}
	
	protected JFishPropertyConf getConf() {
		return conf;
	}

	public void build(){
		if(conf.watchSqlFile){
			fileMonitor = new FileMonitor();
		}
		if(this.sqlfiles==null){
			this.sqlfiles = autoScanSqlDir();
		}
		Assert.notEmpty(sqlfiles);
		PropertiesWraper wrapper = new PropertiesWraper(sqlfiles.toArray(new Properties[sqlfiles.size()]));
		this.namedQueryCache = new HashMap<String, T>(wrapper.size());
		this.buildNamedInfos(wrapper);
	}
	
	public void reloadFile(File file){
		Properties pf = loadSqlFile(file);
		if(pf==null){
			logger.warn("no file relaoded : " + file.getPath());
			return ;
		}
		PropertiesWraper wrapper = new PropertiesWraper(pf);
		buildNamedInfos(wrapper);
		logger.warn("file relaoded : " + file.getPath());
	}
	
	protected List<Properties> autoScanSqlDir(){
		String sqldirPath = FileUtils.getResourcePath(conf.classLoader, conf.dir);

		File[] sqlfileArray = FileUtils.listFiles(sqldirPath, conf.postfix);
		if(StringUtils.isNotBlank(conf.overrideDir)){
			File[] dbsqlfiles = FileUtils.listFiles(sqldirPath+"/"+conf.overrideDir, conf.postfix);
			if(!LangUtils.isEmpty(dbsqlfiles)){
				sqlfileArray = (File[]) ArrayUtils.addAll(sqlfileArray, dbsqlfiles);
			}
		}
		
		if(LangUtils.isEmpty(sqlfileArray))
			return null;
		
		List<Properties> sqlfiles = null;
		sqlfiles = new ArrayList<Properties>(sqlfileArray.length);
		Properties pf = null;
		for(File f : sqlfileArray){
			pf = loadSqlFile(f);
			if(pf==null){
				continue;
			}
			sqlfiles.add(pf);
			if(conf.watchSqlFile){
				this.fileMonitor.addFileChangeListener(new FileChangeListener() {
					
					@Override
					public void fileChanged(File file) {
						reloadFile(file);
					}
				}, f, period);
			}
		}
		return sqlfiles;
	}
	
	protected Properties loadSqlFile(File f){
		String fname = FileUtils.getFileNameWithoutExt(f.getName());
		if(!fname.endsWith(JFISH_SQL_POSTFIX)){
			logger.info("file["+f.getName()+" is not a jfish file, ignore it.");
			return null;
		}
		
		Properties pf = null;
		try {
			
			pf = new Properties();
			List<String> fdatas = FileUtils.readAsList(f);
			String key = null;
			StringBuilder value = null;
			String line = null;
			boolean matchName = false;
			for(int i=0; i<fdatas.size(); i++){
				line = fdatas.get(i).trim();
				if(line.startsWith(NOTE))
					continue;
				if(line.startsWith(NAME_PREFIX)){//@开始到=结束，作为key，其余部分作为value
					if(matchName && value!=null){
						pf.setProperty(key, value.toString());
						matchName = false;
					}
					int eqIndex = line.indexOf(EQUALS_MARK);
					if(eqIndex==-1)
						LangUtils.throwBaseException("the jfish sql file lack a equals mark : " + line);
					matchName = true;
					key = line.substring(NAME_PREFIX.length(), eqIndex).trim();
					value = new StringBuilder();
					value.append(line.substring(eqIndex+EQUALS_MARK.length()));
					value.append(" ");
				}else{
					if(!matchName)
						continue;
//						if(value==null)
//							LangUtils.throwBaseException("can not find the key for value : " + line);
					value.append(line);
					value.append(" ");
				}
			}
			if(StringUtils.isNotBlank(key) && value!=null){
				pf.setProperty(key, value.toString());
			}
			
			System.out.println("loaded jfish file : " + f.getPath());
		} catch (Exception e) {
			LangUtils.throwBaseException("load jfish file error : " + f, e);
		}
		return pf;
	}
	
	
	protected JFishPropertiesManager<T> buildNamedInfos(PropertiesWraper wrapper){
		List<String> keyNames = wrapper.sortedKeys();
		if(logger.isInfoEnabled()){
			logger.info("================>>> build file named query");
			for(String str : wrapper.sortedKeys()){
				logger.info(str);
			}
		}
		T propBean = null;
		boolean newBean = true;
		String preKey = null;
		String val = "";
		for(String key : keyNames){
			if(preKey!=null)
				newBean = !key.startsWith(preKey);
			if(newBean){
				if(propBean!=null){
					namedQueryCache.put(propBean.getName(), propBean);
				}
				propBean = (T)ReflectUtils.newInstance(conf.propertyBeanClass);
				val = wrapper.getAndThrowIfEmpty(key);
				if(key.endsWith(IGNORE_NULL_KEY)){
					throw new BaseException("the query name["+key+"] cant be end with: " + IGNORE_NULL_KEY);
				}
				propBean.setName(key);
				propBean.setValue(val);
				newBean = false;
				preKey = key+".";
			}else{
				val = wrapper.getProperty(key, "");
				String prop = key.substring(preKey.length());
				if(prop.indexOf('.')!=-1){
					prop = StringUtils.toJavaName(prop, '.', false);
				}
				try {
					ReflectUtils.setExpr(propBean, prop, val);
				} catch (Exception e) {
					logger.error("set value error : "+prop);
					LangUtils.throwBaseException(e);
				}
			}
		}
		if(propBean!=null){
			namedQueryCache.put(propBean.getName(), propBean);
		}
		if(logger.isInfoEnabled()){
			logger.info("================>>> builded file query:");
			for(JFishNameValuePair prop : namedQueryCache.values()){
				logger.info(prop.getName()+": \t"+LangUtils.toString(prop));
			}
		}
		
		return this;
	}
	

	public T getJFishProperty(String name) {
		T info = namedQueryCache.get(name);
		return info;
	}

	public void setSqlfiles(List<Properties> sqlfiles) {
		this.sqlfiles = sqlfiles;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder("named query : \n");
		for(T info : this.namedQueryCache.values()){
			sb.append(info).append(",\n");
		}
		return sb.toString();
	}

}
