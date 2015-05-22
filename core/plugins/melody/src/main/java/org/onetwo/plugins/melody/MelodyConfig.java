package org.onetwo.plugins.melody;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class MelodyConfig extends AbstractLoadingConfig {
	
	private String[] monitoringPatterns;
	private boolean disabled;
	private String[] urlPatterns;
	private Map<String, String> monitoringInitParams;
	
	@Override
	protected void initConfig(JFishProperties config) {
		disabled = config.getBoolean("disabled", false);
		monitoringPatterns = config.getStringArray("Spring.monitoring.pointcut.patterns", ",");
		urlPatterns = config.getStringArray("filter.url.patterns", ",");
		if(ArrayUtils.isEmpty(urlPatterns)){
			urlPatterns = new String[]{"/*"};
		}
		
//		this.monitoringLog = config.getProperty("monitoring.filter.log", "true");
		//Parameter.URL_EXCLUDE_PATTERN 可配置排除下载的url，似乎会影响下载
		monitoringInitParams = config.getPropertiesStartWith("filter.init.");
//		System.out.println("load melody ocnfig!");
	}

	public String[] getMonitoringPatterns() {
		return monitoringPatterns;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public String[] getUrlPatterns() {
		return urlPatterns;
	}

	public Map<String, String> getMonitoringInitParams() {
		return monitoringInitParams;
	}
	
}
