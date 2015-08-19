package org.onetwo.common.db.sqlext;

import java.util.List;
import java.util.Map;

public class DeleteExtQueryImpl extends AbstractExtQuery {

	
	public DeleteExtQueryImpl(Class<?> entityClass, String alias, Map<Object, Object> params,
			SQLSymbolManager symbolManager, List<ExtQueryListener> listeners) {
		super(entityClass, alias, params, symbolManager, listeners);
		this.aliasMainTableName = false;
	}

	@Override
	public ExtQuery build() {
		sql = new StringBuilder();
//		sql.append("delete ").append(this.alias).append(" from ").append(this.getFromName(entityClass)).append(" ").append(this.alias).append(" ");
		sql.append("delete ").append("from ").append(this.getFromName(entityClass)).append(" ");
		
		this.buildWhere();
		if(where!=null)
			sql.append(where);
		
		if (isDebug()) {
			logger.info("generated sql : " + sql);
			logger.info("params : " + (Map<?, ?>) this.paramsValue.getValues());
		}

		this.hasBuilt = true;
		return this;
	}

}
