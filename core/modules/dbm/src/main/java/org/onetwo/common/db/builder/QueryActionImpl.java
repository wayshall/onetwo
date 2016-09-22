package org.onetwo.common.db.builder;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.db.InnerBaseEntityManager;
import org.onetwo.common.db.sqlext.ExtQuery;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.support.DbmEntityManager;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("unchecked")
public class QueryActionImpl implements QueryAction {

	protected InnerBaseEntityManager baseEntityManager;
	private SelectExtQuery extQuery;

	public QueryActionImpl(InnerBaseEntityManager baseEntityManager, Class<?> entityClass, String alias, Map<Object, Object> properties){
		this.baseEntityManager = baseEntityManager;
		extQuery = getSQLSymbolManager().createSelectQuery(entityClass, alias, properties);
		extQuery.build();
	}
	
	public SelectExtQuery getExtQuery() {
//		throwIfHasNotBuild();
		return extQuery;
	}

	protected SQLSymbolManager getSQLSymbolManager(){
//		SQLSymbolManager symbolManager = SQLSymbolManagerFactory.getInstance().getJdbc();
		return this.baseEntityManager.getSQLSymbolManager();
	}

	protected void checkOperation(){
		if(this.baseEntityManager==null)
			throw new UnsupportedOperationException("no entityManager");
//		this.build();
	}
	
	@Override
	public <T> T one(){
		checkOperation();
		return (T)baseEntityManager.selectOne(getExtQuery());
	}
	@Override
	public <T> T unique(){
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
	public <T> T extractAs(ResultSetExtractor<T> rse){
		T res = this.getDbmEntityManager().getDbmDao().find(convertAsDbmQueryValue(getExtQuery()), rse);
		return res;
	}
	
	public <T> List<T> listWith(RowMapper<T> rowMapper){
		List<T> res = this.getDbmEntityManager().getDbmDao().findList(convertAsDbmQueryValue(getExtQuery()), rowMapper);
		return res;
	}
	protected DbmEntityManager getDbmEntityManager(){
		return (DbmEntityManager) getBaseEntityManager();
	}

	public InnerBaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}
	
}
