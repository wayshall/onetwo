package org.onetwo.common.ioc.impl;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.Valuer;
import org.onetwo.common.ioc.proxy.BFProxyHandler;

@SuppressWarnings("unchecked")
public class ProxyObjectInfo extends DefaultObjectInfo {

	public ProxyObjectInfo(String name, Valuer value) {
		super(name, value);
		if(!(value.getValue() instanceof BFProxyHandler))
			throw new ServiceException("the bean is not a proxy bean : name=" + name);
	}

	protected BFProxyHandler getProxyHandler() {
		return (BFProxyHandler)valuer.getValue();
	}
	
	public boolean isProxy(){
		return true;
	}

	public Object getBean() {
		return getProxyHandler().getProxyObject();
	}
	
	public Object getActualBean() {
		return getProxyHandler().getSrcObject();
	}
}
