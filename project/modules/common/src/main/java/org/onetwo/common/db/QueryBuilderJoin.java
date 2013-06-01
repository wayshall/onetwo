package org.onetwo.common.db;

import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class QueryBuilderJoin {
	
	public static class OnCause {
		
		private QueryBuilderJoin join;
		private String left;
		private String right;
		
		public OnCause(QueryBuilderJoin join, String left, String right) {
			super();
			this.join = join;
			this.left = left;
			this.right = right;
		}
		
		protected String getFieldName(String field){
			if(field.contains("."))
				return field;
			return join.getBuilder().getAlias()+"."+field;
		}

		public String toSql(){
			return LangUtils.append(left, " = ", right);
		}

		public String toString(){
			return toSql();
		}
	}
	
	private QueryBuilder builder;
	private String joinTable;
	private String alias;
	private List<OnCause> onCauses = LangUtils.newArrayList();
	
	public QueryBuilderJoin(QueryBuilder builder, String joinTable, String alias) {
		super();
		this.joinTable = joinTable;
		this.builder = builder;
		this.alias = alias;
	}
	
	
	public QueryBuilder getBuilder() {
		return builder;
	}


	public QueryBuilder on(String...onCause){
		for(int i=0; i<onCause.length; i=i+2){
			onCauses.add(new OnCause(this, onCause[i], onCause[i+1]));
		}
		return builder;
	}
	
	public String toSql(){
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable).append(" ").append(alias).append(" on (");
		for (int i = 0; i < onCauses.size(); i++) {
			if(i!=0)
				sql.append(" and ");
			sql.append(onCauses.get(i).toSql());
		}
		sql.append(")");
		return sql.toString();
	}
}
