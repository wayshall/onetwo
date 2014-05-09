package org.onetwo.plugins.dq;

import org.onetwo.common.db.CreateQueryable;

public interface QueryObjectFactory {

//	public Object getQueryObject();
	public Object createQueryObject(CreateQueryable em, Class<?>... proxiedInterfaces);

}