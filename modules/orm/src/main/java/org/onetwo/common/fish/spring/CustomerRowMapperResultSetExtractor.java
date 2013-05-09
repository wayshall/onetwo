package org.onetwo.common.fish.spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

abstract public class CustomerRowMapperResultSetExtractor<C, T> implements ResultSetExtractor<C>{

	private final RowMapper<T> rowMapper;

	public CustomerRowMapperResultSetExtractor(RowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}
	
	abstract protected C createResult();

	public C extractData(ResultSet rs) throws SQLException {
		C results = createResult();
		int rowNum = 0;
		T val = null;
		while (rs.next()) {
			val = mappResult(rs, rowNum++);
			addToResult(results, val);
		}
		return results;
	}

	protected T mappResult(ResultSet rs, int rowNum) throws SQLException{
		if(rowMapper!=null){
			return this.rowMapper.mapRow(rs, rowNum);
		}else{
			throw new UnsupportedOperationException("override the operation!");
		}
	}
	
	protected void addToResult(C result, T val){
		if(val==null)
			return ;
		if(Collection.class.isInstance(result)){
			((Collection<T>)result).add(val);
		}else{
			throw new UnsupportedOperationException("override the operation!");
		}
	}

}
