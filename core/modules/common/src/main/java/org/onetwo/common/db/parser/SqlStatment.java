package org.onetwo.common.db.parser;

import java.util.List;

import org.onetwo.common.db.parser.SqlKeywords.SqlType;
import org.onetwo.common.utils.LangUtils;

public class SqlStatment {
	
	private List<SqlObject> sqlObjects = LangUtils.newArrayList();

	public List<SqlObject> getSqlObjects() {
		return sqlObjects;
	}

	public void addSqlObject(SqlObject sqlObj){
		this.sqlObjects.add(sqlObj);
	}
	
	public String toSql(){
		StringBuilder sql = new StringBuilder();
		for(SqlObject sqlobj : sqlObjects){
			sql.append(sqlobj.toFragmentSql());
		}
		return sql.toString().trim();
	}
	
	public SqlType getSqlType(){
		SqlObject sqlobj = LangUtils.getFirst(sqlObjects);
		return SqlParserUtils.getSqlType(sqlobj);
	}
	
	public String toString(){
		StringBuilder sql = new StringBuilder();
		for(SqlObject so : sqlObjects)
			sql.append(so);
		return sql.toString();
	}

}
