package org.onetwo.common.fish.spring;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.SelectExtQuery;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.HqlSymbolParser;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.fish.JFishDataQuery;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.JFishQueryImpl;
import org.onetwo.common.fish.JFishSQLSymbolManagerImpl;
import org.onetwo.common.fish.event.JFishDeleteEvent;
import org.onetwo.common.fish.event.JFishDeleteEvent.DeleteType;
import org.onetwo.common.fish.event.JFishDropRefEvent;
import org.onetwo.common.fish.event.JFishEvent;
import org.onetwo.common.fish.event.JFishEventAction;
import org.onetwo.common.fish.event.JFishEventListener;
import org.onetwo.common.fish.event.JFishEventSource;
import org.onetwo.common.fish.event.JFishExtQueryEvent;
import org.onetwo.common.fish.event.JFishExtQueryEvent.ExtQueryType;
import org.onetwo.common.fish.event.JFishFindEvent;
import org.onetwo.common.fish.event.JFishInsertEvent;
import org.onetwo.common.fish.event.JFishInsertOrUpdateEvent;
import org.onetwo.common.fish.event.JFishSaveRefEvent;
import org.onetwo.common.fish.event.JFishUpdateEvent;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.jpa.JFishSequenceNameManager;
import org.onetwo.common.fish.orm.AbstractDBDialect.DBMeta;
import org.onetwo.common.fish.orm.DBDialect;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.jdbc.JFishJdbcOperations;
import org.onetwo.common.jdbc.JdbcDaoSupport;
import org.onetwo.common.jdbc.NamedJdbcTemplate;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class JFishDaoImpl extends JdbcDaoSupport implements JFishEventSource, JFishDao, ApplicationContextAware {

	private DBDialect dialect;
	private MappedEntryManager mappedEntryManager;
	private ApplicationContext applicationContext;
//	private FileNamedQueryFactory fileNamedQueryFactory;
	
	private SQLSymbolManager sqlSymbolManager;
	
	
//	private EntityManagerOperationImpl entityManagerWraper;
	private SequenceNameManager sequenceNameManager;
	
	
	public JFishDaoImpl(){
	}

	public JFishDaoImpl(DataSource dataSource){
		this.setDataSource(dataSource);
//		this.afterPropertiesSet();
	}
	
	



	public <T> int save(T entity, String... relatedFields){
		return this.insertOrUpdate(entity, true, relatedFields);
	}
	

	public <T> int insertOrUpdate(T entity, boolean dymanicIfUpdate, String... relatedFields){
		if(LangUtils.isNullOrEmptyObject(entity))
			return 0;
		JFishInsertOrUpdateEvent event = new JFishInsertOrUpdateEvent(entity, dymanicIfUpdate, this);
		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}
	
	public <T> int insert(T entity, String... relatedFields){
		return insert(entity, true, relatedFields);
	}
	
	protected <T> int insert(T entity, boolean fetchId, String... relatedFields){
		Assert.notNull(entity);
		JFishInsertEvent event = new JFishInsertEvent(entity, this);
		event.setFetchId(fetchId);
		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}

	public <T> int saveRef(T entity, String... relatedFields){
		return saveRef(entity, false, relatedFields);
	}

	public <T> int saveRef(T entity, boolean dropAllInFirst, String... relatedFields){
		Assert.notNull(entity, "entity can not be null");
		Assert.notEmpty(relatedFields, "relatedFields can not be empty");
		JFishSaveRefEvent event = new JFishSaveRefEvent(entity, dropAllInFirst, this);
		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}

	public <T> int dropRef(T entity, String... relatedFields){
		Assert.notNull(entity, "entity can not be null");
		Assert.notEmpty(relatedFields, "relatedFields can not be empty");
		JFishDropRefEvent event = new JFishDropRefEvent(entity, this);
		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}

	public <T> int clearRef(T entity, String... relatedFields){
		Assert.notNull(entity, "entity can not be null");
		Assert.notEmpty(relatedFields, "relatedFields can not be empty");
		JFishDropRefEvent event = new JFishDropRefEvent(entity, true, this);
		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}
	
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
		JFishEventListener[] listeners = dialect.getDbEventListenerManager().getJFishEventListener(event.getAction());
		for(JFishEventListener listern : listeners){
			listern.doEvent(event);
		}
	}
	
	@Override
	public int update(Object entity, String... relatedFields){
		return update(entity, false, relatedFields);
	}
	
	@Override
	public int dymanicUpdate(Object entity, String... relatedFields){
		return update(entity, true, relatedFields);
	}
	
	@Override
	public int update(Object entity, boolean dymanicUpdate, String... relatedFields){
		Assert.notNull(entity);
		JFishUpdateEvent event = new JFishUpdateEvent(entity, this);
		event.setDynamicUpdate(dymanicUpdate);
		event.setRelatedFields(relatedFields);
		this.fireEvents(event);
		return event.getUpdateCount();
	}
	
	public int delete(Object entity, String... relatedFields){
		Assert.notNull(entity);
		JFishDeleteEvent deleteEvent = new JFishDeleteEvent(entity, this);
		deleteEvent.setRelatedFields(relatedFields);
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
	
	public <T> T findUnique(JFishQueryValue queryValue){
		if(queryValue.isPosition()){
			return findUnique(queryValue.getSql(), queryValue.asList().toArray(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), true));
		}else{
			return findUnique(queryValue.getSql(), queryValue.asMap(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), true));
		}
	}
	
	public <T> T findUnique(JFishQueryValue queryValue, RowMapper<T> row){
		if(queryValue.isPosition()){
			return findUnique(queryValue.getSql(), queryValue.asList().toArray(), row);
		}else{
			return findUnique(queryValue.getSql(), queryValue.asMap(), row);
		}
	}
	
	public Number count(JFishQueryValue queryValue){
		Number count = null;
		SingleColumnRowMapper mapper = new SingleColumnRowMapper<Number>(Number.class);
		if(queryValue.isPosition()){
			count = (Number)findUnique(queryValue.getCountSql(), queryValue.asList().toArray(), mapper);
		}else{
			count = (Number)findUnique(queryValue.getCountSql(), queryValue.asMap(), mapper);
		}
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
		throw new JFishOrmException(newMsg.toString(), e);
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
	
	public <T> List<T> findList(JFishQueryValue queryValue){
		if(queryValue.isPosition()){
			return findList(queryValue.getSql(), queryValue.asList().toArray(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), false));
		}else{
			return findList(queryValue.getSql(), queryValue.asMap(), (RowMapper<T>)getDefaultRowMapper(queryValue.getResultClass(), false));
		}
	}
	
	public <T> void findPage(Page<T> page, JFishQueryValue queryValue){
		long totalCount = count(queryValue).longValue();
		page.setTotalCount(totalCount);
		if(totalCount<1)
			return ;
//		List<T> results = findList(queryValue);a
		JFishQuery jq = createJFishQuery(queryValue.getSql(), queryValue.getResultClass());
		if(queryValue.isNamed()){
			jq.setParameters(queryValue.asMap());
		}else{
			jq.setParameters(queryValue.asList());
		}
		List<T> results = jq.setFirstResult(page.getFirst()-1).setMaxResults(page.getPageSize()).getResultList();
		page.setResult(results);
	}
	
	public <T> T find(JFishQueryValue queryValue, ResultSetExtractor<T> rse){
		if(queryValue.isPosition()){
			return this.getJdbcTemplate().query(queryValue.getSql(), queryValue.asList().toArray(), rse);
		}else{
			return this.getNamedParameterJdbcTemplate().query(queryValue.getSql(), queryValue.asMap(), rse);
		}
	}
	
	public <T> List<T> findList(JFishQueryValue queryValue, RowMapper<T> rowMapper){
		if(queryValue.isPosition()){
			return this.getJdbcTemplate().query(queryValue.getSql(), queryValue.asList().toArray(), rowMapper);
		}else{
			return this.getNamedParameterJdbcTemplate().query(queryValue.getSql(), queryValue.asMap(), rowMapper);
		}
	}
	
	public int executeUpdate(JFishQueryValue queryValue){
		int update = 0;
		if(queryValue.isNamed()){
			update = this.getNamedParameterJdbcTemplate().update(queryValue.getSql(), queryValue.asMap());
		}else{
			update = this.getJFishJdbcTemplate().update(queryValue.getSql(), queryValue.asList().toArray());
		}
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
		if(extQuery.isSqlQuery()){
			q = this.createAsDataQuery(extQuery.getSql(), extQuery.getEntityClass());
			q.setParameters((List)extQuery.getParamsValue().getValues());
		}else{
			q = this.createAsDataQuery(extQuery.getSql(), (Map)extQuery.getParamsValue().getValues());
		}
		JFishQuery jq = q.getRawQuery();
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

	public final JFishJdbcOperations getJFishJdbcTemplate() {
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

	public void setDialect(DBDialect dialect) {
		this.dialect = dialect;
	}

	
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
	}

	@Override
	protected void initDao() throws Exception {
//		super.initDao();
		if(this.dialect==null){
			DBMeta dbmeta = getDBMeta();
			this.dialect = JFishSpringUtils.getMatchDBDiaclet(applicationContext, dbmeta);
			if (this.dialect == null) {
				throw new IllegalArgumentException("'dialect' is required");
			}
		}
		this.mappedEntryManager = SpringUtils.getHighestOrder(applicationContext, MappedEntryManager.class);
		this.setRowMapperFactory(new JFishRowMapperFactory(mappedEntryManager));

		this.initSQLSymbolManager(dialect, mappedEntryManager);
		
		if(this.sequenceNameManager==null){
			this.sequenceNameManager = new JFishSequenceNameManager();
		}
//		this.entityManagerWraper = new EntityManagerOperationImpl(this, sequenceNameManager);
		
	}
	
	private void initSQLSymbolManager(DBDialect dialect, MappedEntryManager mappedEntryManager){
		JFishSQLSymbolManagerImpl newSqlSymbolManager = JFishSQLSymbolManagerImpl.create();
		newSqlSymbolManager.setDialect(dialect);
		newSqlSymbolManager.setMappedEntryManager(mappedEntryManager);
		List<HqlSymbolParser> sqlSymbolParsers = SpringUtils.getBeans(applicationContext, HqlSymbolParser.class);
		for(HqlSymbolParser parser : sqlSymbolParsers){
			newSqlSymbolManager.register(parser);
		}
		this.sqlSymbolManager = newSqlSymbolManager;
	}

	@Override
	protected void checkDaoConfig() {
		super.checkDaoConfig();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	public SQLSymbolManager getSqlSymbolManager() {
		return sqlSymbolManager;
	}

//	public EntityManagerOperationImpl getEntityManagerWraper() {
//		return entityManagerWraper;
//	}

	public SequenceNameManager getSequenceNameManager() {
		return sequenceNameManager;
	}

	public void setSequenceNameManager(SequenceNameManager sequenceNameManager) {
		this.sequenceNameManager = sequenceNameManager;
	}

	@Override
	public NamedJdbcTemplate getNamedParameterJdbcTemplate() {
		return this.namedParameterJdbcTemplate;
	}



}
