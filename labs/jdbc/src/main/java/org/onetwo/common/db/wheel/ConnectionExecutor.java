package org.onetwo.common.db.wheel;

public interface ConnectionExecutor {

	public Object execute(ConnectionCreator creator, ConnectionCallback cb);

}