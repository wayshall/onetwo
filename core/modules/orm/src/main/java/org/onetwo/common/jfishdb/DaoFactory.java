package org.onetwo.common.jfishdb;

import javax.sql.DataSource;

import org.onetwo.common.jfishdb.spring.JFishDao;
import org.onetwo.common.jfishdb.spring.JFishDaoImpl;

public class DaoFactory {
	
	private static final DaoFactory instance = new DaoFactory();
	
	public static DaoFactory getInstance() {
		return instance;
	}

//	private DefaultDatabaseDialetManager databaseDialetManager = new DefaultDatabaseDialetManager();
	
	private DaoFactory(){
//		databaseDialetManager.register(DataBase.MySQL.getName(), new MySQLDialect());
	}
	
	public JFishDao newDao(DataSource dataSource){
//		DataBase db = JdbcUtils.getDataBase(dataSource);
//		DBDialect dialet = databaseDialetManager.getRegistered(db.getName());
		JFishDaoImpl dao = new JFishDaoImpl(dataSource);
		dao.initialize();
		return dao;
	}

}
