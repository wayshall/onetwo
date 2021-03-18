package org.onetwo.boot.module.poi;

import org.onetwo.common.file.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties(prefix=PoiProperties.PREFIX)
public class PoiProperties {
	public static final String PREFIX = "jfish.poi";
	public static final String EXPORT_VIEW_ENABLE_KEY = PREFIX + ".exportView.enabled";
	
	boolean writeToLocal;
	String localDir;
	boolean cacheTemplate;

	public void setLocalDir(String dir){
		this.localDir = FileUtils.convertDir(dir);
	}
}
