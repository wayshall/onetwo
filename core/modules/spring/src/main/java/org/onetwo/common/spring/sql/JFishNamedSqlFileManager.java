package org.onetwo.common.spring.sql;

import org.onetwo.common.db.filequery.MultipCommentsSqlFileParser;
import org.onetwo.common.db.filequery.NamespacePropertiesFileListener;
import org.onetwo.common.db.filequery.NamespacePropertiesFileManagerImpl;
import org.onetwo.common.db.filequery.NamespaceProperty;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
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
		JFishNamedSqlFileManager sqlfileMgr = new JFishNamedSqlFileManager(new DialetNamedSqlConf(watchSqlFile), listener);
//		sqlfileMgr.setDataSource(dataSource);
		return sqlfileMgr;
	}
	
	
	public static final String ATTRS_KEY = JFishNamedFileQueryInfo.TEMPLATE_DOT_KEY;
//	public static final String SQL_POSTFIX = ".sql";
	
//	protected final DataBase databaseType;
//	private DataSource dataSource;
	

	public JFishNamedSqlFileManager(DialetNamedSqlConf conf, NamespacePropertiesFileListener<JFishNamedFileQueryInfo> listener) {
		super(conf, listener);
//		this.databaseType = conf.getDatabaseType();
		this.setSqlFileParser(new MultipCommentsSqlFileParser<JFishNamedFileQueryInfo>());
	}

	/*@Override
	public void build(){
		if(conf==null){
			conf = new DialetNamedSqlConf(watchSqlFile);
		}
		super.build();
	}*/

	/*public DataBase getDatabaseType() {
		return ((DialetNamedSqlConf)conf).getDatabaseType();
	}*/
	protected void extBuildNamedInfoBean(JFishNamedFileQueryInfo propBean){
//		propBean.setDataBaseType(getDatabaseType());
	}

	public JFishNamedFileQueryInfo getNamedQueryInfo(String name) {
		JFishNamedFileQueryInfo info = super.getJFishProperty(name);
		if(info==null)
			throw new BaseException("namedQuery not found : " + name);
		return info;
	}
	
//	@Override
//	protected ResourceAdapter<?>[] scanMatchSqlFiles(JFishPropertyConf<JFishNamedFileQueryInfo> conf){
//		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//		
//		String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + conf.getDir();
////		String sqldirPath = locationPattern+"/*"+conf.getPostfix();
//		String sqldirPath = locationPattern+"/**/*"+conf.getPostfix();
//		
//		ResourceAdapter<?>[] allSqlFiles = null;
//		try {
//			Resource[] sqlfileArray = resourcePatternResolver.getResources(sqldirPath);
////			if(StringUtils.isNotBlank(conf.getOverrideDir())){
////				sqldirPath = locationPattern+"/"+conf.getOverrideDir()+"/**/*"+conf.getPostfix();
////				logger.info("scan database dialect dir : " + sqldirPath);
////				Resource[] dbsqlfiles = resourcePatternResolver.getResources(sqldirPath);
////				if(!LangUtils.isEmpty(dbsqlfiles)){
////					sqlfileArray = (Resource[]) ArrayUtils.addAll(sqlfileArray, dbsqlfiles);
////				}
////			}
//			allSqlFiles = new ResourceAdapter[sqlfileArray.length];
//			int index = 0;
//			for(Resource rs : sqlfileArray){
//				allSqlFiles[index++] = new SpringResourceAdapterImpl(rs);
//			}
//		} catch (Exception e) {
//			throw new BaseException("scan sql file error: " + e.getMessage());
//		}
//		
//		return allSqlFiles;
//	}
	
	private boolean isAttrsProperty(String prop){
		return prop.startsWith(ATTRS_KEY);
	}
	
	private String getAttrsProperty(String prop){
		return prop.substring(ATTRS_KEY.length());
	}

	protected void setNamedInfoProperty(JFishNamedFileQueryInfo bean, String prop, Object val){
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


	/*public static class DefaultJFishNamedSqlFileManager extends JFishNamedSqlFileManager<JFishNamedFileQueryInfo> {

		public DefaultJFishNamedSqlFileManager(
				DataBase databaseType,
				boolean watchSqlFile,
				NamespacePropertiesFileListener<JFishNamedFileQueryInfo> listener) {
			super(listener);
			new DialetNamedSqlConf<JFishNamedFileQueryInfo>(){
				{
					setDatabaseType(databaseType);
//					setPostfix(SQL_POSTFIX);
					setWatchSqlFile(watchSqlFile);
					setPropertyBeanClass(JFishNamedFileQueryInfo.class);
				}
			}
		}
		
	}*/
	
	public static class DialetNamedSqlConf extends JFishPropertyConf<JFishNamedFileQueryInfo> {
//		private DataBase databaseType;
		
		public DialetNamedSqlConf(boolean watchSqlFile){
			setWatchSqlFile(watchSqlFile);
			setPropertyBeanClass(JFishNamedFileQueryInfo.class);
		}

		/*public DataBase getDatabaseType() {
			return databaseType;
		}

		public void setDatabaseType(DataBase databaseType) {
			this.databaseType = databaseType;
			setOverrideDir(databaseType.toString());
		}
		*/
	}
}
