package org.onetwo.common.fish.spring;

import org.onetwo.common.fish.JFishQuery;


public interface JFishNamedFileQueryManager {
	
//	public JFishNamedFileQueryInfo getNamedQueryInfo(String name);
	public void build();

	public boolean containsQuery(String queryName);
	
	public JFishQuery createQuery(JFishDaoImplementor jfishFishDao, String queryName);
	
	public JFishQuery createCountQuery(JFishDaoImplementor jfishFishDao, String queryName);

	/***
	 * 可根据名字在sql文件中查找该名字的查询语句，并返回AnotherQuery对象
	 * @param queryName
	 * @return
	 *//*
	public AnotherQuery createQuery(String queryName);

	*//*****
	 * 可根据名字在sql文件中查找该名字的查询语句，设置参数的值，并返回AnotherQuery对象
	 * @param queryName
	 * @param objects
	 * @return
	 *//*
	public AnotherQuery createQuery(String queryName, Object... objects);

	*//***
	 * 
	 * 可根据名字在sql文件中查找该名字的查询语句，设置命名参数的值，并返回AnotherQuery对象
	 * @param queryName
	 * @param params
	 * @return
	 *//*
	public AnotherQuery createQuery(String queryName, Map<String, Object> params);
*/
}