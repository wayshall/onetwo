package org.onetwo.common.fish.orm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBValueHanlder {

	Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException;

	void setValue(PreparedStatement stat, Object value, int index) throws SQLException;
}
