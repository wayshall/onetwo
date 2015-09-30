package org.onetwo.common.jfishdbm.dialet;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.jfishdbm.event.JFishBatchInsertEventListener;
import org.onetwo.common.jfishdbm.event.JFishBatchUpdateEventListener;
import org.onetwo.common.jfishdbm.event.JFishDeleteEventListener;
import org.onetwo.common.jfishdbm.event.JFishEventAction;
import org.onetwo.common.jfishdbm.event.JFishExtQueryEventListener;
import org.onetwo.common.jfishdbm.event.JFishFindEventListener;
import org.onetwo.common.jfishdbm.event.JFishInsertEventListener;
import org.onetwo.common.jfishdbm.event.JFishInsertOrUpdateListener;
import org.onetwo.common.jfishdbm.event.JFishUpdateEventListener;
import org.onetwo.common.jfishdbm.event.JFishdbEventListenerManager;
import org.onetwo.common.jfishdbm.mapping.DefaultSQLBuilderFactory;
import org.onetwo.common.jfishdbm.mapping.SQLBuilderFactory;


abstract public class AbstractDBDialect implements InnerDBDialet, DBDialect {

	
//	protected MappedEntryManager mappedEntryManager;
	protected DBMeta dbmeta;
//	protected DataBaseConfig dataBaseConfig;
	
	private List<StrategyType> idStrategy = new ArrayList<StrategyType>();
	private boolean autoDetectIdStrategy;

//	protected JFishQueryableEventListener[] queryableEventListeners;
	protected JFishdbEventListenerManager jfishdbEventListenerManager;
	
	private SQLBuilderFactory sqlBuilderFactory;
	
//	protected ApplicationContext applicationContext;
//	private boolean printSql = false;
	
	
	public AbstractDBDialect(DBMeta dbmeta) {
		super();
		this.dbmeta = dbmeta;
//		this.dataBaseConfig = dataBaseConfig;
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
			JFishdbEventListenerManager listMg = new JFishdbEventListenerManager();
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
	
	protected void onDefaultDbEventListenerManager(JFishdbEventListenerManager listMg){
		listMg.registerListeners(JFishEventAction.insertOrUpdate, new JFishInsertOrUpdateListener())
				.registerListeners(JFishEventAction.insert, new JFishInsertEventListener())
				.registerListeners(JFishEventAction.batchInsert, new JFishBatchInsertEventListener())
				.registerListeners(JFishEventAction.batchUpdate, new JFishBatchUpdateEventListener())
				.registerListeners(JFishEventAction.update, new JFishUpdateEventListener())
				.registerListeners(JFishEventAction.delete, new JFishDeleteEventListener())
				.registerListeners(JFishEventAction.find, new JFishFindEventListener())
				.registerListeners(JFishEventAction.extQuery, new JFishExtQueryEventListener());
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


	public JFishdbEventListenerManager getJfishdbEventListenerManager() {
		return jfishdbEventListenerManager;
	}

	public void setJfishdbEventListenerManager(
			JFishdbEventListenerManager jfishdbEventListenerManager) {
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
