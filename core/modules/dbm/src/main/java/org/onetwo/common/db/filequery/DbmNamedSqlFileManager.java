package org.onetwo.common.db.filequery;

import org.onetwo.common.spring.ftl.TemplateParser;
import org.onetwo.dbm.exception.FileNamedQueryException;

/****
 * sql文件管理
 * @author weishao
 *
 * @param <T>
 */
public class DbmNamedSqlFileManager extends NamespacePropertiesFileManagerImpl<DbmNamedFileQueryInfo> {
	

	public static DbmNamedSqlFileManager createNamedSqlFileManager(boolean watchSqlFile) {
		StringTemplateLoaderFileSqlParser<DbmNamedFileQueryInfo> listener = new StringTemplateLoaderFileSqlParser<DbmNamedFileQueryInfo>();
		listener.initialize();
		DbmNamedSqlFileManager sqlfileMgr = new DbmNamedSqlFileManager(new DialetNamedSqlConf(watchSqlFile), listener, listener);
//		sqlfileMgr.setDataSource(dataSource);
		return sqlfileMgr;
	}
	
	
	public static final String ATTRS_KEY = DbmNamedFileQueryInfo.FRAGMENT_DOT_KEY;
	private TemplateParser sqlStatmentParser;
	

	public DbmNamedSqlFileManager(DialetNamedSqlConf conf, TemplateParser sqlStatmentParser, NamespacePropertiesFileListener<DbmNamedFileQueryInfo> listener) {
		super(conf, listener);
//		this.databaseType = conf.getDatabaseType();
		this.setSqlFileParser(new MultipCommentsSqlFileParser());
		this.sqlStatmentParser = sqlStatmentParser;
	}

	protected void putIntoCaches(String key, DbmNamedFileQueryInfo nsp){
		super.putIntoCaches(key, nsp);
		nsp.getAliasList().forEach(aliasName->{
			/*try {
				JFishNamedFileQueryInfo cloneBean = nsp.clone();
				cloneBean.setName(aliasName);
				super.putIntoCaches(aliasName, cloneBean);
			} catch (Exception e) {
				throw new DbmException("clone error: " + key);
			}*/
			super.putIntoCaches(nsp.getFullName(aliasName), nsp);
		});
	}
	
	public TemplateParser getSqlStatmentParser() {
		return sqlStatmentParser;
	}

	protected void extBuildNamedInfoBean(DbmNamedFileQueryInfo propBean){
//		propBean.setDataBaseType(getDatabaseType());
	}

	public DbmNamedFileQueryInfo getNamedQueryInfo(String name) {
		DbmNamedFileQueryInfo info = super.getNamedQueryInfo(name);
		if(info==null)
			throw new FileNamedQueryException("namedQuery not found : " + name);
		return info;
	}
	
	public static class DialetNamedSqlConf extends JFishPropertyConf<DbmNamedFileQueryInfo> {
//		private DataBase databaseType;
		
		public DialetNamedSqlConf(boolean watchSqlFile){
			setWatchSqlFile(watchSqlFile);
			setPropertyBeanClass(DbmNamedFileQueryInfo.class);
		}

	}
}
