package org.onetwo.common.hibernate.sql;

import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;

public class HibernateNamedInfo extends JFishNamedFileQueryInfo {

//	private DataBase dataBaseType;
	private boolean hql;

	public boolean isHql() {
		return hql;
	}

	public void setHql(boolean hql) {
		this.hql = hql;
	}
	public String getSql() {
		String sql = super.getSql();
		sql = processSql(sql);
		return sql;
	}
	

	public String getCountSql() {
		String sql = super.getCountSql();
		sql = processSql(sql);
		return sql;
	}

	protected String processSql(String sql){
		/*sql = sql.replace("out_transaction_${date}", "out_transaction");
		sql = sql.replace("OUT_TRANSACTION_${date}", "out_transaction");
		sql = sql.replace("in_transaction_${date}", "in_transaction");
		sql = sql.replace("IN_TRANSACTION_${date}", "in_transaction");*/
		return sql;
	}
}
