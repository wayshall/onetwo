package org.onetwo.dbm.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class DbmRowMapperResultSetExtractor<C extends Collection<T>, T> extends AbstractResultSetExtractor<T> implements ResultSetExtractor<C>{

	protected final int rowsExpected;

	public DbmRowMapperResultSetExtractor(RowMapper<T> rowMapper) {
		this(rowMapper, 0);
	}
	public DbmRowMapperResultSetExtractor(RowMapper<T> rowMapper, int rowsExpected) {
		super(rowMapper);
		this.rowsExpected = rowsExpected;
	}
	
	@SuppressWarnings("unchecked")
	protected C createResult(){
		return (C) (rowsExpected>0?new ArrayList<T>(rowsExpected): new ArrayList<T>());
	}

	public C extractData(ResultSet rs) throws SQLException {
		C results = createResult();
		int rowNum = 0;
		T val = null;
		while (rs.next()) {
			val = rowMapper.mapRow(rs, rowNum++);
			addToResult(results, val);
		}
		return results;
	}

	protected void addToResult(C result, T val){
		if(val==null)
			return ;
		result.add(val);
	}

}
