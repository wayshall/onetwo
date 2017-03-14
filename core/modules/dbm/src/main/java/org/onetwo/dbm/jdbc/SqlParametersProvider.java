package org.onetwo.dbm.jdbc;

import java.util.List;

public interface SqlParametersProvider {

	public Object[] getSqlParameters();
	public List<?> getSqlParameterList();
	
}
