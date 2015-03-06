package org.onetwo.common.utils.propconf;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;


public class PropertiesNamespaceInfoManagerImpl<T extends NamespaceProperty> extends AbstractPropertiesManager<T> implements NamespacePropertiesManager<T>{

	public class CommonNamespaceProperties implements PropertiesNamespaceInfo<T> {
		private final String namespace;
		private ResourceAdapter source;
		private Map<String, T> namedProperties;
		
		public CommonNamespaceProperties(String namespace){
			this.namespace = namespace;
			this.namedProperties = LangUtils.newHashMap();
		}
		public CommonNamespaceProperties(String namespace, ResourceAdapter source) {
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
		public Collection<T> getNamedProperties() {
			return namedProperties.values();
		}

		public ResourceAdapter getSource() {
			return source;
		}
		@Override
		public T getNamedProperty(String name) {
			return namedProperties.get(name);
		}
		@Override
		public void addAll(Map<String, T> namedInfos, boolean throwIfExist) {
			for(Entry<String, T> entry : namedInfos.entrySet()){
				put(entry.getKey(), entry.getValue(), throwIfExist);
			}
		}
		@Override
		public void put(String name, T info, boolean throwIfExist) {
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

	public class GlobalNamespaceProperties extends CommonNamespaceProperties {
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

	
	Map<String, PropertiesNamespaceInfo<T>> namespaceProperties;
	
//	private List<Properties> sqlfiles;
//	private PropertiesWraper wrapper;
	private Map<String, T> namedQueryCache;
	
	final private PropertiesNamespaceInfoListener<T> listener;

	public PropertiesNamespaceInfoManagerImpl(JFishPropertyConf<T> conf, PropertiesNamespaceInfoListener<T> listener) {
		super(conf);
		if(conf.getPropertyBeanClass()==null){
			Class<T> clz = ReflectUtils.getSuperClassGenricType(this.getClass(), PropertiesNamespaceInfoManagerImpl.class);
			conf.setPropertyBeanClass(clz);
		}
		this.listener = listener;
	}
	
	protected JFishPropertyConf<T> getConf() {
		return conf;
	}

	public void build(){
		this.namedQueryCache = new ConcurrentHashMap<String, T>();
		ResourceAdapter[] sqlfileArray = scanMatchSqlFiles(conf);
		this.namespaceProperties = this.autoScanSqlDir(sqlfileArray);
		this.buildSqlFileMonitor(sqlfileArray);

		if(this.listener!=null){
			this.listener.afterBuild(sqlfileArray, namespaceProperties);
		}
		/*logger.info("all named query : ");
		for(T prop : this.namedQueryCache.values()){
			logger.info(prop.toString());
		}*/
	}
	
	public void reloadFile(ResourceAdapter file){
		/*JFishProperties pf = loadSqlFile(file);
		if(pf==null){
			logger.warn("no file relaoded : " + file);
			return ;
		}*/
		PropertiesNamespaceInfo<T> namepsaceInfo = this.scanAndParseSqlFile(this.namespaceProperties, file, false);
		logger.warn("file relaoded : " + file);
		if(listener!=null){
			listener.afterReload(file, namepsaceInfo);
		}
	}
	
	private boolean isGlobalNamespace(String namespace){
		return GLOBAL_NS_KEY.equals(namespace) || !namespace.contains(".");
	}
	
	protected Map<String, PropertiesNamespaceInfo<T>> autoScanSqlDir(ResourceAdapter[] sqlfileArray){
		if(LangUtils.isEmpty(sqlfileArray)){
			logger.info("no named sql file found.");
			return Collections.EMPTY_MAP;
		}
		
		Map<String, PropertiesNamespaceInfo<T>> nsproperties = LangUtils.newHashMap(sqlfileArray.length);
		for(ResourceAdapter f : sqlfileArray){
			logger.info("parse named sql file: {}", f);
			this.scanAndParseSqlFile(nsproperties, f, true);
		}
		return nsproperties;
	}


	protected PropertiesNamespaceInfo<T> scanAndParseSqlFile(Map<String, PropertiesNamespaceInfo<T>> namespacesMap, ResourceAdapter f, boolean throwIfExist){
		logger.info("scan and parse sql file : " + f.getName());
		
		String namespace = getFileNameNoJfishSqlPostfix(f);
		boolean globalNamespace = isGlobalNamespace(namespace);
		if(globalNamespace){
			namespace = GLOBAL_NS_KEY;
		}
		

		PropertiesNamespaceInfo<T> np = null;
		if(globalNamespace){
			np = namespacesMap.get(namespace);
			if(np==null){
				np = new GlobalNamespaceProperties();
				namespacesMap.put(np.getKey(), np);
			}
//			np.addAll(namedinfos, throwIfExist);
		}else{
			if(throwIfExist && namespacesMap.containsKey(namespace)){
				throw new BaseException("sql namespace has already exist : " + namespace);
			}
			np = new CommonNamespaceProperties(namespace, f);
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

	protected void buildNamedInfosToNamespaceFromResource(PropertiesNamespaceInfo<T> np, ResourceAdapter file){
		JFishPropertiesData jproperties = loadSqlFile(file);
		if(jproperties==null){
//			return Collections.EMPTY_MAP;
			return ;
		}
		logger.info("build [{}] sql file : {}", np.getNamespace(), file.getName());
//		PropertiesWraper wrapper = new PropertiesWraper(pf);
//		Map<String, T> namedInfos = this.buildPropertiesAsNamedInfos(f, ns, pf, (Class<T>)conf.getPropertyBeanClass());
		try {
			this.buildPropertiesAsNamedInfos(np, file, jproperties, (Class<T>)conf.getPropertyBeanClass());
		} catch (Exception e) {
			throw new BaseException("build named info error in " + file.getName() + " : " + e.getMessage(), e);
		}
//		return namedInfos;
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
	

}
