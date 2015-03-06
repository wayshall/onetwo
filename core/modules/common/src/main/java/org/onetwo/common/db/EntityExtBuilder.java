package org.onetwo.common.db;

import java.util.List;

import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.utils.Page;

/*********
 * 提供简易有明确api的查询构造器
 * 
 * @author wayshall
 *
 */
abstract public class EntityExtBuilder extends QueryBuilderImpl { 

	/*public static EntityQueryBuilder where(){
		EntityQueryBuilder q = new EntityQueryBuilder(null, null);
		return q;
	}
*/
	/*public static EntityQueryBuilder where(Class<?> entityClass){
		EntityQueryBuilder q = new EntityQueryBuilder(null, entityClass);
		return q;
	}*/

	public static EntityEMQueryBuilder from(InnerBaseEntityManager baseEntityManager, Class<?> entityClass){
		EntityEMQueryBuilder q = new EntityEMQueryBuilder(baseEntityManager, entityClass);
		return q;
	}
	
	protected InnerBaseEntityManager baseEntityManager;
	
//	private List<SQField> fields = new ArrayList<SQField>();
	

	protected EntityExtBuilder(InnerBaseEntityManager baseEntityManager, Class<?> entityClass){
		super(entityClass);
		this.baseEntityManager = baseEntityManager;
	}
	
	
	protected void checkOperation(){
		if(this.baseEntityManager==null)
			throw new UnsupportedOperationException("no entityManager");
	}

	protected BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	protected SQLSymbolManager getSQLSymbolManager(){
		return getBaseEntityManager().getSQLSymbolManager();
	}
	
	public static class EntityEMQueryBuilder extends EntityExtBuilder implements EMQueryBuilder {
		
		protected EntityEMQueryBuilder(InnerBaseEntityManager baseEntityManager,
				Class<?> entityClass) {
			super(baseEntityManager, entityClass);
		}

		@Override
		public SelectExtQuery getExtQuery(){
			return (SelectExtQuery)super.getExtQuery();
		}

		@Override
		public <T> T one(){
			checkOperation();
			return (T)baseEntityManager.selectUnique(getExtQuery());
		}

		@Override
		public <T> List<T> list(){
			checkOperation();
			return baseEntityManager.select(getExtQuery());
		}

		@Override
		public <T> void page(Page<T> page){
			checkOperation();
			baseEntityManager.selectPage(page, getExtQuery());
		}

		@Override
		public int execute() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
