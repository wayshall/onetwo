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

	public static EntityQueryBuilder from(BaseEntityManager baseEntityManager, Class<?> entityClass){
		EntityQueryBuilder q = new EntityQueryBuilder(baseEntityManager, entityClass);
		return q;
	}
	
	protected BaseEntityManager baseEntityManager;
	
//	private List<SQField> fields = new ArrayList<SQField>();
	

	protected EntityExtBuilder(BaseEntityManager baseEntityManager, Class<?> entityClass){
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
	
	public static class EntityQueryBuilder extends EntityExtBuilder {
		
		protected EntityQueryBuilder(BaseEntityManager baseEntityManager,
				Class<?> entityClass) {
			super(baseEntityManager, entityClass);
		}

		public <T> T one(){
			checkOperation();
			return baseEntityManager.findUnique(build());
		}
		
		public <T> List<T> list(){
			checkOperation();
			return baseEntityManager.findList(build());
		}
		
		public <T> void page(Page<T> page){
			checkOperation();
			baseEntityManager.findPage(page, build());
		}
	}
	
}
