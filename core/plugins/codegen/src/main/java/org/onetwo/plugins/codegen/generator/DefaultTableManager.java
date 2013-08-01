package org.onetwo.plugins.codegen.generator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.map.BaseMap;
import org.onetwo.common.utils.map.NonCaseMap;
import org.onetwo.plugins.codegen.db.DBUtils;
import org.onetwo.plugins.codegen.db.ResultSetMapper;
import org.onetwo.plugins.codegen.db.SqlTypeFactory.DataBase;
import org.onetwo.plugins.codegen.db.TableInfo;

public class DefaultTableManager {
	
	public static class OracleSql {
		public static final String select_all_table = "SELECT TABLE_NAME FROM user_tables";
		public static final String select_column_comments = "SELECT t.column_name, t.comments FROM user_col_comments t where t.table_name = :tableName";
		public static final String select_table_comments = "SELECT * FROM user_tab_comments t where t.table_name = :tableName";
	}

	protected String catalog;
	protected String schema;
//	protected Map<String, TableInfo> tables = new HashMap<String, TableInfo>();
	protected List<String> tableNames = new ArrayList<String>();
	
	/*@Autowired
	private JFishDaoImplementor jfishDao;*/
	
	private DataSource dataSource;
	private DataBase dataBase;
	
	private TableComponentFacotry tableComponentFacotry;

	public DefaultTableManager() {
	}
	
	protected DBConnecton newDBConnecton(){
		return new DBConnecton(this.dataSource);
	}

	public TableInfo createTable(DBConnecton dbcon, String tableName, boolean autoClose) {
		TableInfo table = null;
		ResultSet rs = null;
		try {
			Map<String, Object> rowMap = null;
			if(this.dataBase.isOracle()){
				rs = dbcon.query(OracleSql.select_table_comments, "tableName", tableName);
				if(rs.next()){
					rowMap = DBUtils.toMap(rs);
					rowMap.put("REMARKS", rowMap.get("COMMENTS"));
				}
			}else{
				rs = dbcon.getMetaData().getTables(catalog, schema, tableName.trim(), null);
				if(rs.next())
					rowMap = DBUtils.toMap(rs);
			}
			table = tableComponentFacotry.createTableInfo(rowMap);
			if (table!=null) {
				this.createColumns(table, dbcon).createTablePrimaryKey(table, dbcon);
//				this.cacheTable(table);
			}else
				throw new RuntimeException("can't create table info error: " + tableName);
		} catch (Exception e) {
			throw new RuntimeException("get table info error!", e);
		} finally{
			if(rs!=null)
				DBUtils.closeResultSet(rs);
			if(dbcon!=null)
				dbcon.closePreparedStatement();
			if(autoClose)
				dbcon.close();
		}
		return table;
	}

	public DefaultTableManager createColumns(TableInfo table, DBConnecton dbcon) throws SQLException {
		ResultSet rs = null;
		List<BaseMap> datas = null;
		rs = dbcon.getMetaData().getColumns(catalog, schema, table.getName(), null);
		datas = DBUtils.toList(rs, true, new ResultSetMapper(){

			@Override
			public NonCaseMap map(ResultSet rs, NonCaseMap map) throws SQLException {
				map.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
				map.put("DATA_TYPE", rs.getInt("DATA_TYPE"));
				map.put("REMARKS", rs.getString("REMARKS"));
				return map;
			}
			
		});
		
		if(this.dataBase.isOracle()){
			DBConnecton ndbcon = newDBConnecton();
			try {
				rs = ndbcon.query(OracleSql.select_column_comments, "tableName", table.getName());
				Map comments = LangUtils.mapListToMap(DBUtils.toList(rs, true), "column_name", "comments");

				for(Map row : datas){
					row.put("REMARKS", comments.get(row.get("COLUMN_NAME")));
				}
			} finally {
				ndbcon.close();
			}
			
		}
		
		tableComponentFacotry.createColumnInfo(datas, table);
		
		return this;
	}

	protected DefaultTableManager createTablePrimaryKey(TableInfo table, DBConnecton dbcon) throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbcon.getMetaData().getPrimaryKeys(catalog, schema, table.getName());
			tableComponentFacotry.createPrimaryKey(rs, table);
		} catch (Exception e) {
			throw new ServiceException("createTablePrimaryKey error ", e);
		} finally{
			DBUtils.closeResultSet(rs);
		}
		return this;
	}

	public TableInfo getTable(String tableName) {
		List<TableInfo> tables = this.createTables(tableName);
		return tables.get(0);
	}
	
	public List<TableInfo> getTables(List<String> tnames) {
		return createTables(tnames.toArray(new String[tnames.size()]));
	}

	public List<TableInfo> getTables() {
		return createTables();
	}

	public List<TableInfo> createTables(String...tnames) {
		
		List<String> tableNames = null;
		if(tnames==null || tnames.length==0)
			tableNames = this.getTableNames(false);
		else
			tableNames = MyUtils.asList(tnames);
		
		List<TableInfo> tableInfos = new ArrayList<TableInfo>();
		DBConnecton dbcon = newDBConnecton();
		try {
			for(String tableName : tableNames){
				TableInfo tinfo = createTable(dbcon, tableName, false);
				tableInfos.add(tinfo);
			}
		} finally{
			dbcon.close();
		}
		return tableInfos;
	}
	
	public List<String> getTableNames(boolean refrech) {
		if(!refrech){
			if(!this.tableNames.isEmpty())
				return this.tableNames;
		}
		this.tableNames.clear();
		ResultSet rs = null;
		DBConnecton dbcon = new DBConnecton(this.dataSource);
		try {
			if(this.dataBase.isOracle()){
				rs = dbcon.query(OracleSql.select_all_table);
			}else
				rs = dbcon.getMetaData().getTables(catalog, schema, null, null);
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				this.tableNames.add(tableName);
			}
		} catch (Exception e) {
			throw new RuntimeException("get tables error!", e);
		} finally{
			DBUtils.closeResultSet(rs);
			dbcon.close();
		}
		return this.tableNames;
	}

	public void setTableComponentFacotry(TableComponentFacotry tableComponentFacotry) {
		this.tableComponentFacotry = tableComponentFacotry;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

}
