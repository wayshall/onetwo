package org.onetwo.common.utils.propconf;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.watch.FileChangeListener;
import org.onetwo.common.utils.watch.FileWatcher;
import org.slf4j.Logger;

abstract public class AbstractPropertiesManager<T extends NamespaceProperty> implements JFishPropertiesManager<T> {
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	public static final String GLOBAL_NS_KEY = "global";
	public static class NamespaceProperty extends JFishNameValuePair {
		private String namespace;
		private PropertiesWraper config;
		private ResourceAdapter srcfile;
	
		public String getNamespace() {
			return namespace;
		}
	
		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}
		
		public String getFullName(){
			if(GLOBAL_NS_KEY.equals(namespace))
				return getName();
			return namespace+"."+getName();
		}
		
		public String toString(){
			return LangUtils.append("{ namespace:", namespace, ", name: ", getName(), "}");
		}

		public ResourceAdapter getSrcfile() {
			return srcfile;
		}

		public void setSrcfile(ResourceAdapter srcfile) {
			this.srcfile = srcfile;
		}

		public PropertiesWraper getConfig() {
			return config;
		}

		public void setConfig(PropertiesWraper config) {
			this.config = config;
		}
		
		
	}
	public static class JFishPropertyConf<T> {
		private String dir;
		private String overrideDir;
		private ClassLoader classLoader = FileUtils.getClassLoader();
		private Class<T> propertyBeanClass;
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
			this.overrideDir = overrideDir.toLowerCase();
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
		public void setPropertyBeanClass(Class<T> propertyBeanClass) {
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
	public static final String CONFIG_PREFIX = "@@";
	public static final String NAME_PREFIX = "@";
	public static final String EQUALS_MARK = "=";
//	public static final String IGNORE_NULL_KEY = "ignore.null";
	public static final String JFISH_SQL_POSTFIX = ".jfish.sql";

	private FileWatcher fileMonitor;
	protected JFishPropertyConf<T> conf;
	private long period = 1;
	private boolean debug;// = true;
	
	public AbstractPropertiesManager(JFishPropertyConf<T> conf){
		this.conf = conf;
	}
	
	protected void buildSqlFileMonitor(ResourceAdapter[] sqlfileArray){
		if(conf.isWatchSqlFile() && fileMonitor==null){
			fileMonitor = FileWatcher.newWatcher(1);
			this.watchFiles(sqlfileArray);
		}
	}
	protected ResourceAdapter[] scanMatchSqlFiles(JFishPropertyConf<T> conf){
		String sqldirPath = FileUtils.getResourcePath(conf.getClassLoader(), conf.getDir());

		File[] sqlfileArray = FileUtils.listFiles(sqldirPath, conf.getPostfix());
		if(logger.isInfoEnabled())
			logger.info("find {} sql named file in dir {}", sqlfileArray.length, sqldirPath);
		
		if(StringUtils.isNotBlank(conf.getOverrideDir())){
			String overridePath = sqldirPath+"/"+conf.getOverrideDir();
			logger.info("scan database dialect dir : " + overridePath);
			File[] dbsqlfiles = FileUtils.listFiles(overridePath, conf.getPostfix());

			if(logger.isInfoEnabled())
				logger.info("find {} sql named file in dir {}", dbsqlfiles.length, overridePath);
			
			if(!LangUtils.isEmpty(dbsqlfiles)){
				sqlfileArray = (File[]) ArrayUtils.addAll(sqlfileArray, dbsqlfiles);
			}
		}
		return FileUtils.adapterResources(sqlfileArray);
	}
	
	protected String getFileNameNoJfishSqlPostfix(ResourceAdapter f){
		String fname = f.getName();
		if(!fname.endsWith(JFISH_SQL_POSTFIX)){
			return fname;
		}else{
			return fname.substring(0, fname.length()-JFISH_SQL_POSTFIX.length());
		}
	}
	
	protected List<String> readResourceAsList(ResourceAdapter f){
		if(f.isSupportedToFile())
			return FileUtils.readAsList(f.getFile());
		else
			throw new UnsupportedOperationException();
	}
	
	protected JFishProperties loadSqlFile(ResourceAdapter f){
//		String fname = FileUtils.getFileNameWithoutExt(f.getName());
		if(!f.getName().endsWith(JFISH_SQL_POSTFIX)){
			logger.info("file["+f.getName()+" is not a jfish file, ignore it.");
			return null;
		}
		
		JFishProperties jp = new JFishProperties();
		Properties pf = new Properties();
		Properties config = new Properties();
		try {
			List<String> fdatas = readResourceAsList(f);
			String key = null;
			StringBuilder value = null;
			String line = null;

			boolean matchConfig = false;
			boolean matchName = false;
			for(int i=0; i<fdatas.size(); i++){
				line = fdatas.get(i).trim();
				if(line.startsWith(NOTE))
					continue;
				if(line.startsWith(NAME_PREFIX)){//@开始到=结束，作为key，其余部分作为value
					if(value!=null){
						if(matchConfig){
							config.setProperty(key, value.toString());
						}else{
							pf.setProperty(key, value.toString());
						}
						matchName = false;
						matchConfig = false;
					}
					int eqIndex = line.indexOf(EQUALS_MARK);
					if(eqIndex==-1)
						LangUtils.throwBaseException("the jfish sql file lack a equals mark : " + line);
					
					if(line.startsWith(CONFIG_PREFIX)){
						matchConfig = true;
						key = line.substring(CONFIG_PREFIX.length(), eqIndex).trim();
					}else{
						matchName = true;
						key = line.substring(NAME_PREFIX.length(), eqIndex).trim();
					}
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

			jp.setProperties(new PropertiesWraper(pf));
			jp.setConfig(new PropertiesWraper(config));
			System.out.println("loaded jfish file : " + f);
		} catch (Exception e) {
			LangUtils.throwBaseException("load jfish file error : " + f, e);
		}
		return jp;
	}
	
	protected void extBuildNamedInfoBean(T propBean){
	}
	
	/***********
	 * build a map: 
	 * key is name of beanClassOfProperty
	 * value is instance of beanClassOfProperty
	 * @param f
	 * @param namespace
	 * @param wrapper
	 * @param beanClassOfProperty
	 * @return
	 */
	protected Map<String, T> buildPropertiesAsNamedInfos(ResourceAdapter f, final String namespace, JFishProperties jp, Class<T> beanClassOfProperty){
		PropertiesWraper wrapper = jp.getProperties();
		List<String> keyNames = wrapper.sortedKeys();
		if(isDebug()){
			logger.info("================>>> buildPropertiesAsNamedInfos");
			for(String str : wrapper.sortedKeys()){
				logger.info(str);
			}
		}
		T propBean = null;
		boolean newBean = true;
		String preKey = null;
//		String val = "";
		Map<String, T> namedProperties = LangUtils.newHashMap();
		for(String key : keyNames){
			if(preKey!=null)
				newBean = !key.startsWith(preKey);
			if(newBean){
				if(propBean!=null){
					extBuildNamedInfoBean(propBean);
					namedProperties.put(propBean.getName(), propBean);
				}
				propBean = ReflectUtils.newInstance(beanClassOfProperty);
				String val = wrapper.getAndThrowIfEmpty(key);
				/*if(key.endsWith(IGNORE_NULL_KEY)){
					throw new BaseException("the query name["+key+"] cant be end with: " + IGNORE_NULL_KEY);
				}*/
				propBean.setName(key);
				propBean.setValue(val);
				propBean.setNamespace(namespace);
				newBean = false;
				preKey = key+".";
				propBean.setSrcfile(f);
				propBean.setConfig(jp.getConfig());
			}else{
				String val = wrapper.getProperty(key, "");
				String prop = key.substring(preKey.length());
				if(prop.indexOf('.')!=-1){
					prop = StringUtils.toJavaName(prop, '.', false);
				}
				setNamedInfoProperty(propBean, prop, val);
			}
		}
		if(propBean!=null){
			namedProperties.put(propBean.getName(), propBean);
		}
		if(logger.isInfoEnabled()){
			logger.info("================ {} named query start ================", namespace);
			for(JFishNameValuePair prop : namedProperties.values()){
				logger.info(prop.getName()+": \t"+prop);
			}
			logger.info("================ {} named query end ================", namespace);
		}
		
		return namedProperties;
	}
	
	protected void setNamedInfoProperty(Object bean, String prop, Object val){
		try {
			ReflectUtils.setExpr(bean, prop, val);
		} catch (Exception e) {
			logger.error("set value error : "+prop);
			LangUtils.throwBaseException(e);
		}
	}

	protected void watchFiles(ResourceAdapter[] sqlResourceArray){
		if(LangUtils.isEmpty(sqlResourceArray))
			return ;
		this.fileMonitor.watchFile(period, new FileChangeListener() {
			
			@Override
			public void fileChanged(File file) {
				reloadFile(FileUtils.adapterResource(file));
			}
		}, sqlResourceArray);
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	abstract protected void reloadFile(ResourceAdapter file);
}
