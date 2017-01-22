package org.onetwo.dbm.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.dialet.DBDialect;
import org.onetwo.dbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.dbm.event.JFishDeleteEvent;
import org.onetwo.dbm.event.JFishDeleteEvent.DeleteType;
import org.onetwo.dbm.event.JFishEvent;
import org.onetwo.dbm.event.JFishEventAction;
import org.onetwo.dbm.event.JFishEventListener;
import org.onetwo.dbm.event.JFishEventSource;
import org.onetwo.dbm.event.JFishExtQueryEvent;
import org.onetwo.dbm.event.JFishExtQueryEvent.ExtQueryType;
import org.onetwo.dbm.event.JFishFindEvent;
import org.onetwo.dbm.event.JFishInsertEvent;
import org.onetwo.dbm.event.JFishInsertOrUpdateEvent;
import org.onetwo.dbm.event.JFishUpdateEvent;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.jdbc.DbmJdbcOperations;
import org.onetwo.dbm.jdbc.DbmJdbcTemplate;
import org.onetwo.dbm.jdbc.DbmNamedJdbcTemplate;
import org.onetwo.dbm.jdbc.JdbcDao;
import org.onetwo.dbm.jdbc.NamedJdbcTemplate;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.query.JFishDataQuery;
import org.onetwo.dbm.query.JFishQuery;
import org.onetwo.dbm.query.JFishQueryImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;


