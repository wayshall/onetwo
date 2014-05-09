package org.onetwo.common.spring.sql;

import java.io.IOException;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.SpringResourceAdapterImpl;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.PropertiesNamespaceInfoListener;
import org.onetwo.common.utils.propconf.PropertiesNamespaceInfoManagerImpl;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class JFishNamedSqlFileManager<T extends JFishNamedFileQueryInfo> extends PropertiesNamespaceInfoManagerImpl<T> {
	public static final String ATTRS_KEY = JFishNamedFileQueryInfo.TEMPLATE_DOT_KEY;
	public static final String SQL_POSTFIX = ".sql";
	
	public static class DialetNamedSqlConf<E> extends JFishPropertyConf<E> {
		private DataBase databaseType;
		
		public DialetNamedSqlConf(){
			setDir("sql");
			setPostfix(JFISH_SQL_POSTFIX);
		}

		public DataBase getDatabaseType() {
			return databaseType;
		}

		public void setDatabaseType(DataBase databaseType) {
			this.databaseType = databaseType;
			setOverrideDir(databaseType.toString());
		}
		
	}
	

	/*public JFishNamedSqlFileManager(final String dbname, final boolean watchSqlFile) {
		this(dbname, watchSqlFile, null);
	}*/
	protected final DataBase databaseType;
	
	public JFishNamedSqlFileManager(final DataBase databaseType, final boolean watchSqlFile, final Class<T> propertyBeanClass, PropertiesNamespaceInfoListener<T> listener) {
		super(new DialetNamedSqlConf<T>(){
			{
				setDatabaseType(databaseType);
//				setPostfix(SQL_POSTFIX);
				setWatchSqlFile(watchSqlFile);
				setPropertyBeanClass(propertyBeanClass);
			}
		}, listener);
		this.databaseType = databaseType;
	}

	public DataBase getDatabaseType() {
		return databaseType;
	}
	protected void extBuildNamedInfoBean(T propBean){
		propBean.setDataBaseType(getDatabaseType());
	}

	public T getNamedQueryInfo(String name) {
		T info = super.getJFishProperty(name);
		if(info==null)
			throw new BaseException("namedQuery not found : " + name);
		return info;
	}
	
	@Override
	protected List<String> readResourceAsList(ResourceAdapter f){
		if(f.isSupportedToFile()){
			return FileUtils.readAsList(f.getFile());
		}else{
			Resource res = (Resource)f.getResource();
			try {
				return FileUtils.readAsList(res.getInputStream());
			} catch (IOException e) {
				throw new BaseException("read content error: " + f, e);
			}
		}
	}
	
	@Override
	protected ResourceAdapter[] scanMatchSqlFiles(JFishPropertyConf conf){
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		
		String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + conf.getDir();
		String sqldirPath = locationPattern+"/*"+conf.getPostfix();
		
		ResourceAdapter[] allSqlFiles = null;
		try {
			Resource[] sqlfileArray = resourcePatternResolver.getResources(sqldirPath);
			if(StringUtils.isNotBlank(conf.getOverrideDir())){
				sqldirPath = locationPattern+"/"+conf.getOverrideDir()+"/**/*"+conf.getPostfix();
				logger.info("scan database dialect dir : " + sqldirPath);
				Resource[] dbsqlfiles = resourcePatternResolver.getResources(sqldirPath);
				if(!LangUtils.isEmpty(dbsqlfiles)){
					sqlfileArray = (Resource[]) ArrayUtils.addAll(sqlfileArray, dbsqlfiles);
				}
			}
			allSqlFiles = new ResourceAdapter[sqlfileArray.length];
			int index = 0;
			for(Resource rs : sqlfileArray){
				allSqlFiles[index++] = new SpringResourceAdapterImpl(rs);
			}
		} catch (Exception e) {
			throw new BaseException("scan sql file error: " + e.getMessage());
		}
		
		return allSqlFiles;
	}
	
	private boolean isAttrsProperty(String prop){
		return prop.startsWith(ATTRS_KEY);
	}
	
	private String getAttrsProperty(String prop){
		return prop.substring(ATTRS_KEY.length());
	}

	protected void setNamedInfoProperty(T bean, String prop, Object val){
		if(prop.startsWith(JFishNamedFileQueryInfo.TEMPLATE_DOT_KEY)){
			//no convert to java property name
		}else{
			if(prop.indexOf(NamespaceProperty.DOT_KEY)!=-1){
				prop = StringUtils.toJavaName(prop, NamespaceProperty.DOT_KEY, false);
			}
		}
		
		if(isAttrsProperty(prop)){
			String attrProp = getAttrsProperty(prop);
			bean.getAttrs().put(attrProp, val.toString());
		}else{
			SpringUtils.newBeanWrapper(bean).setPropertyValue(prop, val);
		}
	}
}
