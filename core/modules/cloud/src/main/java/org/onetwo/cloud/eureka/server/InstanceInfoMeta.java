package org.onetwo.cloud.eureka.server;

import java.util.List;
import java.util.Optional;

import org.onetwo.common.utils.StringUtils;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContext;
import com.netflix.eureka.EurekaServerContextHolder;

/**
 * @author weishao zeng
 * <br/>
 */
public class InstanceInfoMeta {
	
	public static Optional<InstanceInfoMeta> findByInstId(String instId) {
		return findInstanceInfoById(instId).map(inst -> {
			return newInstance(inst);
		});
	}
	
	public static InstanceInfoMeta newInstance(InstanceInfo inst) {
		return new InstanceInfoMeta(inst);
	}
	
	public static final String VERSION_1 = "1";
	public static final String VERSION_2 = "2";
	
	final private InstanceInfo inst;

	private InstanceInfoMeta(InstanceInfo inst) {
		super();
		this.inst = inst;
	}
	
	public String getAuthHeaderName(String defaultAuthHeaderName) {
		String authHeaderName = inst.getMetadata().get("management.auth-header-name");
		if (StringUtils.isBlank(authHeaderName)) {
			authHeaderName = defaultAuthHeaderName;
		}
		return authHeaderName;
	}
	
	/****
	 * 刷新配置的url
	 * @author weishao zeng
	 * @return
	 */
	public String getRefreshConfigUrl() {
		String url = inst.getMetadata().get("management.refresh-config-url");
		if (StringUtils.isBlank(url)) {
			url = inst.getMetadata().get("management.context-path");
			if (StringUtils.isBlank(url)) {
				url = isVersion2() ? "/actuator" : "";
			}
			url = url + "/refresh";
		}
		url = inst.getHomePageUrl() + url + "/refresh";
		return url;
	}
	
	public String getVersion() {
		String version = inst.getMetadata().get("management.version");
		if (StringUtils.isBlank(version)) {
			version = VERSION_1;
		}
		return version;
	}
	
	public boolean isVersion2() {
		return getVersion().equals(VERSION_2);
	}

	
	static public Optional<InstanceInfo> findInstanceInfoById(String instId) {
		List<Application> sortedApplications = getEurekaServerContext().getRegistry().getSortedApplications();
		Optional<InstanceInfo> inst = sortedApplications.stream().filter(app -> {
			return app.getByInstanceId(instId)!=null;
		})
		.findFirst()
		.map(app -> app.getByInstanceId(instId));
		return inst;
	}
	
	static public EurekaServerContext getEurekaServerContext() {
		return EurekaServerContextHolder.getInstance().getServerContext();
	}
}

