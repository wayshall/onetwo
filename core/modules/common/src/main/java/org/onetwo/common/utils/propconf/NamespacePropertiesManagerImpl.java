package org.onetwo.common.utils.propconf;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;


public class NamespacePropertiesManagerImpl<T extends NamespaceProperty> extends AbstractPropertiesManager<T> implements NamespacePropertiesManager<T>{

	public class CommonNamespaceProperties implements NamespaceProperties<T> {
		private final String namespace;
		private ResourceAdapter source;
		private final Map<String, T> namedProperties;
		
		public CommonNamespaceProperties(String namespace){
			this.namespace = namespace;
			this.namedProperties = LangUtils.newHashMap();
		}
		public CommonNamespaceProperties(String namespace, ResourceAdapter source, Map<String, T> namedProperties) {
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
				if(throwIfExist && this.namedProperties.containsKey(entry.getKey())){
					NamespaceProperty exitProp = this.namedProperties.get(entry.getKey());
					throw new BaseException("int file["+entry.getValue().getSrcfile()+"], sql key["+entry.getKey()+"] has already exist in namespace: " + namespace+", in file: "+ exitProp.getSrcfile());
				}
				this.namedProperties.put(entry.getKey(), entry.getValue());
			}
		}
		@Override
		public boolean isGlobal() {
			return false;
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
		@Override
		public boolean isGlobal() {
			return true;
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
		ResourceAdapter[] sqlfileArray = scanMatchSqlFiles(conf);
		this.namespaceProperties = this.autoScanSqlDir(sqlfileArray);
		this.buildSqlFileMonitor(sqlfileArray);
		
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
		this.scanAndParseSqlFile(this.namespaceProperties, file, false);
		logger.warn("file relaoded : " + file);
	}
	
	private boolean isGlobalNamespace(String namespace){
		return GLOBAL_NS_KEY.endsWith(namespace) || !namespace.contains(".");
	}
	
	protected Map<String, NamespaceProperties<T>> autoScanSqlDir(ResourceAdapter[] sqlfileArray){
		if(LangUtils.isEmpty(sqlfileArray)){
			logger.info("no named sql file found.");
			return Collections.EMPTY_MAP;
		}
		
		Map<String, NamespaceProperties<T>> nsproperties = LangUtils.newHashMap(sqlfileArray.length);
		for(ResourceAdapter f : sqlfileArray){
			logger.info("parse named sql file: {}", f);
			this.scanAndParseSqlFile(nsproperties, f, true);
		}
		return nsproperties;
	}


	protected NamespaceProperties<T> scanAndParseSqlFile(Map<String, NamespaceProperties<T>> nsproperties, ResourceAdapter f, boolean throwIfExist){
		logger.info("scan and parse sql file : " + f);
		
		String namespace = getFileNameNoJfishSqlPostfix(f);
		if(isGlobalNamespace(namespace)){
			namespace = GLOBAL_NS_KEY;
		}
		
		Map<String, T> namedinfos = buildNamedInfos(namespace, f);
		/*if(namedinfos.isEmpty())
			return null;*/

		NamespaceProperties<T> np = null;
		if(isGlobalNamespace(namespace)){
			np = nsproperties.get(namespace);
			if(np==null){
				np = new GlobalNamespaceProperties();
				nsproperties.put(np.getNamespace(), np);
			}
			np.addAll(namedinfos, throwIfExist);
		}else{
			if(throwIfExist && nsproperties.containsKey(namespace)){
				throw new BaseException("sql namespace has already exist : " + namespace);
			}
			np = new CommonNamespaceProperties(namespace, f, namedinfos);
		}
		nsproperties.put(namespace, np);
		
		for(T nsp : np.getNamedProperties()){
			this.namedQueryCache.put(nsp.getFullName(), nsp);
		}
		
		return np;
	}

	protected Map<String, T> buildNamedInfos(String ns, ResourceAdapter f){
		JFishProperties pf = loadSqlFile(f);
		if(pf==null)
			return Collections.EMPTY_MAP;
		logger.info("build [{}] sql file : {}", ns, f);
//		PropertiesWraper wrapper = new PropertiesWraper(pf);
		Map<String, T> namedInfos = this.buildPropertiesAsNamedInfos(f, ns, pf, (Class<T>)conf.getPropertyBeanClass());
		return namedInfos;
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
	public NamespaceProperties<T> getNamespaceProperties(String namespace) {
		return namespaceProperties.get(namespace);
	}

	public Collection<NamespaceProperties<T>> getAllNamespaceProperties() {
		return namespaceProperties.values();
	}
	

}
