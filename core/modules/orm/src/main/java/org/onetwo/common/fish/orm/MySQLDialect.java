package org.onetwo.common.fish.orm;

import org.onetwo.common.fish.event.DbEventListenerManager;
import org.onetwo.common.fish.event.JFishDefaultDbEventListenerManager;
import org.onetwo.common.fish.relation.JFishRelatedDbEventListenerManager;
import org.onetwo.common.utils.StringUtils;

public class MySQLDialect extends AbstractDBDialect {

	public MySQLDialect(DataBaseConfig dataBaseConfig){
		super(dataBaseConfig);
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

	@Override
	protected DbEventListenerManager createDefaultDbEventListenerManager() {
		JFishDefaultDbEventListenerManager listenerManager = new JFishDefaultDbEventListenerManager();
//		return listenerManager;
		return new JFishRelatedDbEventListenerManager(listenerManager);
	}
	
	

}
