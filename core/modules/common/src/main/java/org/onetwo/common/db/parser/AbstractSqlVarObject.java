package org.onetwo.common.db.parser;


abstract public class AbstractSqlVarObject extends SqlObjectImpl implements SqlVarObject{

	public AbstractSqlVarObject() {
		super();
	}

	public AbstractSqlVarObject(String fragmentSql) {
		super(fragmentSql);
	}
	
	/*@Override
	public String[] getVarnames() {
		return new String[]{getVarname()};
	}

	@Override
	public int getVarsize() {
		return getVarnames().length;
	}*/

}
