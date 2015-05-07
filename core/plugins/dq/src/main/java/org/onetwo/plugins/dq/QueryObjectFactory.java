package org.onetwo.plugins.dq;

import org.onetwo.common.db.QueryProvideManager;

public interface QueryObjectFactory {

//	public Object getQueryObject();
	public Object createQueryObject(QueryProvideManager em, Class<?>... proxiedInterfaces);

}