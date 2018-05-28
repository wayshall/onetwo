package org.onetwo.cloud.feign;

import java.util.concurrent.TimeUnit;

import lombok.Data;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.common.utils.LangOps;
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
	public static final String ENABLE_KEY = PROPERTIES_PREFIX + ".enabled";
	
	public static final String LOCAL_ENABLE_KEY = PROPERTIES_PREFIX + ".local.enabled";
	
	/***
	 * 拒绝插件路径
	 */
	boolean rejectPluginContextPath = true;
	LoggerProps logger = new LoggerProps();
	OkHttpClientProps okHttpClient = new OkHttpClientProps();
	
	String[] keepHeaders;
	
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
	
	@Data
	public class OkHttpClientProps {
		String readTimeout = "50s";
		String connectTimeout = "50s";
		String writeTimeout = "50s";
		
		public Pair<Integer, TimeUnit> getReadTimeoutTime() {
			Pair<Integer, TimeUnit> tu = LangOps.parseTimeUnit(readTimeout);
			return tu;
		}
		public Pair<Integer, TimeUnit> getConnectTimeoutTime() {
			return LangOps.parseTimeUnit(connectTimeout);
		};
		public Pair<Integer, TimeUnit> getWriteTimeoutTime() {
			return LangOps.parseTimeUnit(writeTimeout);
		}
		
	}

}
