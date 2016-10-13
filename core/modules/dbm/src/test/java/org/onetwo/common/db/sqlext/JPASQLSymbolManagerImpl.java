package org.onetwo.common.db.sqlext;

import java.util.List;
import java.util.Map;

public class JPASQLSymbolManagerImpl extends DefaultSQLSymbolManagerImpl {

	public JPASQLSymbolManagerImpl() {
		super(new DefaultExtQueryDialetImpl());
	}
	

	@Override
	public SelectExtQuery createSelectQuery(Class<?> entityClass, String alias, Map<Object, Object> properties) {
		SelectExtQuery q = new JPASelectExtQueryImpl(entityClass, alias, properties, this, this.getListeners());
		q.initQuery();
		return q;
	}
	
	public static class JPASelectExtQueryImpl extends SelectExtQueryImpl {

		public JPASelectExtQueryImpl(Class<?> entityClass, String alias,
				Map<?, ?> params, SQLSymbolManager symbolManager,
				List<ExtQueryListener> listeners) {
			super(entityClass, alias, params, symbolManager, listeners);
		}

		public JPASelectExtQueryImpl(Class<?> entityClass, String alias,
				Map<?, ?> params, SQLSymbolManager symbolManager) {
			super(entityClass, alias, params, symbolManager);
		}


		protected String getFromName(Class<?> entityClass){
			String tableName = entityClass.getSimpleName();
			return tableName;
		}
	}
}
