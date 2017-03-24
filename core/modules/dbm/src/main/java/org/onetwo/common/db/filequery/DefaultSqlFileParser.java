package org.onetwo.common.db.filequery;

import java.util.List;
import java.util.Properties;

import org.onetwo.common.db.filequery.spi.SqlFileParser;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

public class DefaultSqlFileParser implements SqlFileParser {

	public static final String GLOBAL_NS_KEY = "global";
	public static final String COMMENT = "--";
	public static final String MULTIP_COMMENT_START = "/*";
	public static final String MULTIP_COMMENT_END = "*/";
	public static final String CONFIG_PREFIX = "@@";
	public static final String NAME_PREFIX = "@";
	public static final String EQUALS_MARK = "=";
	
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	protected boolean debug = true;
	
	@Override
	public void parseToNamespaceProperty(DbmNamedQueryFile np, ResourceAdapter<?> file) {
		JFishPropertiesData jproperties = loadSqlFile(file);
		if(jproperties==null){
			return ;
		}
		logger.info("build [{}] sql file : {}", np.getNamespace(), file.getName());
		try {
			this.buildPropertiesAsNamedInfos(np, file, jproperties);
		} catch (Exception e) {
			throw new BaseException("build named info error in " + file.getName() + " : " + e.getMessage(), e);
		}
	}
	protected JFishPropertiesData loadSqlFile(ResourceAdapter<?> f){
//		String fname = FileUtils.getFileNameWithoutExt(f.getName());
		if(!f.getName().endsWith(POSTFIX)){
			logger.info("file["+f.getName()+" is not a ["+POSTFIX+"] file, ignore it.");
			return null;
		}
		
		JFishPropertiesData jpData = null;
		Properties pf = new Properties();
		Properties config = new Properties();
		try {
			List<String> fdatas = readResourceAsList(f);
			String key = null;
			StringBuilder value = null;
//			String line = null;

			boolean matchConfig = false;
			boolean matchName = false;
			boolean multiCommentStart = false;
			for(int i=0; i<fdatas.size(); i++){
				final String line = fdatas.get(i).trim();
				/*if(line.startsWith(COMMENT)){
					continue;
				}*/
				
				if(line.startsWith(MULTIP_COMMENT_START)){
					multiCommentStart = true;
					continue;
				}else if(line.endsWith(MULTIP_COMMENT_END)){
					multiCommentStart = false;
					continue;
				}
				if(multiCommentStart){
					continue;
				}
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
				}else if(line.startsWith(COMMENT)){
					continue;
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

//			jp.setProperties(new JFishProperties(pf));
//			jp.setConfig(new JFishProperties(config));
			
			jpData = new JFishPropertiesData(new JFishProperties(pf), new JFishProperties(config));
			System.out.println("loaded jfish file : " + f.getName());
		} catch (Exception e) {
			LangUtils.throwBaseException("load jfish file error : " + f, e);
		}
		return jpData;
	}

	protected List<String> readResourceAsList(ResourceAdapter<?> f){
		/*if(f.isSupportedToFile())
			return FileUtils.readAsList(f.getFile());
		else
			throw new UnsupportedOperationException();*/
		return f.readAsList();
	}
	
	/***********
	 * build a map: 
	 * key is name of beanClassOfProperty
	 * value is instance of beanClassOfProperty
	 * @param resource
	 * @param namespace
	 * @param wrapper
	 * @param beanClassOfProperty
	 * @return
	 */
	protected void buildPropertiesAsNamedInfos(DbmNamedQueryFile namespaceInfo, ResourceAdapter<?> resource, JFishPropertiesData jp){
		JFishProperties wrapper = jp.getProperties();
		List<String> keyNames = wrapper.sortedKeys();
		if(debug){
			logger.info("================>>> buildPropertiesAsNamedInfos");
			for(String str : keyNames){
				logger.info(str);
			}
		}
		DbmNamedQueryInfo propBean = null;
		boolean newBean = true;
		String preKey = null;
//		String val = "";
//		Map<String, T> namedProperties = LangUtils.newHashMap();
		for(String key : keyNames){
			if(preKey!=null)
				newBean = !key.startsWith(preKey);
			if(newBean){
				if(propBean!=null){
					extBuildNamedInfoBean(propBean);
					namespaceInfo.put(propBean.getName(), propBean, true);
				}
				propBean = new DbmNamedQueryInfo();
				String val = wrapper.getAndThrowIfEmpty(key);
				/*if(key.endsWith(IGNORE_NULL_KEY)){
					throw new BaseException("the query name["+key+"] cant be end with: " + IGNORE_NULL_KEY);
				}*/
				propBean.setName(key);
				propBean.setValue(val);
//				propBean.setNamespace(namespace);
				newBean = false;
				preKey = key+DbmNamedQueryInfo.DOT_KEY;
				propBean.setSrcfile(resource);
				propBean.setConfig(jp.getConfig());
				propBean.setDbmNamedQueryFile(namespaceInfo);
			}else{
				String val = wrapper.getProperty(key, "");
				String prop = key.substring(preKey.length());
				/*if(prop.startsWith(NamespaceProperty.ATTRS_DOT_KEY)){
					//no convert to java property name
				}else{
					if(prop.indexOf(NamespaceProperty.DOT_KEY)!=-1){
						prop = StringUtils.toJavaName(prop, NamespaceProperty.DOT_KEY, false);
					}
				}*/
				setNamedInfoProperty(propBean, prop, val);
			}
		}
		if(propBean!=null){
			namespaceInfo.put(propBean.getName(), propBean, true);
		}
		if(logger.isInfoEnabled()){
			logger.info("================ {} named query start ================", namespaceInfo.getNamespace());
			for(DbmNamedQueryInfo prop : namespaceInfo.getNamedProperties()){
				logger.info(prop.getName()+": \t"+prop);
			}
			logger.info("================ {} named query end ================", namespaceInfo.getNamespace());
		}
		
//		return namedProperties;
	}
	protected void extBuildNamedInfoBean(DbmNamedQueryInfo propBean){
	}
	protected void setNamedInfoProperty(DbmNamedQueryInfo bean, String prop, Object val){
		if(prop.indexOf(DbmNamedQueryInfo.DOT_KEY)!=-1){
			prop = StringUtils.toCamel(prop, DbmNamedQueryInfo.DOT_KEY, false);
		}
		try {
			ReflectUtils.setExpr(bean, prop, val);
		} catch (Exception e) {
			logger.error("set value error : "+prop);
			LangUtils.throwBaseException(e);
		}
	}
	protected class JFishPropertiesData {
		final private JFishProperties properties;
		final private JFishProperties config;
		public JFishPropertiesData(JFishProperties properties, JFishProperties config) {
			super();
			this.properties = properties;
			this.config = config;
		}
		public JFishProperties getProperties() {
			return properties;
		}
		public JFishProperties getConfig() {
			return config;
		}
		
	}

}
