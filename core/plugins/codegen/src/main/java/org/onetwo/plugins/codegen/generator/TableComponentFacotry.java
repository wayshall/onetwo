package org.onetwo.plugins.codegen.generator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.map.BaseMap;
import org.onetwo.plugins.codegen.db.TableInfo;

public interface TableComponentFacotry {

	public TableInfo createTableInfo(Map<String, Object> rs);

	public void createColumnInfo(List<BaseMap> rs, TableInfo table) throws SQLException;

	public void createPrimaryKey(ResultSet rs, TableInfo table) throws SQLException;

}