package org.onetwo.common.db;

abstract public class QueryBuilderField {
	
	protected QueryBuilder queryBuilder;

	public QueryBuilderField(QueryBuilder squery) {
		super();
		this.queryBuilder = squery;
	}

	protected QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	abstract public String[] getOPFields();
	
	abstract public Object[] getValues();

}
