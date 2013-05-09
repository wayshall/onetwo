package org.onetwo.common.db.wheel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetter {

//	PreparedStatementSetter simple = JDBC.inst().wheel().createComponent(SimplePreparedStatementSetter.class);//new SimplePreparedStatementSetter();
	PreparedStatementSetter dbhandler = new DBValueHandlerSetter();

	void setParameter(PreparedStatement preStatement, int index, Object value) throws SQLException ;
}
