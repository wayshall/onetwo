package org.onetwo.common.db.builder;

import java.util.Map;

import org.onetwo.common.db.builder.QueryBuilderImpl.SubQueryBuilder;
import org.onetwo.common.db.sqlext.ExtQuery.K;
import org.onetwo.common.utils.LangUtils;

public class DefaultWhereCauseBuilder implements WhereCauseBuilder {
	final protected QueryBuilder queryBuilder;
	final protected Map<Object, Object> params;
	
	public DefaultWhereCauseBuilder(QueryBuilder queryBuilder) {
		super();
		this.queryBuilder = queryBuilder;
		this.params = queryBuilder.getParams();
	}
	
	@Override
	public DefaultWhereCauseBuilder addField(WhereCauseBuilderField field){
		this.params.put(field.getOPFields(), field.getValues());
		return self();
	}

	protected DefaultWhereCauseBuilder self(){
		return (DefaultWhereCauseBuilder)this;
	}
	
	@Override
	public DefaultWhereCauseBuilder debug(){
		this.params.put(K.DEBUG, true);
		return self();
	}
	
	@Override
	public DefaultWhereCauseBuilder or(QueryBuilder subQuery){
		this.checkSubQuery(subQuery);
		this.params.put(K.OR, subQuery.getParams());
		return self();
	}
	
	protected void checkSubQuery(QueryBuilder subQuery){
		if(!(subQuery instanceof SubQueryBuilder)){
			LangUtils.throwBaseException("please use SQuery.sub() method to create sub query .");
		}
	}
	
	@Override
	public DefaultWhereCauseBuilder and(QueryBuilder subQuery){
		this.checkSubQuery(subQuery);
		this.params.put(K.AND, subQuery.getParams());
		return self();
	}
	
	@Override
	public DefaultWhereCauseBuilder ignoreIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Ignore);
		return self();
	}
	
	@Override
	public DefaultWhereCauseBuilder throwIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Throw);
		return self();
	}
	
	@Override
	public DefaultWhereCauseBuilder calmIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Calm);
		return self();
	}
	
	@Override
	public DefaultWhereCauseBuilderField field(String...fields){
//		this.throwIfHasBuild();
		return new DefaultWhereCauseBuilderField(this, fields);
	}

	@Override
	public QueryBuilder end(){
		return queryBuilder;
	}
}
