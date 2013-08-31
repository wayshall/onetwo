package org.onetwo.common.fish.orm;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.fish.event.DbEventListenerManager;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


abstract public class AbstractDBDialect implements InnerDBDialet, DBDialect, InitializingBean, ApplicationContextAware {

	
	public static enum StrategyType {
		increase_id,
		seq
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
	
//	protected MappedEntryManager mappedEntryManager;
	protected DBMeta dbmeta;
	protected DataBaseConfig dataBaseConfig;
	
	private List<StrategyType> idStrategy = new ArrayList<StrategyType>();
	private boolean autoDetectIdStrategy;

//	protected JFishQueryableEventListener[] queryableEventListeners;
	protected DbEventListenerManager dbEventListenerManager;
	
	private SQLBuilderFactory sqlBuilderFactory;
	
	protected ApplicationContext applicationContext;
//	private boolean printSql = false;
	
	
	public AbstractDBDialect(DataBaseConfig dataBaseConfig) {
		super();
		this.dataBaseConfig = dataBaseConfig;
	}

	public void afterPropertiesSet() throws Exception {
		/*if(this.dataBaseConfig==null){
			this.dataBaseConfig = SpringUtils.getHighestOrder(applicationContext, DataBaseConfig.class);
			this.dataBaseConfig = dataBaseConfig!=null?dataBaseConfig:new DefaultDataBaseConfig();
		}*/
		Assert.notNull(dataBaseConfig, "dataBaseConfig can't be null!");
		
		this.registerIdStrategy();
		//优先使用自定义的 DbEventListenerManager
		if(this.dbEventListenerManager==null){
			DbEventListenerManager dbelm = this.findDbEventListenerManagerFromContext(applicationContext);
			if(dbelm==null){
				dbelm = createDefaultDbEventListenerManager();
				this.dbEventListenerManager = dbelm;
			}
		}
		
		this.dbEventListenerManager.registerDefaultEventListeners();
		
		this.initOtherComponents();
	}
	
	/****
	 * 注册id策略
	 */
	protected void registerIdStrategy(){
	}
	
	abstract protected DbEventListenerManager createDefaultDbEventListenerManager();
	
	protected void initOtherComponents(){
		this.sqlBuilderFactory = new DefaultSQLBuilderFactory();
	}
	
	protected DbEventListenerManager findDbEventListenerManagerFromContext(ApplicationContext applicationContext){
		return SpringUtils.getHighestOrder(applicationContext, DbEventListenerManager.class);
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

	public DbEventListenerManager getDbEventListenerManager() {
		return dbEventListenerManager;
	}

	public void setDbEventListenerManager(DbEventListenerManager dbEventListenerManager) {
		this.dbEventListenerManager = dbEventListenerManager;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

//	public boolean isPrintSql() {
//		return printSql;
//	}
//
//	public void setPrintSql(boolean printSql) {
//		this.printSql = printSql;
//	}

	public SQLBuilderFactory getSqlBuilderFactory() {
		return sqlBuilderFactory;
	}

	protected void setSqlBuilderFactory(SQLBuilderFactory sqlBuilderFactory) {
		this.sqlBuilderFactory = sqlBuilderFactory;
	}

	public DataBaseConfig getDataBaseConfig() {
		return dataBaseConfig;
	}

	/*public JFishQueryableEventListener[] getQueryableEventListeners() {
		return queryableEventListeners;
	}

	public void setQueryableEventListeners(JFishQueryableEventListener[] queryableEventListeners) {
		this.queryableEventListeners = queryableEventListeners;
	}*/
	
}
