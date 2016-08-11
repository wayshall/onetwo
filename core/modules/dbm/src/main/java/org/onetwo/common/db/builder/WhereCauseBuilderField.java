package org.onetwo.common.db.builder;

abstract public class WhereCauseBuilderField {
	
	protected WhereCauseBuilder queryBuilder;

	public WhereCauseBuilderField(WhereCauseBuilder squery) {
		super();
		this.queryBuilder = squery;
	}

	protected WhereCauseBuilder getQueryBuilder() {
		return queryBuilder;
	}

	abstract public String[] getOPFields();
	
	abstract public Object[] getValues();

}
