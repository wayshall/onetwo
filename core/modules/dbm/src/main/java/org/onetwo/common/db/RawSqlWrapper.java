package org.onetwo.common.db;

import org.onetwo.common.utils.StringUtils;

public class RawSqlWrapper {
	
	public static RawSqlWrapper wrap(String sql){
		return new RawSqlWrapper(sql);
	}
	
	private String rawSql;

	RawSqlWrapper(String rawSql) {
		super();
		this.rawSql = rawSql;
	}

	public String getRawSql() {
		return rawSql;
	}
	
	public boolean isBlank(){
		return StringUtils.isBlank(rawSql);
	}


}
