package org.onetwo.common.db.builder;

import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import org.onetwo.common.db.sqlext.ExtQueryUtils;
import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.utils.func.Closure;

public class DefaultQueryBuilderField extends QueryBuilderField {
	
	private String[] fields;
	private String op;
	private Object[] values;
	
	private Supplier<Boolean> whenPredicate;

	public DefaultQueryBuilderField(QueryBuilder squery, String... fields) {
		super(squery);
		this.fields = fields;
	}

	public QueryBuilder like(String... values) {
		this.op = FieldOP.like;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;
	}

	public QueryBuilder notLike(String... values) {
		this.op = FieldOP.not_like;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;
	}

	public DefaultQueryBuilderField when(Supplier<Boolean> predicate) {
		this.whenPredicate = predicate;
		return this;
	}

	/***
	 * 等于
	 * @param values
	 * @return
	 */
	public QueryBuilder equalTo(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.eq;
			this.values = values;
		});
	}
	
	public QueryBuilder isNull(boolean isNull) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.is_null;
			this.values = new Object[]{isNull};
		});
	}
	
	protected QueryBuilder doWhenPredicate(Closure whenAction){
		boolean rs = whenPredicate==null?true:Optional.ofNullable(whenPredicate.get()).orElse(false);
		if(rs){
			whenAction.execute();
			queryBuilder.addField(this);
			whenPredicate = null;
		}
		return queryBuilder;
	}

	/****
	 * 不等于
	 * @param values
	 * @return
	 */
	public QueryBuilder notEqualTo(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.neq;
			this.values = values;
		});
	}

	/****
	 * 大于
	 * @param values
	 * @return
	 */
	public QueryBuilder greaterThan(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.gt;
			this.values = values;
		});
		/*
		this.op = FieldOP.gt;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}
	
	public QueryBuilder in(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.gt;
			this.values = values;
		});
		/*
		this.op = FieldOP.in;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}
	
	public QueryBuilder notIn(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.not_in;
			this.values = values;
		});
		/*
		this.op = FieldOP.not_in;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}
	
	public QueryBuilder dateIn(Date start, Date end) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.date_in;
			this.values = new Date[]{start, end};
		});
		/*
		this.op = FieldOP.date_in;
		this.values = new Date[]{start, end};
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}

	/****
	 * 大于或者等于
	 * @param values
	 * @return
	 */
	public QueryBuilder greaterEqual(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.ge;
			this.values = values;
		});
		/*
		this.op = FieldOP.ge;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}

	/****
	 * 少于
	 * @param values
	 * @return
	 */
	public QueryBuilder lessThan(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.lt;
			this.values = values;
		});
		/*
		this.op = FieldOP.lt;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}

	/****
	 * 少于或等于
	 * @param values
	 * @return
	 */
	public QueryBuilder lessEqual(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.le;
			this.values = values;
		});
		/*
		this.op = FieldOP.le;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}
	
	public QueryBuilder isNull(){
		return this.doWhenPredicate(()->{
			this.op = FieldOP.is_null;
			this.setValues(true);
		});
		/*
		this.op = FieldOP.is_null;
		this.setValues(true);
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}
	
	public QueryBuilder isNotNull(){
		return this.doWhenPredicate(()->{
			this.op = FieldOP.is_null;
			this.setValues(false);
		});
		
		/*this.op = FieldOP.is_null;
		this.setValues(false);
		this.queryBuilder.addField(this);
		return queryBuilder;*/
	}
	
	protected void setValues(Object val){
		this.values = new Object[this.fields.length];
		for(int i=0; i<this.fields.length; i++){
			this.values[i] = val;
		}
	}
	
	public String[] getOPFields(){
		return ExtQueryUtils.appendOperationToFields(fields, op);
	}

	public Object[] getValues() {
		return values;
	}

}
