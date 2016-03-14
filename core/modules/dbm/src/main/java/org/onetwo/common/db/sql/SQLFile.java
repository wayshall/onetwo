package org.onetwo.common.db.sql;

import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.propconf.PropConfig;
import org.onetwo.common.propconf.PropUtils;
import org.onetwo.common.utils.StringUtils;

/***
 * .sql文件的管理类
 * 
 * @author weishao
 *
 */
public class SQLFile{
	
//	private static SQLFile instance = newSQLFile("sql.sql");
	
	/*public static SQLFile getInstance() {
		return instance;
	}*/
	
	public static SQLFile newSQLFile(String sqlfile){
		return new SQLFile(sqlfile);
	}
	
	public static SQLFile newSQLFile(PropConfig properties){
		return new SQLFile(properties);
	}
	
	protected PropConfig sqlfile;
	
	protected SQLFile(){
	}
	
	protected SQLFile(String sqlfile) {
		this.sqlfile = PropUtils.loadPropConfig(sqlfile);
	}
	
	protected SQLFile(PropConfig sqlfile) {
		this.sqlfile = sqlfile;
	}

	public PropConfig getSqlfile() {
		return sqlfile;
	}
	
	public void reload(){
		sqlfile.reload();
	}

	public void setSqlfile(PropConfig sqlfile) {
		this.sqlfile = sqlfile;
	}

	public String getSqlOnly(String queryName){
		return getProperty(queryName);
	}
	
	public String getProperty(String key) {
		return sqlfile.getProperty(key);
	}

	/***
	 * 可根据名字在sql文件中查找该名字的查询语句，并返回AnotherQuery对象
	 * @param queryName
	 * @return
	 */
	public DynamicQuery createQuery(String queryName){
		String sql = getProperty(queryName);
		if(StringUtils.isBlank(sql))
			throw new ServiceException("can not find the query : " + queryName);
		
		DynamicQuery query = DynamicQueryFactory.create(sql);
		return query;
	}
	
	/*****
	 * 可根据名字在sql文件中查找该名字的查询语句，设置参数的值，并返回AnotherQuery对象
	 * @param queryName
	 * @param objects
	 * @return
	 */
	public DynamicQuery createQuery(String queryName, Object...objects){
		DynamicQuery query = this.createQuery(queryName);
		for(int i=0; i<objects.length; i++)
			query.setParameter(i, objects[i]);
		query.compile();
		
		return query;
	}
	
	
	/***
	 * 
	 * 可根据名字在sql文件中查找该名字的查询语句，设置命名参数的值，并返回AnotherQuery对象
	 * @param queryName
	 * @param params
	 * @return
	 */
	public DynamicQuery createQuery(String queryName, Map<String, Object> params){
		DynamicQuery query = this.createQuery(queryName);
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, Object> p : params.entrySet())
				query.setParameter(p.getKey(), p.getValue());
		}
		query.compile();
		
		return query;
	}

}
