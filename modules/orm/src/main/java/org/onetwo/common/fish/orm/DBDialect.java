package org.onetwo.common.fish.orm;

import java.util.List;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.fish.event.DbEventListenerManager;
import org.onetwo.common.fish.orm.AbstractDBDialect.DBMeta;
import org.onetwo.common.fish.orm.AbstractDBDialect.StrategyType;

public interface DBDialect {
	
	public String BEAN_NAME = "jfishDialect";
	
//	public MappedEntryManager getMappedEntryManager();
	
	public DBMeta getDbmeta();

	public SQLBuilderFactory getSqlBuilderFactory();
	
	public boolean isSupportedIdStrategy(StrategyType type);
	
	public boolean isAutoDetectIdStrategy();

	public List<StrategyType> getIdStrategy();
	public DbEventListenerManager getDbEventListenerManager();
//	public JFishEventListener[] getQueryableEventListeners();
	
//	public int getMaxResults(int first, int size);
	
	public String getLimitString(String sql);
	
	public String getLimitStringWithNamed(String sql, String firstName, String maxResultName);
	
	public void addLimitedValue(JFishQueryValue params, String firstName, int firstResult, String maxName, int maxResults);
	public boolean isPrintSql();
	public DataBaseConfig getDataBaseConfig();
}
