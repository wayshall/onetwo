package org.onetwo.common.db.generator.dialet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.db.generator.DBConnecton;
import org.onetwo.common.db.generator.meta.ColumnMeta;
import org.onetwo.common.db.generator.meta.TableMeta;
import org.onetwo.common.db.generator.utils.DBUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;

public class OracleMetaDialet extends BaseMetaDialet implements DatabaseMetaDialet {
	private static final String SELECT_TABLE_COMMENTS = "SELECT * FROM user_tab_comments t where t.table_name = :tableName";
	public static final String SELECT_COLUMN_COMMENTS = "SELECT t.column_name, t.comments FROM user_col_comments t where t.table_name = :tableName";
	public static final String SELECT_ALL_TABLE = "SELECT TABLE_NAME FROM user_tables";
	
	public OracleMetaDialet(DataSource dataSource) {
		super(dataSource);
	}
	
	public OracleMetaDialet(DataSource dataSource, String catalog, String schema) {
		super(dataSource, catalog, schema);
	}

	@Override
	public List<String> getTableNames() {
		if(!tableNames.isEmpty()){
			return tableNames;
		}
		ResultSet rs = null;
		DBConnecton dbcon = newDBConnecton();
		try {
			rs = dbcon.query(SELECT_ALL_TABLE);
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				this.tableNames.add(tableName);
			}
		} catch (SQLException e) {
			DBUtils.handleDBException(e);
		}finally{
			dbcon.close();
		}
		return this.tableNames;
	}

	public TableMeta getTableMeta(String tableName){
		Map<String, Object> rowMap = null;
		ResultSet rs = null;
		DBConnecton dbcon = newDBConnecton();
		TableMeta table = null;
		try {
			rs = dbcon.query(SELECT_TABLE_COMMENTS, "tableName", tableName);
			if(rs.next()){
				rowMap = DBUtils.toMap(rs);
			}else{
				throw new BaseException("not table found: " + tableName);
			}

			String tname = (String)rowMap.get("TABLE_NAME");
			String comment = (String)rowMap.get("COMMENTS");
			table = new TableMeta(tname, comment);
			createFieldMeta(dbcon, table, tableName);
			createPrimaryKey(dbcon, table);;
		} catch (SQLException e) {
			DBUtils.handleDBException(e);
		} finally{
			dbcon.close();
		}
		return table;
	}


	protected void createPrimaryKey(DBConnecton dbcon, TableMeta table) throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbcon.getMetaData().getPrimaryKeys(catalog, schema, table.getName());
			if (rs.next()) {
				ColumnMeta column = table.getColumn(rs.getString("COLUMN_NAME"));
				Assert.notNull(column);
				column.setPrimaryKey(true);
				table.setPrimaryKey(column);
			}
		} catch (Exception e) {
			throw new BaseException("createTablePrimaryKey error ", e);
		} finally{
			DBUtils.closeResultSet(rs);
		}
	}
	
	public void createFieldMeta(DBConnecton dbcon, TableMeta table, String tableName) throws SQLException{
		super.createFieldMeta(dbcon, table, tableName);
		
		ResultSet rs = null;
		rs = dbcon.query(SELECT_COLUMN_COMMENTS, "tableName", table.getName());
		List<Map<String, Object>> datalist = DBUtils.toList(rs, true);
		for(Map<String, Object> row : datalist){
			ColumnMeta meta = table.getColumn(StringUtils.emptyIfNull(row.get("column_name")));
			if(meta!=null){
				meta.setComment(StringUtils.emptyIfNull(row.get("comments")));
			}
		}
	}

}
