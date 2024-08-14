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
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".poi";
	public static final String EXPORT_VIEW_ENABLE_KEY = PREFIX + ".exportView.enabled";
	public static final String CONTENT_TYPE = "application/jfxls;charset=utf-8";
	
	boolean writeToLocal;
	String localDir;
	boolean cacheTemplate;
	
	String contentType = CONTENT_TYPE;
	
	/****
	 * 使用@PoiData注解导出时最大的导出数量
	 */
	int maxCountLimit = 10000;

	public void setLocalDir(String dir){
		this.localDir = FileUtils.convertDir(dir);
	}
}
