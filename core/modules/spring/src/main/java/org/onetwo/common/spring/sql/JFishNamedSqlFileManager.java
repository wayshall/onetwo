package org.onetwo.common.spring.sql;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.SpringResourceAdapterImpl;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.NamespacePropertiesManagerImpl;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.onetwo.common.utils.propconf.ResourceAdapterImpl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class JFishNamedSqlFileManager<T extends JFishNamedFileQueryInfo> extends NamespacePropertiesManagerImpl<T> {
	public static final String SQL_POSTFIX = ".sql";
	

	/*public JFishNamedSqlFileManager(final String dbname, final boolean watchSqlFile) {
		this(dbname, watchSqlFile, null);
	}*/
	public JFishNamedSqlFileManager(final String dbname, final boolean watchSqlFile, final Class<T> propertyBeanClass) {
		super(new JFishPropertyConf<T>(){
			{
				setDir("sql");
				setOverrideDir(dbname);
//				setPostfix(SQL_POSTFIX);
				setPostfix(JFISH_SQL_POSTFIX);
				setWatchSqlFile(watchSqlFile);
				setPropertyBeanClass(propertyBeanClass);
			}
		});
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
	

	protected void setNamedInfoProperty(Object bean, String prop, Object val){
		SpringUtils.newBeanWrapper(bean).setPropertyValue(prop, val);
	}
}
