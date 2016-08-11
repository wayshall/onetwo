package org.onetwo.common.db.builder;

import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import org.onetwo.common.db.sqlext.ExtQueryUtils;
import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.utils.func.Closure;

public class DefaultWhereCauseBuilderField extends WhereCauseBuilderField {
	
	private String[] fields;
	private String op;
	private Object[] values;
	
	private Supplier<Boolean> whenPredicate;

	public DefaultWhereCauseBuilderField(WhereCauseBuilder squery, String... fields) {
		super(squery);
		this.fields = fields;
	}

	public WhereCauseBuilder like(String... values) {
		this.op = FieldOP.like;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;
	}

	public WhereCauseBuilder notLike(String... values) {
		this.op = FieldOP.not_like;
		this.values = values;
		this.queryBuilder.addField(this);
		return queryBuilder;
	}

	public DefaultWhereCauseBuilderField when(Supplier<Boolean> predicate) {
		this.whenPredicate = predicate;
		return this;
	}

	/***
	 * 等于
	 * @param values
	 * @return
	 */
	public WhereCauseBuilder equalTo(Object... values) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.eq;
			this.values = values;
		});
	}
	
	public WhereCauseBuilder isNull(boolean isNull) {
		return this.doWhenPredicate(()->{
			this.op = FieldOP.is_null;
			this.values = new Object[]{isNull};
		});
	}
	
	protected WhereCauseBuilder doWhenPredicate(Closure whenAction){
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
	public WhereCauseBuilder notEqualTo(Object... values) {
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
	public WhereCauseBuilder greaterThan(Object... values) {
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
	
	public WhereCauseBuilder in(Object... values) {
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
	
	public WhereCauseBuilder notIn(Object... values) {
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
	
	public WhereCauseBuilder dateIn(Date start, Date end) {
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
	public WhereCauseBuilder greaterEqual(Object... values) {
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
	public WhereCauseBuilder lessThan(Object... values) {
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
	public WhereCauseBuilder lessEqual(Object... values) {
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
	
	public WhereCauseBuilder isNull(){
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
	
	public WhereCauseBuilder isNotNull(){
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
