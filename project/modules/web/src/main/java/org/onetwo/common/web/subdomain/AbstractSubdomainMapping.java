package org.onetwo.common.web.subdomain;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.ServiceException;


@SuppressWarnings( { "unchecked", "serial" })
abstract public class AbstractSubdomainMapping implements SubdomainMapping {

	public static final String HTTP_START_KEY = "http://";

	protected Logger logger = Logger.getLogger(this.getClass());

	protected Map<String, Map<String, SubdomainInfo>> mapping;

	public AbstractSubdomainMapping() {
		mapping = new HashMap<String, Map<String, SubdomainInfo>>();
	}

	protected SubdomainInfo putInMapping(SubdomainInfo subdomain) {
		Map<String, SubdomainInfo> map = mapping.get(subdomain.getSubmain());
		if (map == null) {
			map = new HashMap<String, SubdomainInfo>();
			subdomain.setDefaultSubdomain(true);
		}

		if (map.containsKey(subdomain))
			throw new ServiceException("domain [" + subdomain.getSubmain() + "] has exist!");

		map.put(subdomain.getLanguage(), subdomain);
		if (logger.isInfoEnabled()) {
			logger.info("["+mapping.size()+"]read domain info : [" + subdomain.getLanguage() + ":" + subdomain.getSubmain() + "]");
		}

		mapping.put(subdomain.getSubmain(), map);
		mapping.put(subdomain.getId().toString(), map);
		return subdomain;
	}

	public SubdomainInfo getSubdomainInfo(String domain, String language) {
		SubdomainInfo subdomain = null;
		/*if(domain.indexOf('_')!=-1)
			domain = domain.replace('_', '.');*/
		Map<String, SubdomainInfo> list = mapping.get(domain);

		if (list == null || list.isEmpty())
			return findFromDatabase(domain, language);

		subdomain = list.get(language);
		if (subdomain == null)
			subdomain = findFromDatabase(domain, language);

		if (subdomain != null)
			return subdomain;

		// 如果此域名没有当前的语言版本，则采用默认的
		for (SubdomainInfo sub : list.values()) {
			if (sub.isDefaultSubdomain()) {
				subdomain = sub;
				break;
			}
		}

		return subdomain;
	}

	abstract public SubdomainInfo findFromDatabase(String domain, String language);

	public void reload() {
		this.clear();
		this.readSubdomainInfos();
	}

	public void clear() {
		this.mapping.clear();
	}

	public Map<String, Map<String, SubdomainInfo>> getMapping() {
		return mapping;
	}

}
