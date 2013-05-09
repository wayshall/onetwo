package org.onetwo.common.db.wheel;

import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("unchecked")
public class DeleteEntityOperation extends AbstractEntityOperation {

	public static class DeleteEntityContext extends EntityExeContext  {

		public DeleteEntityContext(EntityBuilder entityBuilder, EnhanceQuery q) {
			super(entityBuilder, q);
		}

		public EntityExeContext setBy(Object entity){
			getSqlParser().setParameter(0, entity);
			return this;
		}
	}
	
	private boolean deleteOnly;
	
	protected DeleteEntityOperation(JDaoSupport dao, EntityBuilder entityBuilder) {
		super(dao, entityBuilder);
	}

	@Override
	protected EntityExeContext createEntityExeContext(EntityBuilder entityBuilder, EnhanceQuery q) {
		return new DeleteEntityContext(entityBuilder, q);
	}

	@Override
	protected EnhanceQuery createSqlParser() {
		EnhanceQuery q = EnhanceQueryFacotry.createUpdate(entityBuilder.makeStaticDeleteSQL());
		q.setIgnoreIfCauseValueNull(false);
		return q;
	}
	protected void beforeExecute(){
		if(isDeleteOnly())
			return ;
		this.result = this.dao.findByTableInfo(this.operationObject, this.entityBuilder.getTableInfo());
		if(result==null)
			LangUtils.throwBaseException("can find the entity : " + this.operationObject);
	}
	protected void afterExeucte(){
	}

	@Override
	protected EnhanceQuery dynamicCreateSqlParser(Object object) {
		EnhanceQuery q = EnhanceQueryFacotry.createUpdate(entityBuilder.makeDynamicDeleteSQL(object));
		return q;
	}

	boolean isDeleteOnly() {
		return deleteOnly;
	}

	public void setDeleteOnly(boolean deleteOnly) {
		this.deleteOnly = deleteOnly;
	}
	
}
