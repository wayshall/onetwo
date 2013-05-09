package org.onetwo.common.db.wheel;


public class InsertEntityOperation extends AbstractEntityOperation {
	
	private boolean insertOnly;
	
	public InsertEntityOperation(JDaoSupport dao, EntityBuilder entityBuilder) {
		super(dao, entityBuilder);
	}

	@Override
	protected EnhanceQuery createSqlParser() {
		EnhanceQuery q = EnhanceQueryFacotry.createUpdate(entityBuilder.makeStaticInsertSQL());
		q.setIgnoreIfCauseValueNull(false);
		return q;
	}

	@Override
	protected EnhanceQuery dynamicCreateSqlParser(Object object) {
		EnhanceQuery q = EnhanceQueryFacotry.createUpdate(entityBuilder.makeDynamicInsertSQLBy(object));
		q.setIgnoreIfCauseValueNull(false);
		return q;
	}

	@Override
	protected EntityExeContext createEntityExeContext(EntityBuilder entityBuilder, EnhanceQuery q) {
		return new EntityExeContext(entityBuilder, q);
	}
	
	protected void afterExeucte(){
		this.result = this.getLastedExeContext().getResult();
	}

	public boolean isInsertOnly() {
		return insertOnly;
	}

	/********
	 * 是否在插入之后回调查询id
	 * @param insertOnly
	 */
	public void setInsertOnly(boolean insertOnly) {
		this.insertOnly = insertOnly;
	}
}
