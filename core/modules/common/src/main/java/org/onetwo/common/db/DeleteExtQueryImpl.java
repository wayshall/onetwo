package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.sqlext.ExtQueryListener;
import org.onetwo.common.db.sqlext.SQLSymbolManager;

public class DeleteExtQueryImpl extends AbstractExtQuery {

	
	public DeleteExtQueryImpl(Class<?> entityClass, String alias, Map params,
			SQLSymbolManager symbolManager, List<ExtQueryListener> listeners) {
		super(entityClass, alias, params, symbolManager, listeners);
	}

	@Override
	public ExtQuery build() {
		sql = new StringBuilder();
		sql.append("delete ").append(this.alias).append(" from ").append(this.getFromName(entityClass)).append(" ").append(this.alias).append(" ");
		
		this.buildWhere();
		if(where!=null)
			sql.append(where);
		
		if (isDebug()) {
			logger.info("generated sql : " + sql);
			logger.info("params : " + (Map) this.paramsValue.getValues());
		}

		this.hasBuilt = true;
		return this;
	}

}
