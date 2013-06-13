package org.onetwo.common.fish.spring;

import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.utils.propconf.NamespacePropertiesManagerImpl;


public class JFishNamedFileQueryManagerImpl extends NamespacePropertiesManagerImpl<JFishNamedFileQueryInfo> implements JFishNamedFileQueryManager{

	public static final String SQL_POSTFIX = ".sql";
	
	public JFishNamedFileQueryManagerImpl(final String dbname, final boolean watchSqlFile) {
		super(new JFishPropertyConf(){
			{
				setDir("sql");
				setOverrideDir(dbname);
				setPostfix(SQL_POSTFIX);
				setWatchSqlFile(watchSqlFile);
			}
		});
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
