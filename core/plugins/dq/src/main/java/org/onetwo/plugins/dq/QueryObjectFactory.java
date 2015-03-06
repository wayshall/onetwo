package org.onetwo.plugins.dq;

import org.onetwo.common.db.QueryProvider;

public interface QueryObjectFactory {

//	public Object getQueryObject();
	public Object createQueryObject(QueryProvider em, Class<?>... proxiedInterfaces);

}