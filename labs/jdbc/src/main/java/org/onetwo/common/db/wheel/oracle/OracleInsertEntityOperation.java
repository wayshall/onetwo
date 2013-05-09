package org.onetwo.common.db.wheel.oracle;

import org.onetwo.common.db.sql.Condition;
import org.onetwo.common.db.wheel.DBConnection;
import org.onetwo.common.db.wheel.EnhanceQuery;
import org.onetwo.common.db.wheel.EntityBuilder;
import org.onetwo.common.db.wheel.EntityExeContext;
import org.onetwo.common.db.wheel.InsertEntityOperation;
import org.onetwo.common.db.wheel.JDaoSupport;
import org.onetwo.common.db.wheel.ValueAdaptor;
import org.onetwo.common.db.wheel.EnhanceQueryImpl.EntityValue;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;

public class OracleInsertEntityOperation extends InsertEntityOperation {

	private static class OracleEntityValue extends EntityValue {

		private String pkSql;
		private EntityBuilder entityBuilder;
		private OracleEntityValue(EntityBuilder entityBuilder, Object entity) {
			super(entity, entityBuilder.getTableInfo().getAlias());
			this.entityBuilder = entityBuilder;
			this.pkSql = entityBuilder.makeStaticFetchPKSQL();
		}

		@Override
		public Object getValue(Condition cond, DBConnection dbcon) {
			if(entityBuilder.getTableInfo().getPrimaryKey()==null){
				return super.getValue(cond, dbcon);
			}else{
				String idName = entityBuilder.getTableInfo().getPrimaryKey().getFirstColumn().getJavaName();
				if(cond.getVarname().equals(idName)){
					Long id = ((Number)dbcon.value(pkSql)).longValue();
					MyUtils.setValue(getEntity(), idName, id);
					return id;
				}else{
					return super.getValue(cond, dbcon);
				}
			}
		}
		
	}
	public static class OracleInsertEntityExeContext extends EntityExeContext {

		public OracleInsertEntityExeContext(EntityBuilder entityBuilder, EnhanceQuery q) {
			super(entityBuilder, q);
		}
		protected ValueAdaptor getEntityValue(Object entity){
			return new OracleEntityValue(entityBuilder, entity);
		}
		
	}

	public OracleInsertEntityOperation(JDaoSupport dao, EntityBuilder entityBuilder) {
		super(dao, entityBuilder);
	}

	@Override
	protected EntityExeContext createEntityExeContext(EntityBuilder entityBuilder, EnhanceQuery q) {
		return new OracleInsertEntityExeContext(entityBuilder, q);
	}

}
