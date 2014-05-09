package org.onetwo.gentype;

public class TestBaseService<T, DAO extends TestBaseDao<T, Long>> {

	private DAO dao;

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}
	
	
}
