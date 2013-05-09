package org.onetwo.common.fish;

import java.util.List;

import org.onetwo.common.db.EntityQueryBuilder;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class JFishQueryBuilder extends EntityQueryBuilder {

	public static JFishQueryBuilder from(JFishEntityManager baseEntityManager, Class<?> entityClass){
		JFishQueryBuilder q = new JFishQueryBuilder(baseEntityManager, entityClass);
		return q;
	}
	
	protected JFishQueryBuilder(JFishEntityManager baseEntityManager, Class<?> entityClass) {
		super(baseEntityManager, entityClass);
	}
	
	protected JFishEntityManager getJFishEntityManager(){
		return (JFishEntityManager) getBaseEntityManager();
	}
	public <T> T queryAs(ResultSetExtractor<T> rse){
		T res = this.getJFishEntityManager().getJfishDao().find(build(), rse);
		return res;
	}
	
	public <T> List<T> listAs(RowMapper<T> rowMapper){
		List<T> res = this.getJFishEntityManager().getJfishDao().findList(build(), rowMapper);
		return res;
	}

}
