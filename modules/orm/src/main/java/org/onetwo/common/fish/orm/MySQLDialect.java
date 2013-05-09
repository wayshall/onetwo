package org.onetwo.common.fish.orm;

import org.onetwo.common.utils.StringUtils;

public class MySQLDialect extends AbstractDBDialect {

	public MySQLDialect(){
		super();
	}

	public void registerIdStrategy(){
		this.getIdStrategy().add(StrategyType.increase_id);
	}
	
	public String getLimitString(String sql, String firstName, String maxResultName) {
		StringBuilder sb = new StringBuilder();
		sb.append( sql );
		if(StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(maxResultName))
			sb.append(" limit :").append(firstName).append(", :").append(maxResultName);
		else{
			sb.append(" limit ?, ?");
		}
		return sb.toString();
	}

}
