package org.onetwo.common.db.wheel.mysql;

import org.onetwo.common.db.wheel.DBConnection;
import org.onetwo.common.db.wheel.EnhanceQuery;
import org.onetwo.common.db.wheel.EnhanceQueryFacotry;
import org.onetwo.common.db.wheel.EntityBuilder;
import org.onetwo.common.db.wheel.EntityExeContext;
import org.onetwo.common.db.wheel.InsertEntityOperation;
import org.onetwo.common.db.wheel.JDaoSupport;
import org.onetwo.common.db.wheel.EnhanceQuery.EventListenerAdaptor;
import org.onetwo.common.db.wheel.EnhanceQuery.SqlOperation;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;

public class MySQLInsertEntityOperation extends InsertEntityOperation {

	public static class MySQLInsertEntityExeContext extends EntityExeContext {
		
		private Long maxId;
		private Object entity;
		
		private InsertEntityOperation insert;
		
		public MySQLInsertEntityExeContext(InsertEntityOperation insert, EntityBuilder entityBuilder, EnhanceQuery q) {
			super(entityBuilder, q);
			this.insert = insert;
		}
		
		public void initBeforeBuild(){
			if(this.insert.isInsertOnly()){
				return ;
			}
			
			this.getSqlParser().registerListener(SqlOperation.update, new EventListenerAdaptor(){
				
				@Override
				public void onAfter(DBConnection dbcon, Object result) {
					maxId = ((Number)dbcon.value(entityBuilder.makeStaticFetchPKSQL())).longValue();
				}
				
			});
		}
		
		public EntityExeContext setBy(Object entity){
			this.entity = entity;
			return super.setBy(entity);
		}

		@Override
		public void setResult(Object result) {
			final String id = entityBuilder.getTableInfo().getPrimaryKey().getFirstColumn().getJavaName();
			MyUtils.setValue(entity, id, maxId);
		}
		
	}
	
	public MySQLInsertEntityOperation(JDaoSupport dao, EntityBuilder entityBuilder) {
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
		return new MySQLInsertEntityExeContext(this, entityBuilder, q);
	}
	protected void afterExeucte(){
		this.result = this.getLastedExeContext().getResult();
	}
}
