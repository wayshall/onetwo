package org.onetwo.dbm.dialet;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.dbm.event.DbmBatchInsertEventListener;
import org.onetwo.dbm.event.DbmBatchUpdateEventListener;
import org.onetwo.dbm.event.DbmDeleteEventListener;
import org.onetwo.dbm.event.DbmEventAction;
import org.onetwo.dbm.event.DbmExtQueryEventListener;
import org.onetwo.dbm.event.DbmFindEventListener;
import org.onetwo.dbm.event.DbmInsertEventListener;
import org.onetwo.dbm.event.DbmInsertOrUpdateListener;
import org.onetwo.dbm.event.DbmUpdateEventListener;
import org.onetwo.dbm.event.DbmdbEventListenerManager;
import org.onetwo.dbm.mapping.DbmTypeMapping;
import org.onetwo.dbm.mapping.DefaultSQLBuilderFactory;
import org.onetwo.dbm.mapping.SQLBuilderFactory;


abstract public class AbstractDBDialect implements InnerDBDialet, DBDialect {

	
//	protected MappedEntryManager mappedEntryManager;
	protected DBMeta dbmeta;
//	protected DataBaseConfig dataBaseConfig;
	
	private List<StrategyType> idStrategy = new ArrayList<StrategyType>();
	private boolean autoDetectIdStrategy;

//	protected JFishQueryableEventListener[] queryableEventListeners;
	protected DbmdbEventListenerManager jfishdbEventListenerManager;
	
	private SQLBuilderFactory sqlBuilderFactory;
	
	private DbmTypeMapping typeMapping;
	
//	protected ApplicationContext applicationContext;
//	private boolean printSql = false;
	
	
	public AbstractDBDialect(DBMeta dbmeta) {
		super();
		this.dbmeta = dbmeta;
		this.typeMapping = new DbmTypeMapping();
//		this.dataBaseConfig = dataBaseConfig;
	}

	final protected void setSqlTypeMapping(DbmTypeMapping sqlTypeMapping) {
		this.typeMapping = sqlTypeMapping;
	}

	public DbmTypeMapping getTypeMapping() {
		return typeMapping;
	}

	@PostConstruct
	public void initialize() {
		/*if(this.dataBaseConfig==null){
			this.dataBaseConfig = SpringUtils.getHighestOrder(applicationContext, DataBaseConfig.class);
			this.dataBaseConfig = dataBaseConfig!=null?dataBaseConfig:new DefaultDataBaseConfig();
		}*/
//		Assert.notNull(dataBaseConfig, "dataBaseConfig can't be null!");
		
		this.registerIdStrategy();
		//优先使用自定义的 DbEventListenerManager
		if(this.jfishdbEventListenerManager==null){
//			DbEventListenerManager dbelm = this.findDbEventListenerManagerFromContext(applicationContext);
			DbmdbEventListenerManager listMg = new DbmdbEventListenerManager();
//			dbelm.registerDefaultEventListeners();
			this.onDefaultDbEventListenerManager(listMg);
			this.jfishdbEventListenerManager = listMg;
		}
		
		
		if(sqlBuilderFactory==null){
			this.sqlBuilderFactory = new DefaultSQLBuilderFactory();
		}
		
		this.initOtherComponents();
	}
	
	/****
	 * 注册id策略
	 */
	protected void registerIdStrategy(){
	}
	
	protected void onDefaultDbEventListenerManager(DbmdbEventListenerManager listMg){
		listMg.registerListeners(DbmEventAction.insertOrUpdate, new DbmInsertOrUpdateListener())
				.registerListeners(DbmEventAction.insert, new DbmInsertEventListener())
				.registerListeners(DbmEventAction.batchInsert, new DbmBatchInsertEventListener())
				.registerListeners(DbmEventAction.batchUpdate, new DbmBatchUpdateEventListener())
				.registerListeners(DbmEventAction.update, new DbmUpdateEventListener())
				.registerListeners(DbmEventAction.delete, new DbmDeleteEventListener())
				.registerListeners(DbmEventAction.find, new DbmFindEventListener())
				.registerListeners(DbmEventAction.extQuery, new DbmExtQueryEventListener());
	}
	
