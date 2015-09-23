package org.onetwo.common.db.filequery;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jfishdbm.exception.FileNamedQueryException;
import org.onetwo.common.spring.ftl.TemplateParser;

/****
 * sql文件管理
 * @author weishao
 *
 * @param <T>
 */
public class JFishNamedSqlFileManager extends NamespacePropertiesFileManagerImpl<JFishNamedFileQueryInfo> {
	

	public static JFishNamedSqlFileManager createNamedSqlFileManager(boolean watchSqlFile) {
		StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo> listener = new StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo>();
		listener.initialize();
		JFishNamedSqlFileManager sqlfileMgr = new JFishNamedSqlFileManager(new DialetNamedSqlConf(watchSqlFile), listener, listener);
//		sqlfileMgr.setDataSource(dataSource);
		return sqlfileMgr;
	}
	
	
	public static final String ATTRS_KEY = JFishNamedFileQueryInfo.FRAGMENT_DOT_KEY;
	private TemplateParser sqlStatmentParser;
	

	public JFishNamedSqlFileManager(DialetNamedSqlConf conf, TemplateParser sqlStatmentParser, NamespacePropertiesFileListener<JFishNamedFileQueryInfo> listener) {
		super(conf, listener);
//		this.databaseType = conf.getDatabaseType();
		this.setSqlFileParser(new MultipCommentsSqlFileParser());
		this.sqlStatmentParser = sqlStatmentParser;
	}
	
	public TemplateParser getSqlStatmentParser() {
		return sqlStatmentParser;
	}

	protected void extBuildNamedInfoBean(JFishNamedFileQueryInfo propBean){
//		propBean.setDataBaseType(getDatabaseType());
	}

	public JFishNamedFileQueryInfo getNamedQueryInfo(String name) {
		JFishNamedFileQueryInfo info = super.getJFishProperty(name);
		if(info==null)
			throw new FileNamedQueryException("namedQuery not found : " + name);
		return info;
	}
	
	public static class DialetNamedSqlConf extends JFishPropertyConf<JFishNamedFileQueryInfo> {
//		private DataBase databaseType;
		
		public DialetNamedSqlConf(boolean watchSqlFile){
			setWatchSqlFile(watchSqlFile);
			setPropertyBeanClass(JFishNamedFileQueryInfo.class);
		}

	}
}
