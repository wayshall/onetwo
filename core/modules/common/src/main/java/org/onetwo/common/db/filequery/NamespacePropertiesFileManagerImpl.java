package org.onetwo.common.db.filequery;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.onetwo.common.utils.watch.FileChangeListener;
import org.onetwo.common.utils.watch.FileWatcher;
import org.slf4j.Logger;


public class NamespacePropertiesFileManagerImpl<T extends NamespaceProperty> /*extends AbstractPropertiesManager<T>*/ implements NamespacePropertiesManager<T>{

	

	public static final String GLOBAL_NS_KEY = "global";
	public static final String COMMENT = "--";
	public static final String MULTIP_COMMENT_START = "/*";
	public static final String MULTIP_COMMENT_END = "*/";
	public static final String CONFIG_PREFIX = "--@@";
	public static final String NAME_PREFIX = "--@";
	public static final String EQUALS_MARK = "=";
//	public static final String IGNORE_NULL_KEY = "ignore.null";

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private FileWatcher fileMonitor;
	protected JFishPropertyConf<T> conf;
	private long period = 1;
	
	Map<String, PropertiesNamespaceInfo<T>> namespaceProperties;
//	private List<Properties> sqlfiles;
//	private PropertiesWraper wrapper;
	private Map<String, T> namedQueryCache;
	private SqlFileParser<T> sqlFileParser = new DefaultSqlFileParser<>();
	final private NamespacePropertiesFileListener<T> listener;

	public NamespacePropertiesFileManagerImpl(JFishPropertyConf<T> conf, NamespacePropertiesFileListener<T> listener) {
//		super(conf);
		this.conf = conf;
		if(conf.getPropertyBeanClass()==null){
			Class<T> clz = ReflectUtils.getSuperClassGenricType(this.getClass(), NamespacePropertiesFileManagerImpl.class);
			conf.setPropertyBeanClass(clz);
		}
		this.listener = listener;
	}

	
	public NamespacePropertiesFileListener<T> getListener() {
		return listener;
	}

	protected JFishPropertyConf<T> getConf() {
		return conf;
	}

	public void build(){
		this.namedQueryCache = new ConcurrentHashMap<String, T>();
		ResourceAdapter<?>[] sqlfileArray = scanMatchSqlFiles(conf);
		this.namespaceProperties = this.parseSqlFiles(sqlfileArray);
		this.buildSqlFileMonitor(sqlfileArray);

		if(this.listener!=null){
			this.listener.afterBuild(sqlfileArray, namespaceProperties);
		}
		/*logger.info("all named query : ");
		for(T prop : this.namedQueryCache.values()){
			logger.info(prop.toString());
		}*/
	}
	
