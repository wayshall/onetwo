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
public class EntityQueryBuilder extends QueryBuilderImpl { 

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
	
	private BaseEntityManager baseEntityManager;
	
//	private List<SQField> fields = new ArrayList<SQField>();
	

	protected EntityQueryBuilder(BaseEntityManager baseEntityManager, Class<?> entityClass){
		super(entityClass);
		this.baseEntityManager = baseEntityManager;
	}
	
	public <T> T one(){
		this.checkOperation();
		return baseEntityManager.findUnique(this.build());
	}
	
	public <T> List<T> list(){
		this.checkOperation();
		return baseEntityManager.findList(this.build());
	}
	
	public <T> void page(Page<T> page){
		this.checkOperation();
		baseEntityManager.findPage(page, this.build());
	}
	
	private void checkOperation(){
		if(this.baseEntityManager==null)
			throw new UnsupportedOperationException("no entityManager");
	}

	protected BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	protected SQLSymbolManager getSQLSymbolManager(){
		return getBaseEntityManager().getSQLSymbolManager();
	}
	
}
