package org.onetwo.common.db.parser;

import java.util.List;

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

}
