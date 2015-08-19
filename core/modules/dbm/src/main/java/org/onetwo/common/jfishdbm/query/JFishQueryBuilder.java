package org.onetwo.common.jfishdbm.query;

import java.util.List;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.sqlext.EntityExtBuilder;
import org.onetwo.common.db.sqlext.ExtQuery;
import org.onetwo.common.jfishdbm.support.JFishEntityManager;
import org.onetwo.common.jfishdbm.support.JFishEntityManagerImpl;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class JFishQueryBuilder extends EntityExtBuilder {

	public static JFishQueryBuilder from(JFishEntityManagerImpl baseEntityManager, Class<?> entityClass){
		JFishQueryBuilder q = new JFishQueryBuilder(baseEntityManager, entityClass);
		return q;
	}
	
	protected JFishQueryBuilder(JFishEntityManagerImpl baseEntityManager, Class<?> entityClass) {
		super(baseEntityManager, entityClass);
	}
	
	protected JFishEntityManager getJFishEntityManager(){
		return (JFishEntityManager) getBaseEntityManager();
	}
	
	protected JFishQueryValue convertAsJFishQueryValue(ExtQuery extQuery){
		JFishQueryValue qv = JFishQueryValue.create(getSQLSymbolManager().getPlaceHolder(), extQuery.getSql());
		qv.setResultClass(extQuery.getEntityClass());
		if(extQuery.getParamsValue().isList()){
			qv.setValue(extQuery.getParamsValue().asList());
		}else{
			qv.setValue(extQuery.getParamsValue().asMap());
		}
		return qv;
	}
	public <T> T queryAs(ResultSetExtractor<T> rse){
		T res = this.getJFishEntityManager().getJfishDao().find(convertAsJFishQueryValue(getExtQuery()), rse);
		return res;
	}
	
	public <T> List<T> listAs(RowMapper<T> rowMapper){
		List<T> res = this.getJFishEntityManager().getJfishDao().findList(convertAsJFishQueryValue(getExtQuery()), rowMapper);
		return res;
	}

}
