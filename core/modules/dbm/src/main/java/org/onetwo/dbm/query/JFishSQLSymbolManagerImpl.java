package org.onetwo.dbm.query;

import java.util.Map;

import org.onetwo.common.db.sqlext.DefaultExtQueryDialetImpl;
import org.onetwo.common.db.sqlext.DefaultSQLSymbolManagerImpl;
import org.onetwo.common.db.sqlext.ExtQueryDialet;
import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.dbm.mapping.JFishMappedEntry;
import org.onetwo.dbm.mapping.MappedEntryManager;

public class JFishSQLSymbolManagerImpl extends DefaultSQLSymbolManagerImpl {
	
//	public static final SQLSymbolManager SQL_SYMBOL_MANAGER = create();
	
	public static JFishSQLSymbolManagerImpl create(){
		ExtQueryDialet sqlDialet = new DefaultExtQueryDialetImpl();
		JFishSQLSymbolManagerImpl newSqlSymbolManager = new JFishSQLSymbolManagerImpl(sqlDialet);
		return newSqlSymbolManager;
	}

//	private DBDialect dialect;
	private MappedEntryManager mappedEntryManager;

	public JFishSQLSymbolManagerImpl(ExtQueryDialet sqlDialet) {
		super(sqlDialet);
	}

	@Override
	public SelectExtQuery createSelectQuery(Class<?> entityClass, String alias, Map<Object, Object> properties) {
		JFishMappedEntry entry = null;
		if(mappedEntryManager!=null){
			entry = this.mappedEntryManager.getEntry(entityClass);
		}
		SelectExtQuery q = new JFishExtQueryImpl(entry, entityClass, alias, properties, this, this.getListeners());
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