	protected void initOtherComponents(){
	}
	
	
	public boolean isSupportedIdStrategy(StrategyType type) {
		return idStrategy.contains(type);
	}
	
	public List<StrategyType> getIdStrategy() {
		return idStrategy;
	}

	/*public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	public JFishMappedEntry getEntry(Object object){
		return this.mappedEntryManager.getEntry(object);
	}*/

	public int getMaxResults(int first, int size){
		return size;
	}

	public String getLimitString(String sql) {
		return getLimitString(sql, null, null);
	}
	
	public String getLimitStringWithNamed(String sql, String firstName, String maxResultName) {
		return getLimitString(sql, firstName, maxResultName);
	}
	
	abstract public String getLimitString(String sql, String firstName, String maxResultName);
	
	public void addLimitedValue(DbmQueryValue params, String firstName, int firstResult, String maxName, int maxResults){
		params.setValue(firstName, firstResult);
		params.setValue(maxName, getMaxResults(firstResult, maxResults));
	}

	/*public void setMappedEntryManager(MappedEntryManager mappedEntryManager) {
		this.mappedEntryManager = mappedEntryManager;
	}*/

	

	public DBMeta getDbmeta() {
		return dbmeta;
	}

	public void setDbmeta(DBMeta dbmeta) {
		this.dbmeta = dbmeta;
	}

	public boolean isAutoDetectIdStrategy() {
		return autoDetectIdStrategy;
	}

	public void setAutoDetectIdStrategy(boolean autoDetectIdStrategy) {
		this.autoDetectIdStrategy = autoDetectIdStrategy;
	}


	public DbmdbEventListenerManager getDbmEventListenerManager() {
		return jfishdbEventListenerManager;
	}

	public void setJfishdbEventListenerManager(
			DbmdbEventListenerManager jfishdbEventListenerManager) {
		this.jfishdbEventListenerManager = jfishdbEventListenerManager;
	}

	public SQLBuilderFactory getSqlBuilderFactory() {
		return sqlBuilderFactory;
	}

	protected void setSqlBuilderFactory(SQLBuilderFactory sqlBuilderFactory) {
		this.sqlBuilderFactory = sqlBuilderFactory;
	}

	/*public DataBaseConfig getDataBaseConfig() {
		return dataBaseConfig;
	}*/

	/*public JFishQueryableEventListener[] getQueryableEventListeners() {
		return queryableEventListeners;
	}

	public void setQueryableEventListeners(JFishQueryableEventListener[] queryableEventListeners) {
		this.queryableEventListeners = queryableEventListeners;
	}*/
	

	
	public static enum StrategyType {
		INCREASE_ID,
		SEQ
	}
	
	public static class DBMeta {
		
		public static DBMeta create(DataBase db){
			return new DBMeta(db);
		}
		
		final private DataBase dataBase;
		private String dbName;
		private String version;
		
		public DBMeta(String dbName) {
			super();
			this.dataBase = DataBase.of(dbName);
			this.dbName = dbName;
		}
		
		public DBMeta(DataBase db) {
			super();
			this.dataBase = db;
			this.dbName = db.getName();
		}
		public boolean isMySQL(){
			return DataBase.MySQL==dataBase;
		}
		public boolean isOracle(){
			return DataBase.Oracle==dataBase;
		}
		public String getDialetName(){
			return dbName + "Dialect";
		}
		
		public String getDbName() {
			return dataBase.getName();
		}

		public String getVersion() {
			return version;
		}

		public void setDbName(String dbName) {
			this.dbName = dbName.toLowerCase();
		}

		public void setVersion(String version) {
			this.version = version;
		}
		
		public DataBase getDataBase() {
			return dataBase;
		}
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("dbmeta [dbName:").append(dbName).append(", version:").append(version).append("]");
			return sb.toString();
		}
	}
}
