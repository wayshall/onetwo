package org.onetwo.common.nutz;

import javax.sql.DataSource;

import org.nutz.dao.impl.NutDao;

public class NutzBaseDao extends NutDao {

	public NutzBaseDao(DataSource dataSource) {
		super(dataSource);
		this.setRunner(new SpringDaoRunner());
	}

}
