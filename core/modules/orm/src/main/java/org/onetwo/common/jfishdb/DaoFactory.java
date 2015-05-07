package org.onetwo.common.jfishdb;

import javax.sql.DataSource;

import org.onetwo.common.jfishdb.spring.JFishDao;
import org.onetwo.common.jfishdb.spring.JFishDaoImpl;

final public class DaoFactory {
	
	
	private DaoFactory(){
	}
	
	public static JFishDao newJFishDao(DataSource dataSource){
		JFishDaoImpl dao = new JFishDaoImpl(dataSource);
		dao.initialize();
		return dao;
	}

}
