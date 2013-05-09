package org.onetwo.common.fish.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.fish.orm.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class TableSQLBuilder {

	public static final String VAR_SEQ_NAME = "seqName";
	public static final String SQL_MYSQL_PK = "select max(${id}) from ${tableName}";
	
	public static final String SQL_INSERT = "insert into ${tableName} ( ${insertFields} ) values ( ${namedFields} )";
	public static final String SQL_UPDATE = "update ${tableName} set ${updateFields} where ${whereCause}";
	public static final String SQL_DELETE = "delete from ${tableName}";
	public static final String SQL_QUERY = "select ${selectFields} from ${tableName} ${alias}";
	public static final String SQL_QUERY_CAUSE = "where ${whereCause}";

	public static final Expression PARSER = Expression.DOLOR;

	
	public static final String QMARK = "?";

	protected String alias = "this_";
	protected String tableName;
	protected String seqName;
	protected PrimaryKey primaryKey;
	protected Collection<BaseColumnInfo> columns = new ArrayList<BaseColumnInfo>();
	protected Collection<BaseColumnInfo> whereCauseColumns = new ArrayList<BaseColumnInfo>();
	
	private boolean debug;
//	private String placeHoder;
	private boolean namedPlaceHoder;
	
	private String sql;
	private SqlBuilderType type;
	
	private DBDialect dialet;
	
	TableSQLBuilder(String table, String alias, boolean namedPlaceHoder, SqlBuilderType type){
		this.tableName = table;
		this.alias = alias;
		this.namedPlaceHoder = namedPlaceHoder;
		this.type = type;
	}
	
	public DBDialect getDialet() {
		return dialet;
	}

	public void setDialet(DBDialect dialet) {
		this.dialet = dialet;
	}

	public String getAlias(){
		return alias;
	}
	
	public String getColumnWithAlias(String alias){
		return getAlias() + "." + alias;
	}
	
	public TableSQLBuilder append(BaseColumnInfo column){
		this.columns.add(column);
		return this;
	}
	
	public TableSQLBuilder appendWhere(BaseColumnInfo column){
		this.whereCauseColumns.add(column);
		return this;
	}
	
	public void setSeqName(String seqName){
		this.seqName = seqName;
	}
	
	public TableSQLBuilder append(Collection<? extends BaseColumnInfo> columns){
		this.columns.addAll(columns);
		return this;
	}
	
	public TableSQLBuilder appendWhere(Collection<? extends BaseColumnInfo> columns){
		this.whereCauseColumns.addAll(columns);
		return this;
	}
	
	public String buildSeq(){
		sql = "select " + seqName + ".nextval from dual";
		return sql;
	}
	
	public String build(){
		if(StringUtils.isNotBlank(sql))
			return sql;
		
		if(SqlBuilderType.insert==type){
			this.sql = this.buildInsert();
		}else if(SqlBuilderType.update==type){
			this.sql = this.buildUpdate();
		}else if(SqlBuilderType.delete==type){
			this.sql = this.buildDelete();
		}else if(SqlBuilderType.query==type){
			this.sql = this.buildQuery();
		}else if(SqlBuilderType.primaryKey==type){
			this.sql = this.buildPrimaryKey();
		}else if(SqlBuilderType.seq==type){
			this.sql = this.buildSeq();
		}else{
			LangUtils.throwBaseException("unsupported type: " + type);
		}
		return this.sql;
	}

	
	public String buildInsert(){
		Assert.notEmpty(columns);
//		List<String> fields = sqlBuildable.getFieldNames(entityClass);
		String insertSql = "";
		List<String> insertFields = nameToString(columns, false);
		List<String> namedFields = javaNameToNamedString(columns, false);
		insertSql = PARSER.parse(SQL_INSERT, 
				"tableName", tableName, 
				"insertFields", StringUtils.join(insertFields, ", "),
				"namedFields", StringUtils.join(namedFields, ", "));
		if(isDebug())
			LangUtils.println("build insert sql : ${0}", insertSql);
		return insertSql;
	}
	
	public String buildUpdate(){
		Assert.notEmpty(columns);
		Assert.notEmpty(whereCauseColumns);
//		List<String> fields = sqlBuildable.getFieldNames(entityClass);
		String update = "";
		List<String> updateFields = toWhereString(columns, false);
		List<String> whereColumns = toWhereString(whereCauseColumns, false);
		update = PARSER.parse(SQL_UPDATE, 
				"tableName", tableName, 
				"updateFields", StringUtils.join(updateFields, ", "),
				"whereCause", StringUtils.join(whereColumns, " and "));
		if(isDebug())
			LangUtils.println("build update sql : ${0}", update);
		return update;
	}
	
	public String buildPrimaryKey(){
		String pkSql = PARSER.parse(SQL_MYSQL_PK, "id", this.primaryKey.getJavaName(), "tableName", this.tableName);
		if(isDebug())
			LangUtils.println("build Primary sql : ${0}", pkSql);
		return pkSql;
	}

	public String buildDelete(){
//		Assert.notEmpty(whereCauseColumns);
//		List<String> fields = sqlBuildable.getFieldNames(entityClass);
		String deleteSql = "";

		String deleteTemplate = SQL_DELETE;
//		Assert.notEmpty(whereCauseFields);
		if(LangUtils.isNotEmpty(whereCauseColumns)){
			deleteTemplate += " " + SQL_QUERY_CAUSE;
		}
		
		List<String> whereColumns = toWhereString(whereCauseColumns, false);
		deleteSql = PARSER.parse(deleteTemplate, 
				"tableName", tableName, 
//				"alias", alias,
				"whereCause", StringUtils.join(whereColumns, " and "));
		if(isDebug())
			LangUtils.println("build delete sql : ${0}", deleteSql);
		return deleteSql;
	}
	
	public String buildQuery(){
//		Assert.notEmpty(columns);
		String selectStr = "*";
		if(!columns.isEmpty()){
			selectStr = StringUtils.join(nameToString(columns, true), ", ");
		}
		String queryTemplate = SQL_QUERY;
//		Assert.notEmpty(whereCauseFields);
		if(LangUtils.isNotEmpty(whereCauseColumns)){
			queryTemplate += " " + SQL_QUERY_CAUSE;
		}
//		List<String> fields = sqlBuildable.getFieldNames(entityClass);
		String querySql = "";
		List<String> whereColumns = toWhereString(whereCauseColumns, true);
		querySql = PARSER.parse(queryTemplate, 
				"tableName", tableName, 
				"alias", alias, 
				"selectFields", selectStr, 
				"whereCause", StringUtils.join(whereColumns, " and ")
				);
		if(isDebug())
			LangUtils.println("build query sql : ${0}", querySql);
		return querySql;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	protected List<String> toWhereString(List<BaseColumnInfo> columns){
		return toWhereString(columns, true);
	}
	
	protected List<String> toWhereString(Collection<BaseColumnInfo> columns, boolean alias){
		List<String> strs = new ArrayList<String>();
		String namedStr = QMARK;
		String fstr = null;
		for(BaseColumnInfo field : columns){
			fstr = alias?field.getNameWithAlias():field.getName();
			if(namedPlaceHoder){
				namedStr = alias?field.getNamedPlaceHolderWithAlias():field.getNamedPlaceHolder();
				strs.add(fstr+" = " + namedStr);
			}else{
				strs.add(fstr+" = "+namedStr);
			}
		}
		return strs;
	}
	

	protected List<String> nameToString(Collection<BaseColumnInfo> columns){
		return this.nameToString(columns, true);
	}
	
	protected List<String> nameToString(Collection<BaseColumnInfo> columns, boolean alias){
		List<String> strs = new ArrayList<String>();
		for(BaseColumnInfo field : columns){
			strs.add(alias?field.getNameWithAlias():field.getName());
		}
		return strs;
	}
	
	protected List<String> javaNameToString(Collection<BaseColumnInfo> columns, String alias){
		List<String> strs = new ArrayList<String>();
		for(BaseColumnInfo field : columns){
			strs.add(field.getJavaNameWithAlias());
		}
		return strs;
	}
	
	protected List<String> javaNameToNamedString(Collection<BaseColumnInfo> columns, boolean alias){
		List<String> strs = new ArrayList<String>();
		for(BaseColumnInfo field : columns){
			if(namedPlaceHoder)
				strs.add(alias?field.getNamedPlaceHolderWithAlias():field.getNamedPlaceHolder());
			else
				strs.add(QMARK);
		}
		return strs;
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public String getSql() {
		return sql;
	}

	public SqlBuilderType getType() {
		return type;
	}

	public Collection<BaseColumnInfo> getColumns() {
		return columns;
	}

	public Collection<BaseColumnInfo> getWhereCauseColumns() {
		return whereCauseColumns;
	}

	public boolean isNamedPlaceHoder() {
		return namedPlaceHoder;
	}

	public void setNamedPlaceHoder(boolean namedPlaceHoder) {
		this.namedPlaceHoder = namedPlaceHoder;
	}
}
