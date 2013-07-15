package org.onetwo.common.fish.spring;

import java.io.File;

import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.NamespacePropertiesManagerImpl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;


public class JFishNamedFileQueryManagerImpl extends NamespacePropertiesManagerImpl<JFishNamedFileQueryInfo> implements JFishNamedFileQueryManager{

	public static final String SQL_POSTFIX = ".sql";
	
	public JFishNamedFileQueryManagerImpl(final String dbname, final boolean watchSqlFile) {
		super(new JFishPropertyConf(){
			{
				setDir("sql");
				setOverrideDir(dbname);
//				setPostfix(SQL_POSTFIX);
				setPostfix(JFISH_SQL_POSTFIX);
				setWatchSqlFile(watchSqlFile);
			}
		});
	}
	
	protected File[] scanMatchSqlFiles(JFishPropertyConf conf){
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		
		String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + conf.getDir();
		String sqldirPath = locationPattern+"/**/*"+conf.getPostfix();
		
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
			throw new JFishOrmException("scan sql file error: " + e.getMessage());
		}
		
		return allSqlFiles;
	}
	
	public JFishNamedFileQueryInfo getNamedQueryInfo(String name) {
		JFishNamedFileQueryInfo info = super.getJFishProperty(name);
		if(info==null)
			throw new JFishOrmException("namedQuery not found : " + name);
		return info;
	}
	
	public JFishQuery createQuery(JFishDaoImplementor jfishFishDao, String queryName){
		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		return new JFishFileQueryImpl(jfishFishDao, nameInfo, false);
	}
	
	public JFishQuery createCountQuery(JFishDaoImplementor jfishFishDao, String queryName){
		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		return new JFishFileQueryImpl(jfishFishDao, nameInfo, true);
	}

	@Override
	public boolean containsQuery(String queryName) {
		return super.contains(queryName);
	}

}
