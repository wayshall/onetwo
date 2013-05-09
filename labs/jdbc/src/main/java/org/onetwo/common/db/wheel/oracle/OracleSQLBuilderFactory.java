package org.onetwo.common.db.wheel.oracle;

import org.onetwo.common.db.wheel.DefaultSQLBuilderFactory;
import org.onetwo.common.db.wheel.JDBC;
import org.onetwo.common.db.wheel.SQLBuilder;
import org.onetwo.common.utils.LangUtils;

public class OracleSQLBuilderFactory extends DefaultSQLBuilderFactory {
	
	public static class OracleSQLBuilder extends SQLBuilder {
		public static final String SQL_ORACLE_PK = "select ${"+VAR_SEQ_NAME+"}.Nextval from dual";

		public OracleSQLBuilder(String table, String alias) {
			super(table, alias, ":name");
		}
		
		public String buildPrimaryKey(){
			String pkSql = PARSER.parse(SQL_ORACLE_PK, VAR_SEQ_NAME, seqName);
			if(JDBC.inst().isDebug())
				LangUtils.println("build Primary sql : ${0}", pkSql);
			return pkSql;
		}
		
	}

	protected SQLBuilder createSQLBuilder(String tableName, String alias){
		SQLBuilder sqlBuilder = new OracleSQLBuilder(tableName, alias);
		return sqlBuilder;
	}
}
