package org.onetwo.common.db.wheel;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler {
	Object handleData(ResultSet rs) throws SQLException;
}
