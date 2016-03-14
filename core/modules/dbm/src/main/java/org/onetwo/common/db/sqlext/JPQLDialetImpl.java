package org.onetwo.common.db.sqlext;


public class JPQLDialetImpl extends DefaultSQLDialetImpl {

	public JPQLDialetImpl() {
	}

	@Override
	public String getPlaceHolder(int position) {
		return "?"+(position+1);
	}
}
