package org.onetwo.common.db.sql;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.db.sqlext.SQLKeys;
import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class Condition {
	protected ConditionToken ctoken;
	protected Object value;
	protected int index;
	
	private boolean mutiValue;
//	private boolean ignore;

//	private int endStrIndex;

	public Object getValue() {
		if(SQLKeys.class.isInstance(value)){
			return ((SQLKeys)value).getJavaValue();
		}
		return value;
	}

	public Condition(ConditionToken ctoken, int index) {
	super();
	this.ctoken = ctoken;
	this.index = index;
}

	public void setValue(Object value) {
		if(LangUtils.isMultiple(value)){
			this.mutiValue = true;
			this.value = LangUtils.asList(value);
		}else{
			this.value = value;
		}
	}

	public void clearValue() {
		this.setValue(null);
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isAvailable(){
//		return value!=null;
		return !isNullOrBlank();
	}
	
	protected boolean isNullOrBlank(){
		return value==null || (String.class.isInstance(value) && StringUtils.isBlank(value.toString()));
	}


	public boolean isIgnore() {
		return isNullOrBlank();
	}

	/*public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}*/
	
	
	
	public String toSqlString(){
		if(FieldOP.in.equalsIgnoreCase(getOp())){
			return toSqlStringWithIn();
		}else{
			return toSqlStringWithOr();
		}
	}
	
	public String getOp() {
		return ctoken.getOp();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String toSqlStringWithOr(){
		StringBuilder sql = new StringBuilder();
		if(ctoken.isInfixOperator()){
			if(isMutiValue()){
				if(isAppendParenthesis())
					sql.append("(");
				else
					sql.append("");
				int vindex = 0;
				List<?> values = new ArrayList(getValueAsList());
				for(Object val : values){
					if(vindex!=0)
						sql.append(" or ");
					appendOneSqlScript(sql, val, vindex);
					vindex++;
				}
				if(isAppendParenthesis())
					sql.append(")");
			}else{
				appendOneSqlScript(sql, value, -1);
			}
		}else{
			sql.append(ctoken.getActualPlaceHolder());
		}
		return sql.toString();
	}
	
	public String getName() {
		return ctoken.getName();
	}
	
	protected void appendOneSqlScript(StringBuilder sql, Object val, int valueIndex){
		if(SQLKeys.Null.equals(val)){
			String op = getOp();
			boolean remove = false;
			if(FieldOP.eq.equals(getOp())){
				sql.append(getName()).append(" is null");
				remove = true;
			}else if(FieldOP.neq.equals(getOp()) || FieldOP.neq2.equals(getOp())){
				sql.append(getName()).append(" is not null");
				remove = true;
			}else{
				sql.append(getName()).append(" ").append(op).append(" ").append(getActualPlaceHolder());
			}
			if(remove){
				if(isMutiValue()){
					getValueAsList().remove(val);
				}else{
					setValue(null);// set to null
				}
			}
		}else{
			if(FieldOP.like.equals(getOp())){
				String valStr = val==null?"":val.toString();
				if(valStr.indexOf("%")==-1){
					valStr = ExtQueryUtils.getLikeString(valStr);
				}
				if(isMutiValue()){
					getValueAsList().set(valueIndex, valStr);
				}else{
					setValue(valStr);
				}
			}
			sql.append(getName()).append(" ").append(getOp()).append(" ").append(getActualPlaceHolder());
		}
	}
	
	@SuppressWarnings("unused")
	public String toSqlStringWithIn(){
		StringBuilder sql = new StringBuilder();
		if(isInfixOperator()){
			if(isMutiValue()){
				sql.append(getName()).append(" ").append(getOp());
				if(isAppendParenthesis())
					sql.append(" (");
				else
					sql.append(" ");
				int vindex = 0;
				for(Object val : getValueAsList()){
					if(vindex!=0)
						sql.append(", ");
					sql.append(getActualPlaceHolder());
					vindex++;
				}
				if(isAppendParenthesis())
					sql.append(")");
			}else{
				sql.append(getName()).append(" ").append(getOp()).append(" ").append(getActualPlaceHolder());
			}
		}else{
			sql.append(getActualPlaceHolder());
		}
		return sql.toString();
	}
	
	public String toString(){
		if(isInfixOperator())
			return LangUtils.append("condition: ", getName(), "[", getVarname(), "]", " ", getOp(), " ", getActualPlaceHolder());
		else
			return LangUtils.append("condition: ", getName());
	}

	public boolean isAppendParenthesis() {
		return isMutiValue();
	}

	
	public String getVarname() {
		return ctoken.getVarname();
	}

	public <T> List<T> getValueAsList(){
		return LangUtils.asList(value);
	}

	public boolean isMutiValue() {
		return mutiValue;
	}

	public String getActualPlaceHolder() {
		return ctoken.getActualPlaceHolder();
	}

	public boolean isInfixOperator() {
		return ctoken.isInfixOperator();
	}

	/*public int getEndStrIndex() {
		return endStrIndex;
	}

	public void setEndStrIndex(int endStrIndex) {
		this.endStrIndex = endStrIndex;
	}*/
	
}
