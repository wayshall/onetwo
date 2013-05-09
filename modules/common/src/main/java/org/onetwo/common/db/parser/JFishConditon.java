package org.onetwo.common.db.parser;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.db.sqlext.SQLKeys;
import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.utils.LangUtils;

public class JFishConditon {
	protected SqlVarObject sqlObject;
	protected Object value;
	protected int index;

	private boolean mutiValue;

	// private boolean ignore;

	// private int endStrIndex;

	public Object getValue() {
		if (SQLKeys.class.isInstance(value)) {
			return ((SQLKeys) value).getJavaValue();
		}
		return value;
	}

	public JFishConditon(SqlVarObject sqlObject, int index) {
		this.sqlObject = sqlObject;
		this.index = index;
	}

	public void setValue(Object value) {
		if (LangUtils.isMultiple(value)) {
			this.mutiValue = true;
			this.value = LangUtils.asList(value);
		} else {
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

	public boolean isAvailable() {
		return !isNullOrBlank();
	}

	protected boolean isNullOrBlank() {
		return LangUtils.isNullOrEmptyObject(value);
	}

	public boolean isIgnore() {
		return isNullOrBlank();
	}
	
	protected SqlInfixVarConditionExpr getSqlInfixVarCondition(){
		return (SqlInfixVarConditionExpr) sqlObject;
	}

	public String toSqlString() {
		if(!sqlObject.isInfix()){
			return toSqlStringWithOr();
		}else{
			if (getSqlInfixVarCondition().getOperator()==SqlTokenKey.IN) {
				return toSqlStringWithIn();
			} else {
				return toSqlStringWithOr();
			}
		}
	}

	/*public String getOp() {
		return sqlObject.getOperatorString();
	}*/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String toSqlStringWithOr() {
		StringBuilder sql = new StringBuilder();
		if (sqlObject.isInfix()) {
			if (isMutiValue()) {
				if (isAppendParenthesis())
					sql.append("( ");
				else
					sql.append("");
				int vindex = 0;
				List<?> values = new ArrayList(getValueAsList());
				for (Object val : values) {
					if (vindex != 0)
						sql.append("or ");
					appendOneSqlScript(sql, val, vindex);
					vindex++;
				}
				if (isAppendParenthesis())
					sql.append(")");
			} else {
				appendOneSqlScript(sql, value, -1);
			}
		} else {
			sql.append(sqlObject.toJdbcSql(1));
		}
		return sql.toString();
	}

	/*public String getName() {
		return getSqlInfixVarCondition().getLeftString();
	}*/

	protected void appendOneSqlScript(StringBuilder sql, Object val, int valueIndex) {
		String op = getSqlInfixVarCondition().getOperatorString();
		if (SQLKeys.Null.equals(val)) {
			boolean remove = false;
			String name = getSqlInfixVarCondition().getLeftString();
			if (FieldOP.eq.equals(op)) {
				sql.append(name).append("is null ");
				remove = true;
			} else if (FieldOP.neq.equals(op) || FieldOP.neq2.equals(op)) {
				sql.append(name).append("is not null ");
				remove = true;
			} else {
				sql.append(sqlObject.toJdbcSql(1));
			}
			if (remove) {
				if (isMutiValue()) {
					getValueAsList().remove(val);
				} else {
					setValue(null);// set to null
				}
			}
		} else {
			if (FieldOP.like.equals(op)) {
				String valStr = val == null ? "" : val.toString();
				
				if(getSqlInfixVarCondition().isRightVar()){
					valStr = ExtQueryUtils.getLikeString(valStr);
				}
				if (isMutiValue()) {
					getValueAsList().set(valueIndex, valStr);
				} else {
					setValue(valStr);
				}
			}
			sql.append(this.sqlObject.toJdbcSql(1));
		}
	}

	@SuppressWarnings("unused")
	public String toSqlStringWithIn() {
		String op = getSqlInfixVarCondition().getOperatorString();
		StringBuilder sql = new StringBuilder();
		if (isInfixOperator()) {
			if (isMutiValue()) {
				/*sql.append(getName()).append(" ").append(op);
				if (isAppendParenthesis())
					sql.append(" (");
				else
					sql.append(" ");
				int vindex = 0;
				for (Object val : getValueAsList()) {
					if (vindex != 0)
						sql.append(", ");
					sql.append(sqlObject.getActualPlaceHolder());
					vindex++;
				}
				if (isAppendParenthesis())
					sql.append(")");*/
				sql.append(this.sqlObject.toJdbcSql(getValueAsList().size()));
			} else {
				sql.append(this.sqlObject.toJdbcSql(1));
			}
		} else {
			sql.append(this.sqlObject.toJdbcSql(1));
		}
		return sql.toString();
	}

	public String toString() {
		return LangUtils.append("condition: ", sqlObject.toJdbcSql(1));
	}

	public boolean isAppendParenthesis() {
		return isMutiValue();
	}

	public String getVarname() {
		return sqlObject.getVarname();
	}

	public <T> List<T> getValueAsList() {
		return LangUtils.asList(value);
	}

	public boolean isMutiValue() {
		return mutiValue;
	}

	public boolean isInfixOperator() {
		return this.sqlObject.isInfix();
	}

}
