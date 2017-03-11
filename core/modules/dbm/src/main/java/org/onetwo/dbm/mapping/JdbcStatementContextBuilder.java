package org.onetwo.dbm.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.event.DbmEventAction;
import org.onetwo.dbm.mapping.SQLBuilderFactory.SqlBuilderType;

public class JdbcStatementContextBuilder implements JdbcStatementContext<List<Object[]>> {
/*
	public static DymanicSQLBuilder create(JFishMappedEntry entry, DSqlType dtype){
		SQLBuilder sb = sqlBuilderFactory.createQMark(entry.getTableInfo().getName(), entry.getTableInfo().getAlias(), dtype);
		return create(entry, sb);
	}*/
	public static JdbcStatementContextBuilder create(DbmEventAction eventAction, AbstractJFishMappedEntryImpl entry, EntrySQLBuilderImpl sqlBuilder){
		JdbcStatementContextBuilder dsql = new JdbcStatementContextBuilder(eventAction, entry, sqlBuilder);
		return dsql;
	}
	
	private AbstractJFishMappedEntryImpl entry;
	private EntrySQLBuilderImpl sqlBuilder;
	private Map<DbmMappedField, Object> columnValues = CUtils.newLinkedHashMap();
	private List<Object> causeValues = new ArrayList<Object>(5);
	private List<Object[]> values;
	private final DbmEventAction eventAction;

	private JdbcStatementContextBuilder(DbmEventAction eventAction, AbstractJFishMappedEntryImpl entry, EntrySQLBuilderImpl sqlBuilder) {
		super();
		this.entry = entry;
		this.sqlBuilder = sqlBuilder;
		this.values = new ArrayList<Object[]>();
		this.eventAction = eventAction;
	}
	
	public JdbcStatementContextBuilder append(DbmMappedField field, Object val){
		this.sqlBuilder.append(field);
		this.columnValues.put(field, val);
		return this;
	}
	
	public JdbcStatementContextBuilder appendWhere(DbmMappedField column, Object val){
		this.sqlBuilder.appendWhere(column);
		this.causeValues.add(val);
		return this;
	}
	
	public SqlBuilderType getSqlType(){
		return this.sqlBuilder.getType();
	}
	
	public JdbcStatementContextBuilder processColumnValues(Object entity){
		Assert.notNull(entity);
		Object val = null;
		EntrySQLBuilderImpl builder = getSqlBuilder();
		for(DbmMappedField field : builder.getFields()){
			val = field.getValue(entity);
//			val = field.getValueForJdbcAndFireDbmEventAction(entity, getEventAction());
			if(field.fireDbmEntityFieldEvents(val, getEventAction())!=val){
				field.setValue(entity, val);
			}
			if(field.isVersionControll()){
				if(DbmEventAction.insert==getEventAction()){
					val = field.getVersionableType().getVersionValule(val);
					field.setValue(entity, val);//write the version value into the entity
				}else if(DbmEventAction.update==getEventAction()){
					Assert.notNull(val, "version field["+field.getName()+"] can't be null: " + entry.getEntityName());
					val = field.getVersionableType().getVersionValule(val);
//					field.setValue(entity, val);
				}
			}
			this.columnValues.put(field, val);
		}
		return this;
	}
	
	public JdbcStatementContextBuilder addBatch(){
		Object[] batchValues = null;
		if(SqlBuilderType.update==getSqlType()){
//			this.getColumnValues().addAll(getCauseValues());
			batchValues = ArrayUtils.addAll(this.columnValues.values().toArray(), causeValues.toArray());
			
		}else if(SqlBuilderType.insert==getSqlType()){
			batchValues = columnValues.values().toArray();
			
		}else if(SqlBuilderType.delete==getSqlType()){
//			this.getColumnValues().addAll(getCauseValues());
			batchValues = ArrayUtils.addAll(columnValues.values().toArray(), causeValues.toArray());
			
		}else if(SqlBuilderType.query==getSqlType()){
//			this.getColumnValues().addAll(getCauseValues());
			batchValues = ArrayUtils.addAll(columnValues.values().toArray(), causeValues.toArray());
			
		}else{
			LangUtils.throwBaseException("not support type : " + getSqlType());
		}
		if(!LangUtils.isEmpty(batchValues)){
			this.values.add(batchValues);
			this.causeValues.clear();
			this.columnValues.clear();
		}
		return this;
	}
	
	public JdbcStatementContextBuilder processWhereCauseValuesFromEntity(Object entity){
		Assert.notNull(entity);
		for(DbmMappedField field : this.sqlBuilder.getWhereCauseFields()){
			Object val = field.getValue(entity);
//			SqlParameterValue pvalue = convertSqlParameterValue(field, val);
			this.causeValues.add(val);
		}
		return this;
	}

	/****
	 * 根据实体的属性做一定的类型转换
	 * @param value
	 * @return
	 
	protected Object convertPropertyValue(Object value){
		return DbmUtils.convertPropertyValue(propertyInfo, value);
	}*/
	/***
	 * 转成spring jdbc sql parameter 参数
	 * @param value
	 * @return
	 */
	/*protected SqlParameterValue convertSqlParameterValue(DbmMappedField field, Object value){
		return DbmUtils.convertSqlParameterValue(field.getPropertyInfo(), value, entry.getSqlTypeMapping());
	}*/
	
	public JdbcStatementContextBuilder addCauseValue(Object value){
		causeValues.add(value);
		return this;
	}
	public JdbcStatementContextBuilder build(){
		sqlBuilder.build();
		addBatch();
		return this;
	}

	public String getSql(){
		return sqlBuilder.getSql();
	}

	public List<Object[]> getValue() {
		return values;
	}

	public Object[] getValueArray() {
		return values.toArray();
	}

	public JFishMappedEntry getEntry() {
		return entry;
	}

	public EntrySQLBuilderImpl getSqlBuilder() {
		return sqlBuilder;
	}

	public DbmEventAction getEventAction() {
		return eventAction;
	}
}
