package org.onetwo.common.jfishdb.dialet;

import org.onetwo.common.utils.StringUtils;

public class MySQLDialect extends AbstractDBDialect {

	public MySQLDialect(){
	}


	public void registerIdStrategy(){
		this.getIdStrategy().add(StrategyType.INCREASE_ID);
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

	/*protected DbEventListenerManager createDefaultDbEventListenerManager() {
		JFishdbEventListenerManager listenerManager = new JFishdbEventListenerManager();
		return listenerManager;
	}
	*/
	

}
