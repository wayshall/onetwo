package org.onetwo.common.jfishdbm.dialet;

import java.util.List;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.jfishdbm.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.common.jfishdbm.dialet.AbstractDBDialect.StrategyType;
import org.onetwo.common.jfishdbm.event.JFishdbEventListenerManager;
import org.onetwo.common.jfishdbm.mapping.SQLBuilderFactory;
import org.onetwo.common.jfishdbm.utils.Initializable;

public interface DBDialect extends Initializable {
	
//	public String BEAN_NAME = "jfishdbDialect";
	
//	public MappedEntryManager getMappedEntryManager();
	
//	public void initialize();
	public DBMeta getDbmeta();

	public SQLBuilderFactory getSqlBuilderFactory();
	
	public boolean isSupportedIdStrategy(StrategyType type);
	
	public boolean isAutoDetectIdStrategy();

	public List<StrategyType> getIdStrategy();
	public JFishdbEventListenerManager getJfishdbEventListenerManager();
//	public JFishEventListener[] getQueryableEventListeners();
	
//	public int getMaxResults(int first, int size);
	
	public String getLimitString(String sql);
	
	public String getLimitStringWithNamed(String sql, String firstName, String maxResultName);
	
	public void addLimitedValue(JFishQueryValue params, String firstName, int firstResult, String maxName, int maxResults);
//	public boolean isPrintSql();
//	public DataBaseConfig getDataBaseConfig();
}
