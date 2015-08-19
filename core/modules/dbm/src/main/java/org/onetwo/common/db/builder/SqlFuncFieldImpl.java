package org.onetwo.common.db.builder;

import org.onetwo.common.db.sqlext.ExtQueryInner;
import org.onetwo.common.db.sqlext.SelectExtQueryImpl;
import org.onetwo.common.utils.LangUtils;

public class SqlFuncFieldImpl extends QueryFieldImpl {

	public static final SqlFuncFieldImpl create(String exp){
		return new SqlFuncFieldImpl(exp);
	}
	
	SqlFuncFieldImpl(String fieldExpr) {
		super(fieldExpr);
	}

	@Override
	public void init(ExtQueryInner extQuery, Object value) {
		if(!extQuery.isSqlQuery()){
			LangUtils.throwBaseException("query is not a sql query, can not use raw sql function!");
		}
		super.init(extQuery, value);
	}

	public String getActualFieldName() {
		SelectExtQueryImpl q = (SelectExtQueryImpl)this.getExtQuery();
		return q.translateAt(super.getFieldName());
	}
}
