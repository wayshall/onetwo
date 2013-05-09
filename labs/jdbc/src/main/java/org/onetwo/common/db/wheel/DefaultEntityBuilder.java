package org.onetwo.common.db.wheel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.wheel.AbstractEntityOperation.EntityOperationType;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class DefaultEntityBuilder implements EntityBuilder  {

	public static final String INSERT_KEY = "__INSERT_KEY";
	public static final String UPDATE_KEY = "__UPDATE_KEY";
	public static final String QUERY_KEY = "__QUERY_KEY";
	public static final String DELETE_KEY = "__DELETE_KEY";
	public static final String FETCH_PK_KEY = "__FETCH_PK_KEY";
	
	/*public static enum EntityOperation {
		insert,
		update,
		delete
	}*/
	
	protected SQLBuilderFactory sqlBuilderFactory;
	protected TableInfo tableInfo;
//	protected AnotherQuery sqlParser;

	protected Class<?> entityClass;
	
	protected Map<String, String> sqlCaches = new HashMap<String, String>();
	
	
	protected DefaultEntityBuilder(TableInfo tableInfo){
		this.tableInfo = tableInfo;
		this.entityClass = tableInfo.getEntityClass();
	}
	
	protected boolean fieldCanInsert(ColumnInfo column, Object value, boolean ignoreNull){
		if(column.isPrimaryKey()){
			if(column.getTable().isDbCreatePrimaryKey())
				return false;
			else
				return true;
		}else{
			if(ignoreNull)
				return value!=null;
			else
				return true;
		}
	}
	
	protected void beforeMakeSQLBuilder(EntityOperationType type, Object object){
	}
	
	protected SQLBuilder makeInsertSQLBuilder(Object object){
		this.beforeMakeSQLBuilder(EntityOperationType.insert, object);
		boolean ignoreNull = (object!=null);
		Object value = null;
		SQLBuilder sqlBuilder = createSQLBuilder();
		for(Map.Entry<String, ColumnInfo> col : tableInfo.getColumns().entrySet()){
			try {
				if(ignoreNull){
					value = ReflectUtils.getExpr(object, col.getValue().getJavaName());
				}
				if(fieldCanInsert(col.getValue(), value, ignoreNull)){
					sqlBuilder.append(col.getValue());
//					insertFields.put(col.getValue().getJavaName(), value);
				}
			} catch (Exception e) {
				LangUtils.throwBaseException("build field["+col.getValue().toString()+"] error : " + e.getMessage(), e);
			}
		}
		return sqlBuilder;
	}

	public String makeDynamicInsertSQLBy(Object object) {
		String sql = this.makeInsertSQLBuilder(object).buildInsert();
		return sql;
	}
	
	public String makeStaticInsertSQL(){
		String key = getKey(INSERT_KEY);
		String sql = getFromCache(key);
		if(StringUtils.isNotBlank(sql))
			return sql;
		
		sql = makeDynamicInsertSQLBy(null);
		putIntoCache(key, sql);
//		LangUtils.println("makeStaticInsertSQL: " + sql);
		
		return sql;
	}
	
	protected String getKey(String key){
		return key;
	}
	
	protected String getFromCache(String key){
		return sqlCaches.get(key);
	}
	
	protected String putIntoCache(String key, String sql){
		return sqlCaches.put(key, sql);
	}

	protected boolean fieldCanUpdate(ColumnInfo column, Object value, boolean ignoreNull){
		if(column.isPrimaryKey()){
			return false;
		}else{
			if(ignoreNull)
				return value!=null;
			else
				return true;
		}
	}
	
	protected SQLBuilder makeUpdateSQLBuilder(Object object){
		this.beforeMakeSQLBuilder(EntityOperationType.update, object);
		boolean ignoreNull = (object!=null);
		Object value = null;
		SQLBuilder sqlBuilder = createSQLBuilder();
		for(Map.Entry<String, ColumnInfo> col : tableInfo.getColumns().entrySet()){
			try {
				if(ignoreNull){
					value = ReflectUtils.getExpr(object, col.getValue().getJavaName());
				}
				if(fieldCanUpdate(col.getValue(), value, ignoreNull)){
					sqlBuilder.append(col.getValue());
				}
			} catch (Exception e) {
				LangUtils.throwBaseException("build field["+col.getValue().toString()+"] error : " + e.getMessage(), e);
			}
		}
		return sqlBuilder;
	}

	public String makeDynamicUpdateSQLBy(Object object) {
		SQLBuilder sqlBuilder = this.makeUpdateSQLBuilder(object);
		List<ColumnInfo> pks = tableInfo.getPrimaryKey().getColumns();
		String sql = sqlBuilder.appendWhere(pks).buildUpdate();
		return sql;
	}
	
	public String makeStaticUpdateSQL(){
		String key = getKey(UPDATE_KEY);
		String sql = getFromCache(key);
		if(StringUtils.isNotBlank(sql))
			return sql;
		
		sql = makeDynamicUpdateSQLBy(null);
		putIntoCache(key, sql);
		LangUtils.println("makeStaticUpdateSQL: " + sql);
		
		return sql;
	}
	
	protected SQLBuilder makeDeleteSQLBuilder(Object object){
		this.beforeMakeSQLBuilder(EntityOperationType.delete, object);
		SQLBuilder sqlBuilder = createSQLBuilder();
		if(object==null){
			sqlBuilder.appendWhere(tableInfo.getPrimaryKey().getColumns());
			return sqlBuilder;
		}
		Object value = null;
		for(Map.Entry<String, ColumnInfo> col : tableInfo.getColumns().entrySet()){
			try {
				value = ReflectUtils.getExpr(object, col.getValue().getJavaName());
				if(value!=null)
					sqlBuilder.appendWhere(col.getValue());
			} catch (Exception e) {
				LangUtils.throwBaseException("build field["+col.getValue().toString()+"] error : " + e.getMessage(), e);
			}
		}
		return sqlBuilder;
	}
	
	public String makeStaticDeleteSQL(){
		String key = getKey(DELETE_KEY);
		String sql = getFromCache(key);
		if(StringUtils.isNotBlank(sql))
			return sql;
		
		sql = makeDeleteSQLBuilder(null).buildDelete();
		putIntoCache(key, sql);
		LangUtils.println("makeStaticDeleteSQL: " + sql);
		
		return sql;
	}
	
	public String makeDynamicDeleteSQL(Object object){
		return makeDeleteSQLBuilder(object).buildDelete();
	}
	
	protected SQLBuilder makeQuerySQLBuilder(Object object){
		this.beforeMakeSQLBuilder(EntityOperationType.query, object);
		SQLBuilder sqlBuilder = createSQLBuilder();
		sqlBuilder.append(tableInfo.getColumnCollection());
		if(object==null){
			sqlBuilder.appendWhere(tableInfo.getPrimaryKey().getColumns());
			return sqlBuilder;
		}
		Object value = null;
		for(Map.Entry<String, ColumnInfo> col : tableInfo.getColumns().entrySet()){
			try {
				value = ReflectUtils.getExpr(object, col.getValue().getJavaName());
				if(value!=null)
					sqlBuilder.appendWhere(col.getValue());
			} catch (Exception e) {
				LangUtils.throwBaseException("build field["+col.getValue().toString()+"] error : " + e.getMessage(), e);
			}
		}
		return sqlBuilder;
	}
	
	public String makeStaticQuerySQL(){
		String key = getKey(QUERY_KEY);
		String sql = getFromCache(key);
		if(StringUtils.isNotBlank(sql))
			return sql;
		
		sql = makeQuerySQLBuilder(null).buildQuery();
		
		putIntoCache(key, sql);
		LangUtils.println("makeStaticQuerySQL: " + sql);
		
		return sql;
	}
	
	public String makeDynamicQuerySQL(Object object){
		return makeQuerySQLBuilder(object).buildQuery();
	}
	
	/****
	 * for mysql
	 * @return
	 */
	protected SQLBuilder makeFetchPKSQLBuilder(){
		SQLBuilder sqlBuilder = createSQLBuilder();
		sqlBuilder.setPrimaryKey(this.tableInfo.getPrimaryKey());
		return sqlBuilder;
	}
	
	public String makeStaticFetchPKSQL(){
		String key = getKey(FETCH_PK_KEY);
		String sql = getFromCache(key);
		if(StringUtils.isNotBlank(sql))
			return sql;
		
		sql = this.makeFetchPKSQLBuilder().buildPrimaryKey();
		
		putIntoCache(key, sql);
//		LangUtils.println("makeFetchPKSQL: " + sql);
		
		return sql;
	}
	
	public SQLBuilderFactory getSQLBuilderFactory(){
		return this.sqlBuilderFactory;
	}
	
	public void setSQLBuilderFactory(SQLBuilderFactory sqlBuilder){
		this.sqlBuilderFactory = sqlBuilder;
	}
	
	public Class getEntityClass(){
		return entityClass;
	}
	
	public TableInfo getTableInfo(){
		return tableInfo;
	}
	
	public SQLBuilder createSQLBuilder(){
		SQLBuilder sb = this.sqlBuilderFactory.getSQLBuilder(tableInfo);
		return sb;
	}
	
	public PrimaryKey getPrimaryKeyInfo(){
		return tableInfo.getPrimaryKey();
	}

}
