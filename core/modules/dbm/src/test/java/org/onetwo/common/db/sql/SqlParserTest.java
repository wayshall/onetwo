package org.onetwo.common.db.sql;

import org.junit.Test;

import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;

public class SqlParserTest {
	
	@Test
	public void test(){
		String sql = "";
		MySqlStatementParser sqlParser = new MySqlStatementParser(sql);
		sqlParser.parseStatement();
	}

}
