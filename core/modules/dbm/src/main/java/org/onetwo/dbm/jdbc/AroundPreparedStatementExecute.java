package org.onetwo.dbm.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementSetter;

abstract public class AroundPreparedStatementExecute {
	public void beforeExecute(PreparedStatementSetter pss, PreparedStatement ps) throws SQLException{
		if (pss != null) {
			pss.setValues(ps);
		}
	}
	abstract public void afterExecute(PreparedStatement ps, int rows) throws SQLException ;
}
