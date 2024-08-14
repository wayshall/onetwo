package org.onetwo.boot.module.swagger;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(SwaggerProperties.PREFIX)
public class SwaggerProperties {
	
	public static final String PREFIX = BootJFishConfig.ZIFISH_CONFIG_PREFIX + ".swagger";
//	public static final String PLUGIN_DEFAULT_ENABLED = PREFIX + ".pluginDefaultEnabled";
	
	public static final String ENABLED_KEY = BootJFishConfig.ZIFISH_CONFIG_PREFIX + ".swagger.enabled";
	
	String basePath;
	
	/****
	 * 插件默认是否导出swagger api
	 */
	boolean pluginDefaultEnabled = true;
	/****
	 * 是否只导出使用@ExportableApi标注了api
	 */
	boolean useExportableApi = false;

}
