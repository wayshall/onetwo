package org.onetwo.dbm.dialet;

import java.util.List;

import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.dbm.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.dbm.dialet.AbstractDBDialect.StrategyType;
import org.onetwo.dbm.event.DbmEventListenerManager;
import org.onetwo.dbm.mapping.DbmTypeMapping;
import org.onetwo.dbm.mapping.SQLBuilderFactory;
import org.onetwo.dbm.utils.Initializable;

public interface DBDialect extends Initializable {
	
//	public String BEAN_NAME = "jfishdbDialect";
	
//	public MappedEntryManager getMappedEntryManager();
	
//	public void initialize();
//	public DataBase getDbmeta();
	public DBMeta getDbmeta();

	public SQLBuilderFactory getSqlBuilderFactory();
	
	public boolean isSupportedIdStrategy(StrategyType type);
	
	public boolean isAutoDetectIdStrategy();

	public List<StrategyType> getIdStrategy();
	public DbmEventListenerManager getDbmEventListenerManager();
//	public JFishEventListener[] getQueryableEventListeners();
	
//	public int getMaxResults(int first, int size);
	
	public String getLimitString(String sql);
	
	public String getLimitStringWithNamed(String sql, String firstName, String maxResultName);
	
	public void addLimitedValue(DbmQueryValue params, String firstName, int firstResult, String maxName, int maxResults);
//	public boolean isPrintSql();
//	public DataBaseConfig getDataBaseConfig();
	
	public DbmTypeMapping getTypeMapping();
}
