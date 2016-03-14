package org.onetwo.common.jfishdbm.query;

import java.util.List;

import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.db.sqlext.EntityExtBuilder;
import org.onetwo.common.db.sqlext.ExtQuery;
import org.onetwo.common.jfishdbm.support.DbmEntityManager;
import org.onetwo.common.jfishdbm.support.DbmEntityManagerImpl;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class JFishQueryBuilder extends EntityExtBuilder {

	public static JFishQueryBuilder from(DbmEntityManagerImpl baseEntityManager, Class<?> entityClass){
		JFishQueryBuilder q = new JFishQueryBuilder(baseEntityManager, entityClass);
		return q;
	}
	
	protected JFishQueryBuilder(DbmEntityManagerImpl baseEntityManager, Class<?> entityClass) {
		super(baseEntityManager, entityClass);
	}
	
	protected DbmEntityManager getJFishEntityManager(){
		return (DbmEntityManager) getBaseEntityManager();
	}
	
	protected DbmQueryValue convertAsJFishQueryValue(ExtQuery extQuery){
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
		T res = this.getJFishEntityManager().getDbmDao().find(convertAsJFishQueryValue(getExtQuery()), rse);
		return res;
	}
	
	public <T> List<T> listAs(RowMapper<T> rowMapper){
		List<T> res = this.getJFishEntityManager().getDbmDao().findList(convertAsJFishQueryValue(getExtQuery()), rowMapper);
		return res;
	}

}
