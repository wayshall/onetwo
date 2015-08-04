package org.onetwo.common.db.dquery;

import org.onetwo.common.db.QueryProvideManager;

public interface QueryObjectFactory {

//	public Object getQueryObject();
	public Object createQueryObject(QueryProvideManager em, Class<?>... proxiedInterfaces);

}