package org.onetwo.common.hibernate.sql;

import java.util.List;
import java.util.Map;

import org.hibernate.NonUniqueResultException;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.db.exception.NotUniqueResultException;
import org.onetwo.common.spring.sql.AbstractFileNamedQueryFactory;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;

/****
 * 文件的命名查询管理器
 * @author wayshall
 *
 */
public class HibernateFileQueryManagerImpl extends AbstractFileNamedQueryFactory<JFishNamedFileQueryInfo> {
	
//	private JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager;
//	private TemplateParser parser;
	
	public HibernateFileQueryManagerImpl(FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		this(null, fileNamedQueryFactoryListener);
	}
	public HibernateFileQueryManagerImpl(JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager, FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super(sqlFileManager, fileNamedQueryFactoryListener);
		//Class<HibernateNamedInfo> clazz = find(HibernateNamedInfo.class);
//		StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo> p = new StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo>();
//		p.initParser();
		/*if(sqlFileManager!=null){

			this.parser = (TemplateParser)sqlFileManager.getListener();
//			sqlFileManager = new HibernateNamedSqlFileManager(databaseType, watchSqlFile, JFishNamedFileQueryInfo.class, p);
//			sqlFileManager = JFishNamedSqlFileManager.createDefaultJFishNamedSqlFileManager(databaseType, watchSqlFile, p);
			this.sqlFileManager = sqlFileManager;
//			this.baseEntityManager = baseEntityManager;
		}*/
	}
	
	public HibernateFileQueryImpl createHibernateFileQuery(String queryName, Object... args){
		return createHibernateFileQuery(false, queryName, PlaceHolder.NAMED, args);
	}
	
	public HibernateFileQueryImpl createCountHibernateFileQuery(String queryName, Object... args){
		return createHibernateFileQuery(true, queryName, PlaceHolder.NAMED, args);
	}

	public HibernateFileQueryImpl createHibernateFileQuery(boolean count, String queryName, PlaceHolder type, Object... args){
		Assert.notNull(type);

		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		HibernateFileQueryImpl jq = new HibernateFileQueryImpl(getCreateQueryable(), nameInfo, count, parser);
//		HibernateFileQueryImpl jq = new TempHibernateFileQueryImpl(baseEntityManager, nameInfo, count, parser);
		
		if(type==PlaceHolder.POSITION){
			jq.setParameters(LangUtils.asList(args));
		}else{
			if(args.length==1 && LangUtils.isMap(args[0])){
				jq.setParameters((Map)args[0]);
			}else{
				jq.setQueryAttributes(LangUtils.asMap(args));
			}
		}
		return jq;
	}
	
	public DataQuery createCountQuery(String queryName){
		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		return new HibernateFileQueryImpl(getCreateQueryable(), nameInfo, true, parser);
//		return new TempHibernateFileQueryImpl(baseEntityManager, nameInfo, true, parser);
	}
	

	@Override
	public DataQuery createQuery(String queryName, PlaceHolder type, Object... args){
		return createHibernateFileQuery(false, queryName, type, args);
	}
	
	@Override
	public DataQuery createQuery(String queryName, Object... args) {
		return createHibernateFileQuery( queryName, args);
	}
	
	/*@Override
	public FileNamedSqlGenerator<JFishNamedFileQueryInfo> createFileNamedSqlGenerator(String queryName, Map<Object, Object> params) {
		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		FileNamedSqlGenerator<JFishNamedFileQueryInfo> g = new DefaultFileNamedSqlGenerator<JFishNamedFileQueryInfo>(nameInfo, false, parser, params);
		return g;
	}*/

	@Override
	public DataQuery createCountQuery(String queryName, Object... args) {
		return createCountHibernateFileQuery(queryName, args);
	}
	

	@Override
	public <T> List<T> findList(String queryName, Object... params) {
		DataQuery jq = this.createQuery(queryName, params);
		return jq.getResultList();
	}


	@Override
	public <T> T findUnique(String queryName, Object... params) {
		DataQuery jq = this.createQuery(queryName, params);
		try {
			return jq.getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new NotUniqueResultException(e.getMessage(), e);
		}
	}


	@Override
	public <T> Page<T> findPage(String queryName, Page<T> page, Object... params) {
		DataQuery jq = null;
		if(page.isAutoCount()){
			jq = this.createCountQuery(queryName, params);
			Long total = jq.getSingleResult();
			total = (total==null?0:total);
			page.setTotalCount(total);
			if(total<1)
				return page;
		}
		
		jq = this.createQuery(queryName, params).setPageParameter(page);
//		jq.setFirstResult(page.getFirst()-1);
//		jq.setMaxResults(page.getPageSize());
		List<T> datalist = jq.getResultList();
		page.setResult(datalist);
		if(!page.isAutoCount()){
			page.setTotalCount(datalist.size());
		}
		
		return page;
	}
	
	@Override
	public NamespacePropertiesManager<JFishNamedFileQueryInfo> getNamespacePropertiesManager() {
		return sqlFileManager;
	}

	public JFishNamedSqlFileManager<JFishNamedFileQueryInfo> getSqlFileManager() {
		return sqlFileManager;
	}

	public void setSqlFileManager(
			JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager) {
		this.sqlFileManager = sqlFileManager;
	}
	
}
