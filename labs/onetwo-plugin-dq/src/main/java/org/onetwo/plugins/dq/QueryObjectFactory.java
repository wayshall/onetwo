package org.onetwo.plugins.dq;

import org.onetwo.common.fish.JFishEntityManager;

public interface QueryObjectFactory {

//	public Object getQueryObject();
	public Object createQueryObject(JFishEntityManager em, Class<?>... proxiedInterfaces);

}