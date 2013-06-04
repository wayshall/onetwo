package org.onetwo.common.utils.propconf;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;


public class NamespacePropertiesManagerImpl<T extends NamespaceProperty> extends AbstractPropertiesManager<T> implements NamespacePropertiesManager<T>{

	public static interface NamespaceProperties<T> {
		public String getNamespace();
		public Map<String, T> getNamedProperties();
		public T getNamedProperty(String name);
	}
	public class CommonNamespaceProperties implements NamespaceProperties<T> {
		private final String namespace;
		private File source;
		private final Map<String, T> namedProperties;
		
		public CommonNamespaceProperties(String namespace){
			this.namespace = namespace;
			this.namedProperties = LangUtils.newHashMap();
		}
		public CommonNamespaceProperties(String namespace, File source, Map<String, T> namedProperties) {
			super();
			this.namespace = namespace;
			this.source = source;
			this.namedProperties = namedProperties;
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		@Override
		public Map<String, T> getNamedProperties() {
			return namedProperties;
		}

		public File getSource() {
			return source;
		}
		@Override
		public T getNamedProperty(String name) {
			return namedProperties.get(name);
		}
		
	}

	public class GlobalNamespaceProperties extends CommonNamespaceProperties {
		private final List<File> sources;
		
		private GlobalNamespaceProperties() {
			super(GLOBAL_NS_KEY);
			this.sources = LangUtils.newArrayList();
		}

		public List<File> getSources() {
			return sources;
		}
		
	}

	
	Map<String, NamespaceProperties<T>> namespaceProperties;
	
//	private List<Properties> sqlfiles;
//	private PropertiesWraper wrapper;
	private Map<String, T> namedQueryCache;

	public NamespacePropertiesManagerImpl(JFishPropertyConf conf) {
		super(conf);
		if(conf.getPropertyBeanClass()==null){
			Class<T> clz = ReflectUtils.getSuperClassGenricType(this.getClass(), NamespacePropertiesManagerImpl.class);
			conf.setPropertyBeanClass(clz);
		}
	}
	
	protected JFishPropertyConf getConf() {
		return conf;
	}

	public void build(){
		this.namedQueryCache = LangUtils.newHashMap();
		File[] sqlfileArray = scanMatchSqlFiles(conf);
		this.namespaceProperties = this.autoScanSqlDir(sqlfileArray);
		this.buildSqlFileMonitor(sqlfileArray);
		
		System.out.println("all named query : ");
		for(T prop : this.namedQueryCache.values()){
			System.out.println(prop);
		}
	}
	
	public void reloadFile(File file){
		Properties pf = loadSqlFile(file);
		if(pf==null){
			logger.warn("no file relaoded : " + file.getPath());
			return ;
		}
		this.scanAndParseSqlFile(this.namespaceProperties, file);
		logger.warn("file relaoded : " + file.getPath());
	}
	
	private boolean isGlobalNamespace(String namespace){
		return GLOBAL_NS_KEY.endsWith(namespace) || !namespace.contains(".");
	}
	
	protected Map<String, NamespaceProperties<T>> autoScanSqlDir(File[] sqlfileArray){
		if(LangUtils.isEmpty(sqlfileArray))
			return null;
		
		Map<String, NamespaceProperties<T>> nsproperties = LangUtils.newHashMap(sqlfileArray.length);
		for(File f : sqlfileArray){
			this.scanAndParseSqlFile(nsproperties, f);
		}
		return nsproperties;
	}


	protected NamespaceProperties<T> scanAndParseSqlFile(Map<String, NamespaceProperties<T>> nsproperties, File f){
		logger.info("scan and parse sql file : " + f.getPath());
		
		String ns = getFileNameNoJfishSqlPostfix(f);
		if(isGlobalNamespace(ns)){
			ns = GLOBAL_NS_KEY;
		}
		
		Map<String, T> namedinfos = buildNamedInfos(ns, f);
		if(namedinfos.isEmpty())
			return null;

		NamespaceProperties<T> np = null;
		if(isGlobalNamespace(ns)){
			np = nsproperties.get(ns);
			if(np==null){
				np = new GlobalNamespaceProperties();
				nsproperties.put(np.getNamespace(), np);
			}
			np.getNamedProperties().putAll(namedinfos);
		}else{
			if(nsproperties.containsKey(ns)){
				throw new BaseException("sql namespace has already exist : " + ns);
			}
			np = new CommonNamespaceProperties(ns, f, namedinfos);
		}
		nsproperties.put(ns, np);
		
		for(T nsp : np.getNamedProperties().values()){
			this.namedQueryCache.put(nsp.getFullName(), nsp);
		}
		
		return np;
	}

	protected Map<String, T> buildNamedInfos(String ns, File f){
		Properties pf = loadSqlFile(f);
		if(pf==null || pf.isEmpty())
			return Collections.EMPTY_MAP;
		logger.info("build [{}] sql file : {}", ns, f.getPath());
		PropertiesWraper wrapper = new PropertiesWraper(pf);
		Map<String, T> namedInfos = this.buildPropertiesAsNamedInfos(ns, wrapper, (Class<T>)conf.getPropertyBeanClass());
		return namedInfos;
	}
	

	public T getJFishProperty(String name) {
		T info = namedQueryCache.get(name);
		return info;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder("named query : \n");
		for(T info : this.namedQueryCache.values()){
			sb.append(info).append(",\n");
		}
		return sb.toString();
	}

	@Override
	public NamespaceProperties<T> getNamespaceProperties(String namespace) {
		return namespaceProperties.get(namespace);
	}

}
