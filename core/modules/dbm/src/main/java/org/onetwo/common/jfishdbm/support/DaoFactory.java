package org.onetwo.common.jfishdbm.support;

import javax.sql.DataSource;

final public class DaoFactory {
	
	
	private DaoFactory(){
	}
	
	public static JFishDao newJFishDao(DataSource dataSource){
		JFishDaoImpl dao = new JFishDaoImpl(dataSource);
		dao.initialize();
		return dao;
	}

}
