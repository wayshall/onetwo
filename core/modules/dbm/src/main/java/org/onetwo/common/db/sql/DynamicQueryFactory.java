package org.onetwo.common.db.sql;

import org.onetwo.common.db.parser.JFishDynamicQueryImpl;
import org.onetwo.common.db.parser.JFishSqlParserManager;
import org.onetwo.common.db.parser.SqlStatment;

abstract public class DynamicQueryFactory {

	public static DynamicQuery create(String sql){
		return new DynamicQueryImpl(sql);
	}
	
	public static DynamicQuery create(String sql, Class<?> entityClass){
		return new DynamicQueryImpl(sql, SqlCauseParser.SIMPLE_CACHE.parseSql(sql), entityClass);
	}
	public static DynamicQuery createJFishDynamicQuery(String sql){
		SqlStatment statments = JFishSqlParserManager.getInstance().getSqlStatment(sql);
		return new JFishDynamicQueryImpl(sql, statments, null);
	}
	public static DynamicQuery createJFishDynamicQuery(String sql, Class<?> entityClass){
		SqlStatment statments = JFishSqlParserManager.getInstance().getSqlStatment(sql);
		return new JFishDynamicQueryImpl(sql, statments, entityClass);
	}
}