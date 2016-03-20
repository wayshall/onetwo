package org.onetwo.common.db.builder;

import java.util.List;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.db.InnerBaseEntityManager;
import org.onetwo.common.db.sqlext.ExtQuery;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.common.jfishdbm.support.DbmEntityManager;
import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/*********
 * 提供简易有明确api的查询构造器
 * 
 * @author wayshall
 *
 */
public class EntityQueryBuilderImpl extends QueryBuilderImpl{ 

	/*public static EntityQueryBuilder where(){
		EntityQueryBuilder q = new EntityQueryBuilder(null, null);
		return q;
	}
*/
	/*public static EntityQueryBuilder where(Class<?> entityClass){
		EntityQueryBuilder q = new EntityQueryBuilder(null, entityClass);
		return q;
	}*/
	protected InnerBaseEntityManager baseEntityManager;
	
//	private List<SQField> fields = new ArrayList<SQField>();
	

	protected EntityQueryBuilderImpl(InnerBaseEntityManager baseEntityManager, Class<?> entityClass){
		super(entityClass);
		this.baseEntityManager = baseEntityManager;
	}
	
	
	protected void checkOperation(){
		if(this.baseEntityManager==null)
			throw new UnsupportedOperationException("no entityManager");
		this.build();
	}

	protected BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	protected SQLSymbolManager getSQLSymbolManager(){
		return getBaseEntityManager().getSQLSymbolManager();
	}

	@Override
	public EntityQueryBuilderImpl build(){
		super.build();
		return this;
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
	public <T> Page<T> page(Page<T> page){
		checkOperation();
		baseEntityManager.selectPage(page, getExtQuery());
		return page;
	}

	protected DbmQueryValue convertAsDbmQueryValue(ExtQuery extQuery){
		DbmQueryValue qv = DbmQueryValue.create(extQuery.getSql());
		qv.setResultClass(extQuery.getEntityClass());
		/*if(extQuery.getParamsValue().isList()){
			qv.setValue(extQuery.getParamsValue().asList());
		}else{
			qv.setValue(extQuery.getParamsValue().asMap());
		}*/
		qv.setValue(extQuery.getParamsValue().asMap());
		
		return qv;
	}
	public <T> T queryAs(ResultSetExtractor<T> rse){
		T res = this.getDbmEntityManager().getDbmDao().find(convertAsDbmQueryValue(getExtQuery()), rse);
		return res;
	}
	
	public <T> List<T> listAs(RowMapper<T> rowMapper){
		List<T> res = this.getDbmEntityManager().getDbmDao().findList(convertAsDbmQueryValue(getExtQuery()), rowMapper);
		return res;
	}
	protected DbmEntityManager getDbmEntityManager(){
		return (DbmEntityManager) getBaseEntityManager();
	}
	
}
