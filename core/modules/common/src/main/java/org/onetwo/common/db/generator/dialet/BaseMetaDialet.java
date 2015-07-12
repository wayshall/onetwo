package org.onetwo.common.db.generator.dialet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.db.generator.DBConnecton;
import org.onetwo.common.db.generator.mapping.ColumnMapping;
import org.onetwo.common.db.generator.mapping.MetaMapping;
import org.onetwo.common.db.generator.mapping.ResultSetMapper;
import org.onetwo.common.db.generator.mapping.SimpleMetaMapping;
import org.onetwo.common.db.generator.meta.ColumnMeta;
import org.onetwo.common.db.generator.meta.TableMeta;
import org.onetwo.common.db.generator.utils.DBUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.slf4j.Logger;

abstract public class BaseMetaDialet implements DatabaseMetaDialet {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected List<String> tableNames = new ArrayList<>();
	final private DataSource dataSource;
	private MetaMapping metaMapping = new SimpleMetaMapping();
	protected String catalog;
	protected String schema;
	
	public BaseMetaDialet(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	public BaseMetaDialet(DataSource dataSource, String catalog, String schema) {
		super();
		this.dataSource = dataSource;
		this.catalog = catalog;
		this.schema = schema;
	}

	@Override
	public List<String> getTableNames() {
		if(!tableNames.isEmpty()){
			return tableNames;
		}
		ResultSet rs = null;
		DBConnecton dbcon = newDBConnecton();
		try {
			rs = dbcon.getMetaData().getTables(catalog, schema, null, null);
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

	protected DBConnecton newDBConnecton(){
		return new DBConnecton(this.dataSource);
	}

	public MetaMapping getMetaMapping() {
		return metaMapping;
	}
	

	public ColumnMapping getColumnMapping(int sqlType){
		return getMetaMapping().getColumnMapping(sqlType);
	}

	public void setMetaMapping(MetaMapping sqlTypeMapping) {
		this.metaMapping = sqlTypeMapping;
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
	
	protected void createFieldMeta(DBConnecton dbcon, TableMeta table, String tableName) throws SQLException{
		ResultSet rs = dbcon.getMetaData().getColumns(catalog, schema, table.getName(), null);
		DBUtils.toList(rs, true, new ResultSetMapper<ColumnMeta>(){

			@Override
			public ColumnMeta map(ResultSet rs) throws SQLException {
				Map<String, Object> rsMap = DBUtils.toMap(rs, false);
				String colName = rs.getString("COLUMN_NAME");
				int sqlType = rs.getInt("DATA_TYPE");
				String remark = rs.getString("REMARKS");
				/*if("SELL_STATUS".equalsIgnoreCase(colName)){
					System.out.println("test");
				}*/
				String isNullable  = rs.getString("IS_NULLABLE");
				int columnSize = rs.getInt("COLUMN_SIZE");
				ColumnMapping mapping = getMetaMapping().getRequiredColumnMapping(sqlType);
				logger.info("mapping -> colunm: {}, sqltype: {}", colName, mapping);
				ColumnMeta meta = new ColumnMeta(table, colName, mapping);
				meta.setComment(remark);
				meta.setNullable("yes".equalsIgnoreCase(isNullable));
				meta.setColumnSize(columnSize);
				
				table.addColumn(meta);
				return meta;
			}
			
		});
	}
	
}
