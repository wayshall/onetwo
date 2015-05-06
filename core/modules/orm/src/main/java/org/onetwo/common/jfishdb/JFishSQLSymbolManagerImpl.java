package org.onetwo.common.jfishdb;

import java.util.Map;

import org.onetwo.common.db.SelectExtQuery;
import org.onetwo.common.db.sqlext.DefaultSQLDialetImpl;
import org.onetwo.common.db.sqlext.DefaultSQLSymbolManagerImpl;
import org.onetwo.common.db.sqlext.SQLDialet;
import org.onetwo.common.jfishdb.orm.JFishMappedEntry;
import org.onetwo.common.jfishdb.orm.MappedEntryManager;

public class JFishSQLSymbolManagerImpl extends DefaultSQLSymbolManagerImpl {
	
//	public static final SQLSymbolManager SQL_SYMBOL_MANAGER = create();
	
	public static JFishSQLSymbolManagerImpl create(){
		SQLDialet sqlDialet = new DefaultSQLDialetImpl();
		JFishSQLSymbolManagerImpl newSqlSymbolManager = new JFishSQLSymbolManagerImpl(sqlDialet);
		return newSqlSymbolManager;
	}

//	private DBDialect dialect;
	private MappedEntryManager mappedEntryManager;

	public JFishSQLSymbolManagerImpl(SQLDialet sqlDialet) {
		super(sqlDialet);
	}

	@Override
	public SelectExtQuery createSelectQuery(Class<?> entityClass, String alias, Map<Object, Object> properties) {
		JFishMappedEntry entry = null;
		if(mappedEntryManager!=null){
			entry = this.mappedEntryManager.getEntry(entityClass);
		}
		SelectExtQuery q = new JFishExtQueryImpl(entry, entityClass, alias, properties, this);
		q.initQuery();
		return q;
	}

	/*public DBDialect getDialect() {
		return dialect;
	}

	public void setDialect(DBDialect dialect) {
		this.dialect = dialect;
	}*/

	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	public void setMappedEntryManager(MappedEntryManager mappedEntryManager) {
		this.mappedEntryManager = mappedEntryManager;
	}

}
