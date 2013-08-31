package org.onetwo.common.hibernate.sql;

import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;

public class HibernateNamedInfo extends JFishNamedFileQueryInfo {

	private boolean hql;

	public boolean isHql() {
		return hql;
	}

	public void setHql(boolean hql) {
		this.hql = hql;
	}
	
}
