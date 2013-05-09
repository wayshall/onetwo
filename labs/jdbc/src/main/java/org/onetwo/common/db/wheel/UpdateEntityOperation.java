package org.onetwo.common.db.wheel;


@SuppressWarnings("unchecked")
public class UpdateEntityOperation extends AbstractEntityOperation {
	
	protected UpdateEntityOperation(JDaoSupport dao, EntityBuilder entityBuilder) {
		super(dao, entityBuilder);
	}

	/*@Override
	protected EntityExeContext buildEntityExeContext() {
		AnotherQuery q = AnotherQueryFactory.createUpdate(entityBuilder.makeStaticUpdateSQL());
		q.setIgnoreIfCauseValueNull(false);
		EntityExeContext ectx = createEntityExeContext(entityBuilder, q);
		return ectx;
	}

	@Override
	protected EntityExeContext dynamicBuildEntityExeContext(Object object) {
		AnotherQuery q = AnotherQueryFactory.createUpdate(entityBuilder.makeDynamicUpdateSQLBy(object));
		q.setIgnoreIfCauseValueNull(false);
		EntityExeContext ectx = createEntityExeContext(entityBuilder, q);
		return ectx;
	}*/

	@Override
	protected EnhanceQuery createSqlParser() {
		EnhanceQuery q = EnhanceQueryFacotry.createUpdate(entityBuilder.makeStaticUpdateSQL());
		q.setIgnoreIfCauseValueNull(false);
		return q;
	}

	@Override
	protected EnhanceQuery dynamicCreateSqlParser(Object object) {
		EnhanceQuery q = EnhanceQueryFacotry.createUpdate(entityBuilder.makeDynamicUpdateSQLBy(object));
		q.setIgnoreIfCauseValueNull(true);
		return q;
	}
	
}
