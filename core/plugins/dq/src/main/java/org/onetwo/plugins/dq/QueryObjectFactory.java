package org.onetwo.plugins.dq;

import org.onetwo.common.db.BaseEntityManager;

public interface QueryObjectFactory {

//	public Object getQueryObject();
	public Object createQueryObject(BaseEntityManager em, Class<?>... proxiedInterfaces);

}