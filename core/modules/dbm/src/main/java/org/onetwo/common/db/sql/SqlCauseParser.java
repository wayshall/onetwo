package org.onetwo.common.db.sql;

import java.util.List;

import org.onetwo.common.utils.SToken;

public interface SqlCauseParser {

	SimpleSqlCauseParser SIMPLE = new SimpleSqlCauseParser(-1);
	SimpleSqlCauseParser SIMPLE_CACHE = new SimpleSqlCauseParser(256);

	public List<SToken> parseSql(String originalSql);

}