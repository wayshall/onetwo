package org.onetwo.plugins.codegen.generator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.fish.orm.ColumnInfo;
import org.onetwo.common.fish.orm.PrimaryKey;
import org.onetwo.common.fish.orm.TableInfo;
import org.onetwo.common.utils.map.BaseMap;

public class DefaultTableComponentFacotry implements TableComponentFacotry {
	
	@Override
	public TableInfo createTableInfo(Map<String, Object> rs){
		TableInfo table = null;
		try {
			if (rs!=null) {
				table = newTable(rs);
			}
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
		return table;
	}

	protected TableInfo newTable(Map<String, Object> rs) throws SQLException {
		String tname = (String)rs.get("TABLE_NAME");
		TableInfo table = new TableInfo(tname);
		String comment = (String)rs.get("REMARKS");
		table.setComment(comment);
		return table;
	}

	@Override
	public void createColumnInfo(List<BaseMap> rs, TableInfo table) throws SQLException {
		ColumnInfo column = null;
		for(BaseMap row : rs){
			column = newColumn(row);
			table.addColumn(column);
		}
	}
	
	protected ColumnInfo newColumn(BaseMap rs) throws SQLException{
		
		String name = (String)rs.get("COLUMN_NAME");
		int sqlType = rs.getInteger("DATA_TYPE");
		String comment = rs.getString("REMARKS");
		ColumnInfo column = new ColumnInfo(name, sqlType);
		column.setComment(comment);
		return column;
	}
	

	@Override
	public void createPrimaryKey(ResultSet rs, TableInfo table) throws SQLException {
		PrimaryKey pk = null;
		boolean first = true;
		while (rs.next()) {
			if (first) {
				pk = buildPrimaryKey(rs, pk);
				first = false;
			}
			String columnName = rs.getString("COLUMN_NAME");
			pk.addColumn((ColumnInfo)table.getColumn(columnName));
		}
		table.setPrimaryKey(pk);
	}
	
	protected PrimaryKey buildPrimaryKey(ResultSet rs, PrimaryKey pk) throws SQLException{
		pk = new PrimaryKey();
//		pk.setName(rs.getString("PK_NAME"));
		pk.setName(rs.getString("COLUMN_NAME"));
		return pk;
	}
	
}