	/****
	 * 根据配置扫描文件
	 * @param conf
	 * @return
	 */
	protected ResourceAdapter<?>[] scanMatchSqlFiles(JFishPropertyConf<T> conf){
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
	
	/***
	 * 监测sql文件变化
	 * @param sqlfileArray
	 */
	protected void buildSqlFileMonitor(ResourceAdapter<?>[] sqlfileArray){
		if(conf.isWatchSqlFile() && fileMonitor==null){
			fileMonitor = FileWatcher.newWatcher(1);
			this.watchFiles(sqlfileArray);
		}
	}
	protected void watchFiles(ResourceAdapter<?>[] sqlResourceArray){
		if(LangUtils.isEmpty(sqlResourceArray))
			return ;
		this.fileMonitor.watchFile(period, new FileChangeListener() {
			
			@Override
			public void fileChanged(File file) {
				reloadFile(FileUtils.adapterResource(file));
			}
		}, sqlResourceArray);
	}
	
	public void reloadFile(ResourceAdapter<?> file){
		/*JFishProperties pf = loadSqlFile(file);
		if(pf==null){
			logger.warn("no file relaoded : " + file);
			return ;
		}*/
		PropertiesNamespaceInfo<T> namepsaceInfo = this.parseSqlFile(this.namespaceProperties, file, false);
		logger.warn("file relaoded : " + file);
		if(listener!=null){
			listener.afterReload(file, namepsaceInfo);
		}
	}
	
	private boolean isGlobalNamespace(String namespace){
		return GLOBAL_NS_KEY.equals(namespace) || !namespace.contains(".");
	}
	
	protected Map<String, PropertiesNamespaceInfo<T>> parseSqlFiles(ResourceAdapter<?>[] sqlfileArray){
		if(LangUtils.isEmpty(sqlfileArray)){
			logger.info("no named sql file found.");
			return Collections.EMPTY_MAP;
		}
		
		Map<String, PropertiesNamespaceInfo<T>> nsproperties = LangUtils.newHashMap(sqlfileArray.length);
		for(ResourceAdapter<?> f : sqlfileArray){
//			logger.info("parse named sql file: {}", f);
			this.parseSqlFile(nsproperties, f, true);
		}
		return nsproperties;
	}


	protected PropertiesNamespaceInfo<T> parseSqlFile(Map<String, PropertiesNamespaceInfo<T>> namespacesMap, ResourceAdapter<?> f, boolean throwIfExist){
		logger.info("parse named sql file: {}" + f.getName());
		
		String namespace = getFileNameNoJfishSqlPostfix(f);
		boolean globalNamespace = isGlobalNamespace(namespace);
		if(globalNamespace){
			namespace = GLOBAL_NS_KEY;
		}
		

		PropertiesNamespaceInfo<T> np = null;
		if(globalNamespace){
			np = namespacesMap.get(namespace);
			if(np==null){
				np = new GlobalNamespaceProperties<T>();
				namespacesMap.put(np.getKey(), np);
			}
//			np.addAll(namedinfos, throwIfExist);
		}else{
			if(throwIfExist && namespacesMap.containsKey(namespace)){
				throw new BaseException("sql namespace has already exist : " + namespace);
			}
			np = new CommonNamespaceProperties<T>(namespace, f);
			namespacesMap.put(np.getKey(), np);
		}
		
		buildNamedInfosToNamespaceFromResource(np, f);
		/*if(namedinfos.isEmpty())
			return null;*/

//		namespacesMap.put(namespace, np);
		
		for(T nsp : np.getNamedProperties()){
			this.namedQueryCache.put(nsp.getFullName(), nsp);
		}
		
		return np;
	}
	protected String getFileNameNoJfishSqlPostfix(ResourceAdapter<?> f){
		String fname = f.getName();
		if(!fname.endsWith(conf.getPostfix())){
			return fname;
		}else{
			return fname.substring(0, fname.length()-conf.getPostfix().length());
		}
	}

	/****
	 * 解释sql文件，构建为对象
	 * @param np
	 * @param file
	 */
	protected void buildNamedInfosToNamespaceFromResource(PropertiesNamespaceInfo<T> np, ResourceAdapter<?> file){
//		SqlFileParser<T> sqlFileParser = new DefaultSqlFileParser<>();a
		sqlFileParser.parseToNamespaceProperty(conf, np, file);
	}

	public T getJFishProperty(String fullname) {
		T info = namedQueryCache.get(fullname);
		return info;
	}
	
	public boolean contains(String fullname){
		return namedQueryCache.containsKey(fullname);
	}
	
	public boolean containsNamespace(String namespace){
		return namespaceProperties.containsKey(namespace);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder("named query : \n");
		for(T info : this.namedQueryCache.values()){
			sb.append(info).append(",\n");
		}
		return sb.toString();
	}

	@Override
	public PropertiesNamespaceInfo<T> getNamespaceProperties(String namespace) {
		return namespaceProperties.get(namespace);
	}

	public Collection<PropertiesNamespaceInfo<T>> getAllNamespaceProperties() {
		return namespaceProperties.values();
	}
	
	final public void setSqlFileParser(SqlFileParser<T> sqlFileParser) {
		this.sqlFileParser = sqlFileParser;
	}



	public static class CommonNamespaceProperties<E extends NamespaceProperty> implements PropertiesNamespaceInfo<E> {
		private final String namespace;
		private ResourceAdapter<?> source;
		private Map<String, E> namedProperties;
		
		public CommonNamespaceProperties(String namespace){
			this.namespace = namespace;
			this.namedProperties = LangUtils.newHashMap();
		}
		public CommonNamespaceProperties(String namespace, ResourceAdapter<?> source) {
			super();
			this.namespace = namespace;
			this.source = source;
			this.namedProperties = LangUtils.newHashMap();
//			this.namedProperties = namedProperties;
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		/****
		 * cache key
		 */
		@Override
		public String getKey() {
			return namespace;
		}

		@Override
		public Collection<E> getNamedProperties() {
			return namedProperties.values();
		}

		public ResourceAdapter<?> getSource() {
			return source;
		}
		@Override
		public E getNamedProperty(String name) {
			return namedProperties.get(name);
		}
		@Override
		public void addAll(Map<String, E> namedInfos, boolean throwIfExist) {
			for(Entry<String, E> entry : namedInfos.entrySet()){
				put(entry.getKey(), entry.getValue(), throwIfExist);
			}
		}
		@Override
		public void put(String name, E info, boolean throwIfExist) {
			Assert.hasText(name);
			Assert.notNull(info);
			if(throwIfExist && this.namedProperties.containsKey(name)){
				NamespaceProperty exitProp = this.namedProperties.get(name);
				throw new BaseException("int file["+info.getSrcfile()+"], sql key["+name+"] has already exist in namespace: " + namespace+", in file: "+ exitProp.getSrcfile());
			}
			this.namedProperties.put(name, info);
		}
		@Override
		public boolean isGlobal() {
			return false;
		}
		
	}

	public class GlobalNamespaceProperties<E extends NamespaceProperty> extends CommonNamespaceProperties<E> {
		private final List<File> sources;
		
		private GlobalNamespaceProperties() {
			super("");
			this.sources = LangUtils.newArrayList();
		}

		@Override
		public String getKey() {
			return GLOBAL_NS_KEY;
		}

		public List<File> getSources() {
			return sources;
		}
		@Override
		public boolean isGlobal() {
			return true;
		}
	}
	
	/***
	 * 配置信息
	 * @author way
	 *
	 * @param <T>
	 */
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
		public Class<T> getPropertyBeanClass() {
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
}
