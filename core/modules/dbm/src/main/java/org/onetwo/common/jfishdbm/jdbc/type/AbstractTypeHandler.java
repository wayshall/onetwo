package org.onetwo.common.jfishdbm.jdbc.type;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.onetwo.common.jfishdbm.exception.DbmException;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;


abstract public class AbstractTypeHandler<T> implements TypeHandler<T> {

	@Override
	public void setParameter(PreparedStatement ps, int parameterIndex, T value, JDBCType jdbcType) throws SQLException {
		if(value==null){
			if(jdbcType==null){
				throw new DbmException("jdbcType can not be null!");
			}
			ps.setNull(parameterIndex, jdbcType.getVendorTypeNumber());
		}else{
			setParameterNotNullValue(ps, parameterIndex, value, jdbcType);
		}
	}
	
	abstract protected void setParameterNotNullValue(PreparedStatement ps, int parameterIndex, T value, JDBCType jdbcType) throws SQLException ;

	@Override
	public T getResult(ResultSetWrappingSqlRowSet resultSet, String columName) {
		T value = getResultValue(resultSet, columName);
		if(resultSet.wasNull()){
			return null;
		}
		return value;
	}
	

	abstract protected T getResultValue(ResultSetWrappingSqlRowSet resultSet, String columName);

	@Override
	public T getResult(ResultSetWrappingSqlRowSet resultSet, int columIndex) {
		T value = getResultValue(resultSet, columIndex);
		if(resultSet.wasNull()){
			return null;
		}
		return value;
	}

	abstract protected T getResultValue(ResultSetWrappingSqlRowSet resultSet, int columIndex);
	
}
