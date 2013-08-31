package org.onetwo.common.spring.sql;

import java.io.File;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.NamespacePropertiesManagerImpl;
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
	
	
	protected File[] scanMatchSqlFiles(JFishPropertyConf conf){
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		
		String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + conf.getDir();
		String sqldirPath = locationPattern+"/*"+conf.getPostfix();
		
		File[] allSqlFiles = null;
		try {
			Resource[] sqlfileArray = resourcePatternResolver.getResources(sqldirPath);
			if(StringUtils.isNotBlank(conf.getOverrideDir())){
				sqldirPath = locationPattern+"/"+conf.getOverrideDir()+"/**/*"+conf.getPostfix();
				Resource[] dbsqlfiles = resourcePatternResolver.getResources(sqldirPath);
				if(!LangUtils.isEmpty(dbsqlfiles)){
					sqlfileArray = (Resource[]) ArrayUtils.addAll(sqlfileArray, dbsqlfiles);
				}
			}
			allSqlFiles = new File[sqlfileArray.length];
			int index = 0;
			for(Resource rs : sqlfileArray){
				allSqlFiles[index++] = rs.getFile();
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
