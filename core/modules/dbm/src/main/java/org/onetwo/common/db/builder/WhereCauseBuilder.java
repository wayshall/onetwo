package org.onetwo.common.db.builder;


public interface WhereCauseBuilder {
	public WhereCauseBuilder debug();

	public WhereCauseBuilder or(QueryBuilder subQuery);

	public WhereCauseBuilder and(QueryBuilder subQuery);


	public DefaultWhereCauseBuilder addFields(Object entity);
	
	public WhereCauseBuilder addField(WhereCauseBuilderField field);

	public WhereCauseBuilder ignoreIfNull();

	public WhereCauseBuilder throwIfNull();

	public WhereCauseBuilder calmIfNull();

	public DefaultWhereCauseBuilderField field(String... fields);
	
	public QueryBuilder end();
}
