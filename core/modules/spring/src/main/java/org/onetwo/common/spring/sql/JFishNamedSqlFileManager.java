package org.onetwo.common.spring.sql;

import org.onetwo.common.db.filequery.NamespacePropertiesFileListener;
import org.onetwo.common.db.filequery.NamespacePropertiesFileManagerImpl;
import org.onetwo.common.db.filequery.NamespaceProperty;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.ftl.TemplateParser;
import org.onetwo.common.utils.StringUtils;

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
			throw new BaseException("namedQuery not found : " + name);
		return info;
	}
	
	/*private boolean isAttrsProperty(String prop){
		return prop.startsWith(ATTRS_KEY);
	}
	
	private String getAttrsProperty(String prop){
		return prop.substring(ATTRS_KEY.length());
	}*/

	/*protected void setNamedInfoProperty(JFishNamedFileQueryInfo bean, String prop, Object val){
		if(prop.startsWith(JFishNamedFileQueryInfo.FRAGMENT_DOT_KEY)){
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
	}*/


	public static class DialetNamedSqlConf extends JFishPropertyConf<JFishNamedFileQueryInfo> {
//		private DataBase databaseType;
		
		public DialetNamedSqlConf(boolean watchSqlFile){
			setWatchSqlFile(watchSqlFile);
			setPropertyBeanClass(JFishNamedFileQueryInfo.class);
		}

	}
}
