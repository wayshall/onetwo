package org.onetwo.boot.module.poi;

import lombok.Data;

import org.onetwo.common.file.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties(prefix=PoiProperties.PREFIX)
public class PoiProperties {
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".poi";
	public static final String EXPORT_VIEW_ENABLE_KEY = PREFIX + ".exportView.enabled";
	
	boolean writeToLocal;
	String localDir;

	public void setLocalDir(String dir){
		this.localDir = FileUtils.convertDir(dir);
	}
}
