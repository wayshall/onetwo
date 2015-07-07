package org.onetwo.common.db.generator.dialet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.db.generator.DBConnecton;
import org.onetwo.common.db.generator.DBUtils;
import org.onetwo.common.db.generator.meta.TableMeta;

public class MysqlMetaDialet extends BaseMetaDialet implements DatabaseMetaDialet {
	
	
	public MysqlMetaDialet(DataSource dataSource) {
		super(dataSource);
	}


	public TableMeta getTableMeta(String tableName){
		Map<String, Object> rowMap = null;
		ResultSet rs = null;
		DBConnecton dbcon = newDBConnecton();
		TableMeta table = null;
		try {
			rs = dbcon.getMetaData().getTables(catalog, schema, tableName.trim(), null);
			if(rs.next())
				rowMap = DBUtils.toMap(rs);

			String tname = (String)rowMap.get("TABLE_NAME");
			String comment = (String)rowMap.get("REMARKS");
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
	

}
