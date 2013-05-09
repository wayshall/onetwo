package org.onetwo.common.db.wheel;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class SQLBuilder {

	public static final String VAR_SEQ_NAME = "seqName";
	public static final String SQL_MYSQL_PK = "select max(${id}) from ${tableName}";
	
	public static final String SQL_INSERT = "insert into ${tableName} ( ${insertFields} ) values ( ${namedFields} )";
	public static final String SQL_UPDATE = "update ${tableName} set ${updateFields} where ${whereCause}";
	public static final String SQL_DELETE = "delete from ${tableName} where ${whereCause}";
	public static final String SQL_QUERY = "select ${selectFields} from ${tableName} ${alias}";
	public static final String SQL_QUERY_CAUSE = "where ${whereCause}";

	public static final Expression PARSER = Expression.DOLOR;
	
	
	public static SQLBuilder Create(String table, String alias){
		return new SQLBuilder(table, alias, ":name");
	}

	protected String alias = "this_";
	protected String tableName;
	protected String seqName;
	protected PrimaryKey primaryKey;
	protected List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
	protected List<ColumnInfo> whereCauseFields = new ArrayList<ColumnInfo>();
	
	private String placeHoder;
	
	public SQLBuilder(String table, String alias, String posistionStr){
		this.tableName = table;
		this.alias = alias;
		if(StringUtils.isBlank(posistionStr))
			posistionStr = "?";
		this.placeHoder = posistionStr;
	}
	
	public String getAlias(){
		return alias;
	}
	
	public String getColumnWithAlias(String alias){
		return getAlias() + "." + alias;
	}
	
	public SQLBuilder append(ColumnInfo column){
		this.columns.add(column);
		return this;
	}
	
	public SQLBuilder appendWhere(ColumnInfo column){
		this.whereCauseFields.add(column);
		return this;
	}
	
	public void setSeqName(String seqName){
		this.seqName = seqName;
	}
	
	public SQLBuilder append(List<ColumnInfo> columns){
		this.columns.addAll(columns);
		return this;
	}
	
	public SQLBuilder appendWhere(List<ColumnInfo> columns){
		this.whereCauseFields.addAll(columns);
		return this;
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
		LangUtils.println("build insert sql : ${0}", insertSql);
		return insertSql;
	}
	
	public String buildUpdate(){
		Assert.notEmpty(whereCauseFields);
//		List<String> fields = sqlBuildable.getFieldNames(entityClass);
		String update = "";
		List<String> updateFields = toWhereString(columns, false);
		List<String> whereColumns = toWhereString(whereCauseFields, false);
		update = PARSER.parse(SQL_UPDATE, 
				"tableName", tableName, 
				"updateFields", StringUtils.join(updateFields, ", "),
				"whereCause", StringUtils.join(whereColumns, " and "));
		if(JDBC.inst().isDebug())
			LangUtils.println("build update sql : ${0}", update);
		return update;
	}
	
	public String buildPrimaryKey(){
		String pkSql = PARSER.parse(SQL_MYSQL_PK, "id", this.primaryKey.getJavaName(), "tableName", this.tableName);
		if(JDBC.inst().isDebug())
			LangUtils.println("build Primary sql : ${0}", pkSql);
		return pkSql;
	}

	public String buildDelete(){
		Assert.notEmpty(whereCauseFields);
		if(StringUtils.isBlank(placeHoder))
			placeHoder = "?";
//		List<String> fields = sqlBuildable.getFieldNames(entityClass);
		String deleteSql = "";
		List<String> whereColumns = toWhereString(whereCauseFields, false);
		deleteSql = PARSER.parse(SQL_DELETE, 
				"tableName", tableName, 
//				"alias", alias,
				"whereCause", StringUtils.join(whereColumns, " and "));
		if(JDBC.inst().isDebug())
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
		if(LangUtils.isNotEmpty(whereCauseFields)){
			queryTemplate += " " + SQL_QUERY_CAUSE;
		}
		if(StringUtils.isBlank(placeHoder))
			placeHoder = "?";
//		List<String> fields = sqlBuildable.getFieldNames(entityClass);
		String querySql = "";
		List<String> whereColumns = toWhereString(whereCauseFields, true);
		querySql = PARSER.parse(queryTemplate, 
				"tableName", tableName, 
				"alias", alias, 
				"selectFields", selectStr, 
				"whereCause", StringUtils.join(whereColumns, " and ")
				);
		if(JDBC.inst().isDebug())
			LangUtils.println("build query sql : ${0}", querySql);
		return querySql;
	}
	

	protected List<String> toWhereString(List<ColumnInfo> columns){
		return toWhereString(columns, true);
	}
	
	protected List<String> toWhereString(List<ColumnInfo> columns, boolean alias){
		List<String> strs = new ArrayList<String>();
		int index = 0;
		String namedStr = "?";
		for(ColumnInfo field : columns){
			if(placeHoder.startsWith(":name")){
				namedStr = alias?field.getNamedPlaceHolderWithAlias():field.getNamedPlaceHolder();
				strs.add((alias?field.getNameWithAlias():field.getName())+" = " + namedStr);
			}else
				strs.add(field.getNameWithAlias()+" = "+namedStr);
			index++;
		}
		return strs;
	}
	

	protected List<String> nameToString(List<ColumnInfo> columns){
		return this.nameToString(columns, true);
	}
	
	protected List<String> nameToString(List<ColumnInfo> columns, boolean alias){
		List<String> strs = new ArrayList<String>();
		for(ColumnInfo field : columns){
			strs.add(alias?field.getNameWithAlias():field.getName());
		}
		return strs;
	}
	
	protected List<String> javaNameToString(List<ColumnInfo> columns, String alias){
		List<String> strs = new ArrayList<String>();
		for(ColumnInfo field : columns){
			strs.add(field.getJavaNameWithAlias());
		}
		return strs;
	}
	
	protected List<String> javaNameToNamedString(List<ColumnInfo> columns, boolean alias){
		List<String> strs = new ArrayList<String>();
		for(ColumnInfo field : columns){
			strs.add(alias?field.getNamedPlaceHolderWithAlias():field.getNamedPlaceHolder());
		}
		return strs;
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	/*public String getNamedPlaceHolderForWhere(String alias, String name, String placeHolder, int index){
		return getAliasName(alias, name) + " = " + namedString(getAliasName(alias, placeHolder+index, "_"));
	}
	public String getPlaceHolderForWhere(String alias, String name, int index){
		return getAliasName(alias, name) + " = " + "?";
	}
	
	public String namedString(String name){
		return ":"+name;
	}

	public String getAliasName(String alias, String name){
		return getAliasName(alias, name, ".");
	}
	
	public String getAliasName(String alias, String name, String concatStr){
		if(StringUtils.isBlank(alias))
			return name;
		return alias + concatStr + name;
	}*/
}
