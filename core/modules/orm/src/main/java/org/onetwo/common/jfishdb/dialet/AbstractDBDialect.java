package org.onetwo.common.jfishdb.dialet;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.jfishdb.event.JFishBatchInsertEventListener;
import org.onetwo.common.jfishdb.event.JFishBatchUpdateEventListener;
import org.onetwo.common.jfishdb.event.JFishDeleteEventListener;
import org.onetwo.common.jfishdb.event.JFishEventAction;
import org.onetwo.common.jfishdb.event.JFishExtQueryEventListener;
import org.onetwo.common.jfishdb.event.JFishFindEventListener;
import org.onetwo.common.jfishdb.event.JFishInsertEventListener;
import org.onetwo.common.jfishdb.event.JFishInsertOrUpdateListener;
import org.onetwo.common.jfishdb.event.JFishUpdateEventListener;
import org.onetwo.common.jfishdb.event.JFishdbEventListenerManager;
import org.onetwo.common.jfishdb.orm.DefaultSQLBuilderFactory;
import org.onetwo.common.jfishdb.orm.InnerDBDialet;
import org.onetwo.common.jfishdb.orm.SQLBuilderFactory;


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
	
	
	public AbstractDBDialect() {
		super();
//		this.dataBaseConfig = dataBaseConfig;
	}

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
			JFishdbEventListenerManager dbelm = new JFishdbEventListenerManager();
//			dbelm.registerDefaultEventListeners();
			this.jfishdbEventListenerManager = dbelm;
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
	
	public void addLimitedValue(JFishQueryValue params, String firstName, int firstResult, String maxName, int maxResults){
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
		private DataBase db;
		private String dbName;
		private String version;

		public boolean isMySQL(){
			return DataBase.MySQL==db;
		}
		public boolean isOracle(){
			return DataBase.Oracle==db;
		}
		public String getDialetName(){
			return dbName + "Dialect";
		}
		
		public String getDbName() {
			return db.getName();
		}

		public String getVersion() {
			return version;
		}

		public void setDbName(String dbName) {
			this.dbName = dbName.toLowerCase();
			this.db = DataBase.of(this.dbName);
		}

		public void setVersion(String version) {
			this.version = version;
		}
		
		public DataBase getDb() {
			return db;
		}
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("dbmeta [dbName:").append(dbName).append(", version:").append(version).append("]");
			return sb.toString();
		}
	}
}
