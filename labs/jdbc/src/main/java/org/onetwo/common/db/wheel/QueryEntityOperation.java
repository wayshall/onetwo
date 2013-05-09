package org.onetwo.common.db.wheel;

import java.util.List;

import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("unchecked")
public class QueryEntityOperation extends AbstractEntityOperation {

	public static class QuerySingleEntityExeContext extends EntityExeContext {
		ResultSetLooperHandler handler;
		public QuerySingleEntityExeContext(DefaultEntityBuilder entityBuilder, EnhanceQuery q) {
			super(entityBuilder, q);
			BeanDataRowMapper mapper = new BeanDataRowMapper(entityBuilder.getEntityClass());
			handler = new ResultSetLooperHandler(mapper);
		}

		@Override
		public ResultSetHandler getResultSetHandler() {
			return handler;
		}
		
		public EntityExeContext setBy(Object entity){
			getSqlParser().setParameter(0, entity);
			return this;
		}

		@Override
		public void setResult(Object result) {
			List list = LangUtils.asList(result);
			if(list.isEmpty())
				this.result = null;
			else
				this.result = list.get(0);
		}
		
	}
	
	public QueryEntityOperation(JDaoSupport dao, EntityBuilder entityBuilder) {
		super(dao, entityBuilder);
	}

	protected EntityExeContext createEntityExeContext(DefaultEntityBuilder entityBuilder, EnhanceQuery q){
		return new QuerySingleEntityExeContext(entityBuilder, q);
	}

	@Override
	protected EnhanceQuery createSqlParser() {
		EnhanceQuery q = EnhanceQueryFacotry.create(entityBuilder.makeStaticQuerySQL());
		q.setIgnoreIfCauseValueNull(false);
		return q;
	}

	@Override
	protected EnhanceQuery dynamicCreateSqlParser(Object object) {
		EnhanceQuery q = EnhanceQueryFacotry.create(entityBuilder.makeDynamicQuerySQL(object));
		return q;
	}
	
}
