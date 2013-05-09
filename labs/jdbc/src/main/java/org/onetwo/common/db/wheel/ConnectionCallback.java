package org.onetwo.common.db.wheel;


public interface ConnectionCallback {

	public Object doInConnection(DBConnection dbcon);
}
