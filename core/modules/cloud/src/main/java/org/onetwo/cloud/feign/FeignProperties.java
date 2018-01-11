package org.onetwo.cloud.feign;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import feign.Logger;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(FeignProperties.PROPERTIES_PREFIX)
@Data
public class FeignProperties {
	
	public static final String PROPERTIES_PREFIX = "jfish.cloud.feign";
	
	LoggerProps logger = new LoggerProps();
	
	/***
	 * 
jfish: 
    cloud: 
        feign: 
            logger: 
                level: BASIC
                autoChangeLevel: true
	 * @author wayshall
	 *
	 */
	@Data
	public class LoggerProps {
		Logger.Level level;
		/***
		 * feign的日志使用debug打印，设置是否自动修改相关的client logger的级别为debug
		 */
		boolean autoChangeLevel = true;
	}

}
