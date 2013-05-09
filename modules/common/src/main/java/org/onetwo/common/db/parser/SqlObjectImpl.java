package org.onetwo.common.db.parser;



public class SqlObjectImpl implements SqlObject {

	protected String fragmentSql;
	
	public SqlObjectImpl(String fragmentSql) {
		this.fragmentSql = fragmentSql;
	}
	
	public String toString(){
		return getClass().getSimpleName()+":"+toFragmentSql();
	}
	
	@Override
	public String toFragmentSql(){
		return fragmentSql;
	}

}