/****
 * 基于jdbc的数据访问基类
 * 
 * @author weishao
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DbmDaoImpl extends JdbcDao implements JFishEventSource, DbmDao {

	private DBDialect dialect;
	private MappedEntryManager mappedEntryManager;
//	private ApplicationContext applicationContext;
//	private FileNamedQueryFactory fileNamedQueryFactory;
	
	private SQLSymbolManager sqlSymbolManager;
	
	
//	private EntityManagerOperationImpl entityManagerWraper;
	private SequenceNameManager sequenceNameManager;
	
	private DefaultDatabaseDialetManager databaseDialetManager;
	protected DbmConfig dataBaseConfig;
	
//	protected String[] packagesToScan;
	
	private SimpleDbmInnerServiceRegistry serviceRegistry;
	
	/*public JFishDaoImpl(){
	}*/

	public DbmDaoImpl(DataSource dataSource){
		Assert.notNull(dataSource);
		this.serviceRegistry = SimpleDbmInnerServiceRegistry.createServiceRegistry(dataSource, null);
		this.setDataSource(dataSource);
	}

	public DbmDaoImpl(DataSource dataSource, DBDialect dialect){
		Assert.notNull(dataSource);
		Assert.notNull(dialect);
		this.serviceRegistry = SimpleDbmInnerServiceRegistry.createServiceRegistry(dataSource, null);
		this.setDataSource(dataSource);
	}
	
	@Override
	protected DbmJdbcOperations createJdbcTemplate(DataSource dataSource) {
		return new DbmJdbcTemplate(dataSource, serviceRegistry.getJdbcParameterSetter());
	}

	@Override
	protected NamedJdbcTemplate createNamedJdbcTemplate(DataSource dataSource) {
		DbmNamedJdbcTemplate template = new DbmNamedJdbcTemplate(getJdbcTemplate());
		template.setJdbcParameterSetter(serviceRegistry.getJdbcParameterSetter());
		return template;
	}

	@Override
	protected void checkDaoConfig() {
		Assert.notNull(getDataSource());
//		Assert.notNull(dialect);
//		Assert.notNull(databaseDialetManager);
		super.checkDaoConfig();
	}

	@Override
	protected void initDao() throws Exception {
		/*this.serviceRegistry = new SimpleDbmInnserServiceRegistry();
		this.serviceRegistry.initialize(getDataSource(), packagesToScan);*/
		Assert.notNull(serviceRegistry);
		
		this.dataBaseConfig = this.serviceRegistry.getDataBaseConfig();
		this.databaseDialetManager = this.serviceRegistry.getDatabaseDialetManager();
		this.dialect = this.serviceRegistry.getDialect();
		this.mappedEntryManager = this.serviceRegistry.getMappedEntryManager();
		this.sqlSymbolManager = this.serviceRegistry.getSqlSymbolManager();
		this.setRowMapperFactory(this.serviceRegistry.getRowMapperFactory());
		this.sequenceNameManager = this.serviceRegistry.getSequenceNameManager();
		
		/*if(dataBaseConfig==null){
			dataBaseConfig = new DefaultDataBaseConfig();
		}
		if(databaseDialetManager==null){
			this.databaseDialetManager = new DefaultDatabaseDialetManager();
			this.databaseDialetManager.register(DataBase.MySQL.getName(), new MySQLDialect());
			this.databaseDialetManager.register(DataBase.Oracle.getName(), new OracleDialect());
		}
		
		
//		super.initDao();
		if(this.dialect==null){
			DBMeta dbmeta = DbmetaFetcher.create(getDataSource()).getDBMeta();
//			this.dialect = JFishSpringUtils.getMatchDBDiaclet(applicationContext, dbmeta);
			this.dialect = this.databaseDialetManager.getRegistered(dbmeta.getDbName());
			if (this.dialect == null) {
				throw new IllegalArgumentException("'dialect' is required");
			}
//			LangUtils.cast(dialect, InnerDBDialet.class).setDbmeta(dbmeta);
			this.dialect.getDbmeta().setVersion(dbmeta.getVersion());
			this.dialect.initialize();
		}
		
		//mappedEntryManager
		if(mappedEntryManager==null){
			this.mappedEntryManager = new MutilMappedEntryManager(this.dialect);
			this.mappedEntryManager.initialize();
		}
		if(ArrayUtils.isNotEmpty(packagesToScan)){
			mappedEntryManager.scanPackages(packagesToScan);
		}
		
		//init sql symbol
		if(sqlSymbolManager==null){
			JFishSQLSymbolManagerImpl newSqlSymbolManager = JFishSQLSymbolManagerImpl.create();
//			newSqlSymbolManager.setDialect(dialect);
			newSqlSymbolManager.setMappedEntryManager(mappedEntryManager);
			newSqlSymbolManager.setListeners(Arrays.asList(new DataQueryFilterListener()));
			this.sqlSymbolManager = newSqlSymbolManager;
		}
		
//		this.mappedEntryManager = SpringUtils.getHighestOrder(applicationContext, MappedEntryManager.class);
		this.setRowMapperFactory(new JFishRowMapperFactory(mappedEntryManager));

		if(this.sequenceNameManager==null){
			this.sequenceNameManager = new JFishSequenceNameManager();
		}*/
//		this.entityManagerWraper = new EntityManagerOperationImpl(this, sequenceNameManager);
	}
	

	public DefaultDatabaseDialetManager getDatabaseDialetManager() {
		return databaseDialetManager;
	}

	public void setDatabaseDialetManager(
			DefaultDatabaseDialetManager databaseDialetManager) {
		this.databaseDialetManager = databaseDialetManager;
	}

	public void setMappedEntryManager(MappedEntryManager mappedEntryManager) {
		this.mappedEntryManager = mappedEntryManager;
	}

	public void setSqlSymbolManager(SQLSymbolManager sqlSymbolManager) {
		this.sqlSymbolManager = sqlSymbolManager;
	}

	public void setDataBaseConfig(DbmConfig dataBaseConfig) {
		this.dataBaseConfig = dataBaseConfig;
	}

	public DbmConfig getDataBaseConfig() {
		return dataBaseConfig;
	}

	public <T> int save(T entity){
		return this.insertOrUpdate(entity, true);
	}
	

	public <T> int insertOrUpdate(T entity, boolean dymanicIfUpdate){
		if(LangUtils.isNullOrEmptyObject(entity))
			throw new DbmException("entity can not be null or empty: " + entity);
		JFishInsertOrUpdateEvent event = new JFishInsertOrUpdateEvent(entity, dymanicIfUpdate, this);
//		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}
	
	public <T> int insert(T entity){
		return insert(entity, true);
	}
	
	protected <T> int insert(T entity, boolean fetchId){
		Assert.notNull(entity);
		JFishInsertEvent event = new JFishInsertEvent(entity, this);
		event.setFetchId(fetchId);
//		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}

	/*public <T> int saveRef(T entity){
		return saveRef(entity, false);
	}*/

	/*public <T> int saveRef(T entity, boolean dropAllInFirst){
		Assert.notNull(entity, "entity can not be null");
		Assert.notEmpty(relatedFields, "relatedFields can not be empty");
		JFishSaveRefEvent event = new JFishSaveRefEvent(entity, dropAllInFirst, this);
		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}*/

	/*public <T> int dropRef(T entity){
		Assert.notNull(entity, "entity can not be null");
		Assert.notEmpty(relatedFields, "relatedFields can not be empty");
		JFishDropRefEvent event = new JFishDropRefEvent(entity, this);
		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}*/

	/*public <T> int clearRef(T entity){
		Assert.notNull(entity, "entity can not be null");
//		Assert.notEmpty(relatedFields, "relatedFields can not be empty");
		JFishDropRefEvent event = new JFishDropRefEvent(entity, true, this);
//		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}*/
	
	@Override
	public <T> int justInsert(T entity){
		return insert(entity, false);
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.fish.spring.JFishDao#batchInsert(java.util.List)
	 */
	@Override
	public <T> int batchInsert(Collection<T> entities){
//		Assert.notNull(entities);
		JFishInsertEvent event = new JFishInsertEvent(entities, this);
		event.setAction(JFishEventAction.batchInsert);
//		this.fireBatchInsertEvent(event);
		this.fireEvents(event);
		return event.getUpdateCount();
	}
	
	public <T> int batchUpdate(Collection<T> entities){
		JFishUpdateEvent event = new JFishUpdateEvent(entities, this);
		event.setDynamicUpdate(false);
		event.setAction(JFishEventAction.batchUpdate);
		this.fireEvents(event);
		return event.getUpdateCount();
	}
	
	/*protected void fireEvents(JFishEventListener[] listeners, JFishEvent event){
		for(JFishEventListener listern : listeners){
			listern.doEvent(event);
		}
	}*/
	
	protected void fireEvents(JFishEvent event){
		JFishEventListener[] listeners = dialect.getJfishdbEventListenerManager().getListeners(event.getAction());
		for(JFishEventListener listern : listeners){
			listern.doEvent(event);
		}
	}
	
	@Override
	public int update(Object entity){
		return update(entity, false);
	}
	
	@Override
	public int dymanicUpdate(Object entity){
		return update(entity, true);
	}
	
	@Override
	public int update(Object entity, boolean dymanicUpdate){
		Assert.notNull(entity);
		JFishUpdateEvent event = new JFishUpdateEvent(entity, this);
		event.setDynamicUpdate(dymanicUpdate);
//		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}
	
	public int delete(Object entity){
		Assert.notNull(entity);
		JFishDeleteEvent deleteEvent = new JFishDeleteEvent(entity, this);
//		deleteEvent.setRelatedFields(relatedFields);
		this.fireEvents(deleteEvent);
		return deleteEvent.getUpdateCount();
	}
	
	public int delete(Class<?> entityClass, Object id){
		Assert.notNull(id);
		JFishDeleteEvent deleteEvent = new JFishDeleteEvent(id, this);
		deleteEvent.setEntityClass(entityClass);
		deleteEvent.setDeleteType(DeleteType.byIdentify);
		this.fireEvents(deleteEvent);
		return deleteEvent.getUpdateCount();
	}
	
	public int deleteAll(Class<?> entityClass){
		JFishDeleteEvent deleteEvent = new JFishDeleteEvent(null, this);
		deleteEvent.setEntityClass(entityClass);
		deleteEvent.setDeleteType(DeleteType.deleteAll);
		this.fireEvents(deleteEvent);
		return deleteEvent.getUpdateCount();
	}
	
	@Override
	public <T> T findById(Class<T> entityClass, Serializable id){
		JFishFindEvent event = new JFishFindEvent(id, this);
		event.setEntityClass(entityClass);
		this.fireEvents(event);
		return (T)event.getResultObject();
	}
	

	public <T> List<T> findAll(Class<T> entityClass){
		JFishFindEvent event = new JFishFindEvent(null, this);
		event.setFindAll(true);
		event.setEntityClass(entityClass);
		this.fireEvents(event);
		return (List<T>)event.getResultObject();
	}
	
	public <T> List<T> findByProperties(Class<T> entityClass, Map<Object, Object> properties){
		JFishExtQueryEvent event = new JFishExtQueryEvent(ExtQueryType.DEFUALT, entityClass, properties, this);
		this.fireEvents(event);
		return (List<T>)event.getResultObject();
	}
	
	public void findPageByProperties(Class<?> entityClass, Page<?> page, Map<Object, Object> properties){
		JFishExtQueryEvent event = new JFishExtQueryEvent(page, ExtQueryType.PAGINATION, entityClass, properties, this);
		this.fireEvents(event);
	}
	
	/***
	 * 查找唯一记录，如果找不到返回null，如果多于一条记录，抛出异常。
	 */
	public <T> T findUniqueByProperties(Class<T> entityClass, Map<Object, Object> properties){
		JFishExtQueryEvent event = new JFishExtQueryEvent(ExtQueryType.UNIQUE, entityClass, properties, this);
		this.fireEvents(event);
		return (T)event.getResultObject();
	}
	
	public Number countByProperties(Class<?> entityClass, Map<Object, Object> properties){
		JFishExtQueryEvent event = new JFishExtQueryEvent(ExtQueryType.COUNT, entityClass, properties, this);
		this.fireEvents(event);
		return (Number)event.getResultObject();
	}
	
	/*@Override
	public <T> List<T> queryList(Object queryableEntity){
		JFishQueryableEvent event = new JFishQueryableEvent(queryableEntity, this);
		this.fireEvents(dialect.getQueryableEventListeners(), event);
		return (List<T>)event.getResultObject();
	}*/
	
	public <T> T findUnique(String sql, Map<String, ?> params, Class<T> type){
		return findUnique(sql, params, getDefaultRowMapper(type, true));
	}
	
	public <T> T findUnique(String sql, Map<String, ?> params, RowMapper<T> rowMapper){
		T result = null;
		try{
			result = this.getNamedParameterJdbcTemplate().queryForObject(sql, params, rowMapper);
		}catch(EmptyResultDataAccessException e){
			logger.error("findUnique : "+e.getMessage());
		}
		return result;
	}
	
	public <T> T findUnique(String sql, Object[] args, Class<T> type){
		return findUnique(sql, args, getDefaultRowMapper(type, true));
	}
	
	public <T> T findUnique(DbmQueryValue queryValue){
		/*if(queryValue.isPosition()){
			return findUnique(queryValue.getSql(), queryValue.asList().toArray(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), true));
		}else{
			return findUnique(queryValue.getSql(), queryValue.asMap(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), true));
		}*/
		return findUnique(queryValue.getSql(), queryValue.asMap(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), true));
	}
	
	public <T> T findUnique(DbmQueryValue queryValue, RowMapper<T> row){
		/*if(queryValue.isPosition()){
			return findUnique(queryValue.getSql(), queryValue.asList().toArray(), row);
		}else{
			return findUnique(queryValue.getSql(), queryValue.asMap(), row);
		}*/
		return findUnique(queryValue.getSql(), queryValue.asMap(), row);
	}
	
	public Number count(DbmQueryValue queryValue){
		Number count = null;
		SingleColumnRowMapper mapper = new SingleColumnRowMapper<Number>(Number.class);
		/*if(queryValue.isPosition()){
			count = (Number)findUnique(queryValue.getCountSql(), queryValue.asList().toArray(), mapper);
		}else{
			count = (Number)findUnique(queryValue.getCountSql(), queryValue.asMap(), mapper);
		}*/
		count = (Number)findUnique(queryValue.getCountSql(), queryValue.asMap(), mapper);
		return count;
	}
	
	public <T> T findUnique(DynamicQuery query){
		return createJFishQuery(query).getSingleResult();
	}
	
	public <T> T findUnique(String sql, Object[] args, RowMapper<T> row){
		T result = null;
		try{
			result = this.getJFishJdbcTemplate().queryForObject(sql, args, row);
		}catch(EmptyResultDataAccessException e){
			logger.warn("findUnique : "+e.getMessage());
		}
		return result;
	}
	
	public <T> List<T> findList(String sql, Object[] args, RowMapper<T> rowMapper){
		List<T> result = null;
		try {
			result = this.getJFishJdbcTemplate().query(sql, args, rowMapper);
		} catch (Exception e) {
			handleException("findList", sql, e);
		}
		return result;
	}
	
	protected void handleException(String tag, String msg, Exception e){
		StringBuilder newMsg = new StringBuilder();
		newMsg.append(tag).append(" error : ");
		if(e instanceof ClassCastException){
			newMsg.append("may be the query result type mapped error, check it.");
		}
		newMsg.append("[").append(msg).append("]");
		throw new DbmException(newMsg.toString(), e);
	}
	
	public <T> List<T> findList(DynamicQuery query){
		return createJFishQuery(query).getResultList();
	}
	
	public <T> List<T> findList(String sql, Object[] args, Class<T> type){
		return findList(sql, args, getDefaultRowMapper(type, false));
	}
	
	public <T> List<T> findList(String sql, Map<String, ?> params, RowMapper<T> rowMapper){
		List<T> result = this.getNamedParameterJdbcTemplate().query(sql, params, rowMapper);
		return result;
	}
	
	public <T> List<T> findList(String sql, Map<String, ?> params, Class<T> type){
		return findList(sql, params, getDefaultRowMapper(type, false));
	}
	
	public <T> List<T> findList(DbmQueryValue queryValue){
		/*if(queryValue.isPosition()){
			return findList(queryValue.getSql(), queryValue.asList().toArray(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), false));
		}else{
			return findList(queryValue.getSql(), queryValue.asMap(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), false));
		}*/
		return findList(queryValue.getSql(), queryValue.asMap(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), false));
	}
	
	public <T> void findPage(Page<T> page, DbmQueryValue queryValue){
		long totalCount = count(queryValue).longValue();
		page.setTotalCount(totalCount);
		if(totalCount<1)
			return ;
//		List<T> results = findList(queryValue);a
		JFishQuery jq = createJFishQuery(queryValue.getSql(), queryValue.getResultClass());
		/*if(queryValue.isNamed()){
			jq.setParameters(queryValue.asMap());
		}else{
			jq.setParameters(queryValue.asList());
		}*/
		jq.setParameters(queryValue.asMap());
		List<T> results = jq.setFirstResult(page.getFirst()-1).setMaxResults(page.getPageSize()).getResultList();
		page.setResult(results);
	}
	
	public <T> T find(DbmQueryValue queryValue, ResultSetExtractor<T> rse){
		/*if(queryValue.isPosition()){
			return this.getJdbcTemplate().query(queryValue.getSql(), queryValue.asList().toArray(), rse);
		}else{
			return this.getNamedParameterJdbcTemplate().query(queryValue.getSql(), queryValue.asMap(), rse);
		}*/
		return this.getNamedParameterJdbcTemplate().query(queryValue.getSql(), queryValue.asMap(), rse);
	}
	
	/****
	 * Extractor: RowMapperResultSetExtractor
	 */
	public <T> List<T> findList(DbmQueryValue queryValue, RowMapper<T> rowMapper){
		/*if(queryValue.isPosition()){
			return this.getJdbcTemplate().query(queryValue.getSql(), queryValue.asList().toArray(), rowMapper);
		}else{
			return this.getNamedParameterJdbcTemplate().query(queryValue.getSql(), queryValue.asMap(), rowMapper);
		}*/
		return this.getNamedParameterJdbcTemplate().query(queryValue.getSql(), queryValue.asMap(), rowMapper);
	}
	
	public int executeUpdate(DbmQueryValue queryValue){
		int update = 0;
		/*if(queryValue.isNamed()){
			update = this.getNamedParameterJdbcTemplate().update(queryValue.getSql(), queryValue.asMap());
		}else{
			update = this.getJFishJdbcTemplate().update(queryValue.getSql(), queryValue.asList().toArray());
		}*/
		update = this.getNamedParameterJdbcTemplate().update(queryValue.getSql(), queryValue.asMap());
		return update;
	}
	
	public int executeUpdate(String sql, Map<String, ?> params){
		int update = this.getNamedParameterJdbcTemplate().update(sql, params);
		return update;
	}
	
	public int executeUpdate(String sql, Object...args){
		int update = this.getJFishJdbcTemplate().update(sql, args);
		return update;
	}
	
	public int executeUpdate(DynamicQuery query){
		int update = this.getJFishJdbcTemplate().update(query.getTransitionSql(), query.getValues().toArray());
		return update;
	}
	
	public JFishQuery createJFishQuery(String sql){
		return createJFishQuery(sql, null);
	}
	
	public JFishQuery createJFishQuery(String sql, Class<?> entityClass){
		return new JFishQueryImpl(this, sql, entityClass);
	}
	
	public JFishQuery createJFishQuery(DynamicQuery query){
		query.compile();
		JFishQuery jq = createJFishQuery(query.getTransitionSql(), query.getEntityClass());
		jq.setParameters(query.getValues());
		if(query.isPage()){
			jq.setFirstResult(query.getFirstRecord());
			jq.setMaxResults(query.getMaxRecords());
		}
		return jq;
	}
	
	public SelectExtQuery createExtQuery(Class<?> entityClass, Map<Object, Object> properties){
		SelectExtQuery q = this.getSqlSymbolManager().createSelectQuery(entityClass, "ent", properties);
		return q;
	}
	
	public DataQuery createAsDataQuery(SelectExtQuery extQuery){
		DataQuery q = null;
		/*if(extQuery.isSqlQuery()){
			q = this.createAsDataQuery(extQuery.getSql(), extQuery.getEntityClass());
			q.setParameters((List)extQuery.getParamsValue().getValues());
		}else{
			q = this.createAsDataQuery(extQuery.getSql(), (Map)extQuery.getParamsValue().getValues());
		}*/
		q = this.createAsDataQuery(extQuery.getSql(), (Map)extQuery.getParamsValue().getValues());
		
		JFishQuery jq = q.getRawQuery(JFishQuery.class);
		jq.setResultClass(extQuery.getEntityClass());
		if(extQuery.needSetRange()){
			q.setLimited(extQuery.getFirstResult(), extQuery.getMaxResults());
		}
		q.setQueryConfig(extQuery.getQueryConfig());
		return q;
	}
	
	public DataQuery createAsDataQuery(String sqlString, Class entityClass) {
		JFishQuery jq = createJFishQuery(sqlString, entityClass);
		DataQuery query = new JFishDataQuery(jq);
		return query;
	}
	
	public DataQuery createAsDataQuery(String sql, Map<String, Object> values){
		DataQuery q = createAsDataQuery(sql, (Class)null);
		q.setParameters(values);
		return q;
	}

	public <T> RowMapper<T> getDefaultRowMapper(Class<T> type){
		return getDefaultRowMapper(type, false);
	}
	
	protected <T> RowMapper<T> getDefaultRowMapper(Class<T> type, boolean unique){
		return (RowMapper<T>)this.getRowMapperFactory().createDefaultRowMapper(type);
	}

	/*protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new JFishJdbcTemplate(dataSource){
			public boolean isPrintSql(){
				return dialect.isPrintSql();
			}
		};
	}*/

	public final DbmJdbcOperations getJFishJdbcTemplate() {
	  return this.jdbcTemplate;
	}

	protected RowMapper getColumnMapRowMapper() {
		return new ColumnMapRowMapper();
	}

	public MappedEntryManager getMappedEntryManager() {
		return this.mappedEntryManager;
	}

	public DBDialect getDialect() {
		return dialect;
	}

	/*
	protected DBMeta getDBMeta(){
		Connection dbcon = null;
		DBMeta dbmeta = new DBMeta();
		try {
			dbcon = getConnection();
			DatabaseMetaData meta = dbcon.getMetaData();
			dbmeta.setDbName(meta.getDatabaseProductName());
			dbmeta.setVersion(meta.getDatabaseProductVersion());
			logger.info("database : " + dbmeta);
		} catch (Exception e) {
			LangUtils.throwBaseException("initialize error : " + e.getMessage() , e);
		} finally{
			releaseConnection(dbcon);
		}
		return dbmeta;
	}*/


	public SQLSymbolManager getSqlSymbolManager() {
		return sqlSymbolManager;
	}

//	public EntityManagerOperationImpl getEntityManagerWraper() {
//		return entityManagerWraper;
//	}

	public SequenceNameManager getSequenceNameManager() {
		return sequenceNameManager;
	}

	@Override
	public NamedJdbcTemplate getNamedParameterJdbcTemplate() {
		return this.namedParameterJdbcTemplate;
	}

	/*public void setPackagesToScan(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}*/

	public SimpleDbmInnerServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(SimpleDbmInnerServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

}
