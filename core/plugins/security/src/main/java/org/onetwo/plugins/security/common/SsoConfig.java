package org.onetwo.plugins.security.common;

import org.onetwo.common.utils.propconf.JFishProperties;

abstract public class SsoConfig extends JFishProperties {

	abstract public boolean isServerSide();
	abstract public boolean isClientSide();
	

	public String getReturnUrl(){
		return getAndThrowIfEmpty("return.url");
	}
	
	abstract public String getLoginUrl();
}
