package org.onetwo.common.web.subdomain;

public interface SubdomainMapping {

	public void readSubdomainInfos();
	 
	public SubdomainInfo getSubdomainInfo(String domain, String language);

}