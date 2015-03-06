package org.onetwo.plugins.dq;


public class DqEntityManager extends NullBaseEntityManager {
	private DataQueryHolderForTest dqholder;
	
	public DataQueryHolderForTest createSQLQuery(String sqlString, Class<?> entityClass) {
		dqholder.setSql(sqlString);
		return dqholder;
	}

	public DataQueryHolderForTest getDqholder() {
		return dqholder;
	}

	public void setDqholder(DataQueryHolderForTest dqholder) {
		this.dqholder = dqholder;
	}

	
	
	
}
