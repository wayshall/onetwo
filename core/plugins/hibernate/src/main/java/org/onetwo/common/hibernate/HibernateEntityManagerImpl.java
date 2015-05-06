package org.onetwo.common.hibernate;

import java.io.Serializable;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.OptimizerFactory.StandardOptimizerDescriptor;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.id.factory.spi.MutableIdentifierGeneratorFactory;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.RootClass;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.service.Service;
import org.javatuples.Pair;
import org.onetwo.common.base.HibernateSequenceNameManager;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.utils.CopyConfig;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.ReflectUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class HibernateEntityManagerImpl extends AbstractEntityManager implements HibernateQueryProvider, InitializingBean {

	
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	private SessionFactory sessionFactory; 
	
	private SequenceNameManager sequenceNameManager = new HibernateSequenceNameManager();
	
//	@Resource
	private FileNamedQueryFactory<JFishNamedFileQueryInfo> fileNamedQueryFactory;
	
//	@Resource
//	private DataSource dataSource;
	
	@Resource
	private ApplicationContext applicationContext;
	@Resource
	private LocalSessionFactoryBean localSessionFactoryBean;
	
//	private boolean watchSqlFile = BaseSiteConfig.getInstance().isDev();
	
//	@Resource
//	private AppConfig appConfig;
//	@Autowired
//	private JFishPropertyPlaceholder configHolder;

	public HibernateEntityManagerImpl(){
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		Assert.notNull(appConfig, "appConfig can not be null.");
		/*boolean watchSqlFile = configHolder.getPropertiesWraper().getBoolean(FileNamedQueryFactory.WATCH_SQL_FILE);
		String db = JdbcUtils.getDataBase(dataSource).toString();
		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		FileNamedQueryFactory<HibernateNamedInfo> fq = new HibernateFileQueryManagerImpl(db, watchSqlFile, this, listener);
		fq.initQeuryFactory(this);
		this.fileNamedQueryFactory = fq;*/
		
		this.fileNamedQueryFactory = SpringUtils.getBean(applicationContext, FileNamedQueryFactory.class);
	}


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass){
		/*SQLQuery query = getSession().createSQLQuery(sqlString);
		if(entityClass!=null){
			if(isResultMappedToSingleColumnTransformer(entityClass))
				query.setResultTransformer(new SingleColumnTransformer(entityClass));
			else
				query.setResultTransformer(new RowToBeanTransformer(entityClass));
		}*/
		DataQuery dquery = createSQLQuery(sqlString, entityClass, true);
		return dquery;
	}

	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass, boolean stateless){
		SharedSessionContract session = getSession(stateless);
		SQLQuery query = session.createSQLQuery(sqlString);
		if(entityClass!=null){
			if(isResultMappedToSingleColumnTransformer(entityClass))
				query.setResultTransformer(new SingleColumnTransformer(entityClass));
			else
				query.setResultTransformer(new RowToBeanTransformer(entityClass));
		}
		DataQuery dquery = new HibernateQueryImpl(query);
		return dquery;
	}
	
	/***
	 * 根据返回对象的class判断是否映射到单列
	 * @param resultClass
	 * @return
	 */
	protected boolean isResultMappedToSingleColumnTransformer(Class<?> resultClass){
		return LangUtils.isSimpleType(resultClass) || NiceDate.class.isAssignableFrom(resultClass);
	}
	public DataQuery createEntitySQLQuery(String sqlString, Class<?>... entityClass){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		for(Class<?> e : entityClass){
			query.addEntity(e);
		}
		DataQuery dquery = new HibernateQueryImpl(query);
		return dquery;
	}
	
	public DataQuery createNamedQuery(String name){
		Query query = getSession().getNamedQuery(name);
		DataQuery dquery = new HibernateQueryImpl(query);
		return dquery;
	}
	
	public DataQuery createQuery(String ejbqlString){
		DataQuery dquery = new HibernateQueryImpl(getSession().createQuery(ejbqlString));
		return dquery;
	}
	
	public DataQuery createQuery(String ejbqlString, boolean statful){
		DataQuery dquery = new HibernateQueryImpl(getSession(statful).createQuery(ejbqlString));
		return dquery;
	}


	/***
	 * 根据id查找实体，如果没有，返回null
	 */
	public <T> T findById(Class<T> entityClass, Serializable id){
		if(!MyUtils.checkIdValid(id))
			return null;
		T entity = (T) getSession().get(entityClass, id);
		return entity;
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	/****
	 * 这里直接使用hibernate的load方法
	 * 而在service层则使用findById重新实现load方法。
	 */
	@Override
	public <T> T load(Class<T> entityClass, Serializable id) {
		this.checkEntityIdValid(id);
		T entity = (T)getSession().load(entityClass, id);
		/*T entity = (T) findById(entityClass, id);
		if(entity==null)
			throw new ServiceException("entity["+entityClass.getName()+"] is not exist. id:["+id+"]");
		*/
		return entity;
	}

	@Override
	public void persist(Object entity) {
		getSession().persist(entity);
	}
	@Override
	public void update(Object entity) {
		getSession().update(entity);
	}

	@Override
	public void remove(Object entity) {
		if(entity==null)
			return ;
		if(entity instanceof ILogicDeleteEntity){
			ILogicDeleteEntity le = (ILogicDeleteEntity) entity;
			le.deleted();
			getSession().update(le);
		}else{
			getSession().delete(entity);
		}
	}

	@Override
	public <T> T removeById(Class<T> entityClass, Serializable id) {
		T entity = findById(entityClass, id);
		if(entity!=null)
			remove(entity);
		return entity;
	}

	@Override
	public <T> T save(T entity) {
		/*try {
			getSession().saveOrUpdate(entity);
		} catch (SQLGrammarException e) {
			SQLException sqle = (SQLException)e.getCause();
			if(sqle.getErrorCode()==2289 && "42000".equals(sqle.getSQLState())){
				this.createSequence(entity.getClass());
				getSession().saveOrUpdate(entity);
			}
		}
		return entity;*/
		getSession().saveOrUpdate(entity);
		return entity;
	}
	
	public Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	public <T extends SharedSessionContract> T getSession(boolean statefull){
		if(statefull){
			return (T)this.sessionFactory.getCurrentSession();
		}else{
			return (T)this.sessionFactory.openStatelessSession();
		}
	}
	
	public EntityManagerProvider getEntityManagerProvider(){
		return EntityManagerProvider.Hibernate;
	}

	@Override
	public SequenceNameManager getSequenceNameManager() {
		return sequenceNameManager;
	}

	public void setSequenceNameManager(SequenceNameManager sequenceNameManager) {
		this.sequenceNameManager = sequenceNameManager;
	}

	@Override
	public DataQuery createMappingSQLQuery(String sqlString, String resultSetMapping) {
		throw new UnsupportedOperationException("unsupported createMappingSQLQuery");
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public <T> T merge(T entity) {
		return (T)getSession().merge(entity);
	}

	@Override
	public SessionFactory getRawManagerObject() {
		return sessionFactory;
	}
	

	@Override
	public FileNamedQueryFactory<?> getFileNamedQueryFactory() {
		return fileNamedQueryFactory;
	}
	
	public <R extends Service> R getService(Class<R> serviceRole){
		R service = getRawManagerObject(SessionFactoryImplementor.class).getServiceRegistry().getService(serviceRole);
		return service;
	}
	

	/*@Override
	public <T> Collection<T> saves(Collection<T> entities) {
		//optimize for TableGenerator strategy of batch insert
		T e = LangUtils.getFirst(entities);
		PersistentClass pc = getPersistentClass(e.getClass().getName());
		SimpleValue kv = (SimpleValue)pc.getIdentifier();
		Class<?> idStrategyClass = getConfiguration().getIdentifierGeneratorFactory().getIdentifierGeneratorClass(kv.getIdentifierGeneratorStrategy());
		if(TableGenerator.class.isAssignableFrom(idStrategyClass)){
			int size = entities.size();
			Pair<Long, Long> idRange = getTableGeneratedIdRange(e.getClass(), size);
			if(idRange.getValue1()-idRange.getValue0()+1<size)
				throw new BaseException("not enough ids for size["+size+"] : " + idRange.getValue0()+" to " + idRange.getValue1());
			
			long currentId = idRange.getValue0();
			Method writeMd = ReflectUtils.getPropertyDescriptor(e.getClass(), pc.getIdentifierProperty().getName()).getWriteMethod();
			for (T entity : entities) {
				ReflectUtils.invokeMethod(writeMd, entity, currentId);
				save(entity);
				currentId++;
			}
			return entities;
		}
		
		return super.saves(entities);
	}*/
	
	/*protected KeyValue getModelIdentifier(String entityName){
		PersistentClass model = getConfiguration().getClassMapping(entityName);
		if(model==null)
			throw new BaseException("no hibernate model found:" + entityName);
		return model.getIdentifier();
	}*/
	
	protected PersistentClass getPersistentClass(String entityName){
		PersistentClass model = getConfiguration().getClassMapping(entityName);
		if(model==null)
			throw new BaseException("no hibernate model found:" + entityName);
		return model;
	}
	
	protected Configuration getConfiguration(){
		Configuration cfg = localSessionFactoryBean.getConfiguration();
		return cfg;
	}
	
	public Pair<Long, Long> getTableGeneratedIdRange(Class<?> entityClass, int size){
		MutableIdentifierGeneratorFactory idgService = getService(MutableIdentifierGeneratorFactory.class);
		Assert.notNull(idgService, "id service not found !");
		
		SessionFactoryImplementor sf = (SessionFactoryImplementor)localSessionFactoryBean.getObject();
		PersistentClass model = getPersistentClass(entityClass.getName());
		SimpleValue kv = (SimpleValue)model.getIdentifier();
		SimpleValue sv = new SimpleValue(kv.getMappings());
		ReflectUtils.copy(kv, sv, CopyConfig.create().ignoreIfNoSetMethod()
									.throwIfError());
		
		
		Properties idconfig = new Properties();
		idconfig.putAll(kv.getIdentifierGeneratorProperties());
		idconfig.setProperty(TableGenerator.INCREMENT_PARAM, String.valueOf(size<1?1:size));
		idconfig.setProperty(TableGenerator.OPT_PARAM, StandardOptimizerDescriptor.POOLED_LO.getExternalName());
		sv.setIdentifierGeneratorProperties(idconfig);
		sv.getConstraintColumns().addAll(kv.getConstraintColumns());
		
		TableGenerator generator = (TableGenerator)sv.createIdentifierGenerator(
				getConfiguration().getIdentifierGeneratorFactory(),
				sf.getDialect(),
		        sf.getSettings().getDefaultCatalogName(),
		        sf.getSettings().getDefaultSchemaName(),
		        (RootClass) model
		);
		
		/*Properties config = new Properties();
		config.putAll(getService( ConfigurationService.class ).getSettings());
		config.setProperty( IdentifierGenerator.ENTITY_NAME, entityClass.getName());
		
		TableGenerator idg = (TableGenerator)idgService.createIdentifierGenerator(TableGenerator.class.getName(), LongType.INSTANCE, config);*/
		Long id = (Long)generator.generate((SessionImplementor)getSession(), null);
		
		return Pair.with(id, id+size-1);
	}

	
}
