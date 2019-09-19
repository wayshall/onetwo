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
	
	public static final String VERSION_1 = "1";
	public static final String VERSION_2 = "2";
	
	/****
	 * actuator的版本
	 */
	public static final String MANAGEMENT_VERSION = "management.version";
	public static final String MANAGEMENT_AUTH_HEADER_NAME = "management.auth-header-name";
	public static final String MANAGEMENT_CONTEXT_PATH = "management.context-path";
	public static final String MANAGEMENT_REFRESH_CONFIG_URL = "management.refresh-config-url";
	
	public static Optional<InstanceInfoMeta> findByInstId(String instId) {
		return findInstanceInfoById(instId).map(inst -> {
			return newInstance(inst);
		});
	}
	
	public static InstanceInfoMeta newInstance(InstanceInfo inst) {
		return new InstanceInfoMeta(inst);
	}
	
	final private InstanceInfo inst;

	private InstanceInfoMeta(InstanceInfo inst) {
		super();
		this.inst = inst;
	}
	
	public String getAuthHeaderName(String defaultAuthHeaderName) {
		String authHeaderName = inst.getMetadata().get(MANAGEMENT_AUTH_HEADER_NAME);
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
		String url = inst.getMetadata().get(MANAGEMENT_REFRESH_CONFIG_URL);
		if (StringUtils.isBlank(url)) {
			url = inst.getMetadata().get(MANAGEMENT_CONTEXT_PATH);
			if (StringUtils.isBlank(url)) {
				url = isVersion2() ? "actuator" : "";
			}
		}
		url = inst.getHomePageUrl() + StringUtils.emptyIfNull(url) + "/refresh";
		return url;
	}
	
	public String getVersion() {
		String version = inst.getMetadata().get(MANAGEMENT_VERSION);
		if (StringUtils.isBlank(version)) {
			version = VERSION_1;
		}
		return version;
	}
	
	public boolean isVersion2() {
		return getVersion().startsWith(VERSION_2);
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

