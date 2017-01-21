package org.onetwo.common.db.builder;

import javax.sql.DataSource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.InnerBaseEntityManager;
import org.onetwo.dbm.support.Dbms;

/****
 * alias for QueryBuilderFactory 
 * @author way
 *
 */
final public class Querys {

	public static QueryBuilder from(BaseEntityManager baseEntityManager, Class<?> entityClass){
		QueryBuilderImpl q = new QueryBuilderImpl(baseEntityManager.narrowAs(InnerBaseEntityManager.class), entityClass);
		return q;
	}

	public static QueryBuilder from(Class<?> entityClass){
		return from(Dbms.obtainBaseEntityManager(), entityClass);
	}

	public static QueryBuilder from(DataSource dataSource, Class<?> entityClass){
		return from(Dbms.obtainBaseEntityManager(dataSource), entityClass);
	}

	private Querys(){
	}
}
