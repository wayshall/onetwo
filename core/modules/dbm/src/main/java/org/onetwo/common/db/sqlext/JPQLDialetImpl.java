package org.onetwo.common.db.sqlext;


public class JPQLDialetImpl extends DefaultExtQueryDialetImpl {

	public JPQLDialetImpl() {
	}

	@Override
	public String getPlaceHolder(int position) {
		return "?"+(position+1);
	}
}
