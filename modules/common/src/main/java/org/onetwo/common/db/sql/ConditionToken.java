package org.onetwo.common.db.sql;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.LangUtils;

public class ConditionToken extends QueryToken {
	public static final String JDBC_PARAM_PLACEHODER = "?";
//	public static final String OPERATION_IN  = "in";
	
//	protected String name;
	private String varname;
	private String actualPlaceHolder;
	private String op;
	private int index;
	
	private boolean infixOperator = true;
//	private boolean ignore;

//	private int endStrIndex;
	
	public ConditionToken(){
		this(null, 0);
	}
	
	public ConditionToken(String name, int index) {
		super(name, index);
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}
	
	public boolean isNamedParameter(){
		return StringUtils.isNotBlank(varname);
	}

	public boolean isInfixOperator() {
		return infixOperator;
	}

	public void setInfixOperator(boolean isWhereCause) {
		this.infixOperator = isWhereCause;
	}

	/*public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}*/
	
	
	public String getName() {
		return super.getName();
	}
	
	
	public String toString(){
		if(isInfixOperator())
			return LangUtils.append("condition: ", getName(), "[", getVarname(), "]", " ", getOp(), " ", JDBC_PARAM_PLACEHODER);
		else
			return LangUtils.append("condition: ", getName());
	}

	public String getActualPlaceHolder() {
		return actualPlaceHolder;
	}

	public void setActualPlaceHolder(String fullVarname) {
		this.actualPlaceHolder = fullVarname;
	}

	/*public int getEndStrIndex() {
		return endStrIndex;
	}

	public void setEndStrIndex(int endStrIndex) {
		this.endStrIndex = endStrIndex;
	}*/
	
}
